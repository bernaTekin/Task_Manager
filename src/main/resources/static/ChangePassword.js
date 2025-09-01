function submitPasswordChange() {
  const companyId = localStorage.getItem("companyId");
  const oldPassword = document.getElementById("oldPassword").value.trim();
  const newPassword = document.getElementById("newPassword").value.trim();
  const message = document.getElementById("message");

  if (!companyId) {
    message.textContent = "Kullanıcı oturumu bulunamadı. Lütfen tekrar giriş yapın.";
    message.style.color = "red";
    return;
  }

  if (!oldPassword || !newPassword) {
    message.textContent = "Lütfen her iki alanı da doldurun.";
    message.style.color = "red";
    return;
  }

  fetch("http://localhost:8080/user-dashboard/change-password", {
    method: "POST",
    headers: { "Content-Type": "application/json" },
    body: JSON.stringify({
      companyId: companyId,
      oldPassword: oldPassword,
      newPassword: newPassword
    })
  })
  .then(response => {
    if (!response.ok) throw new Error("Şifre değiştirilemedi");
    return response.text();
  })
  .then(text => {
    message.textContent = "✅ Şifreniz başarıyla değiştirildi. Giriş ekranına yönlendiriliyorsunuz...";
    message.style.color = "green";
    document.getElementById("oldPassword").value = "";
    document.getElementById("newPassword").value = "";

    setTimeout(() => {
      logout();
    }, 2000);
  })
  .catch(err => {
    message.textContent = "Hata: " + err.message;
    message.style.color = "red";
  });
}
function logout() {
  localStorage.removeItem("companyId");
  window.location.href = "http://localhost:8080/task/login";
}
