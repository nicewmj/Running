var loginUser = JSON.parse(sessionStorage.getItem("loginUser"));
if (!loginUser) {
    location.href = 'error/noLogin.html';
}