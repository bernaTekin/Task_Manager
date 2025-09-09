// === KİMLİK DOĞRULAMA KONTROLÜ ===
const loggedInCompanyId = localStorage.getItem("companyId");
const loggedInUserId = localStorage.getItem("userId");

if (!loggedInCompanyId && !loggedInUserId) {
  window.location.href = "http://localhost:8080/task/login";
} else {

  function getLoggedIncompanyId() {
    return parseInt(localStorage.getItem("companyId")) || null;
  }

  function submitRequest() {
    const header = document.getElementById('taskHeader').value.trim();
    const body = document.getElementById('requestInput').value.trim();
    const companyId = getLoggedIncompanyId();

    if (!companyId) {
      alert("Kullanıcı oturumu bulunamadı. Lütfen tekrar giriş yapın.");
      window.location.href = "http://localhost:8080/task/login";
      return;
    }

    if (!header || !body) {
      alert("Lütfen başlık ve içerik girin.");
      return;
    }

    if (header.length > 255) {
      alert("Başlık alanı 255 karakterden uzun olamaz.");
      return;
    }

    if (body.length > 255) {
      alert("İçerik alanı 255 karakterden uzun olamaz.");
      return;
    }

    fetch('/user-dashboard/new', {
      method: 'POST',
      headers: { 'Content-Type': 'application/json' },
      body: JSON.stringify({
        header: header,
        body: body,
        companyId: companyId
      })
    })
    .then(response => {
      if (response.status === 200 || response.status === 201) {
        alert("Talep başarıyla gönderildi.");
        document.getElementById('taskHeader').value = '';
        document.getElementById('requestInput').value = '';
      } else {
        return response.text().then(msg => {
          alert("Gönderme başarısız oldu: " + msg);
        });
      }
    })
    .catch(err => {
      console.error("Sunucu hatası:", err);
      alert("Sunucuya bağlanılamadı.");
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
function showNewRequest() {
    document.getElementById('newRequestContainer').style.display = 'block';
    document.getElementById('my-requests-content').style.display = 'none';
}

function openMyRequests() {
    document.getElementById('newRequestContainer').style.display = 'none';
    document.getElementById('my-requests-content').style.display = 'block';
    loadMyRequests();
}

function loadMyRequests() {
    const companyId = localStorage.getItem("companyId");

    fetch(`http://localhost:8080/user-dashboard/requests?companyId=${companyId}`)
        .then(response => response.json())
        .then(data => {
            const table = document.querySelector("#my-requests-content table");
            const tbody = document.createElement("tbody");
            tbody.innerHTML = ""; // önceki satırları temizle


            const filteredData = data.filter(r => r.activeAdmin !== false);

            filteredData.forEach(r => {
                const tr = document.createElement("tr");

                const dateCellContent = `
                    <button class="date-detail-btn">Detaylar</button>
                    <div class="date-hover-details">
                        <div><strong>Talep:</strong> ${r.talep_date ? new Date(r.talep_date).toLocaleString() : '-'}</div>
                        <div><strong>Atanma:</strong> ${r.atanma_date ? new Date(r.atanma_date).toLocaleString() : '-'}</div>
                        <div><strong>Kabul:</strong> ${r.kabul_date ? new Date(r.kabul_date).toLocaleString() : '-'}</div>
                        <div><strong>Bitiş:</strong> ${r.bitis_date ? new Date(r.bitis_date).toLocaleString() : '-'}</div>
                        <div><strong>İptal:</strong> ${r.iptal_date ? new Date(r.iptal_date).toLocaleString() : '-'}</div>
                    </div>
                `;

                tr.innerHTML = `
                    <td style="text-align:center;">${r.id}</td>
                    <td>${r.header}</td>
                    <td>${r.body}</td>
                    <td class="status-cell"><span class="status-badge status-${r.status}">${r.status}</span></td>
                    <td class="date-cell">${dateCellContent}</td>
                `;

                tbody.appendChild(tr);
            });

            // önceki tbody varsa kaldır
            const oldTbody = table.querySelector("tbody");
            if (oldTbody) table.removeChild(oldTbody);
            table.appendChild(tbody);

            // ID’ye göre büyükten küçüğe sırala
            sortTableById("desc");

            document.getElementById("my-requests-content").style.display = "block";
        })
        .catch(err => console.error("Talepler yüklenirken hata oluştu:", err));
}


const idSortMenu = document.getElementById("idSortMenu");


// Menü seçenekleri tıklandığında sıralama yapacak
document.querySelectorAll("#idSortMenu .sortOption").forEach(option => {
    option.addEventListener("click", function () {
        const order = this.getAttribute("data-order");
        sortTableById(order); // sıralama fonksiyonunu çağır
        document.getElementById("idSortMenu").style.display = "none"; // menüyü kapat
    });
});

// ID sütununa göre tabloyu sıralayan fonksiyon
function sortTableById(order) {
    const table = document.querySelector("#my-requests-content table");
    const tbody = table.querySelector("tbody");
    if (!tbody) return;

    Array.from(tbody.rows)
        .sort((a, b) => {
            // Hücre değerini trimleyip parseInt ile al, NaN ise 0 yap
            let valA = parseInt(a.cells[0].textContent.trim()) || 0;
            let valB = parseInt(b.cells[0].textContent.trim()) || 0;
            return order === "asc" ? valA - valB : valB - valA;
        })
        .forEach(row => tbody.appendChild(row));
}



}






