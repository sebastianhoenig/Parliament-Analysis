// Speichert den Graphen zu Sentiment (Fraktionen).
let myRadarChartSentiment = undefined;


// /**
//  * API Abfrage zu Sentiment pro Sprecher*in.
//  * @param id
//  * @returns {Promise<unknown>}
//  */
// function getSentimentOfSpeaker(id) {
//     return new Promise(resolve => {
//         $.ajax({
//             method: "GET",
//             dataType: "json",
//             url: "http://api.prg2021.texttechnologylab.org/sentiment?user=" + id,
//             success: function (data) {
//                 var sentimentResult = data.result;
//                 const sentimentData = {neg: 0, neu: 0, pos: 0};
//
//                 for (let i = 0; i < sentimentResult.length; i++) {
//                     if (sentimentResult[i].sentiment > 0) {
//                         sentimentData.pos += sentimentResult[i].count;
//                     } else if (sentimentResult[i].sentiment < 0) {
//                         sentimentData.neg += sentimentResult[i].count;
//                     } else {
//                         sentimentData.neu += sentimentResult[i].count;
//                     }
//                 };
//                 resolve(sentimentData);
//             },
//             error: function (error) {
//                 console.log(error);
//                 resolve();
//             }
//         });
//     });
// };

/**
 * API Abfrage zu Sentiment pro Sprecher*in.
 * @param id
 * @returns {Promise<unknown>}
 */
function getSentimentAll() {
    return new Promise(resolve => {
        $.ajax({
            method: "GET",
            dataType: "json",
            url: "http://localhost:4567/sentiment",
            success: function (data) {
                var sentimentResult = data.result.map((element) => element.sentiment);
                const sentimentData = {neg: 0, neu: 0, pos: 0};
                let tempData = [];
                sentimentResult.forEach((e) => {
                    e.forEach((sentiment) => {
                        tempData.push(sentiment)
                    })
                })

                sentimentData.neg = tempData.filter((e) => e < 0.0).length;
                sentimentData.neu = tempData.filter((e) => e == 0.0 || e == null).length;
                sentimentData.pos = tempData.filter((e) => e > 0.0).length;

                resolve(sentimentData);
            },
            error: function (error) {
                console.log(error);
                resolve();
            }
        });
    });
};

plotSentimentAll();
/*
Plottet den Graphen zu Sentiment (Sprecher*in).
 */
async function plotSentimentAll() {
    const data = await getSentimentAll();
    const nameDiv = document.getElementById("SentimentNameField");
    if (myRadarChartSentiment == undefined) {
        const ctx = document.getElementById("myRadarChartSentiment");
        myRadarChartSentiment = new Chart(ctx, {
            type: 'radar',
            data: {
                labels: ["Positiv", "Negativ", "Neutral"],
                datasets: [{
                    data: [data.pos, data.neg, data.neu],
                    backgroundColor: ['#3dc88a', '#e74a3a', '#858796'],
                    hoverBackgroundColor: ['#3a805b', '#7e281d', '#4c4e56'],
                    hoverBorderColor: "rgba(234, 236, 244, 1)",
                    pointRadius: 5
                }],
            },
            options: {
                plugins : {
                    legend: {
                        display: false
                    }
                }
            }
        });
        nameDiv.innerHTML = "All";
    } else {
        myRadarChartSentiment.data = {
            labels: ["Positiv", "Negativ", "Neutral"],
            datasets: [{
                data: [data.pos, data.neg, data.neu],
                backgroundColor: ['#e74a3a', '#858796', '#3dc88a'],
                hoverBackgroundColor: ['#7e281d', '#4c4e56', '#3a805b'],
                hoverBorderColor: "rgba(234, 236, 244, 1)",
            }]
        };
        myRadarChartSentiment.update();
        nameDiv.innerHTML = "";
    }


};