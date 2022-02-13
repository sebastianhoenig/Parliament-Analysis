// Speichert den Graphen zu Sentiment (Fraktionen).
let sentimentPerFractionChart = undefined;

const btnSentimentPerFraction = document.querySelector("#buttonSentimentPerFraction");

// Click Event Listener für den Suchbutton.
btnSentimentPerFraction.addEventListener("click", function () {
    const input = document.getElementById("inputSentimentPerFraction");
    const name = input.value;
    const fraction =  fractions.filter((fraction)=> fraction == name);
    if (name == fraction[0]){
        document.getElementById("FractionNameInSentiment").innerHTML = "Laden...";
        plotSentimentPerFractionChart(fraction[0]);
    };
    input.value = "";
});


/*
API Abfrage zu Sentiment pro Fraktion.
 */
function getSentimentOfFraction(fraction) {
    return new Promise(resolve => {
        $.ajax({
            method: "GET",
            dataType: "json",
            url: "http://api.prg2021.texttechnologylab.org/sentiment?fraction=" + fraction,
            success: function (data) {
                const sentimentResult = data.result;
                const sentimentData = {neg: 0, neu: 0, pos: 0};

                const pos = sentimentResult.filter((x)=> x.sentiment > 0);
                const posCount = pos.map((x)=> x.count)
                for (let i = 0; i < posCount.length; i++) {
                    sentimentData.pos += posCount[i];
                };

                const neu = sentimentResult.filter((x)=> x.sentiment == 0);
                const neuCount = neu.map((x)=> x.count)
                for (let i = 0; i < neuCount.length; i++) {
                    sentimentData.neu += neuCount[i];
                };

                const neg = sentimentResult.filter((x)=> x.sentiment < 0);
                const negCount = neg.map((x)=> x.count)
                for (let i = 0; i < negCount.length; i++) {
                    sentimentData.neg += negCount[i];
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
Plottet den Graphen zu Sentiment (Fraktion).
 */
async function plotSentimentPerFractionChart(fraction) {
    const data = await getSentimentOfFraction(fraction);
    const divFractionname = document.getElementById("FractionNameInSentiment");
    if (sentimentPerFractionChart == undefined) {
        const ctx = document.getElementById("myPieChart Fraction");
        sentimentPerFractionChart = new Chart(ctx, {
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
        divFractionname.innerHTML = fraction;
    } else {
        sentimentPerFractionChart.data = {
            labels: ["Negativ", "Neutral", "Positiv"],
            datasets: [{
                data: [data.neg, data.neu, data.pos],
                backgroundColor: ['#e74a3a', '#858796', '#3dc88a'],
                hoverBackgroundColor: ['#7e281d', '#4c4e56', '#3a805b'],
                hoverBorderColor: "rgba(234, 236, 244, 1)",
            }]
        };
        sentimentPerFractionChart.update();
        divFractionname.innerHTML = fraction;
    }
};