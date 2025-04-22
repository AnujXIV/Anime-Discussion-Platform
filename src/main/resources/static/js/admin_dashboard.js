document.addEventListener("DOMContentLoaded", () => {
    fetchUsers();
    fetchAnimes();
    fetchCharacters();
    fetchDiscussions();

    document.getElementById("addAnimeForm").addEventListener("submit", addAnime);
    document.getElementById("addCharacterForm").addEventListener("submit", addCharacter);
    document.getElementById("addDiscussionForm").addEventListener("submit", addDiscussion);
});

async function fetchUsers() {
    const res = await fetch("/api/users");
    const users = await res.json();
    const table = document.getElementById("usersTable");

    table.innerHTML = users.map(user => `
        <tr>
            <td>${user.username}</td>
            <td>${user.email}</td>
            <td>
                <select onchange="updateRole('${user.id}', this.value)">
                    <option ${user.role === 'USER' ? 'selected' : ''}>USER</option>
                    <option ${user.role === 'ADMIN' ? 'selected' : ''}>ADMIN</option>
                </select>
            </td>
        </tr>
    `).join("");
}

async function updateRole(userId, newRole) {
    await fetch(`/api/users/${userId}/role`, {
        method: "PUT",
        headers: { 'Content-Type': 'application/json' },
        body: JSON.stringify({ role: newRole })
    });
    alert("Role updated");
}

async function fetchAnimes() {
    const res = await fetch("/api/anime");
    const animes = await res.json();
    const table = document.getElementById("animesTable");

    table.innerHTML = animes.map(a => `
        <tr>
            <td>${a.name}</td>
            <td>${a.genres}</td>
            <td>${a.rating}</td>
        </tr>
    `).join("");
}

async function addAnime(e) {
    e.preventDefault();
    const form = e.target;
    const anime = {
        name: form.name.value,
        description: form.description.value,
        pic: form.pic.value,
        tags: form.tags.value,
        status: form.status.value,
        rating: form.rating.value,
        genres: form.genres.value,
        studios: form.studios.value,
        origin: form.origin.value,
    };

    await fetch("/api/anime", {
        method: "POST",
        headers: { 'Content-Type': 'application/json' },
        body: JSON.stringify(anime)
    });

    form.reset();
    fetchAnimes();
}

async function fetchCharacters() {
    const res = await fetch("/api/characters");
    const characters = await res.json();
    const table = document.getElementById("charactersTable");

    table.innerHTML = characters.map(c => `
        <tr>
            <td>${c.name}</td>
            <td>${c.anime?.name || 'N/A'}</td>
            <td>${c.age || 'N/A'}</td>
        </tr>
    `).join("");
}

async function addCharacter(e) {
    e.preventDefault();
    const form = e.target;
    const character = {
        name: form.name.value,
        description: form.description.value,
        pic: form.pic.value,
        age: form.age.value,
        height: form.height.value,
        birthdate: form.birthdate.value,
        animeId: form.animeId.value
    };

    await fetch("/api/characters", {
        method: "POST",
        headers: { 'Content-Type': 'application/json' },
        body: JSON.stringify(character)
    });

    form.reset();
    fetchCharacters();
}

async function fetchDiscussions() {
    const res = await fetch("/api/discussions");
    const discussions = await res.json();
    const table = document.getElementById("discussionsTable");

    table.innerHTML = discussions.map(d => `
        <tr>
            <td>${d.topic}</td>
            <td>${d.user?.username || 'N/A'}</td>
            <td>
                <button onclick="deleteDiscussion(${d.id})" class="btn btn-sm btn-danger">Delete</button>
            </td>
        </tr>
    `).join("");
}

async function addDiscussion(e) {
    e.preventDefault();
    const form = e.target;

    const discussion = {
        topic: form.topic.value,
        animeId: form.animeId.value || null,
        characterId: form.characterId.value || null,
    };

    await fetch("/api/discussions", {
        method: "POST",
        headers: { 'Content-Type': 'application/json' },
        body: JSON.stringify(discussion)
    });

    form.reset();
    fetchDiscussions();
}

async function deleteDiscussion(id) {
    await fetch(`/api/discussions/${id}`, { method: "DELETE" });
    fetchDiscussions();
}
