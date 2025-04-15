// Dummy data (replace with dynamic fetch from backend)
const artworks = [
    { title: "Sunset Lake", imageUrl: "100girlfriends.jpg", upvotes: 120 },
    { title: "Clay Warrior", imageUrl: "50girlfriends.jpg", upvotes: 90 },
    { title: "Pink Dreams", imageUrl: "25girlfriends.jpg", upvotes: 150 },
    { title: "Sakura Pulse", imageUrl: "10girlfriends.jpg", upvotes: 300 },
    { title: "Abstract Night", imageUrl: "1girlfriends.jpg", upvotes: 75 },
    { title: "Digital Angel", imageUrl: "0.1girlfriends.jpg", upvotes: 240 },
    { title: "Neon Tokyo", imageUrl: "Meme.jpg", upvotes: 210 },
    { title: "Wings of Fire", imageUrl: "100girlfriends.jpg", upvotes: 180 },
    { title: "Lucid Bloom", imageUrl: "100girlfriends.jpg", upvotes: 170 },
    { title: "Moonlit Path", imageUrl: "100girlfriends.jpg", upvotes: 95 }
];

artworks.sort((a, b) => b.upvotes - a.upvotes);

const container = document.getElementById("leaderboardGrid");
const maxUpvotes = artworks[0].upvotes;

artworks.forEach(art => {
    const img = new Image();
    img.src = art.imageUrl;
    img.onload = () => {
        const ratio = img.naturalHeight / img.naturalWidth;
        const baseWidth = 200;
        const scale = 0.8 + (art.upvotes / maxUpvotes) * 0.6;

        const card = document.createElement("div");
        card.className = "leader-card";
        card.style.width = `${baseWidth * scale}px`;
        card.style.height = `${baseWidth * scale * ratio}px`;

        card.innerHTML = `
      <img src="${art.imageUrl}" alt="${art.title}" />
      <div class="leader-info">
        <strong>${art.title}</strong> • ❤️ ${art.upvotes}
      </div>
    `;

        container.appendChild(card);
    };
});
