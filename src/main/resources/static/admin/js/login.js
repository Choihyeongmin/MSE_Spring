document.getElementById('login-form').addEventListener('submit', async function (e) {
    e.preventDefault();

    const credentials = {
        username: document.getElementById('username').value,
        password: document.getElementById('password').value
    };

    const response = await fetch('/user/login', {
        method: 'POST',
        headers: { 'Content-Type': 'application/json' },
        body: JSON.stringify(credentials)
    });

    if (response.ok) {
        const data = await response.json();
        localStorage.setItem('accessToken', data.accessToken);
        localStorage.setItem('refreshToken', data.refreshToken);
        alert('로그인 성공!');
        window.location.href = '/admin/index.html';
    } else {
        alert('로그인 실패!');
    }
});
