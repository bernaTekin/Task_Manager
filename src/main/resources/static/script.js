document.addEventListener('DOMContentLoaded', () => {
    const loginForm = document.getElementById('login-form');
    const messageDiv = document.getElementById('message');

    loginForm.addEventListener('submit', async (e) => {
        e.preventDefault();

        const companyId = document.getElementById('company_id').value;
        const password = document.getElementById('password').value;

        const data = {
            company_id: parseInt(companyId),
            password: password
        };

        console.log('Gönderilecek veri:', data);

        try {

            const response = await fetch('http://localhost:8080/task/login', {
                method: 'POST',
                headers: {
                    'Content-Type': 'application/json',
                },
                body: JSON.stringify(data),
            });

            if (response.ok) {
                const result = await response.json();


                localStorage.setItem("companyId", result.companyId);

                if (result.redirectUrl) {
                    window.location.href = result.redirectUrl;
                } else {
                    messageDiv.textContent = 'Giriş başarılı!';
                    messageDiv.style.color = 'green';
                }
            } else {
                const errorText = await response.text();
                messageDiv.textContent = `Giriş başarısız: ${errorText}`;
                messageDiv.style.color = 'red';
            }
        } catch (error) {
            messageDiv.textContent = 'Bir hata oluştu: ' + error.message;
            messageDiv.style.color = 'red';
        }
    });
});