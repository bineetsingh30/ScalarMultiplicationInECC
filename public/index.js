$(function(){
    $('#dataform').ajaxForm(function(result){
        $('#dataform').hide()

        google.charts.load('current', {packages: ['corechart', 'bar']});
        google.charts.setOnLoadCallback(drawMultSeries);

        function drawMultSeries() {
            //time graph
            var data = google.visualization.arrayToDataTable([
              ['Algorithm', 'Time required'],
              ['Naive Method', result['time'][0] + 1],
              ['Binary Left to Right Method', result['time'][1] + 1],
              ['Binary Right to Left Method', result['time'][2] + 1],
              ['Addition Subtraction Method', result['time'][3] + 1],
              ['Windowed Method', result['time'][4] + 1],
              ['Montgomery Ladder Method', result['time'][5] + 1],
              ['wNAF method', result['time'][6] + 1]
            ]);
      
            var options = {
              chartArea: {width: '50%'},
              hAxis: {
                title: 'Time taken in ms',
                minValue: 0
              },
              vAxis: {
                title: 'Algorithm'
              }
            };
      
            var time_chart = new google.visualization.BarChart(document.getElementById('time_chart'));
            time_chart.draw(data, options);

            //addition graph
            data = google.visualization.arrayToDataTable([
                ['Algorithm', 'Addition operations'],
                ['Naive Method', result['adds'][0]],
                ['Binary Left to Right Method', result['adds'][1]],
                ['Binary Right to Left Method', result['adds'][2]],
                ['Addition Subtraction Method', result['adds'][3]],
                ['Windowed Method', result['adds'][4]],
                ['Montgomery Ladder Method', result['adds'][5]],
                ['wNAF method', result['adds'][6]]
              ]);
        
              options = {
                chartArea: {width: '50%'},
                hAxis: {
                  title: 'Number of addition operations',
                  minValue: 0
                },
                vAxis: {
                  title: 'Algorithm'
                }
              };
        
              var add_chart = new google.visualization.BarChart(document.getElementById('add_chart'));
              add_chart.draw(data, options);

            //doubling graph
            data = google.visualization.arrayToDataTable([
                ['Algorithm', 'Doubling operations'],
                ['Naive Method', result['doubles'][0]],
                ['Binary Left to Right Method', result['doubles'][1]],
                ['Binary Right to Left Method', result['doubles'][2]],
                ['Addition Subtraction Method', result['doubles'][3]],
                ['Windowed Method', result['doubles'][4]],
                ['Montgomery Ladder Method', result['doubles'][5]],
                ['wNAF method', result['doubles'][6]]
              ]);
        
              options = {
                chartArea: {width: '50%'},
                hAxis: {
                  title: 'Number of doubling operations',
                  minValue: 0
                },
                vAxis: {
                  title: 'Algorithm'
                }
              };
        
              var double_chart = new google.visualization.BarChart(document.getElementById('double_chart'));
              double_chart.draw(data, options);

            //precomputation graph
            data = google.visualization.arrayToDataTable([
                ['Algorithm', 'Precomputations'],
                ['Naive Method', result['precomputations'][0]],
                ['Binary Left to Right Method', result['precomputations'][1]],
                ['Binary Right to Left Method', result['precomputations'][2]],
                ['Addition Subtraction Method', result['precomputations'][3]],
                ['Windowed Method', result['precomputations'][4]],
                ['Montgomery Ladder Method', result['precomputations'][5]],
                ['wNAF method', result['precomputations'][6]]
              ]);
        
              options = {
                chartArea: {width: '50%'},
                hAxis: {
                  title: 'Precomputations required',
                  minValue: 0
                },
                vAxis: {
                  title: 'Algorithm'
                }
              };
        
              var precomputation_chart = new google.visualization.BarChart(document.getElementById('precomputation_chart'));
              precomputation_chart.draw(data, options);

            //hamming wt graph
            data = google.visualization.arrayToDataTable([
                ['Algorithm', 'Hamming Weight'],
                ['Naive Method', result['hammingwt'][0]],
                ['Binary Left to Right Method', result['hammingwt'][1]],
                ['Binary Right to Left Method', result['hammingwt'][2]],
                ['Addition Subtraction Method', result['hammingwt'][3]],
                ['Windowed Method', result['hammingwt'][4]],
                ['Montgomery Ladder Method', result['hammingwt'][5]],
                ['wNAF method', result['hammingwt'][6]]
              ]);
        
              options = {
                chartArea: {width: '50%'},
                hAxis: {
                  title: 'Hamming Weight of k',
                  minValue: 0
                },
                vAxis: {
                  title: 'Algorithm'
                }
              };
        
              var hammingwt_chart = new google.visualization.BarChart(document.getElementById('hammingwt_chart'));
              hammingwt_chart.draw(data, options);
          }
    })
})