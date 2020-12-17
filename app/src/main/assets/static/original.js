function getHighChart(ticker) {
        alert('Query Variable ');
        Highcharts.getJSON('https://stockbackend.azurewebsites.net/api/historydata/'+ticker, function (data) {

          // split the data set into ohlc and volume
          var ohlc = [],
            volume = [],
            dataLength = data.length,
            // set the allowed units for data grouping
            groupingUnits = [[
              'week',             // unit name
              [1]               // allowed multiples
            ], [
              'month',
              [1, 2, 3, 4, 6]
            ]],

            i = 0;

          for (i; i < dataLength; i += 1) {
            ohlc.push([
              data[i][0], // the date
              data[i][1], // open
              data[i][2], // high
              data[i][3], // low
              data[i][4] // close
            ]);

            volume.push([
              data[i][0], // the date
              data[i][5] // the volume
            ]);
          }


          // create the chart
          Highcharts.stockChart('charts', {

            rangeSelector: {
              selected: 2
            },

            title: {
              text: null
            },

            yAxis: [{
              startOnTick: false,
              endOnTick: false,
              labels: {
                align: 'right',
                x: -3
              },
              title: {
                text: 'OHLC'
              },
              height: '60%',
              lineWidth: 2,
              resize: {
                enabled: true
              }
            }, {
              labels: {
                align: 'right',
                x: -3
              },
              title: {
                text: 'Volume'
              },
              top: '65%',
              height: '35%',
              offset: 0,
              lineWidth: 2
            }],

            tooltip: {
              split: true
            },

            plotOptions: {
              series: {
                dataGrouping: {
                  units: groupingUnits
                }
              }
            },

            series: [{
              type: 'candlestick',
              name: ticker,
              id: ticker,
              zIndex: 2,
              data: ohlc
            }, {
              type: 'column',
              name: 'Volume',
              id: 'volume',
              data: volume,
              yAxis: 1
            }, {
              type: 'vbp',
              linkedTo: ticker,
              params: {
                volumeSeriesID: 'volume'
              },
              dataLabels: {
                enabled: false
              },
              zoneLines: {
                enabled: false
              }
            }, {
              type: 'sma',
              linkedTo: ticker,
              zIndex: 1,
              marker: {
                enabled: false
              }
            }]
          });
        });
}
//getHighChart("IBM");