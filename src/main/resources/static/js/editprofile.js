// In edit-profile.js
document.addEventListener('DOMContentLoaded', () => {
    const avatarInput = document.getElementById('avatarInput');
    const avatarPreview = document.getElementById('avatarPreview');
    const usernameInput = document.getElementById('username');
    const bioInput = document.getElementById('bio');
    const editProfileForm = document.getElementById('editProfileForm');
    const cancelButton = document.querySelector('.cancel-btn');

    // --- 1. Fetch and Pre-fill User Data ---
    async function loadUserData() {
        try {
            const response = await fetch('/user');
            if (!response.ok) throw new Error('Could not fetch user data.');

            const user = await response.json();

            if (user) {
                usernameInput.value = user.username || '';
                bioInput.value = user.bio || '';
                if (user.profileImg) {
                    avatarPreview.src = user.profileImg;
                }
            }
        } catch (error) {
            console.error('Error loading user data:', error);
            alert('Could not load your profile data.');
        }
    }

    // --- 2. Handle Avatar Preview ---
    avatarInput.addEventListener('change', () => {
        const file = avatarInput.files[0];
        if (file) {
            const reader = new FileReader();
            reader.onload = (e) => {
                avatarPreview.src = e.target.result;
            };
            reader.readAsDataURL(file);
        }
    });

    // --- 3. Handle Form Submission ---
    editProfileForm.addEventListener('submit', async (e) => {
        e.preventDefault();

        const formData = new FormData();
        formData.append('username', usernameInput.value);
        formData.append('bio', bioInput.value);
        if (avatarInput.files[0]) {
            formData.append('avatarFile', avatarInput.files[0]);
        }

        try {
            const response = await fetch('/update-profile', {
                method: 'POST',
                body: formData
            });

            if (response.ok || response.redirected) {
                alert('Profile saved successfully!');
                window.location.href = '/profile';
            } else {
                const errorText = await response.text();
                alert('Error saving profile: ' + errorText);
            }
        } catch (error) {
            console.error('Error submitting form:', error);
            alert('An unexpected error occurred.');
        }
    });

    // --- 4. Handle Cancel Button ---
    cancelButton.addEventListener('click', () => {
        window.location.href = '/profile';
    });

    // --- Load data when the page opens ---
    loadUserData();
});