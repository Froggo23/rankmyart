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

        // 2. Render the comments
        renderComments(data.comments);

    } catch (error) {
        console.error('Error:', error);
        document.getElementById('artwork-title').textContent = 'Could not load artwork.';
    }
}

// This function now conditionally renders the delete button
function renderComments(comments) {
    document.getElementById('comment-header-title').textContent = `Comments (${comments ? comments.length : 0})`;

    // Get the currently logged-in user from auth.js
    const loggedInUser = getLoggedInUser();
    const commentListContainer = document.getElementById('comment-list-container');
    commentListContainer.innerHTML = '';

    if (!comments || comments.length === 0) {
        commentListContainer.innerHTML = '<p class="no-comments">Be the first to comment!</p>';
    } else {
        comments.forEach(comment => {
            let deleteButtonHTML = ''; // Start with an empty delete button

            // Only create the delete button if a user is logged in AND they are the comment's author
            if (loggedInUser && loggedInUser === comment.author) {
                deleteButtonHTML = `
                    <button class="delete-comment-btn"
                            comment-id="${comment.id}"
                            author="${comment.author}"
                            onclick="deleteComment(this)">
                        <svg xmlns="http://www.w3.org/2000/svg" viewBox="0 0 24 24" fill="currentColor">
                            <path fill-rule="evenodd" d="M16.5 4.478v.227a48.816 48.816 0 013.878.512.75.75 0 11-.256 1.478l-.209-.035-1.005 13.109a3 3 0 01-2.991 2.77H8.084a3 3 0 01-2.991-2.77L4.087 6.66l-.209.035a.75.75 0 01-.256-1.478A48.567 48.567 0 017.5 4.705v-.227c0-1.564 1.213-2.9 2.816-2.9h1.368c1.603 0 2.816 1.336 2.816 2.9zM12 3.25a.75.75 0 01.75.75v.008l.008.008H12v-.008L12 4zM9.75 4.5v.008H14.25v-.008a.75.75 0 01-.75-.75h-3a.75.75 0 01-.75.75z" clip-rule="evenodd" />
                            <path d="M5.25 6.375a.75.75 0 01.75-.75h12a.75.75 0 01.75.75v.042l-.22 2.861a3 3 0 01-2.99 2.712H8.71a3 3 0 01-2.99-2.712l-.22-2.861v-.042z" />
                        </svg>
                    </button>`;
            }

            const commentElement = document.createElement('div');
            commentElement.className = 'comment';
            commentElement.innerHTML = `
                <img src="https://i.pravatar.cc/40" alt="Avatar" class="comment-avatar">
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