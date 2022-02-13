// Speichert den Graphen zu Token (Sprecher*in).
let tokenPerSpeakerChart = undefined;

const btnTokenPerSpeaker = document.querySelector("#buttonTokenPerSpeaker");

// Click Event Listener für den Suchbutton.
btnTokenPerSpeaker.addEventListener("click", function () {
    const input = document.getElementById("inputTokenPerSpeaker");
    try {
        const name = input.value.split(", ");
        const firstname = name[0];
        const suffix = name[1].split(" (");
        const lastname = suffix[0];
        const party = suffix[1].substring(0, suffix[1].length-1);
        const speaker =  speakers.filter((speaker)=> speaker.firstname == firstname & speaker.name == lastname & speaker.party == party);
        if (speaker.length != 0) {
            document.getElementById("Token Speaker Name").innerHTML = "Laden...";
            plotTokenPerSpeakerChart(speaker[0].id);
        };
    } catch (e) {
        // pass
    } finally {
        input.value = "";
    };
});

/*
API Abfrage zu Token pro Sprecher*in.
 */
function getTokenOfSpeaker(id) {
    return new Promise(async resolve => {
        await $.ajax({
            method: "GET",
            dataType: "json",
            url: "http://api.prg2021.texttechnologylab.org/tokens?minimum=100&user=" + id,
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
Plottet den Graphen zu Token (Sprecher*in).
 */
async function plotTokenPerSpeakerChart(id) {
    const data = await getTokenOfSpeaker(id);
    const divSpeakername = document.getElementById("Token Speaker Name");
    const speakername = speakers.filter((speaker)=> speaker.id == id)[0];
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
        divSpeakername.innerHTML = speakername.firstname + " " + speakername.name;
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
        divSpeakername.innerHTML = speakername.firstname + " " + speakername.name;
    }
};