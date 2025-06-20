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
