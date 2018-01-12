/**
* Simpele HTTP client die aangeroepen kan wordne met .get() om een nieuwe aanvraag te doen
* Naar de server.
**/
var HttpClient = function() {
    this.get = function(aUrl, aCallback) {
        var anHttpRequest = new XMLHttpRequest();
        anHttpRequest.onreadystatechange = function() {
            if (anHttpRequest.readyState == 4){
              if(anHttpRequest.status == 200){
                aCallback(anHttpRequest.responseText);

              }
              else {
                var errorModel = $('#errorModal');
                if(!errorModel.is(':visible'))
                  errorModel.modal('show');
              }
            }

        }

        anHttpRequest.open( 'GET', aUrl, true );
        anHttpRequest.send( null );
    }
}
/**
* Genereer een nieuw Rapport
* Toont alle data van de gebruiker tussen de twee opgegeven data.
* @author T.J van der Ende / idp project.
* @param token : login-token die gebruikt moet worden om een rapport aan te vragen van een gebruiker.
* @param startDate : Startdatum waarvoor het rapport wordt getoond.
* @param endDate: Einddatum waarvoor het rapport wordt getoond.

**/
var Rapport = function(token, startDate, endDate){
    this.token = token;
    this.startDate = this.startDate || '2017-01-01';
    this.endDate = this.endDate || '2017-02-01';
    this.url = 'http://tj-digital.nl/fitness/api/public/api/customer/rapport/timeframe/advice';
    this.urlGoals = 'http://tj-digital.nl/fitness/api/public/api/goals';
    this.request.get.bind(this)();
    this.heartrateContainer = '#rapport-heartrate'; // container waarin de hartslag data wordt getoond.
    this.otherContainer = '#rapport-other'; // container waarin de overige data wordt getoond.
}

Rapport.prototype.request = {
  /**
  * Haal alle data op met behulp van Httpclient.
  **/
    get(){
        var client = new HttpClient();
        client.get(this.url+'/'+this.startDate+'/'+this.endDate+'?token='+this.token, (response) => {
          var data = JSON.parse(response);
          var devices = data['devices'];
          this.request.getGoals.bind(this)(data.avg_heartrate, data.heartrateAdvice);
          this.view.meta.bind(this)(data.BMI, data.timeframe, data.is_healthy);
          devices.forEach((device) => {
            this.view.devices(device);
          });
        });
    },
    /**
    * Haal alle doelen op die ingeladen worden met behulp van client.
    * @param heartrate, gemiddelde hartslag van een gebruiker.
    * @param heartrateAdvice, een advies dat gegenereerd is door de API.
    * @param is_healthy, geeft true of false of het doel is nagestreefd volgens de kavronen formule (Zie API)
    **/
    getGoals(heartrate, heartrateAdvice, is_healthy){
      var client = new HttpClient();
      client.get(this.urlGoals, (response) => {
        var data = JSON.parse(response);
        this.view.heartrate.bind(this)(data, heartrate, heartrateAdvice, is_healthy);
      });
    }
}
Rapport.prototype.view = {
  /**
  * @param device: Json object met toestel checkins
  **/
  devices(device){
    new DeviceCard(device); // handel dit stuk af door een nieuwe device card aan te roepen met grafieken.

  },
  /**
  * @param BMI, de BMI van de gebruiker
  * @param timeframe, de timeframe waarin de data wordt getoond.
  **/
  meta(bmi, timeframe){
    var otherContainer = $(this.otherContainer);
    var table = $(`
      <table class='table table-responsive table-hover'>
        <thead>
          <th>Gegeven</th>
          <th>Data</th>
        </thead>
        <tbody>
          <tr><td>Getoonde timeframe</td><td>`+timeframe+`</td></tr>
          <tr><td>BMI</td><td>`+bmi+`</td></tr>
        </tbody>
      </table>
      `);
    var tbody = table.find('tbody');

    otherContainer.append(table);
  },
  /**
  *
  **/
  heartrate(data, avg_heartrate, heartrateAdvice, is_healthy){
    var heartrateContainer = $(this.heartrateContainer);
    var table = $(`
      <table class='table table-responsive table-hover'>
        <thead>
          <th>Doel</th><th>min/max %</th><th>Omschrijving</th></thead>
        <tbody>
        </tbody>
      </table>`);
    var success = (is_healthy) ? 'text-success' : 'text-danger';
    var block = $('<p class="lead '+success+`">
                    Gemiddelde hartslag: `+avg_heartrate+`
                  </p>`);
    var advice = $('<p class="lead '+success+'">'+heartrateAdvice+'</p>');
    var tbody = table.find('tbody');
    heartrateContainer.append(advice);
    heartrateContainer.append(block);
    heartrateContainer.append(table);
    data.forEach((row) => {
      tbody.append($('<tr><td>'+row.name+'</td><td>'+row.min+'-'+row.max+' %</td><td>'+row.description+'</td></tr>'));
    });
  }
}
