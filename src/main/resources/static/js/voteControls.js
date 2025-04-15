document.querySelectorAll('.card').forEach(card => {
    const upvote = card.querySelector('.vote-icon.upvote');
    const downvote = card.querySelector('.vote-icon.downvote');
    const countEl = card.querySelector('.vote-count');

    let count = parseInt(countEl.textContent);
    let state = null;

    function applyBounce(icon) {
        icon.classList.add('bounce');
        icon.addEventListener('animationend', () => {
            icon.classList.remove('bounce');
        }, { once: true });
    }

    upvote.addEventListener('click', () => {
        if (state === 'upvoted') {
            // Undo vote — no animation
            count--;
            state = null;
            upvote.classList.remove('active');
        } else {
            // Apply vote — do animation
            if (state === 'downvoted') {
                count += 2;
                downvote.classList.remove('active');
            } else {
                count++;
            }
            state = 'upvoted';
            upvote.classList.add('active');
            downvote.classList.remove('active');
            applyBounce(upvote);
        }
        countEl.textContent = count;
    });

    downvote.addEventListener('click', () => {
        if (state === 'downvoted') {
            // Undo vote — no animation
            count++;
            state = null;
            downvote.classList.remove('active');
        } else {
            // Apply vote — do animation
            if (state === 'upvoted') {
                count -= 2;
                upvote.classList.remove('active');
            } else {
                count--;
            }
            state = 'downvoted';
            downvote.classList.add('active');
            upvote.classList.remove('active');
            applyBounce(downvote);
        }
        countEl.textContent = count;
    });
});