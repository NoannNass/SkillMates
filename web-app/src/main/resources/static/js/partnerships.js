// Fonction utilitaire pour obtenir les jetons CSRF
function getCsrf() {
  const token = document
    .querySelector('meta[name="_csrf"]')
    ?.getAttribute("content");
  const header = document
    .querySelector('meta[name="_csrf_header"]')
    ?.getAttribute("content");
  return { header, token };
}

document.addEventListener("DOMContentLoaded", function () {
  // Vérifier si nous venons d'un rafraîchissement après acceptation d'un partenariat
  const urlParams = new URLSearchParams(window.location.search);
  if (urlParams.has("refresh")) {
    // Supprimer le paramètre de l'URL sans recharger la page
    const newUrl = window.location.pathname;
    window.history.replaceState({}, document.title, newUrl);

    // Mettre en évidence la section des partenariats actifs
    const activePartnershipsHeader = document.querySelector(
      ".section-header:nth-of-type(2)"
    );
    if (activePartnershipsHeader) {
      activePartnershipsHeader.scrollIntoView({ behavior: "smooth" });
      activePartnershipsHeader.style.backgroundColor = "#f0f8ff";
      activePartnershipsHeader.style.transition = "background-color 1s";
      setTimeout(() => {
        activePartnershipsHeader.style.backgroundColor = "";
      }, 2000);
    }
  }

  // Gestion des boutons d'acceptation
  document.querySelectorAll(".btn.accept").forEach((button) => {
    button.addEventListener("click", function () {
      const partnershipId = this.getAttribute("data-id");
      if (confirm("Voulez-vous accepter cette demande de partenariat ?")) {
        const { header, token } = getCsrf();
        button.disabled = true;
        fetch(`/partnerships/${partnershipId}/accept`, {
          method: "POST",
          headers: {
            "Content-Type": "application/json",
            ...(header && token ? { [header]: token } : {}),
          },
        })
          .then((response) => {
            if (response.ok) {
              // Trouver et supprimer la carte de partenariat
              const partnershipCard = button.closest(".partnership-card");
              if (partnershipCard) {
                // Animation de disparition
                partnershipCard.style.opacity = "0";
                partnershipCard.style.transition = "opacity 0.5s";

                // Supprimer l'élément après l'animation
                setTimeout(() => {
                  partnershipCard.remove();

                  // Vérifier s'il reste des partenariats en attente
                  const pendingList =
                    document.querySelector(".partnerships-list");
                  if (pendingList && pendingList.children.length === 0) {
                    const noPartnershipsMessage = document.querySelector(
                      ".no-partnerships-message"
                    );
                    if (noPartnershipsMessage) {
                      noPartnershipsMessage.style.display = "block";
                    }
                  }

                  // Recharger les partenariats actifs sans recharger toute la page
                  loadActivePartnerships();
                }, 500);
              }
            } else {
              console.error("Erreur lors de l'acceptation du partenariat");
              button.disabled = false;
            }
          })
          .catch((error) => {
            console.error(
              "Erreur lors de l'acceptation du partenariat:",
              error
            );
            button.disabled = false;
          });
      }
    });
  });

  // Gestion des boutons de refus
  document.querySelectorAll(".btn.deny").forEach((button) => {
    button.addEventListener("click", function () {
      const partnershipId = this.getAttribute("data-id");
      if (confirm("Voulez-vous refuser cette demande de partenariat ?")) {
        const { header, token } = getCsrf();
        button.disabled = true;
        fetch(`/partnerships/${partnershipId}/deny`, {
          method: "POST",
          headers: {
            "Content-Type": "application/json",
            ...(header && token ? { [header]: token } : {}),
          },
        })
          .then((response) => {
            if (response.ok) {
              // Trouver et supprimer la carte de partenariat
              const partnershipCard = button.closest(".partnership-card");
              if (partnershipCard) {
                // Animation de disparition
                partnershipCard.style.opacity = "0";
                partnershipCard.style.transition = "opacity 0.5s";

                // Supprimer l'élément après l'animation
                setTimeout(() => {
                  partnershipCard.remove();

                  // Vérifier s'il reste des partenariats en attente
                  const pendingList =
                    document.querySelector(".partnerships-list");
                  if (pendingList && pendingList.children.length === 0) {
                    const noPartnershipsMessage = document.querySelector(
                      ".no-partnerships-message"
                    );
                    if (noPartnershipsMessage) {
                      noPartnershipsMessage.style.display = "block";
                    }
                  }
                }, 500);
              }
            } else {
              console.error("Erreur lors du refus du partenariat");
              button.disabled = false;
            }
          })
          .catch((error) => {
            console.error("Erreur lors du refus du partenariat:", error);
            button.disabled = false;
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

      // Si c'est l'onglet "Trouver un mate", activer l'onglet et mettre l'ancre sans redirection
      if (this.textContent === "Trouver un mate") {
        showTab("find");
        window.location.hash = ""; // URL: /partnerships#
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
    // Toujours passer par le contrôleur web `/partnerships/api/...` pour rester sur le même domaine
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
                    <h3>
                      ${user.username || "Utilisateur"}
                      <span class="user-objectives" style="font-weight: normal; font-size: 0.9em; color: #555;">
                        ${
                          user.learningObjectives &&
                          user.learningObjectives.length
                            ? ` — Objectifs: ${user.learningObjectives
                                .map((o) => o.title)
                                .join(", ")}`
                            : ""
                        }
                      </span>
                    </h3>
                </div>
            </div>
            <div class="user-actions">
                <button class="btn btn-primary" data-user-id="${
                  user.id
                }" data-username="${
        user.username || "Utilisateur"
      }" onclick="openRequestModal(this)">
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

// Modale: ouverture
function openRequestModal(button) {
  const modal = document.getElementById("request-modal");
  const username = button.getAttribute("data-username") || "Utilisateur";
  const userId = button.getAttribute("data-user-id");
  document.getElementById("request-modal-user").textContent = `À: ${username}`;
  document.getElementById("request-user-id").value = userId;
  document.getElementById("request-message").value = "";
  modal.classList.remove("hidden");
}

// Modale: câblage des boutons
document.addEventListener("DOMContentLoaded", function () {
  const modal = document.getElementById("request-modal");
  const cancelBtn = document.getElementById("request-cancel");
  const sendBtn = document.getElementById("request-send");
  if (cancelBtn) {
    cancelBtn.addEventListener("click", () => modal.classList.add("hidden"));
  }
  if (sendBtn) {
    sendBtn.addEventListener("click", () => {
      const requestedId = document.getElementById("request-user-id").value;
      const message = document.getElementById("request-message").value.trim();
      if (!message) {
        alert("Veuillez saisir un message.");
        return;
      }
      const payload = { requestedId, message };
      sendBtn.disabled = true;
      const { header, token } = getCsrf();
      fetch("/partnerships/request", {
        method: "POST",
        headers: {
          "Content-Type": "application/json",
          ...(header && token ? { [header]: token } : {}),
        },
        body: JSON.stringify(payload),
      })
        .then(async (response) => {
          const contentType = response.headers.get("Content-Type") || "";
          if (!contentType.includes("application/json")) {
            const text = await response.text();
            throw new Error(`Réponse non JSON (${response.status}): ${text}`);
          }
          return response.json();
        })
        .then((data) => {
          if (data && data.success) {
            alert("Demande de partenariat envoyée avec succès !");
            window.location.reload();
          } else {
            const msg = (data && data.message) || "Erreur inconnue";
            alert("Erreur lors de l'envoi de la demande: " + msg);
          }
        })
        .catch((error) => {
          console.error("Erreur d'envoi:", error);
          alert(
            "Erreur lors de l'envoi de la demande: " + (error.message || "")
          );
        })
        .finally(() => {
          sendBtn.disabled = false;
          modal.classList.add("hidden");
        });
    });
  }
});

// Fonction pour charger les partenariats actifs
async function loadActivePartnerships() {
  try {
    const userId =
      document.querySelector(".partnership-card")?.dataset?.currentUserId;
    if (!userId) {
      // Si nous ne pouvons pas récupérer l'ID utilisateur, rechargeons la page
      window.location.reload();
      return;
    }

    // Afficher un indicateur de chargement
    const activeSection =
      document.querySelector(".partnerships-list + .no-partnerships-message") ||
      document.querySelector(".section-header + .partnerships-list");
    if (activeSection) {
      const loadingIndicator = document.createElement("div");
      loadingIndicator.id = "loading-partnerships";
      loadingIndicator.innerHTML =
        "<p>Chargement des partenariats actifs...</p>";
      loadingIndicator.style.textAlign = "center";
      loadingIndicator.style.padding = "1rem";
      activeSection.parentNode.insertBefore(
        loadingIndicator,
        activeSection.nextSibling
      );
    }

    // Attendre un court instant pour montrer l'animation de chargement et permettre à l'UI de se mettre à jour
    await new Promise((resolve) => setTimeout(resolve, 1000));

    // Recharger la page avec un paramètre pour indiquer qu'il s'agit d'un rafraîchissement
    window.location.href = "/partnerships?refresh=true";
  } catch (error) {
    console.error("Erreur lors du chargement des partenariats actifs:", error);
    // En cas d'erreur, recharger la page
    window.location.reload();
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
