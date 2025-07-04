async function submitComment() {
    const content = document.getElementById('comment').value;
    const urlParams = new URLSearchParams(window.location.search);
    const artworkId = urlParams.get('id');

    if (!content || !artworkId) {
        alert("Please write a comment.");
        return;
    }

    // Note: The Comment model expects 'artworkId', so we send it.
    const response = await fetch('/submitComment', {
        method: 'POST',
        headers: { 'Content-Type': 'application/json' },
        body: JSON.stringify({ artworkId, content })
    });

    if (response.ok) {
        document.getElementById('comment').value = ''; // Clear the textarea
        await loadPageData(artworkId); // Reload all data
    } else {
        alert('Error adding comment. Please make sure you are logged in.');
    }
}

// Deletes a comment and reloads the page data
async function deleteComment(buttonElement) {
    const commentId = buttonElement.getAttribute('comment-id');
    const author = buttonElement.getAttribute('author');
    const urlParams = new URLSearchParams(window.location.search);
    const artworkId = urlParams.get('id');

    if (confirm('Are you sure you want to delete this comment?')) {
        const response = await fetch('/delete-comment', {
            method: 'POST',
            headers: { 'Content-Type': 'application/json' },
            body: JSON.stringify({ id: commentId, author })
        });

        const resultText = await response.text();
        if (resultText === 'success') {
            await loadPageData(artworkId); // Reload all data
        } else {
            alert('You are not authorized to delete this comment.');
        }
    }
}