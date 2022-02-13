let btnTokenAll = document.querySelector("#buttonTokenAll");

// Click Event Listener für den Alle Button.
btnTokenAll.addEventListener("click", function () {
    document.getElementById("Token Speaker Name").innerHTML = "Laden..."
    plotTokenAllChart();
});

/*
API Abfrage zu Token Alle.
 */
function getTokenOfAllSpeaker() {
    return new Promise(async resolve => {
        await $.ajax({
            method: "GET",
            dataType: "json",
            url: "http://api.prg2021.texttechnologylab.org/tokens?minimum=15000",
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
Plottet den Graphen zu Token (Alle).
 */
async function plotTokenAllChart() {
    const data = await getTokenOfAllSpeaker();
    const divSpeakername = document.getElementById("Token Speaker Name");
    if (tokenPerSpeakerChart == undefined) {
        const ctx = document.getElementById("myAreaChart Token Speaker");
        tokenPerSpeakerChart = new Chart(ctx, {
            type: 'bar',
            data: {
                labels: data.map((token)=> token.token),
                datasets: [{
                    data: data.map((token)=> token.count),
                    backgroundColor: "rgba(78,115,223)",
                    hoverBackgroundColor: "rgb(28,64,173)",
                }],
            },
            options: {
                legend: {
                    display: false
                },
            },
        });
        divSpeakername.innerHTML = "Token von allen Sprecher*innen";
    } else {
        tokenPerSpeakerChart.data = {
            labels: data.map((token)=> token.token),
            datasets: [{
                data: data.map((token)=> token.count),
                backgroundColor: "rgba(78,115,223)",
                hoverBackgroundColor: "rgb(28,64,173)",
            }],
        };
        tokenPerSpeakerChart.update();
        divSpeakername.innerHTML = "Token von allen Sprecher*innen"
    }
};