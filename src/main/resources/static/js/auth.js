function getLoggedInUser() {
    const cookies = document.cookie.split(';').map(cookie => cookie.trim());
    const userCookie = cookies.find(cookie => cookie.startsWith('username='));
    return userCookie ? userCookie.split('=')[1] : null;
}

document.addEventListener('DOMContentLoaded', () => {
    const body = document.body;
    const isLoggedIn = !!getLoggedInUser();

    if (isLoggedIn) {
        body.classList.add('logged-in');
        body.classList.remove('logged-out');
    } else {
        body.classList.add('logged-out');
        body.classList.remove('logged-in');
    }
});