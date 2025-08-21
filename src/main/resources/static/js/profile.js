document.addEventListener('DOMContentLoaded', () => {
    const logoutButton = document.getElementById('logout-btn');
    const editProfileButton = Array.from(document.querySelectorAll('.logout-btn')).find(btn => btn.textContent === 'Edit Profile');
    const pinContainer = document.querySelector('.pin_container');

    // --- 1. Load User's Main Profile Data ---
    async function loadProfileData() {
        try {
            const response = await fetch('/user');
            if (!response.ok) return;

            const user = await response.json();

            if (user) {
                document.querySelector('.username').textContent = user.username;
                document.querySelector('.user-bio').textContent = user.bio || 'No bio yet.';
                if (user.profileImg) {
                    document.querySelector('.profile-avatar img').src = user.profileImg;
                }

                // --- NEW: After loading user data, load their artworks ---
                if (user.username) {
                    loadUserArtworks(user.username);
                }
            }
        } catch (error) {
            console.error('Failed to load profile data:', error);
        }
    }

    // --- 2. NEW: Function to Fetch and Display User's Artworks ---
    async function loadUserArtworks(username) {
        try {
            const response = await fetch(`/api/user/${username}/artworks`);
            if (!response.ok) return;

            const artworks = await response.json();

            // Clear the placeholder images
            pinContainer.innerHTML = '';

            if (artworks.length === 0) {
                pinContainer.innerHTML = '<p style="color: #aaa;">No uploads yet.</p>';
                return;
            }

            // Create and append each artwork card
            artworks.forEach(artwork => {
                const card = document.createElement('div');
                card.className = 'card';
                card.dataset.artworkId = artwork.id; // For making it clickable

                const img = document.createElement('img');
                img.src = artwork.imageUrl;
                img.alt = artwork.title;

                card.appendChild(img);
                pinContainer.appendChild(card);

                // Add click listener to navigate to artinfo page
                card.addEventListener('click', () => {
                    window.location.href = `/artinfo?id=${artwork.id}`;
                });
            });

        } catch (error) {
            console.error('Failed to load user artworks:', error);
            pinContainer.innerHTML = '<p style="color: #f88;">Could not load artworks.</p>';
        }
    }

    // --- 3. Button Logic (Unchanged) ---
    if (logoutButton) {
        logoutButton.addEventListener('click', () => {
            document.cookie = "username=; expires=Thu, 01 Jan 1970 00:00:00 UTC; path=/;";
            alert("You have been logged out.");
            window.location.href = '/gallery';
        });
    }

    if (editProfileButton) {
        editProfileButton.addEventListener('click', () => {
            window.location.href = '/editprofile';
        });
    }

    // --- Load all data when the page opens ---
    loadProfileData();
});
