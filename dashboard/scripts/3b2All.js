// Speichert den Graphen zu POS (Alle).
const btnPOSAll = document.querySelector("#buttonPOSAll");

// Click Event Listener für den "Alle".
btnPOSAll.addEventListener("click", function () {
    document.getElementById("POS Speaker Name").innerHTML = "Laden..."
    plotPOSAllChart();
});

/*
API Abfrage zu POS für Alle.
 */
function getPOSOfAllSpeaker() {
    return new Promise(async resolve => {
        await $.ajax({
            method: "GET",
            dataType: "json",
            url: "http://api.prg2021.texttechnologylab.org/pos?minimum=10000",
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
Plottet den Graphen zu POS (Alle).
 */
async function plotPOSAllChart() {
    const data = await getPOSOfAllSpeaker();
    const divSpeakername = document.getElementById("POS Speaker Name");
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
            },
        });
        divSpeakername.innerHTML = "POS von allen Sprecher*innen";
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
        divSpeakername.innerHTML = "POS von allen Sprecher*innen"
    }


};