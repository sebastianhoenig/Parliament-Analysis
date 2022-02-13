// Speichert den Graphen zu POS (Sprecher*in).
let posPerSpeakerChart = undefined;

const btnPOSPerSpeaker = document.querySelector("#buttonPOSPerSpeaker");

// Click Event Listener für den Suchbutton.
btnPOSPerSpeaker.addEventListener("click", function () {
    const input = document.getElementById("inputPOSPerSpeaker");
    try {
        const name = input.value.split(", ");
        const firstname = name[0];
        const suffix = name[1].split(" (");
        const lastname = suffix[0];
        const party = suffix[1].substring(0, suffix[1].length-1);
        const speaker =  speakers.filter((speaker)=> speaker.firstname == firstname & speaker.name == lastname & speaker.party == party);
        if (speaker.length != 0) {
            document.getElementById("POS Speaker Name").innerHTML = "Laden...";
            plotPOSPerSpeakerChart(speaker[0].id);
        };
    } catch (e) {
        // pass
    } finally {
        input.value = "";
    };
});

/*
API Abfrage zu POS pro Sprecher*in.
 */
function getPOSOfSpeaker(id) {
    return new Promise(async resolve => {
        await $.ajax({
            method: "GET",
            dataType: "json",
            url: "http://api.prg2021.texttechnologylab.org/pos?minimum=100&user=" + id,
            success: function (data) {
                resolve(data.result);
            },
            error: function (error) {
                console.log(error);
                resolve();
            }
        });
    });
};

/*
Plottet den Graphen zu POS (Sprecher*in).
 */
async function plotPOSPerSpeakerChart(id) {
    const data = await getPOSOfSpeaker(id);
    const divSpeakername = document.getElementById("POS Speaker Name");
    const speakername = speakers.filter((speaker)=> speaker.id == id)[0];
    if (posPerSpeakerChart == undefined) {
        const ctx = document.getElementById("myAreaChart POS Speaker");
        posPerSpeakerChart = new Chart(ctx, {
            type: 'bar',
            data: {
                labels: data.map((pos)=> pos.pos),
                datasets: [{
                    data: data.map((pos)=> pos.count),
                    backgroundColor: "rgba(231,74,59)",
                    hoverBackgroundColor: "rgb(108,46,39)",
                }],
            },
            options: {
                legend: {
                    display: false
                },
                scales: {
                    xAxes: [{
                        ticks: {
                            min: 0
                        }
                    }]
                }

            },
        });
        divSpeakername.innerHTML = speakername.firstname + " " + speakername.name;
    } else {
        posPerSpeakerChart.data = {
            labels: data.map((pos)=> pos.pos),
            datasets: [{
                data: data.map((pos)=> pos.count),
                backgroundColor: "rgba(231,74,59)",
                hoverBackgroundColor: "rgb(108,46,39)",
            }],
        };
        posPerSpeakerChart.update();
        divSpeakername.innerHTML = speakername.firstname + " " + speakername.name;
    }


};