// === GLOBAL FETCH HATASI YAKALAYICI ===

const originalFetch = window.fetch;
window.fetch = async (...args) => {
    const response = await originalFetch(...args);

    if (response.status === 401 || response.status === 403) {
        alert("Oturumunuz sona erdi veya bu alana erisim yetkiniz yok. Lutfen tekrar giris yapin.");
        window.location.href = "http://localhost:8080/task/login";
        return new Promise(() => {});
    }

    return response;
};



// === KİMLİK DOĞRULAMA KONTROLÜ ===
const loggedInCompanyId = localStorage.getItem("companyId");
const loggedInUserId = localStorage.getItem("userId");

if (!loggedInCompanyId && !loggedInUserId) {
    window.location.href = "http://localhost:8080/task/login";
} else {

    // === GLOBAL DEĞİŞKENLER ===
    let reports = [];
    let personnelList = [];
    let monthlyReportsData = [];
    let allUsersList = [];
    let isEditMode = false;
    let taskStatusChart = null;
    let assignedPersonnelTaskChart = null;
    let departmentTaskChart = null;

    const CURRENT_USER_ID = parseInt(localStorage.getItem("userId"), 10) || 1;


    // === SAYFA NAVİGASYONU VE GÖRÜNÜM ===
    function showPage(pageId) {
        document.querySelectorAll('.page-content').forEach(page => {
            page.style.display = 'none';
        });
        const pageToShow = document.getElementById(pageId);
        if (pageToShow) {
            pageToShow.style.display = 'block';
        }
    }

    function handleHomepageClick() {
        const homepage = document.getElementById('homepage-content');
        if (homepage && homepage.style.display === 'block') {
            location.reload();
        } else {
            showPage('homepage-content');
        }
    }


    // === GRAFİK OLUŞTURMA ===

    function createAllCharts() {
        createTaskStatusChart();
        createAssignedPersonnelChart();
        createDepartmentChart();
    }

    function createTaskStatusChart() {
        if (reports.length === 0) return;
        const statusCounts = reports.reduce((acc, report) => {
            if (isReportActive(report)) {
                acc[report.status] = (acc[report.status] || 0) + 1;
            }
            return acc;
        }, {});
        const statusColors = {'BEKLIYOR': '#ff7237', 'ATANDI': '#874fff', 'AKTIF': '#00b6ff', 'TAMAMLANDI': '#24cb71', 'IPTAL': '#ff3737', 'REDDEDILDI': '#a0a0a0'};
        const labels = Object.keys(statusCounts);
        const data = Object.values(statusCounts);
        const backgroundColors = labels.map(label => statusColors[label] || '#000000');
        const ctx = document.getElementById('taskStatusChart').getContext('2d');
        if (taskStatusChart) taskStatusChart.destroy();
        taskStatusChart = new Chart(ctx, {
            type: 'doughnut', data: { labels, datasets: [{ label: 'Görev Sayısı', data, backgroundColor: backgroundColors, borderColor: '#fff', borderWidth: 2 }] },
            options: { responsive: true, maintainAspectRatio: false, plugins: { legend: { position: 'top' }, title: { display: true, text: 'Görevlerin Durum Dağılımı', font: { size: 14 } } } }
        });
    }

    function createAssignedPersonnelChart() {
        if (reports.length === 0 || personnelList.length === 0) return;
        const assignedCounts = reports.reduce((acc, report) => {
            if (isReportActive(report) && report.personalId) {
                acc[report.personalId] = (acc[report.personalId] || 0) + 1;
            }
            return acc;
        }, {});
        const personnelIds = Object.keys(assignedCounts);
        const labels = personnelIds.map(personalId => {
            const personnel = personnelList.find(p => p.companyId == personalId);
            return personnel ? `${personnel.name} ${personnel.surName}` : `Bilinmeyen (${personalId})`;
        });
        const data = Object.values(assignedCounts);
        const personnelColors = { '2222': '#24cb71', '7777': '#00b6ff', '105': '#6c757d' };
        const defaultColor = '#cccccc';
        const backgroundColors = personnelIds.map(id => personnelColors[id] || defaultColor);
        const ctx = document.getElementById('assignedPersonnelChart').getContext('2d');
        if (assignedPersonnelTaskChart) assignedPersonnelTaskChart.destroy();
        assignedPersonnelTaskChart = new Chart(ctx, {
            type: 'doughnut', data: { labels, datasets: [{ label: 'Atanan Görev Sayısı', data, backgroundColor: backgroundColors, borderColor: '#fff', borderWidth: 2 }] },
            options: { responsive: true, maintainAspectRatio: false, plugins: { legend: { position: 'top' }, title: { display: true, text: 'Personele Göre Görev Dağılımı', font: { size: 14 } } } }
        });
    }

    function createDepartmentChart() {
        if (reports.length === 0) return;
        const departmentCounts = reports.reduce((acc, report) => {
            if (isReportActive(report) && report.department) {
                acc[report.department] = (acc[report.department] || 0) + 1;
            }
            return acc;
        }, {});
        const departmentColors = { 'YAZILIM': '#00b6ff', 'DONANIM': '#24cb71', 'HEPSI': '#874fff' };
        const labels = Object.keys(departmentCounts);
        const data = Object.values(departmentCounts);
        const backgroundColors = labels.map(label => departmentColors[label] || '#adb5bd');
        const ctx = document.getElementById('departmentChart').getContext('2d');
        if (departmentTaskChart) departmentTaskChart.destroy();
        departmentTaskChart = new Chart(ctx, {
            type: 'doughnut', data: { labels, datasets: [{ label: 'Görev Sayısı', data, backgroundColor: backgroundColors, borderColor: '#fff', borderWidth: 2 }] },
            options: { responsive: true, maintainAspectRatio: false, plugins: { legend: { position: 'top' }, title: { display: true, text: 'Departmana Göre Görev Dağılımı', font: { size: 14 } } } }
        });
    }


    // === YARDIMCI FONKSİYONLAR ===
    function isReportActive(r) {
      const flag = (r.is_active_admin ?? r.active);
      return flag !== false;
    }
    function ts(v) {
      if (!v) return -Infinity;
      const d = new Date(v);
      return isNaN(d.getTime()) ? -Infinity : d.getTime();
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


    // === GÖREV YÖNETİMİ TABLOSU VE FİLTRELEME (tarih filtreleri kaldırıldı) ===
    function renderTable(reportsToRender) {
      const tbody = document.getElementById('reportBody');
      tbody.innerHTML = '';
      const visibleReports = reportsToRender.filter(isReportActive).sort((a, b) => ts(b.createdAt) - ts(a.createdAt));
      if (visibleReports.length === 0) {
          tbody.innerHTML = '<tr><td colspan="10">Kayıt bulunamadı.</td></tr>';
          return;
      }
      visibleReports.forEach(report => {
        const row = document.createElement('tr');
        const statusClass = `status-${report.status}`;
        let assignedPersonnelName = 'Atanmamış';
        if (report.personalId && personnelList.length > 0) {
          const match = personnelList.find(p => Number(p.companyId) === Number(report.personalId));
          if (match) assignedPersonnelName = `${match.name} ${match.surName}`;
        }
        let actionButtonsHtml = '';
        if (report.status === 'REDDEDILDI') {
          actionButtonsHtml = `<button class="approve-btn" onclick="approveCancellation(${report.id})">İptal Talebini Onayla</button><button class="reject-btn" onclick="rejectCancellation(${report.id})">İptal Talebini Reddet</button>`;
        } else {
          actionButtonsHtml = `<button class="complete-btn" onclick="completeReport(${report.id})">Tamamlandı</button><button class="reject-btn" onclick="rejectReport(${report.id})">Talebi Reddet</button><button class="delete-btn" onclick="deactivateReport(${report.id})">Sil</button>`;
        }
        row.innerHTML = `
          <td class="col-id">${report.id}</td><td>${report.header}</td><td>${report.gonderen_isim}</td><td>${report.body}</td><td><span class="status-badge ${statusClass}">${report.status}</span></td><td>${report.department || '-'}</td> <td><strong>${assignedPersonnelName}</strong></td>
          <td><div><strong>Oluşturuldu:</strong> ${fmt(report.createdAt)}</div><div><strong>Atandı:</strong> ${fmt(report.assignedAt)}</div><div><strong>Tamamlandı:</strong> ${fmt(report.completedAt)}</div><div><strong>Reddedildi:</strong> ${fmt(report.rejectedAt)}</div></td>
          <td><select id="departmentSelect-${report.id}" style="margin-bottom: 5px;"><option value="YAZILIM">Yazılım</option><option value="DONANIM">Donanım</option><option value="HEPSI">Hepsi</option></select><select id="personnelSelect-${report.id}"><option value="">Seçiniz</option>${personnelList.map(p => `<option value="${p.companyId}" ${p.id === report.personalId ? 'selected' : ''}>${p.name} ${p.surName} (${p.companyId})</option>`).join('')}</select><button class="assign-btn" onclick="assignPersonnel(${report.id})">Tamam</button></td>
          <td>${actionButtonsHtml}</td>`;
        tbody.appendChild(row);
      });
    }

    function populatePersonnelFilter() {
        const select = document.getElementById('filterPersonnel');
        select.innerHTML = '<option value="">Tümü</option><option value="UNASSIGNED">Atanmamış</option>';
        personnelList.forEach(p => {
            const option = document.createElement('option');
            option.value = p.companyId;
            option.textContent = `${p.name} ${p.surName}`;
            select.appendChild(option);
        });
    }

    // Tarih alanları ve kontrolleri kaldırıldı
    function applyFiltersAndRender() {
        const status = document.getElementById('filterStatus').value;
        const sender = document.getElementById('filterSender').value.toLowerCase();
        const department = document.getElementById('filterDepartment').value;
        const personnelId = document.getElementById('filterPersonnel').value;

        const filteredReports = reports.filter(report => {
            if (status && report.status !== status) return false;
            if (sender && !report.gonderen_isim.toLowerCase().includes(sender)) return false;
            if (department && report.department !== department) return false;
            if (personnelId) {
                if (personnelId === 'UNASSIGNED' && report.personalId) return false;
                if (personnelId !== 'UNASSIGNED' && Number(report.personalId) !== Number(personnelId)) return false;
            }
            return true;
        });

        renderTable(filteredReports);
    }

    function clearFiltersAndRender() {
        document.getElementById('filterStatus').value = '';
        document.getElementById('filterSender').value = '';
        document.getElementById('filterDepartment').value = '';
        document.getElementById('filterPersonnel').value = '';
        applyFiltersAndRender();
    }

    // === RAPOR CRUD İŞLEMLERİ ===
    function updateReportAndRefresh(updatedReport) {
        const idx = reports.findIndex(r => r.id === updatedReport.id);
        if(idx !== -1) {
            reports[idx] = updatedReport;
        }
        applyFiltersAndRender();
        createAllCharts();
    }

    function assignPersonnel(reportId) {
      const select = document.getElementById(`personnelSelect-${reportId}`);
      const personalId = Number(select.value);
      const departmentSelect = document.getElementById(`departmentSelect-${reportId}`);
      const department = departmentSelect.value;
      if (!personalId || !department) {
          alert("Lütfen personel ve departman seçin.");
          return;
      }
      fetch(`http://localhost:8080/admin-dashboard/reports/${reportId}/assign?personalId=${personalId}&department=${department}`, { method: 'POST' })
      .then(res => res.json())
      .then(updatedReport => {
          alert(`Görev ${reportId} ATANDI.`);
          updateReportAndRefresh(updatedReport);
      });
    }

    function rejectReport(reportId) {
      if (!confirm("Bu görevi reddetmek istediğinize emin misiniz?")) return;
      fetch(`http://localhost:8080/admin-dashboard/reports/${reportId}/reject`, { method: 'POST' })
      .then(res => res.json())
      .then(updatedReport => {
          alert(`Görev ${reportId} IPTAL.`);
          updateReportAndRefresh(updatedReport);
      });
    }

    function deactivateReport(reportId) {
      if (!confirm("Bu raporu listeden kaldırmak istediğinize emin misiniz?")) return;
      fetch(`http://localhost:8080/admin-dashboard/reports/${reportId}/deactivate`, { method: 'POST' })
      .then(res => res.json())
      .then(updatedReport => {
          alert(`Rapor ${reportId} kaldırıldı.`);
          updateReportAndRefresh(updatedReport);
      });
    }

    function completeReport(reportId) {
      if (!confirm("Bu görevi tamamlamak istediğinize emin misiniz?")) return;
      fetch(`http://localhost:8080/admin-dashboard/reports/${reportId}/complete?actorId=${CURRENT_USER_ID}`, { method: 'POST' })
      .then(res => res.json())
      .then(updatedReport => {
          alert(`Görev ${reportId} tamamlandı.`);
          updateReportAndRefresh(updatedReport);
      });
    }

    function approveCancellation(reportId) {
      if (!confirm("Personelin iptal talebini onaylamak istediğinize emin misiniz?")) return;
      fetch(`http://localhost:8080/admin-dashboard/reports/${reportId}/approve-cancellation`, { method: 'POST' })
      .then(res => res.json())
      .then(updatedReport => {
          alert(`İptal talebi onaylandı.`);
          updateReportAndRefresh(updatedReport);
      });
    }

    function rejectCancellation(reportId) {
      if (!confirm("Personelin iptal talebini reddetmek istediğinize emin misiniz?")) return;
      fetch(`http://localhost:8080/admin-dashboard/reports/${reportId}/reject-cancellation`, { method: 'POST' })
      .then(res => res.json())
      .then(updatedReport => {
          alert(`İptal talebi reddedildi.`);
          updateReportAndRefresh(updatedReport);
      });
    }


    // === AYLIK RAPORLAR MODALI (tarih filtreleri kaldırıldı) ===
    const MONTHLY_REPORTS_URL = "http://localhost:8080/admin-dashboard/job-reports/last-month";
    function openMonthlyReports() {
      const modal = document.getElementById('monthlyModal'); const tbody = document.getElementById('monthlyTbody');
      const loading = document.getElementById('monthlyLoading'); const error = document.getElementById('monthlyError');
      error.hidden = true; loading.hidden = false; tbody.innerHTML = ''; modal.classList.add('open');
      fetch(MONTHLY_REPORTS_URL).then(res => res.ok ? res.json() : Promise.reject(`HTTP ${res.status}`)).then(list => {
          monthlyReportsData = Array.isArray(list) ? list : []; clearMonthlyFiltersAndRender(); loading.hidden = true;
      }).catch(err => { console.error(err); loading.hidden = true; error.hidden = false; error.textContent = 'Raporlar alınamadı.'; });
    }
    function closeMonthlyModal() { document.getElementById('monthlyModal').classList.remove('open'); }
    function renderMonthlyRows(items) {
      const tbody = document.getElementById('monthlyTbody'); tbody.innerHTML = '';
      const filteredItems = items.filter(r => r.personelRaporBaslik || r.personelRaporIcerik);
      if(filteredItems.length === 0) { tbody.innerHTML = `<tr><td colspan="14">Kayıt bulunamadı.</td></tr>`; return; }
      filteredItems.forEach(r => {
        const tr = document.createElement('tr');
        tr.innerHTML = `<td class="col-id">${r.reportId ?? '-'}</td><td>${r.gonderenIsmi ?? '-'}</td><td>${r.header ?? '-'}</td><td>${r.body ?? '-'}</td><td>${r.raporuYazanAdSoyad ?? '-'}</td><td>${r.personelRaporBaslik ?? '-'}</td><td>${r.personelRaporIcerik ?? '-'}</td><td><span class="status-badge status-${r.status || ''}">${r.status ?? '-'}</span></td><td>${r.department ?? '-'}</td><td>${fmt(r.atanmaTarihi)}</td><td>${fmt(r.kabulTarihi)}</td><td>${fmt(r.iptalTarihi)}</td><td>${fmt(r.bitisTarihi)}</td><td><button onclick="previewPdf(${r.reportId})">PDF</button></td>`;
        tbody.appendChild(tr);
      });

    }
    function applyMonthlyFiltersAndRender() {
        const status = document.getElementById('monthlyFilterStatus').value;
        const sender = document.getElementById('monthlyFilterSender').value.toLowerCase();
        const writer = document.getElementById('monthlyFilterWriter').value.toLowerCase();
        const department = document.getElementById('monthlyFilterDepartment').value;

        const filteredReports = monthlyReportsData.filter(report => {
            if (status && report.status !== status) return false;
            if (department && report.department !== department) return false;
            if (sender && !(report.gonderenIsmi || '').toLowerCase().includes(sender)) return false;
            if (writer && !(report.raporuYazanAdSoyad || '').toLowerCase().includes(writer)) return false;
            return true;
        });

        renderMonthlyRows(filteredReports);
    }
    function clearMonthlyFiltersAndRender() {
        document.getElementById('monthlyFilterStatus').value = '';
        document.getElementById('monthlyFilterSender').value = '';
        document.getElementById('monthlyFilterWriter').value = '';
        document.getElementById('monthlyFilterDepartment').value = '';
        applyMonthlyFiltersAndRender();
    }


    // === PDF ÖNİZLEME ===
    function openPdfPreviewModal() { document.getElementById('pdfPreviewModal').classList.add('open'); }
    function closePdfPreviewModal() { const modal = document.getElementById('pdfPreviewModal'); document.getElementById('pdf-viewer').src = 'about:blank'; modal.classList.remove('open'); }
    function previewPdf(reportId) {
        const report = monthlyReportsData.find(r => r.reportId === reportId); if (!report) { alert("Rapor verisi bulunamadı!"); return; }
        const { jsPDF } = window.jspdf; const doc = new jsPDF(); const tableColumn = ["Alan", "Detay"];
        const tableRows = [
            ["Rapor ID", String(report.reportId ?? '-')],
            ["Gönderen", report.gonderenIsmi ?? '-'],
            ["Talep Basligi", report.header ?? '-'],
            ["Talep Icerigi", report.body ?? '-'],
            ["Departman", report.department ?? '-'],
            ["Raporu Yazan", report.raporuYazanAdSoyad ?? '-'],
            ["Rapor Basligi", report.personelRaporBaslik ?? '-'],
            ["Rapor Icerigi", report.personelRaporIcerik ?? '-'],
            ["Durum", report.status ?? '-'],
            ["Atanma Tarihi", fmt(report.atanmaTarihi)],
            ["Kabul Tarihi", fmt(report.kabulTarihi)],
            ["Iptal Tarihi", fmt(report.iptalTarihi)],
            ["Bitis Tarihi", fmt(report.bitisTarihi)],
        ];
        doc.setFontSize(18); doc.text(`Is Raporu Detayi: ID ${report.reportId}`, 14, 22); doc.autoTable({ startY: 30, head: [tableColumn], body: tableRows, theme: 'grid' });
        document.getElementById('pdf-viewer').src = doc.output('datauristring'); openPdfPreviewModal();
    }


    // === KULLANICI İŞLEMLERİ MODALI ===
    function openAddUserModal() { document.getElementById('addUserModal').classList.add('open'); }
    function closeAddUserModal() { document.getElementById('addUserForm').reset(); document.getElementById('addUserModal').classList.remove('open'); }
    document.getElementById('addUserForm')?.addEventListener('submit', function(event) {
        event.preventDefault(); const newUser = { companyId: parseInt(document.getElementById('companyId').value, 10), name: document.getElementById('name').value, surName: document.getElementById('surName').value, password: document.getElementById('password').value, userRole: document.getElementById('userRole').value };
        if (!newUser.companyId || !newUser.name || !newUser.surName || !newUser.password || !newUser.userRole) { alert('Lütfen tüm alanları doldurun.'); return; }
        fetch('http://localhost:8080/admin-dashboard/users/add', { method: 'POST', headers: { 'Content-Type': 'application/json' }, body: JSON.stringify(newUser) })
        .then(response => response.ok ? response.json() : response.text().then(text => Promise.reject(text))).then(data => {
            alert(`Kullanıcı başarıyla oluşturuldu: ${data.name} ${data.surName}`); closeAddUserModal(); location.reload();
        }).catch(error => { console.error('Kullanıcı oluşturma hatası:', error); alert(`Hata: ${error}`); });
    });


    // === KULLANICI BİLGİLERİ MODALI ===
    function openUserInfoModal() {
        const modal = document.getElementById('userInfoModal'); const tbody = document.getElementById('userInfoTbody'); tbody.innerHTML = '<tr><td colspan="5">Yükleniyor...</td></tr>';
        fetch('http://localhost:8080/admin-dashboard/searchable-users').then(res => res.json()).then(data => {
                allUsersList = data; renderUserInfoTable(allUsersList); modal.classList.add('open');
        }).catch(err => { console.error("Kullanıcılar alınamadı:", err); tbody.innerHTML = '<tr><td colspan="5">Kullanıcılar yüklenirken bir hata oluştu.</td></tr>'; });
    }
    function closeUserInfoModal() {
        const modal = document.getElementById('userInfoModal');
        document.getElementById('userSearchInput').value = '';
        renderUserInfoTable(allUsersList);
        document.getElementById('editSaveUserBtn').textContent = 'Düzenle';
        document.getElementById('userInfoTable').classList.remove('editable-mode');
        isEditMode = false;
        modal.classList.remove('open');
    }
    function applyUserFilters() {
        const searchTerm = document.getElementById('userSearchInput').value.toLowerCase();

        if (!searchTerm) {
            renderUserInfoTable(allUsersList);
            return;
        }

        const filteredUsers = allUsersList.filter(user => {
            const name = (user.name || '').toLowerCase();
            const surName = (user.surName || '').toLowerCase();
            const companyId = String(user.companyId || '').toLowerCase();
            const userRole = (user.userRole || '').toLowerCase();

            return name.includes(searchTerm) ||
                   surName.includes(searchTerm) ||
                   companyId.includes(searchTerm) ||
                   userRole.includes(searchTerm);
        });

        renderUserInfoTable(filteredUsers);
    }
    function renderUserInfoTable(users) {
        const tbody = document.getElementById('userInfoTbody'); tbody.innerHTML = '';
        if (users.length === 0) { tbody.innerHTML = '<tr><td colspan="5">Filtreye uygun kullanıcı bulunamadı.</td></tr>'; return; }
        users.forEach(user => {
            const row = document.createElement('tr'); row.dataset.companyId = user.companyId;
            row.innerHTML = `
                        <td><input type="text" value="${user.name}" data-field="name" disabled></td>
                        <td><input type="text" value="${user.surName}" data-field="surName" disabled></td>
                        <td><input type="text" value="${user.companyId}" data-field="companyId" disabled></td>
                        <td><input type="text" value="${user.password}" data-field="password" disabled></td>
                        <td>
                            <select data-field="userRole" disabled>
                                <option value="USER" ${user.userRole === 'USER' ? 'selected' : ''}>USER</option>
                                <option value="PERSONEL" ${user.userRole === 'PERSONEL' ? 'selected' : ''}>PERSONEL</option>
                                <option value="ADMIN" ${user.userRole === 'ADMIN' ? 'selected' : ''}>ADMIN</option>
                            </select>
                        </td>
                        <td>
                            <button class="delete-user-btn" data-user-id="${user.companyId}" data-user-name="${user.name} ${user.surName}">&times;</button>
                        </td>
                    `;
                    tbody.appendChild(row);
                });
    }
    document.getElementById('editSaveUserBtn')?.addEventListener('click', function() {
        const table = document.getElementById('userInfoTable'); const inputs = table.querySelectorAll('input, select'); const btn = this; isEditMode = !isEditMode;
        if (isEditMode) { btn.textContent = 'Kaydet'; table.classList.add('editable-mode'); inputs.forEach(input => { if (input.dataset.field !== 'companyId') input.disabled = false; }); } else {
            btn.textContent = 'Kaydediliyor...'; btn.disabled = true;
            const updatedUsers = Array.from(table.querySelectorAll('tbody tr')).map(row => { const user = { companyId: row.dataset.companyId }; row.querySelectorAll('input, select').forEach(input => { user[input.dataset.field] = input.value; }); return user; });
            const savePromises = updatedUsers.map(user => fetch(`http://localhost:8080/admin-dashboard/users/update/${user.companyId}`, { method: 'PUT', headers: { 'Content-Type': 'application/json' }, body: JSON.stringify(user) }).then(res => res.ok ? res.json() : Promise.reject(`Kullanıcı ${user.companyId} güncellenemedi.`)));
            Promise.all(savePromises).then(results => {
                alert('Tüm değişiklikler başarıyla kaydedildi!');
                results.forEach(updatedUser => { const index = allUsersList.findIndex(u => u.companyId == updatedUser.companyId); if(index !== -1) allUsersList[index] = updatedUser; });
                applyUserFilters();
            }).catch(err => { console.error('Kaydetme hatası:', err); alert('Değişiklikler kaydedilirken bir hata oluştu.'); }).finally(() => {
                btn.textContent = 'Düzenle'; btn.disabled = false; table.classList.remove('editable-mode'); inputs.forEach(input => input.disabled = true);
            });
        }
    });

    document.getElementById('userInfoTbody')?.addEventListener('click', function(event) {
        if (event.target.classList.contains('delete-user-btn')) {
            const button = event.target;
            const companyId = button.dataset.userId;
            const userName = button.dataset.userName;

            if (confirm(`'${userName}' adlı kullanıcıyı (Şirket ID: ${companyId}) kalıcı olarak silmek istediğinizden emin misiniz?`)) {
                deleteUserByCompanyId(companyId);
            }
        }
    });

    async function deleteUserByCompanyId(companyId) {
        try {
            const response = await fetch(`/admin-dashboard/users/delete/${companyId}`, {
                method: 'DELETE'
            });

            if (response.ok) {
                alert('Kullanıcı başarıyla silindi.');

                const rowToDelete = document.querySelector(`#userInfoTbody tr[data-company-id="${companyId}"]`);
                if (rowToDelete) {
                    rowToDelete.remove();
                }

                allUsersList = allUsersList.filter(user => user.companyId != companyId);

            } else {
                let errorMessage = response.statusText;
                try {
                    const errorBody = await response.json();
                    if (errorBody && errorBody.message) {
                        errorMessage = errorBody.message;
                    }
                } catch (e) {
                    console.error("Hata yanıtı JSON formatında değil:", e);
                }
                alert(`Kullanıcı silinirken bir hata oluştu: ${errorMessage}`);
            }
        } catch (error) {
            console.error('Kullanıcı silme hatası:', error);
            alert('Sunucuya bağlanırken bir hata oluştu.');
        }
    }

    // === GENEL SAYFA İŞLEVLERİ VE BAŞLATMA ===

    function logout() {
      localStorage.removeItem("companyId");
      localStorage.removeItem("userId");
      window.location.href = "/task/logout";
    }
    function changePassword() {
      window.location.href = '/user-dashboard/change-password';
    }
    function toggleSidebar() {
        document.body.classList.toggle('sidebar-closed');
    }
    document.addEventListener('DOMContentLoaded', () => {
        showPage('homepage-content');

        if (localStorage.getItem('sidebar-closed') === 'true') {
            document.body.classList.add('sidebar-closed');
        }
        window.addEventListener('beforeunload', () => {
            localStorage.setItem('sidebar-closed', document.body.classList.contains('sidebar-closed'));
        });
        if (CURRENT_USER_ID) {
            fetch(`http://localhost:8080/admin-dashboard/user/${CURRENT_USER_ID}`)
                .then(res => res.ok ? res.json() : Promise.reject('Kullanıcı bulunamadı'))
                .then(user => { document.getElementById('admin-name').textContent = `${user.name} ${user.surName}`; })
                .catch(err => { console.error("Admin adı alınamadı:", err); document.getElementById('admin-name').textContent = 'Admin Paneli'; });
        }
        fetch("http://localhost:8080/admin-dashboard/all-users")
            .then(res => res.json())
            .then(data => {
                personnelList = data;
                populatePersonnelFilter();
            })
            .then(() => fetch("http://localhost:8080/admin-dashboard/all-reports"))
            .then(res => res.json())
            .then(data => {
                reports = data;
                renderTable(reports);
                createAllCharts();
            })
            .catch(err => console.error("Yükleme hatası:", err));

        document.getElementById('applyFilterBtn').addEventListener('click', applyFiltersAndRender);
        document.getElementById('clearFilterBtn').addEventListener('click', clearFiltersAndRender);
        document.getElementById('applyMonthlyFilterBtn').addEventListener('click', applyMonthlyFiltersAndRender);
        document.getElementById('clearMonthlyFilterBtn').addEventListener('click', clearMonthlyFiltersAndRender);
        document.getElementById('userSearchInput').addEventListener('input', applyUserFilters);
    });
}
