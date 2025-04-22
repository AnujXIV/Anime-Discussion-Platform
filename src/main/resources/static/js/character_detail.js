document.addEventListener('DOMContentLoaded', () => {
    const id = window.location.pathname.split('/').pop();

    const loader = document.getElementById('characterLoader');
    const content = document.getElementById('characterContent');

    const img = document.getElementById('charImg');
    const name = document.getElementById('charName');
    const desc = document.getElementById('charDesc');
    const birthdate = document.getElementById('charBirthdate');
    const height = document.getElementById('charHeight');
    const age = document.getElementById('charAge');
    const animeLink = document.getElementById('charAnimeLink');

    fetch(`/api/characters/${id}`)
        .then(res => res.json())
        .then(character => {
            img.src = `/images/CharacterThumbnail_${character.id}.jpg`;
            img.onerror = () => { img.src = '/images/default-character.jpg'; };

            name.textContent = character.name || 'Unknown';
            desc.textContent = character.description || 'No description available.';
            birthdate.textContent = character.birthdate || 'N/A';
            height.textContent = character.height || 'N/A';
            age.textContent = character.age || 'N/A';

            if (character.anime) {
                animeLink.href = `/anime/${character.anime.id}`;
                animeLink.textContent = character.anime.name || 'Unknown';
            } else {
                animeLink.textContent = 'Unknown';
                animeLink.removeAttribute('href');
            }

            loader.classList.add('d-none');
            content.classList.remove('d-none');
        })
        .catch(err => {
            loader.innerHTML = `<p class="text-danger">Failed to load character info.</p>`;
            console.error("Character Load Error:", err);
        });
});
