document.addEventListener('DOMContentLoaded', () => {
    const grid = document.getElementById('characterGrid');
    const loader = document.getElementById('loader');
    const pagination = document.getElementById('pagination');
    const animeFilter = document.getElementById('animeFilter');

    let currentPage = 1;
    let currentAnime = '';

    loadAnimeOptions();
    loadCharacters();

    animeFilter.addEventListener('change', () => {
        currentAnime = animeFilter.value;
        currentPage = 1;
        loadCharacters();
    });

    function loadCharacters() {
        loader.style.display = 'block';
        grid.innerHTML = '';
        pagination.innerHTML = '';

        const query = currentAnime ? `&anime=${encodeURIComponent(currentAnime)}` : '';
        fetch(`/api/characters?page=${currentPage}${query}`)
            .then(res => res.json())
            .then(data => {
                renderCharacters(data.content);
                renderPagination(data.totalPages, data.pageable.pageNumber + 1);
            })
            .catch(err => {
                grid.innerHTML = `<p class="text-danger text-center">Failed to load characters.</p>`;
                console.error(err);
            })
            .finally(() => loader.style.display = 'none');
    }

    function renderCharacters(characters) {
        if (characters.length === 0) {
            grid.innerHTML = `<p class="text-center text-light">No characters found.</p>`;
            return;
        }

        characters.forEach(char => {
            const name = char.name || 'N/A';
            const desc = char.description || 'No description';
            const age = char.age || 'N/A';
            const height = char.height || 'N/A';
            const birth = char.birthdate || 'N/A';
            const anime = char.anime?.name || 'Unknown';
            const img = encodeURI(`/images/CharacterThumbnail_${char.id}.jpg`);


            const card = document.createElement('div');
            card.className = 'col-md-4 col-lg-3';
            card.innerHTML = `
        <div class="card bg-secondary text-light h-100">
          <img src="${img}" class="card-img-top" alt="${name}" onerror="this.onerror=null;this.src='/images/default-character.jpg';">
          <div class="card-body d-flex flex-column">
            <h5 class="card-title">${name}</h5>
            <p class="card-text">${desc}</p>
            <ul class="list-unstyled small mt-auto">
              <li><strong>Anime:</strong> ${anime}</li>
              <li><strong>Age:</strong> ${age}</li>
              <li><strong>Height:</strong> ${height}</li>
              <li><strong>Birthdate:</strong> ${birth}</li>
            </ul>
          </div>
        </div>
      `;
            grid.appendChild(card);
        });
    }

    function renderPagination(totalPages, current) {
        if (totalPages <= 1) return;

        for (let i = 1; i <= totalPages; i++) {
            const btn = document.createElement('button');
            btn.className = `btn btn-sm ${i === current ? 'btn-light' : 'btn-outline-light'} mx-1`;
            btn.textContent = i;
            btn.addEventListener('click', () => {
                currentPage = i;
                loadCharacters();
            });
            pagination.appendChild(btn);
        }
    }

    function loadAnimeOptions() {
        fetch('/api/anime/titles')
            .then(res => res.json())
            .then(titles => {
                titles.forEach(title => {
                    const opt = document.createElement('option');
                    opt.value = title;
                    opt.textContent = title;
                    animeFilter.appendChild(opt);
                });
            });
    }
});
