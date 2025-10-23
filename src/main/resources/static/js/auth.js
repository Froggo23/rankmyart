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

    // Initialize Google Sign-In
    initializeGoogleSignIn();
});

/**
 * Initialize Google Sign-In button and callback
 */
function initializeGoogleSignIn() {
    // Check if Google Identity Services library is loaded
    if (typeof google === 'undefined' || !google.accounts) {
        console.error('Google Identity Services library not loaded');
        return;
    }

    // Initialize Google Sign-In
    google.accounts.id.initialize({
        client_id: 'YOUR_GOOGLE_CLIENT_ID.apps.googleusercontent.com', // Replace with your actual client ID
        callback: handleGoogleCallback
    });

    // Render the Google Sign-In button if container exists
    const googleButtonContainer = document.getElementById('google-signin-button');
    if (googleButtonContainer) {
        google.accounts.id.renderButton(
            googleButtonContainer,
            {
                theme: 'outline',
                size: 'large',
                text: 'signin_with',
                width: '300'
            }
        );
    }

    // Also prompt One Tap if user is not logged in
    const userCookie = document.cookie.split(';').find(c => c.trim().startsWith('username='));
    if (!userCookie) {
        google.accounts.id.prompt();
    }
}

/**
 * Handle Google OAuth callback
 * @param {Object} response - The response from Google containing the JWT credential
 */
async function handleGoogleCallback(response) {
    try {
        // Decode the JWT token to get user information
        const credential = response.credential;
        const payload = parseJwt(credential);

        // Extract user information from the JWT payload
        const googleUserInfo = {
            googleId: payload.sub,
            email: payload.email,
            name: payload.name
        };

        // Send to backend
        const backendResponse = await fetch('/auth/google', {
            method: 'POST',
            headers: {
                'Content-Type': 'application/json'
            },
            body: JSON.stringify(googleUserInfo)
        });

        if (backendResponse.ok) {
            const user = await backendResponse.json();
            console.log('Login successful:', user);
            // Reload the page to update the UI with logged-in state
            window.location.reload();
        } else {
            console.error('Login failed:', await backendResponse.text());
            alert('Login failed. Please try again.');
        }
    } catch (error) {
        console.error('Error during Google login:', error);
        alert('An error occurred during login. Please try again.');
    }
}

/**
 * Parse a JWT token to extract the payload
 * @param {string} token - The JWT token
 * @returns {Object} - The parsed payload
 */
function parseJwt(token) {
    try {
        const base64Url = token.split('.')[1];
        const base64 = base64Url.replace(/-/g, '+').replace(/_/g, '/');
        const jsonPayload = decodeURIComponent(atob(base64).split('').map(function(c) {
            return '%' + ('00' + c.charCodeAt(0).toString(16)).slice(-2);
        }).join(''));
        return JSON.parse(jsonPayload);
    } catch (error) {
        console.error('Error parsing JWT:', error);
        return null;
    }
}
