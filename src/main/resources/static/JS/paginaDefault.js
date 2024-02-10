document.addEventListener("DOMContentLoaded", function() {
    // Quando la pagina è completamente caricata
    document.body.style.opacity = 1; // Imposta l'opacità al valore completo
    var header = document.querySelector("header"); // Seleziona l'elemento header
    header.style.opacity = 1; // Imposta l'opacità dell'header al valore completo

});
$(document).ready(function(){
    $('.navbar-toggler').click(function(){
      $('#navbarNav').toggleClass('show');
    });
  });
