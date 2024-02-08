
  // Mostra lo spinner quando inizia il caricamento
  function showSpinner() {
    document.getElementById("loadingSpinner").style.display = "block";
  }

  // Nascondi lo spinner quando il caricamento è completato
  function hideSpinner() {
    document.getElementById("loadingSpinner").style.display = "none";
  }

  // Esempio di utilizzo durante una chiamata AJAX
  function fetchData() {
    // Mostra lo spinner prima di effettuare la chiamata AJAX
    showSpinner();


    // Quando il caricamento è completato, nascondi lo spinner
    // Supponiamo che qui ci sia il completamento della chiamata AJAX
    setTimeout(hideSpinner, 1000); // nascondi lo spinner dopo 3 secondi
  }

  // Esegui la funzione per mostrare lo spinner al caricamento della pagina
  fetchData();