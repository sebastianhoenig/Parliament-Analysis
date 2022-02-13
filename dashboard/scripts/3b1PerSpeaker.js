// Speichert den Graphen zu Sentiment (Fraktionen).
let sentimentPerSpeakerChart = undefined;

const btnSentimentPerSpeaker = document.querySelector("#buttonSentimentPerSpeaker");

// Click Event Listener für den Suchbutton.
btnSentimentPerSpeaker.addEventListener("click", function () {
    const input = document.getElementById("inputSentimentPerSpeaker");
    try {
        const name = input.value.split(", ");
        const firstname = name[0];
        const suffix = name[1].split(" (");
        const lastname = suffix[0];
        const party = suffix[1].substring(0, suffix[1].length-1);
        const speaker =  speakers.filter((speaker)=> speaker.firstname == firstname & speaker.name == lastname & speaker.party == party);
        if (speaker.length != 0) {
            document.getElementById("SpeakerNameInSentiment").innerHTML = "Laden...";
            plotSentimentPerSpeakerChart(speaker[0].id);
        };
    } catch (e) {
        // pass
    } finally {
        input.value = "";
    };
});

/*
API Abfrage zu Sentiment pro Sprecher*in.
 */
function getSentimentOfSpeaker(id) {
    return new Promise(resolve => {
        $.ajax({
            method: "GET",
            dataType: "json",
            url: "http://api.prg2021.texttechnologylab.org/sentiment?user=" + id,
            success: function (data) {
                var sentimentResult = data.result;
                const sentimentData = {neg: 0, neu: 0, pos: 0};

                for (let i = 0; i < sentimentResult.length; i++) {
                    if (sentimentResult[i].sentiment > 0) {
                        sentimentData.pos += sentimentResult[i].count;
                    } else if (sentimentResult[i].sentiment < 0) {
                        sentimentData.neg += sentimentResult[i].count;
                    } else {
                        sentimentData.neu += sentimentResult[i].count;
                    }
                };
                resolve(sentimentData);
            },
            error: function (error) {
                console.log(error);
                resolve();
            }
        });
    });
};

/*
Plottet den Graphen zu Sentiment (Sprecher*in).
 */
async function plotSentimentPerSpeakerChart(id) {
    const data = await getSentimentOfSpeaker(id);
    const divSpeakername = document.getElementById("SpeakerNameInSentiment");
    const speakername = speakers.filter((speaker)=> speaker.id == id)[0];
    if (sentimentPerSpeakerChart == undefined) {
        const ctx = document.getElementById("myPieChart Speaker");
        sentimentPerSpeakerChart = new Chart(ctx, {
            type: 'doughnut',
            data: {
                labels: ["Negativ", "Neutral", "Positiv"],
                datasets: [{
                    data: [data.neg, data.neu, data.pos],
                    backgroundColor: ['#e74a3a', '#858796', '#3dc88a'],
                    hoverBackgroundColor: ['#7e281d', '#4c4e56', '#3a805b'],
                    hoverBorderColor: "rgba(234, 236, 244, 1)",
                }],
            },
            options: {
                maintainAspectRatio: false,
                tooltips: {
                    backgroundColor: "rgb(255,255,255)",
                    bodyFontColor: "#858796",
                    borderColor: '#dddfeb',
                    borderWidth: 1,
                    xPadding: 15,
                    yPadding: 15,
                    displayColors: false,
                    caretPadding: 10,
                },
                legend: {
                    display: false
                },
                cutoutPercentage: 50,
            },
        });
        divSpeakername.innerHTML = speakername.firstname + " " + speakername.name;
    } else {
        sentimentPerSpeakerChart.data = {
            labels: ["Negativ", "Neutral", "Positiv"],
            datasets: [{
                data: [data.neg, data.neu, data.pos],
                backgroundColor: ['#e74a3a', '#858796', '#3dc88a'],
                hoverBackgroundColor: ['#7e281d', '#4c4e56', '#3a805b'],
                hoverBorderColor: "rgba(234, 236, 244, 1)",
            }]
        };
        sentimentPerSpeakerChart.update();
        divSpeakername.innerHTML = speakername.firstname + " " + speakername.name;
    }


};