document.addEventListener('DOMContentLoaded', () => {
    /**
     * Attaches vote listeners to all artwork cards.
     * This function should be called after the HTML elements are loaded (e.g., by Thymeleaf).
     */
    function attachVoteListeners() {
        // Select all elements with the class 'card'
        document.querySelectorAll('.card').forEach(card => {
            // Get the upvote and downvote buttons and the vote count span within each card
            const upvoteBtn = card.querySelector('.vote-icon.upvote');
            const downvoteBtn = card.querySelector('.vote-icon.downvote');
            const voteCountSpan = card.querySelector('.vote-count');

            // Attach listener to the upvote button
            if (upvoteBtn) {
                // Use a data attribute to prevent attaching multiple listeners to the same button
                if (upvoteBtn.dataset.listenerAttached) return;
                upvoteBtn.dataset.listenerAttached = 'true';

                upvoteBtn.addEventListener('click', async (e) => {
                    e.stopPropagation(); // Prevent any parent click events on the card
                    const artworkId = card.dataset.artworkId; // Get artwork ID from the card's data attribute

                    try {
                        // Send POST request to the backend for upvoting
                        const response = await fetch(`/upvote?id=${artworkId}`, {
                            method: 'POST'
                        });

                        if (response.ok) {
                            console.log(`Upvoted artwork with ID: ${artworkId}`);
                            // Optimistically update the displayed vote count
                            let currentVotes = parseInt(voteCountSpan.textContent);
                            voteCountSpan.textContent = currentVotes + 1;
                        } else {
                            console.error('Failed to upvote:', response.status, await response.text());
                        }
                    } catch (error) {
                        console.error('Error during upvote API call:', error);
                    }
                });
            }

            // Attach listener to the downvote button (similar logic to upvote)
            if (downvoteBtn) {
                if (downvoteBtn.dataset.listenerAttached) return;
                downvoteBtn.dataset.listenerAttached = 'true';

                downvoteBtn.addEventListener('click', async (e) => {
                    e.stopPropagation();
                    const artworkId = card.dataset.artworkId;

                    try {
                        // Send POST request to the backend for downvoting
                        const response = await fetch(`/downvote?id=${artworkId}`, {
                            method: 'POST'
                        });

                        if (response.ok) {
                            console.log(`Downvoted artwork with ID: ${artworkId}`);
                            // Optimistically update the displayed vote count
                            let currentVotes = parseInt(voteCountSpan.textContent);
                            voteCountSpan.textContent = currentVotes - 1;
                        } else {
                            console.error('Failed to downvote:', response.status, await response.text());
                        }
                    } catch (error) {
                        console.error('Error during downvote API call:', error);
                    }
                });
            }
        });
    }

    // Call attachVoteListeners when the DOM is fully loaded.
    // This ensures listeners are applied to all cards rendered by Thymeleaf.
    attachVoteListeners();
});