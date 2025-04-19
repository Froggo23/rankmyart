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

window.addEventListener("load", () => {
  const container = document.querySelector(".pin_container");
  const allCards = Array.from(container.querySelectorAll(".card"));

  // Sort cards by vote count
  const sorted = allCards
    .map(card => {
      const votes = parseInt(card.querySelector(".vote-count")?.textContent.trim()) || 0;
      return { element: card, votes };
    })
    .sort((a, b) => b.votes - a.votes); // Descending

  // Extract top 3
  const top3 = sorted.slice(0, 3);
  const rest = sorted.slice(3);

  // Remove all cards
  allCards.forEach(card => card.remove());

  // Reinsert top 3 first
  top3.forEach((item, i) => {
    const badge = document.createElement("div");
    badge.className = `rank-badge rank-${i + 1}`;
    badge.textContent = `#${i + 1}`;
    item.element.appendChild(badge);
    container.appendChild(item.element);
  });

  // Then add the rest
  rest.forEach(item => {
    container.appendChild(item.element);
  });
});
