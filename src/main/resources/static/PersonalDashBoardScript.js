// === KİMLİK DOĞRULAMA KONTROLÜ ===
const loggedInCompanyId = localStorage.getItem("companyId");
const loggedInUserId = localStorage.getItem("userId");
console.log("deneme",localStorage);
if (!loggedInCompanyId && !loggedInUserId) {
  window.location.href = "http://localhost:8080/task/login";
} else {

  let reports = [];
  const CURRENT_USER_ID = localStorage.getItem("companyId");

  document.addEventListener('DOMContentLoaded', () => {
    fetchAssignedReports();
  });

  function getIsActivePersonelFlag(r) {
    return (
      r?.isActivePersonel ??
      r?.isActivePersonnel ??
      r?.isactivepersonel ??
      r?.is_active_personel ??
      r?.personelActive ??
      r?.activeForPersonnel
    );
  }

  function fmt(dt) {
    if (!dt) return '-';
    try {
      const d = new Date(dt);
      if (isNaN(d.getTime())) return dt;
      const pad = n => String(n).padStart(2, '0');
      return `${pad(d.getDate())}.${pad(d.getMonth()+1)}.${d.getFullYear()} ${pad(d.getHours())}:${pad(d.getMinutes())}`;
    } catch { return dt; }
  }

  function getHasJobReportFlag(r) {
    const direct =
      r?.hasJobReport ??
      (r?.jobReport != null) ??
      (r?.job_report != null);
    return direct ?? r?.hasJobReport ?? false;
  }

  function isVisibleForPersonnel(r) {
    const flag = getIsActivePersonelFlag(r);
    return flag !== false;
  }

  async function parseJsonSafe(res) {
    const text = await res.text();
    if (!text) return null;
    try { return JSON.parse(text); } catch { return null; }
  }

  function normalizeReport(r) {
    const personelActive = getIsActivePersonelFlag(r);
    const hasJobReport = getHasJobReportFlag(r);
    return { ...r, isActivePersonel: personelActive, hasJobReport };
  }

  function upsertReportInList(updated) {
    const idx = reports.findIndex(r => Number(r.id) === Number(updated.id));
    if (idx !== -1) {
      reports[idx] = normalizeReport({ ...reports[idx], ...updated });
    } else {
      reports.push(normalizeReport(updated));
    }
  }

  function markJobReportAdded(reportId) {
    const idx = reports.findIndex(r => Number(r.id) === Number(reportId));
    if (idx !== -1) {
      reports[idx] = normalizeReport({ ...reports[idx], hasJobReport: true });
      renderTable();
    }
  }

  function fetchAssignedReports() {
    fetch(`http://localhost:8080/personal-dashboard/reports/${CURRENT_USER_ID}`)
      .then(res => {
        if (!res.ok) throw new Error("Atanan raporlar alınamadı");
        return res.json();
      })
      .then(data => {
        console.log(data);
        reports = (data || []).map(normalizeReport);
        renderTable();
      })
      .catch(err => {
        console.error("Atanan raporlar alınamadı:", err);
        alert("Raporlar yüklenirken bir hata oluştu.");
      });
  }

  function renderTable() {
    const tbody = document.getElementById('reportBody');
    tbody.innerHTML = '';

    const visible = (reports || []).filter(isVisibleForPersonnel);

    visible.forEach(report => {
      const row = document.createElement('tr');
      const statusText = (report.status || '').toString();
      const statusKey = statusText.toUpperCase().replace('İ','I');
      const statusClass = `status-${statusKey}`;

      let actionButtonsHtml = '';
      if (statusKey === 'BEKLIYOR' || statusKey === 'ATANDI') {
        actionButtonsHtml = `<button class="accept-btn" onclick="acceptReport(${report.id})">Görevi Kabul Et</button> <button class="reject-btn" onclick="rejectReport(${report.id})">Görevi Reddet</button>`;
      } else if (statusKey === 'AKTIF') {
        actionButtonsHtml = `<button class="complete-btn" onclick="completeReport(${report.id})">Tamamlandı</button> <button class="reject-btn" onclick="rejectReport(${report.id})">İptal Et</button>`;
      } else if (statusKey === 'TAMAMLANDI' || statusKey === 'IPTAL') {
        const disabledAttr = report.hasJobReport ? 'disabled title="Bu rapora iş raporu eklenmiş"' : `onclick="openJobReportModal(${report.id})"`;
        const disabledStyle = report.hasJobReport ? 'style="opacity:.6;cursor:not-allowed;"' : '';
        actionButtonsHtml = `<button class="jobreport-btn" ${disabledAttr} ${disabledStyle}>Rapor Ekle</button>`;
      }
       else if (statusKey === 'REDDEDILDI') {
              actionButtonsHtml = `<button class="jobreport-btn" onclick="openJobReportModal(${report.id})">Rapor Ekle</button>`;
            }

      row.innerHTML = `
        <td class="col-id">${report.id ?? '-'}</td>
        <td>${report.gonderen_isim ?? ''}</td>
        <td>${report.header ?? ''}</td>
        <td>${report.body ?? ''}</td>
        <td><span class="status-badge ${statusClass}">${statusText}</span></td>
        <td>${report.department || ''}</td>
       <td>
         <div class="date-detail-wrapper">
           <button class="date-detail-btn">Detay</button>
           <div class="date-detail-tooltip">
             <div><strong>Atandı:</strong> ${fmt(report.talep_date ?? '-')}</div>
             <div><strong>Kabul:</strong> ${fmt(report.kabul_date ?? '-')}</div>
             <div><strong>Reddedildi:</strong> ${fmt(report.iptal_date ?? '-')}</div>
             <div><strong>Tamamlandı:</strong> ${fmt(report.bitis_date ?? '-')}</div>
           </div>
         </div>
       </td>

        <td>${actionButtonsHtml}</td>`;
      tbody.appendChild(row);
    });
  }

  function acceptReport(reportId) {
    const actorId = CURRENT_USER_ID;
    if (!confirm("Bu görevi kabul etmek istediğinize emin misiniz?")) return;

    fetch(`http://localhost:8080/personal-dashboard/reports/${reportId}/accept?actorId=${actorId}`, { method: 'POST' })
      .then(res => !res.ok ? Promise.reject("Sunucu hatası") : parseJsonSafe(res))
      .then(updatedReport => {
        if (updatedReport) upsertReportInList(updatedReport);
        alert(`Görev ${reportId} kabul edildi.`);
        return fetchAssignedReports();
      })
      .catch(e => {
        console.error(e);
        alert("Görevi kabul ederken hata oluştu.");
      });
  }

  function rejectReport(reportId) {
    const actorId = CURRENT_USER_ID;
    if (!confirm("Bu görevi iptal etmek istediğinize emin misiniz?")) return;

    fetch(`http://localhost:8080/personal-dashboard/reports/${reportId}/reject?actorId=${actorId}`, { method: 'POST' })
      .then(res => !res.ok ? Promise.reject("Sunucu hatası") : parseJsonSafe(res))
      .then(updatedReport => {
        if (updatedReport) upsertReportInList(updatedReport);
        alert(`Görev ${reportId} iptal edildi.`);
        return fetchAssignedReports();
      })
      .catch(e => {
        console.error(e);
        alert("Görevi iptal ederken hata oluştu.");
      });
  }

    function deactivateReport(reportId) {
      if (!confirm("Bu raporu listeden kaldırmak istediğinize emin misiniz?")) return;
      fetch(`http://localhost:8080/personal-dashboard/reports/${reportId}/deactivate`, { method: 'POST' })
      .then(res => res.json())
      .then(updatedReport => {
          alert(`Rapor ${reportId} kaldırıldı.`);
          updateReportAndRefresh(updatedReport);
      });
    }

  function completeReport(reportId) {
    const actorId = CURRENT_USER_ID;
    if (!confirm("Bu görevi tamamlamak istediğinize emin misiniz?")) return;

    fetch(`http://localhost:8080/personal-dashboard/reports/${reportId}/complete?actorId=${actorId}`, { method: 'POST' })
      .then(res => !res.ok ? Promise.reject('Sunucuda hata oluştu') : parseJsonSafe(res))
      .then(updatedReport => {
        if (updatedReport) upsertReportInList(updatedReport);
        alert(`Görev ${reportId} tamamlandı.`);
        return fetchAssignedReports();
      })
      .catch(err => {
        console.error(err);
        alert('Tamamlama sırasında bir hata oluştu.');
      });
  }

  function raportamamlandıvesiliyorum(reportId) {
    fetch(`http://localhost:8080/personal-dashboard/reports/${reportId}/deactivate`, { method: 'POST', headers: { 'Content-Type': 'application/json' } })
      .then(res => res.ok ? parseJsonSafe(res) : Promise.reject('Sunucuda hata oluştu'))
      .then(updatedReport => {
        const idx = reports.findIndex(r => Number(r.id) === Number(updatedReport?.id ?? reportId));
        if (idx !== -1) {
          reports[idx].isActivePersonel = false;
        }
        alert(`Görev ${reportId} için rapor eklendi ve listenizden kaldırıldı.`);
        renderTable();
      })
      .catch(err => {
        console.error(err);
        alert('Silme sırasında bir hata oluştu.');
      });
  }

  function openJobReportModal(reportId) {
    const old = document.getElementById('jobReportModal');
    if (old) old.remove();

    const overlay = document.createElement('div');
    overlay.id = 'jobReportModal';
    overlay.innerHTML = `
      <div style="position:fixed; inset:0; background:rgba(0,0,0,0.35); display:flex; align-items:center; justify-content:center; z-index:9999;">
        <div style="background:#fff; padding:16px; width:520px; max-width:90%; border-radius:8px; box-shadow:0 6px 24px rgba(0,0,0,.2)">
          <h3 style="margin-top:0">İş Raporu Ekle</h3>
          <div style="display:flex; flex-direction:column; gap:8px">
            <label>Başlık<input id="jr-title" type="text" style="width:97%; padding:8px" placeholder="Rapor Başlığı"></label>
            <label>Metin<textarea id="jr-content" rows="6" style="width:97%; padding:8px" placeholder="Rapor Açıklaması"></textarea></label>
          </div>
          <div style="margin-top:12px; display:flex; gap:8px; justify-content:flex-end">
            <button class="modal-cancel-btn" onclick="closeJobReportModal()">Vazgeç</button>
            <button class="modal-save-btn" onclick="submitJobReport(${reportId})">Kaydet</button>
          </div>
        </div>
      </div>`;
    document.body.appendChild(overlay);
  }

  function closeJobReportModal() {
    const el = document.getElementById('jobReportModal');
    if (el) el.remove();
  }

  function submitJobReport(reportId) {
    const title = (document.getElementById('jr-title')?.value || '').trim();
    const content = (document.getElementById('jr-content')?.value || '').trim();
    if (!title || !content) return alert("Başlık ve Metin zorunludur.");

    fetch(`http://localhost:8080/personal-dashboard/reports/${reportId}/job-report`, {
      method: 'POST',
      headers: { 'Content-Type': 'application/json' },
      body: JSON.stringify({ header: title, body: content })
    })
    .then(async res => {
      if (res.status === 409) {
        markJobReportAdded(reportId);
        throw new Error(await res.text() || 'Bu rapor için zaten bir İş Raporu mevcut.');
      }
      if (!res.ok) throw new Error(await res.text() || 'Sunucu hatası');
      return parseJsonSafe(res);
    })
    .then(() => {
      closeJobReportModal();
      alert('İş Raporu kaydedildi.');
      raportamamlandıvesiliyorum(reportId);
      markJobReportAdded(reportId);
    })
    .catch(err => {
      console.error(err);
      alert(`Kaydetme başarısız: ${err.message}`);
    });
  }

  function changePassword() {
    window.location.href = '/user-dashboard/change-password';
  }

  function logout() {
    localStorage.removeItem("companyId");
    localStorage.removeItem("userId");
    window.location.href = "http://localhost:8080/task/login";
  }
}