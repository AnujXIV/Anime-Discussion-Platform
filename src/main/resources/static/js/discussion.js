document.addEventListener('DOMContentLoaded', () => {
    let step = 1;
    let selectedType = null;
    let selectedAnimeId = null;
    let selectedCharacterId = null;
    let currentUserId = null;


    const stepIndicator = document.getElementById('stepIndicator');
    const step1 = document.getElementById('step1');
    const step2 = document.getElementById('step2');
    const step3 = document.getElementById('step3');

    const backBtn = document.getElementById('backBtn');
    const nextBtn = document.getElementById('nextBtn');
    const entitySelection = document.getElementById('entitySelection');


    // Get User
    fetch('/api/auth/me')
        .then(res => res.json())
        .then(user => {
            currentUserId = user.id;
            console.log("Logged-in user ID:", currentUserId);

            // Now you can use `currentUserId` in discussion or message payloads
        })
        .catch(err => {
            console.error("❌ Failed to fetch current user", err);
        });


    // Step 1: Choose anime/character type
    document.querySelectorAll('.option-btn').forEach(btn => {
        btn.addEventListener('click', () => {
            document.querySelectorAll('.option-btn').forEach(b => b.classList.remove('active'));
            btn.classList.add('active');
            selectedType = btn.dataset.type;
            nextBtn.classList.remove('disabled');
        });
    });

    nextBtn.addEventListener('click', () => {
        if (step === 1 && selectedType) {
            step = 2;
            renderEntities();
        } else if (step === 2 && selectedAnimeId !== null) {
            step = 3;
            showStep(step);
            bindFormValidation();
        } else if (step === 3) {
            submitDiscussion();
        }
    });

    backBtn.addEventListener('click', () => {
        if (step > 1) {
            step--;
            showStep(step);
        }
    });

    function showStep(n) {
        step1.classList.toggle('d-none', n !== 1);
        step2.classList.toggle('d-none', n !== 2);
        step3.classList.toggle('d-none', n !== 3);

        stepIndicator.textContent = {
            1: 'Choose Discussion Type',
            2: `Select ${selectedType === 'anime' ? 'Anime' : 'Characters'}`,
            3: 'Discussion Details'
        }[n];

        backBtn.classList.toggle('d-none', n === 1);
        nextBtn.textContent = n === 3 ? 'Start Discussion' : 'Next →';

        // Disable Next by default
        nextBtn.classList.add('disabled');
    }

    function renderEntities() {
        showStep(step);
        entitySelection.innerHTML = '<p class="text-light">Loading...</p>';

        fetch(`/api/${selectedType}?page=0&size=100`)
            .then(res => res.json())
            .then(data => {
                const items = Array.isArray(data) ? data : (data.content || []);
                entitySelection.innerHTML = '';

                items.forEach(item => {
                    const col = document.createElement('div');
                    col.className = 'col-sm-6 col-md-4 col-lg-3';

                    col.innerHTML = `
                        <div class="card entity-card text-center p-2 bg-dark text-white h-100">
                            <img src="/images/${selectedType === 'anime' ? 'AnimeThumbnail_' : 'AnimeCharacter_'}${item.id}.jpg"
                                 class="card-img-top rounded mb-2"
                                 style="height:200px;object-fit:cover;"
                                 onerror="this.src='/images/${selectedType === 'anime' ? 'placeholder' : 'default-character'}.jpg'">
                            <div class="fw-bold">${item.name}</div>
                        </div>
                    `;

                    col.querySelector('.entity-card').addEventListener('click', () => {
                        document.querySelectorAll('.entity-card').forEach(c => c.classList.remove('selected'));
                        col.querySelector('.entity-card').classList.add('selected');
                        selectedAnimeId = selectedType === 'anime' ? item.id : item.anime.id;
                        selectedCharacterId = selectedType === 'characters' ? item.id : null;
                        console.log("Anime ID :"+selectedAnimeId+", Character ID :"+ selectedCharacterId);
                        nextBtn.classList.remove('disabled');
                    });

                    entitySelection.appendChild(col);
                });
            });
    }

    function bindFormValidation() {
        const titleInput = step3.querySelector('input[name="title"]');
        const descInput = step3.querySelector('textarea[name="description"]');

        const validate = () => {
            const valid = titleInput.value.trim() && descInput.value.trim();
            nextBtn.classList.toggle('disabled', !valid);
        };

        titleInput.addEventListener('input', validate);
        descInput.addEventListener('input', validate);
        validate(); // Initial check
    }

    function submitDiscussion() {
        const title = step3.querySelector('input[name="title"]').value.trim();
        const description = step3.querySelector('textarea[name="description"]').value.trim();
        if (!title || !description) return;

        nextBtn.innerHTML = `<span class="spinner-border spinner-border-sm me-2"></span> Creating...`;
        nextBtn.disabled = true;

        console.log("Anime Id :"+ selectedAnimeId+", Character ID :"+selectedCharacterId);
        const payload = {
            title: title,
            description: description,
            userId: currentUserId,
            animeId: selectedAnimeId,
            characterId: selectedCharacterId
        };

        console.log("Payload :"+payload);

        fetch('/api/discussions', {
            method: 'POST',
            headers: {
                'Content-Type': 'application/json'
            },
            body: JSON.stringify(payload)
        })
            .then(res => {
                if (!res.ok) throw new Error('Failed to create discussion');
                return res.json();
            })
            .then(created => {
                window.location.href = `/discussions/${created.id}`;
            })
            .catch(err => {
                alert('Failed to create discussion');
                console.error(err);
                nextBtn.innerHTML = 'Start Discussion';
                nextBtn.disabled = false;
            });
    }

});
