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
function openMyRequests() {
    document.getElementById('newRequestContainer').style.display = 'none';
    document.getElementById('my-requests-content').style.display = 'block';
}
function showNewRequest() {
    // Talep oluşturma bölümünü göster
    const newRequest = document.getElementById('newRequestContainer');
    newRequest.style.display = 'block';

    // Diğer bölümleri gizle
    const other = document.getElementById('otherSection');
    if (other) other.style.display = 'none';

}

}







