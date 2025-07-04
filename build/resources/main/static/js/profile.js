document.addEventListener('DOMContentLoaded', () => {
    const logoutButton = document.getElementById('logout-btn');

    if (logoutButton) {
        logoutButton.addEventListener('click', () => {
            // "Delete" the cookie by setting its expiration date to the past
            document.cookie = "username=; expires=Thu, 01 Jan 1970 00:00:00 UTC; path=/;";

            // Alert the user and redirect
            alert("You have been logged out.");
            window.location.href = '/gallery';
        });
    }
});