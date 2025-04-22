document.addEventListener('DOMContentLoaded', () => {
    const wallpapers = document.querySelectorAll('.wallpaper');
    const quoteElement = document.getElementById('animeQuote');
    const quotes = [
        "“Arise.” – Sung Jin-Woo (Solo Leveling)",
        "“Those who forgive themselves, and are able to accept their true nature... They are the strong ones!” – Itachi",
        "“Power comes in response to a need, not a desire.” – Goku",
        "“Throughout heaven and earth, I alone am the honored one.” – Gojo Satoru",
        "“I’m gonna be King of the Pirates!” – Luffy"
    ];

    let currentIndex = 0;

    const changeWallpaper = () => {
        wallpapers.forEach((img, index) => {
            img.classList.remove('active');
            if (index === currentIndex) {
                img.classList.add('active');
            }
        });

        quoteElement.textContent = quotes[currentIndex];

        currentIndex = (currentIndex + 1) % wallpapers.length;
    };

    setInterval(changeWallpaper, 5000); // Change every 5 seconds
    changeWallpaper(); // Initial load
});
