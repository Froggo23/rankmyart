// In auth.js
function getLoggedInUser() {
    const userCookie = document.cookie.split('; ').find(row => row.startsWith('username='));
    return userCookie ? userCookie.split('=')[1] : null;
}

document.addEventListener('DOMContentLoaded', async () => {
    const body = document.body;
    const userCookie = document.cookie.split(';').find(c => c.trim().startsWith('username='));
    const isLoggedIn = !!userCookie;

    if (isLoggedIn) {
        body.classList.add('logged-in');
        body.classList.remove('logged-out');

        // Fetch user data to update profile pictures
        try {
            const response = await fetch('/user'); // This endpoint is in UserRestController
            if (response.ok) {
                const user = await response.json();

                const headerProfileImg = document.getElementById('header-profile-img');
                if (headerProfileImg && user.profileImg) {
                    headerProfileImg.src = user.profileImg;
                }

                const commentFormAvatar = document.getElementById('current-user-comment-avatar');
                if (commentFormAvatar && user.profileImg) {
                    commentFormAvatar.src = user.profileImg;
                }
            }
        } catch (error) {
            console.error('Could not fetch user for header update:', error);
        }

    } else {
        body.classList.add('logged-out');
        body.classList.remove('logged-in');
    }
});


function getLoggedInUser() {
    const userCookie = document.cookie.split('; ').find(row => row.startsWith('username='));
    return userCookie ? userCookie.split('=')[1] : null;
}
