
(function() {
  var token = location.search.split('token=')[1];

  /**
  * Voorbeeld van een rappor.t
  * Belangrijk is dat het rapport gegenereerd wordt na dat de DOM is geladen.
  **/
  new Rapport(
    token
  );
})();
