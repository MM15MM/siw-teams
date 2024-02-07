document.addEventListener("DOMContentLoaded", function() {
    // Quando la pagina è completamente caricata
    document.body.style.opacity = 1; // Imposta l'opacità al valore completo
    var header = document.querySelector("header"); // Seleziona l'elemento header
    header.style.opacity = 1; // Imposta l'opacità dell'header al valore completo

});

// Lista degli URL delle pagine da precaricare
var pagesToPreload = [
    '/admin/players',
    '/admin/deletePlayer/',
    '/admin/formNewPlayer',
    '/admin/player/',
    '/admin/updatePlayers/',
    '/admin/teams',
    '/admin/team/',
    '/admin/formNewTeam',
    '/admin/formUpdateTeam/',
    '/admin/newPresident',
    '/success',
    '/admin/indexAdmin',
    '/'
];

// Cicla attraverso la lista e precarica le pagine
pagesToPreload.forEach(function(url) {
    fetch(url);
});