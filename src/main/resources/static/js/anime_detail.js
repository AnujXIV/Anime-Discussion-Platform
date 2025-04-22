document.addEventListener('DOMContentLoaded', () => {
    const id = window.location.pathname.split('/').pop();

    const loader = document.getElementById('animeLoader');
    const content = document.getElementById('animeContent');

    const img = document.getElementById('animeImg');
    const title = document.getElementById('animeTitle');
    const desc = document.getElementById('descText');
    const toggle = document.getElementById('descToggle');
    const status = document.getElementById('animeStatus');
    const rating = document.getElementById('animeRating');
    const genres = document.getElementById('animeGenres');
    const studios = document.getElementById('animeStudios');
    const origin = document.getElementById('animeOrigin');

    const charSection = document.getElementById('charactersSection');
    const charList = document.getElementById('characterList');
    const discSection = document.getElementById('discussionsSection');
    const discList = document.getElementById('discussionList');

    fetch(`/api/anime/${id}`)
        .then(res => res.json())
        .then(anime => {
            // 🖼️ Image based on anime ID (fallback to placeholder if not found)
            const imageUrl = `/images/AnimeThumbnail_${anime.id}.jpg`;
            img.src = encodeURI(imageUrl);
            img.onerror = () => { img.src = '/images/placeholder.jpg'; };

            // 📝 Text info
            title.textContent = anime.name || 'Unknown';
            rating.textContent = anime.rating || 'N/A';
            status.textContent = anime.status || 'N/A';
            studios.textContent = anime.studios || 'N/A';
            origin.textContent = anime.origin || 'N/A';

            // 🏷️ Genre badges
            const genreArray = anime.genres ? anime.genres.split(',') : [];
            genres.innerHTML = genreArray.map(g => `<span class="badge">${g.trim()}</span>`).join('');

            // 🧾 Description with see more
            const fullDesc = anime.description || 'No description available.';
            const shortDesc = fullDesc.length > 300 ? fullDesc.substring(0, 300) + '...' : fullDesc;
            desc.textContent = shortDesc;

            if (fullDesc.length > 300) {
                toggle.classList.remove('d-none');
                toggle.addEventListener('click', () => {
                    const isShort = desc.textContent.endsWith('...');
                    desc.textContent = isShort ? fullDesc : shortDesc;
                    toggle.textContent = isShort ? 'See less' : 'See more';
                });
            }

            // 👥 Characters
            if (anime.characters && anime.characters.length > 0) {
                charSection.classList.remove('d-none');
                charList.innerHTML = anime.characters.map(c => `
          <div class="col-sm-6 col-md-4 col-lg-3">
            <div class="card h-100">
<img src="/images/CharacterThumbnail_${c.id}.jpg" 
     onerror="this.onerror=null;this.src='/images/default-character.jpg'" 
     class="card-img-top" alt="${c.name}" />
              <div class="card-body">
                <h6 class="card-title text-white">${c.name}</h6>
                <a href="/characters/${c.id}" class="btn btn-sm btn-outline-light ms-auto">View</a>
              </div>
            </div>
          </div>
        `).join('');
            }

            // 💬 Discussions
            if (anime.discussions && anime.discussions.length > 0) {
                discSection.classList.remove('d-none');
                discList.innerHTML = anime.discussions.map(d => `
          <a href="/discussions/${d.id}" class="list-group-item list-group-item-action">
            <strong>${d.title}</strong>
            <div class="small text-white">Started on: ${d.createdAt || 'Unknown'}</div>
          </a>
        `).join('');
            }

            // ✅ Show content
            loader.classList.add('d-none');
            content.classList.remove('d-none');
        })
        .catch(err => {
            loader.innerHTML = `<p class="text-danger">Failed to load anime info.</p>`;
            console.error(err);
        });
});
