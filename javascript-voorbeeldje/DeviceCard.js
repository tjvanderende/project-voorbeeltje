/**
* Genereer een toestel kaart met de
* @author IDP project T.J van der Ende
* @version 1.0
**/
var DeviceCard = function(data){
  this.template = '#rapport-devices';
  this.parser = new DOMParser();

  this.data = data;
  var view = this.view.get.bind(this);
  view(data);// laadt de view.
}

DeviceCard.prototype.view = {
  /**
  * Koppel alles aan elkaar vast.
  **/
  get(){
    var parent = document.querySelector(this.template);
    var kcalChart = this.view.chart.bind(this)();
    var minChart = this.view.chart.bind(this)();

    var card = `
    <div class="card mb-4">
      <div class="card-header">
        <h4 class="card-title">`+this.data.device+`</h4>
      </div>
      <div class="card-block row">
      </div>
    </div>`;
    var cardElement = $(card);
    var cardBlock = cardElement.find('.card-block')[0];
    cardBlock.append(kcalChart[0]);
    cardBlock.append(minChart[0]);
    parent.append(cardElement[0]);
    this.view.chartJs.bind(this)(kcalChart, this.view._parseData.bind(this)(this.data, 'calorieen'));
    this.view.chartJs.bind(this)(minChart,  this.view._parseData.bind(this)(this.data, 'time'));

  },
  _parseLabels(data){
    var labels = [];
    data.forEach((value, key) => {
      key += 1;
      labels.push('week'+key);
    });
    return labels;
  },
  /**
  * Zet de API json om naar een geschikt formaat dat geladen kan worden door Chart.js
  * @param data, de JSON object die wordt omgezet.
  * @param key, de sleutel die getoond wordt (Time of kcal).
  **/
  _parseData(data, key){
    var previousData = data[key][0]['previous'];
    var currentData = data[key][1]['current'];
    var labels = this.view._parseLabels(currentData);
    function _parseArray(dataToParse) {
      var data = [];
      dataToParse.forEach((value, key) => {
        data.push(value[labels[key]]);
      });
      return data;
    }
    return {
      label: key,
      previous: _parseArray(previousData),
      current: _parseArray(currentData),
      labels: labels
    };
  },
  /**
  * Genereer een chart.js chart.
  * @param chart, een gegenereerd Canvas object.
  * @param data, de data die speciaal is omgezet door middel van _parseData
  **/
  chartJs(chart, data){
    new Chart(chart, {

        type: 'line',
        data: {
            labels: data.labels,
            datasets: [{
                fill: false,
                label: 'Huidige maand',
                data: data.current,
                backgroundColor: 'rgb(248,151,27)',
                borderColor: 'rgba(248,151,27, 0.3)',
                pointHoverBorderColor: 'rgba(248,151,27)',
                pointHoverBorderColor: 'rgba(248,151,27)',

            }, {
              fill: false,
              backgroundColor: 'rgba(98, 103,109,1)',
              borderColor: 'rgba(98, 103,109,0.3)',
              pointHoverBackgroundColor: 'rgba(98, 103,109,1)',
              pointHoverBorderColor: 'rgba(98, 103,109,1)',
              label: 'Vorige maand',
              data: data.previous
            }]
        },
        responsive:true
    });
  },
  /**
  * Genereer een nieuw Canvas object.
  **/
  chart(){
    var guid = this.guid();
    var chart = '<canvas class=\'col-6\' id="'+guid+'"></canvas>';
    return $(chart);
  }
}
/**
* Genereer een unieke ID voor een chart.
**/
DeviceCard.prototype.guid = function(){
  function s4() {
    return Math.floor((1 + Math.random()) * 0x10000)
      .toString(16)
      .substring(1);
  }
  return s4() + s4() + '-' + s4() + '-' + s4() + '-' +s4() + '-' + s4() + s4() + s4();
}
