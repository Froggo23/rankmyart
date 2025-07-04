document.addEventListener('DOMContentLoaded', () => {
    const body = document.body;
    const isLoggedIn = document.cookie.split(';').some((item) => item.trim().startsWith('username='));

    if (isLoggedIn) {
        body.classList.add('logged-in');
        body.classList.remove('logged-out');
    } else {
        body.classList.add('logged-out');
        body.classList.remove('logged-in');
    }
});