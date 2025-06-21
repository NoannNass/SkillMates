document.addEventListener("DOMContentLoaded", function () {
  // Gestion des boutons d'acceptation
  document.querySelectorAll(".btn.accept").forEach((button) => {
    button.addEventListener("click", function () {
      const partnershipId = this.getAttribute("data-id");
      if (confirm("Voulez-vous accepter cette demande de partenariat ?")) {
        fetch(`/partnerships/${partnershipId}/accept`, {
          method: "POST",
          headers: {
            "Content-Type": "application/json",
          },
        }).then(() => {
          window.location.reload();
        });
      }
    });
  });

  // Gestion des boutons de refus
  document.querySelectorAll(".btn.deny").forEach((button) => {
    button.addEventListener("click", function () {
      const partnershipId = this.getAttribute("data-id");
      if (confirm("Voulez-vous refuser cette demande de partenariat ?")) {
        fetch(`/partnerships/${partnershipId}/deny`, {
          method: "POST",
          headers: {
            "Content-Type": "application/json",
          },
        }).then(() => {
          window.location.reload();
        });
      }
    });
  });

  // Gestion des boutons de message
  document.querySelectorAll(".btn.message").forEach((button) => {
    button.addEventListener("click", function () {
      const partnershipId = this.getAttribute("data-id");
      window.location.href = `/messages/conversation/${partnershipId}`;
    });
  });

  // Gestion des boutons de planification
  document.querySelectorAll(".btn.schedule").forEach((button) => {
    button.addEventListener("click", function () {
      const partnershipId = this.getAttribute("data-id");
      window.location.href = `/partnerships/${partnershipId}/schedule`;
    });
  });

  // Gestion des onglets
  const tabs = document.querySelectorAll(".tab");
  tabs.forEach((tab) => {
    tab.addEventListener("click", function () {
      // Retirer la classe active de tous les onglets
      tabs.forEach((t) => t.classList.remove("active"));
      // Ajouter la classe active à l'onglet cliqué
      this.classList.add("active");

      // Si c'est l'onglet "Trouver un mate"
      if (this.textContent === "Trouver un mate") {
        window.location.href = "/partnerships/suggestions";
      }
    });
  });

  // Gestion de la barre de recherche avec debounce
  const searchInput = document.querySelector(".search-bar input");
  let debounceTimeout;

  searchInput.addEventListener("input", function (e) {
    const searchTerm = e.target.value.trim();

    // Clear the existing timeout
    clearTimeout(debounceTimeout);

    // Si le terme de recherche est vide, on affiche tous les partenaires
    if (searchTerm === "") {
      document.querySelectorAll(".partnership-card").forEach((card) => {
        card.style.display = "grid";
      });
      return;
    }

    // Set a new timeout
    debounceTimeout = setTimeout(() => {
      // Faire la requête à l'API
      fetch(`/api/partnerships/search?query=${encodeURIComponent(searchTerm)}`)
        .then((response) => response.json())
        .then((data) => {
          if (data.success && data.data) {
            const userIds = new Set(data.data.map((user) => user.id));

            // Filtrer les cartes de partenariat
            document.querySelectorAll(".partnership-card").forEach((card) => {
              const partnerId = card.dataset.partnerId;
              card.style.display = userIds.has(partnerId) ? "grid" : "none";
            });
          }
        })
        .catch((error) => {
          console.error("Erreur lors de la recherche:", error);
        });
    }, 300); // Délai de 300ms pour le debounce
  });

  // Gestion des boutons Message
  const messageButtons = document.querySelectorAll(".btn-primary");
  messageButtons.forEach((button) => {
    button.addEventListener("click", function () {
      // Rediriger vers la page de messages avec le partenaire
      const partnerCard = this.closest(".partnership-card");
      const partnerId = partnerCard.dataset.partnerId;
      window.location.href = `/messages/${partnerId}`;
    });
  });
});

// Gestion des onglets
function showTab(tabName) {
  // Masquer tous les contenus d'onglets
  const tabContents = document.querySelectorAll(".tab-content");
  tabContents.forEach((content) => {
    content.classList.remove("active");
  });

  // Désactiver tous les onglets
  const tabs = document.querySelectorAll(".tab");
  tabs.forEach((tab) => {
    tab.classList.remove("active");
  });

  // Afficher l'onglet sélectionné
  const selectedTab = document.getElementById(tabName + "-tab");
  if (selectedTab) {
    selectedTab.classList.add("active");
  }

  // Activer le bouton d'onglet correspondant
  const tabButtons = document.querySelectorAll(".tab");
  tabButtons.forEach((button) => {
    if (
      button.textContent
        .toLowerCase()
        .includes(tabName === "partners" ? "partenaires" : "mate")
    ) {
      button.classList.add("active");
    }
  });

  // Si c'est l'onglet "Trouver un mate", charger les utilisateurs
  if (tabName === "find") {
    loadAllUsers();
  }
}

// Charger tous les utilisateurs
async function loadAllUsers() {
  try {
    const response = await fetch(
      "/partnerships/api/partnerships/search?query="
    );
    const data = await response.json();

    if (data.success) {
      displayUsers(data.data);
    } else {
      console.error(
        "Erreur lors du chargement des utilisateurs:",
        data.message
      );
    }
  } catch (error) {
    console.error("Erreur lors du chargement des utilisateurs:", error);
  }
}

// Afficher les utilisateurs dans la liste
function displayUsers(users) {
  const usersList = document.getElementById("users-list");

  if (!users || users.length === 0) {
    usersList.innerHTML =
      '<div class="no-partnerships-message"><p>Aucun utilisateur trouvé</p></div>';
    return;
  }

  usersList.innerHTML = users
    .map(
      (user) => `
        <div class="user-card">
            <div class="user-info">
                <div class="avatar">${
                  user.username ? user.username.charAt(0).toUpperCase() : "U"
                }</div>
                <div class="user-details">
                    <h3>${user.username || "Utilisateur"}</h3>
                    <p>${user.email || "Email non disponible"}</p>
                </div>
            </div>
            <div class="user-actions">
                <button class="btn btn-primary" onclick="sendPartnershipRequest('${
                  user.id
                }', '${user.username}')">
                    <i class="fas fa-user-plus"></i> Demander un partenariat
                </button>
            </div>
        </div>
    `
    )
    .join("");
}

// Recherche d'utilisateurs en temps réel
let searchTimeout;
document.addEventListener("DOMContentLoaded", function () {
  const searchInput = document.getElementById("user-search");
  if (searchInput) {
    searchInput.addEventListener("input", function () {
      clearTimeout(searchTimeout);
      const query = this.value.trim();

      searchTimeout = setTimeout(() => {
        if (query.length >= 2) {
          searchUsers(query);
        } else if (query.length === 0) {
          loadAllUsers();
        }
      }, 300);
    });
  }
});

// Rechercher des utilisateurs
async function searchUsers(query) {
  try {
    const response = await fetch(
      `/partnerships/api/partnerships/search?query=${encodeURIComponent(query)}`
    );
    const data = await response.json();

    if (data.success) {
      displayUsers(data.data);
    } else {
      console.error("Erreur lors de la recherche:", data.message);
    }
  } catch (error) {
    console.error("Erreur lors de la recherche:", error);
  }
}

// Envoyer une demande de partenariat
function sendPartnershipRequest(userId, username) {
  const message = prompt(
    `Envoyez un message à ${username} pour expliquer pourquoi vous souhaitez un partenariat :`
  );

  if (message && message.trim()) {
    const requestData = {
      requestedId: userId,
      message: message.trim(),
    };

    fetch("/partnerships/request", {
      method: "POST",
      headers: {
        "Content-Type": "application/json",
      },
      body: JSON.stringify(requestData),
    })
      .then((response) => response.json())
      .then((data) => {
        if (data.success) {
          alert("Demande de partenariat envoyée avec succès !");
          // Recharger la page pour afficher les nouveaux partenariats
          window.location.reload();
        } else {
          alert("Erreur lors de l'envoi de la demande: " + data.message);
        }
      })
      .catch((error) => {
        console.error("Erreur:", error);
        alert("Erreur lors de l'envoi de la demande");
      });
  }
}

// Confirmation pour les actions de partenariat
function confirmAction(action, partnershipId) {
  const messages = {
    accept: "Êtes-vous sûr de vouloir accepter ce partenariat ?",
    deny: "Êtes-vous sûr de vouloir refuser ce partenariat ?",
    cancel: "Êtes-vous sûr de vouloir annuler ce partenariat ?",
    end: "Êtes-vous sûr de vouloir terminer ce partenariat ?",
  };

  if (confirm(messages[action])) {
    const form = document.createElement("form");
    form.method = "POST";
    form.action = `/partnerships/${partnershipId}/${action}`;
    document.body.appendChild(form);
    form.submit();
  }
}
