document.addEventListener('DOMContentLoaded', () => {
    fetch('/api/anime')
        .then(response => response.json())
        .then(animes => {
            let animeCards = '';
            animes.forEach(anime => {
                animeCards += `<div class="card"><h3>${anime.name}</h3></div>`;
            });
            document.getElementById('anime-cards').innerHTML = animeCards;
        });
});
