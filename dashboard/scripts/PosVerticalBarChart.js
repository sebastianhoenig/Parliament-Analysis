getAllPos();

function getAllPos(){
    $.ajax({
        url: "http://localhost:4567/pos",
        method: 'GET',
        dataType: 'json',
        success: function (data) {
            var allPos = [];
            var posCount = [];

            for(let i = 0; i < data.result.length; i++){
                allPos.push(data.result[i].pos);
                posCount.push(data.result[i].count);
            }


            createBarChart(allPos, posCount);

        },
        error: function () {
            console.log("Geht nicht... :")

        }

    });

}

//getPosSpeaker();

function getPosSpeaker(){
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
                    getPosBySpeaker(curr_id);

                }
            }*/
    });
}

function getPosBySpeaker(speakerID){
    $.ajax({
        url: "http://localhost:4567/pos?speakerID="+speakerID,
        method: 'GET',
        dataType: 'json',
        success: function (data) {
            var allPos = [];
            var posCount = [];

            for(let i = 0; i < data.result.length; i++){
                allPos.push(data.result[i].pos);
                posCount.push(data.result[i].count);
            }


            createBarChart(allPos, posCount);

        },
        error: function () {
            console.log("Geht nicht... :")

        }

    });
}


//getPosParty();

function getPosParty(){
    document.querySelector("#btnParty").addEventListener("click", function(){
        const input = document.getElementById("inputParty");
        getPosByParty(input.value);
    });
}


function getPosByParty(party){
    $.ajax({
        url: "http://localhost:4567/pos?party="+party,
        method: 'GET',
        dataType: 'json',
        success: function (data) {
            var allPos = [];
            var posCount = [];

            for(let i = 0; i < data.result.length; i++){
                allPos.push(data.result[i].pos);
                posCount.push(data.result[i].count);
            }


            createBarChart(allPos, posCount);

        },
        error: function () {
            console.log("Geht nicht... :")

        }

    });

}


function createBarChart(labelsInput, dataInput){
    var ctx = document.getElementById("myBarChartAllPos");
    var myBarChart = new Chart(ctx, {
        type: 'bar',
        data: {
            labels: labelsInput,
            datasets: [{
                axis: 'x',
                barThickness: 6,
                data: dataInput,
                backgroundColor: "#36b9cc",

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