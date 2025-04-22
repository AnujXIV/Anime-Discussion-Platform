document.addEventListener('DOMContentLoaded', () => {
    const grid = document.getElementById('animeGrid');
    const loader = document.getElementById('loader');
    const pagination = document.getElementById('pagination');
    const genreTags = document.getElementById('genreTags');
    const searchInput = document.getElementById('searchInput');

    let currentPage = 1;
    let selectedGenres = [];
    let currentSearch = '';
    let pageSize = calculatePageSize();

    window.addEventListener('resize', () => {
        const newSize = calculatePageSize();
        if (newSize !== pageSize) {
            pageSize = newSize;
            currentPage = 1;
            loadAnime();
        }
    });

    searchInput.addEventListener('input', e => {
        currentSearch = e.target.value.trim();
        currentPage = 1;
        loadAnime();
    });

    function calculatePageSize() {
        const width = window.innerWidth;
        if (width <= 576) return 4;
        if (width <= 768) return 6;
        return 8; // for desktops and large screens
    }

    function loadAnime() {
        loader.classList.remove('hidden');
        grid.innerHTML = '';
        pagination.innerHTML = '';

        const genreQuery = selectedGenres.length > 0 ? `&genre=${selectedGenres.join(',')}` : '';
        const url = `/api/anime?page=${currentPage - 1}&size=${pageSize}${genreQuery}`;

        fetch(url)
            .then(res => res.json())
            .then(data => {
                const animes = currentSearch
                    ? data.content.filter(a => a.name.toLowerCase().includes(currentSearch.toLowerCase()))
                    : data.content;

                renderAnime(animes);
                renderPagination(data.totalPages, currentPage);
            })
            .catch(() => {
                grid.innerHTML = `<p class="text-danger text-center">❌ Failed to load anime list.</p>`;
            })
            .finally(() => loader.classList.add('hidden'));
    }

    function renderAnime(animes) {
        grid.innerHTML = '';
        if (animes.length === 0) {
            grid.innerHTML = `<p class="text-center text-light">No anime found.</p>`;
            return;
        }

        animes.forEach(anime => {
            const card = document.createElement('a');
            const name = anime.name || 'N/A';
            const desc = anime.description || 'No description.';
            const rating = anime.rating || 'N/A';
            const genres = anime.genres ? anime.genres.split(',').map(g => g.trim()) : ['N/A'];

            const imagePath = encodeURI(`/images/AnimeThumbnail_${anime.id}.jpg`);

            card.href = `/anime/${anime.id}`;
            card.className = 'anime-card';

            card.innerHTML = `
            <div class="img-container">
                <img src="${imagePath}"
                     alt="Anime Poster"
                     class="img-fluid"
                     onerror="this.onerror=null; this.src='/images/placeholder.jpg';" />
                <span class="rating-tag">${rating}</span>
            </div>
            <div class="anime-info">
                <h5 class="anime-title">${name}</h5>
                <p class="anime-desc">${desc}</p>
                <div class="anime-meta">
                    ${genres.map(g => `<span class="badge">${g}</span>`).join('')}
                </div>
            </div>
        `;

            grid.appendChild(card);
        });
    }

    function renderPagination(totalPages, current) {
        pagination.innerHTML = '';
        if (totalPages <= 1) return;

        for (let i = 1; i <= totalPages; i++) {
            const li = document.createElement('li');
            li.className = `page-item ${i === current ? 'active' : ''}`;

            const a = document.createElement('a');
            a.className = 'page-link';
            a.href = '#';
            a.textContent = i;

            a.onclick = e => {
                e.preventDefault();
                currentPage = i;
                loadAnime();
            };

            li.appendChild(a);
            pagination.appendChild(li);
        }
    }


    function renderGenres(genres) {
        genreTags.innerHTML = '';
        genres.forEach(genre => {
            const tag = document.createElement('span');
            tag.className = 'badge bg-secondary genre-tag';
            tag.textContent = genre;
            tag.onclick = () => {
                tag.classList.toggle('active-tag');
                if (selectedGenres.includes(genre)) {
                    selectedGenres = selectedGenres.filter(g => g !== genre);
                } else {
                    selectedGenres.push(genre);
                }
                currentPage = 1;
                loadAnime();
            };
            genreTags.appendChild(tag);
        });
    }

    function loadGenres() {
        fetch('/api/anime/genres')
            .then(res => res.json())
            .then(renderGenres);
    }

    loadGenres();
    loadAnime();
});
