document.addEventListener("DOMContentLoaded", function() {
    // Quando la pagina è completamente caricata
    document.body.style.opacity = 1; // Imposta l'opacità al valore completo
    var header = document.querySelector("header"); // Seleziona l'elemento header
    header.style.opacity = 1; // Imposta l'opacità dell'header al valore completo

});

// Lista degli URL delle pagine da precaricare
var pagesToPreload = [
    '/players',
    '/player/',
    '/updatePlayers/',
    '/teams',
    '/team/',
    '/addNewPlayer/',
    '/addNewPlayerToTeam/',
    '/removePlayerFromTeam/',
    '/success',
    '/index',
    '/'
];

// Cicla attraverso la lista e precarica le pagine
pagesToPreload.forEach(function(url) {
    fetch(url);
});