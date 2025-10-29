document.addEventListener('DOMContentLoaded', async () => {
    const params = new URLSearchParams(window.location.search);
    const artworkId = params.get('id');
    if (!artworkId) {
        document.getElementById('artwork-title').textContent = 'Artwork not found.';
        return;
    }

    // Load all page data from the new, combined endpoint
    await loadPageData(artworkId);
});

async function loadPageData(artworkId) {
    try {
        const response = await fetch(`/api/artwork-details/${artworkId}`);
        if (!response.ok) {
            throw new Error('Failed to fetch page data.');
        }
        const data = await response.json();

        if (data.error) {
            throw new Error(data.error);
        }

        // 1. Populate the artwork details
        const artwork = data.artwork;
        document.title = `${artwork.title} | RankMyArt`;
        document.getElementById('artwork-image').src = artwork.imageUrl;
        document.getElementById('bg-image-blur').style.backgroundImage = `url(${artwork.imageUrl})`;
        document.getElementById('artwork-title').textContent = artwork.title;
        document.getElementById('artwork-description').textContent = artwork.description;
        document.getElementById('artwork-upvotes').textContent = artwork.upvotes || 0;
        document.getElementById('artwork-views').textContent = artwork.views || 0;

        // --- NEW ---
        // Set the uploader's avatar and name from the new API data
        const uploaderAvatar = document.getElementById('uploader-avatar');
        const uploaderName = document.getElementById('uploader-name');

        if (artwork.uploaderProfileImg) {
            uploaderAvatar.src = artwork.uploaderProfileImg;
        } else {
            // Use a fallback image if the uploader has no profile picture
            uploaderAvatar.src = '/img/default-avatar.svg';
        }
        uploaderName.textContent = artwork.uploaderUsername || 'Unknown';

        // 2. Render the comments
        renderComments(data.comments);

    } catch (error) {
        console.error('Error:', error);
        // This line will now only run if the API call truly fails
        document.getElementById('artwork-title').textContent = 'Could not load artwork.';
    }
}

// This function now conditionally renders the delete button
function renderComments(comments) {
    document.getElementById('comment-header-title').textContent = `Comments (${comments ? comments.length : 0})`;

    const loggedInUser = getLoggedInUser();
    const commentListContainer = document.getElementById('comment-list-container');
    commentListContainer.innerHTML = '';

    if (!comments || comments.length === 0) {
        commentListContainer.innerHTML = '<p class="no-comments">Be the first to comment!</p>';
    } else {
        comments.forEach(comment => {
            let deleteButtonHTML = '';
            if (loggedInUser && loggedInUser === comment.author) {
                deleteButtonHTML = `
                    <button class="delete-comment-btn"
                            comment-id="${comment.id}"
                            author="${comment.author}"
                            onclick="deleteComment(this)">
                        </button>`;
            }

            // --- MODIFIED ---
            // Use the author's real profile image from the API, or a default if it's not set
            const authorImg = comment.authorProfileImg ? comment.authorProfileImg : '/img/default-avatar.svg';

            const commentElement = document.createElement('div');
            commentElement.className = 'comment';
            // Use the authorImg variable in the template below
            commentElement.innerHTML = `
                <img src="${authorImg}" alt="Avatar" class="comment-avatar">
                <div class="comment-content">
                    <div class="comment-header">
                        <span class="commenter-name">${comment.author}</span>
                        ${deleteButtonHTML}
                    </div>
                    <p class="comment-text">${comment.content}</p>
                </div>
            `;
            commentListContainer.appendChild(commentElement);
        });
    }
}