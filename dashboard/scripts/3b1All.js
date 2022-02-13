// Speichert den Graphen zu Sentiment (Alle).
let sentimentAllChart = undefined;

/*
API Abfrage zu Sentiment.
 */
function getSentimentOfAllSpeaker() {
    return new Promise(async resolve => {
        const sentimentData = {neg: 0, neu: 0, pos: 0};
        await $.ajax({
            method: "GET",
            dataType: "json",
            url: "http://api.prg2021.texttechnologylab.org/sentiment",
            success: function (data) {
                const sentimentResult = data.result;

                for (let i = 0; i < sentimentResult.length; i++) {
                    if (sentimentResult[i].sentiment > 0) {
                        sentimentData.pos += sentimentResult[i].count;
                    } else if (sentimentResult[i].sentiment < 0) {
                        sentimentData.neg += sentimentResult[i].count;
                    } else {
                        sentimentData.neu += sentimentResult[i].count;
                    }
                };
            },
            error: function (error) {
                console.log(error);
                resolve();
            }
        });
        resolve(sentimentData);
    });
};

/*
Plottet den Graphen zu Sentiment (Alle).
 */
async function plotSentimentAllChart() {
    const data = await getSentimentOfAllSpeaker();
    const divAll = document.getElementById("AllInSentiment");

    const ctx = document.getElementById("myPieChart All");
    sentimentAllChart = new Chart(ctx, {
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
    divAll.innerHTML = "Sentiment von allen Sprecher*innen";
};