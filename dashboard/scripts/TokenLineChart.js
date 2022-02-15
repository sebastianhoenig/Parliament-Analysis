getAllToken();

function getAllToken(){
    $.ajax({
        url: "http://localhost:4567/token",
        method: 'GET',
        dataType: 'json',
        success: function (data) {
            var allToken = [];
            var tokenCount = [];

            for(let i = 0; i < data.result.length; i++){
                if(data.result[i].count>100000){
                    allToken.push(data.result[i].token);
                    tokenCount.push(data.result[i].count);
                }
            }

            var colorList = [];
            for(let j=0; j<allToken.length; j++){
                colorList.push("#1cc88a");
            }

            createLineChart(allToken, tokenCount);

        },
        error: function () {
            console.log("Geht nicht... :")

        }

    });
}

getTokenSpeaker();

function getTokenSpeaker(){
    document.querySelector("#btnSpeaker").addEventListener("click", function(){
        const input = document.getElementById("inputSpeaker");
        const splitInput = input.value.split(" ");
        const curr_firstname = splitInput[0];
        const curr_name = splitInput[1];

        // TO-DO: Wir brauchen eine Funktion oder Liste die uns zu einem Abgeordneten Namen die ID gibt

    /*    for (a = 0; a < data.result.length; a++) {
            if(data.result[a].name == curr_name & data.result[a].firstname == curr_firstname){
                const curr_id = data.result[a].id;
                console.log(curr_id);
                getTokenBySpeaker(curr_id);

            }
        }*/
    });
}
function getTokenBySpeaker(id){
    $.ajax({
        url: "http://localhost/token?id="+id,
        method: 'GET',
        dataType: 'json',
        success: function (data) {
            var allToken = [];
            var tokenCount = [];

            for(let i = 0; i < data.result.length; i++){
                if(data.result[i].count>100){
                    allToken.push(data.result[i].token);
                    tokenCount.push(data.result[i].count);
                }
            }


            createLineChart(allToken, tokenCount);

        },
        error: function () {
            console.log("Geht nicht... :")

        }

    });
}

getTokenParty();

function getTokenParty(){
    document.querySelector("#btnParty").addEventListener("click", function(){
        const input = document.getElementById("inputParty");
        getTokenByParty(input.value);
    });
}

function getTokenByParty(party){
    $.ajax({
        url: "http://localhost:4567/token?party="+party,
        method: 'GET',
        dataType: 'json',
        success: function (data) {
            var allToken = [];
            var tokenCount = [];

            for(let i = 0; i < data.result.length; i++){
                if(data.result[i].count>5000){
                    allToken.push(data.result[i].token);
                    tokenCount.push(data.result[i].count);
                }
            }

            createLineChart(allToken, tokenCount);

        },
        error: function () {
            console.log("Geht nicht... :")

        }

    });

}




function createLineChart(labelsInput, dataInpput){
    var ctx = document.getElementById("myLineChartAllToken");
    var myBarChart = new Chart(ctx, {
        type: 'line',
        data: {
            labels: labelsInput,
            datasets: [{
                fill: false,
                data: dataInpput,
                borderColor: "#1cc88a",
                pointBorderColor: "#1cc88a",

            }]
        },
        options: {
            plugins: {
                legend: false // Hide legend
            },
            scales: {
                xAxes: [{
                    gridLines: {
                        color: "rgba(0, 0, 0, 0)",
                    }
                }],
                yAxes: [{
                    gridLines: {
                        color: "rgba(0, 0, 0, 0)",
                    },
                    ticks:{
                        beginAtZero: true
                    }
                }]
            }
            }
    });
}