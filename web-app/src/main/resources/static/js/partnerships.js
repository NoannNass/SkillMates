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
});
