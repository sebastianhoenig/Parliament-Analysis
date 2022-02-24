/**
 * API Abfrage zu Sentiment pro Sprecher*in.
 * @param id
 * @returns {Promise<unknown>}
 */
function getSentimentAll() {
  return new Promise((resolve) => {
    $.ajax({
      method: "GET",
      dataType: "json",
      url: "http://localhost:4567/sentiment",
      success: function (data) {
        var sentimentResult = data.result.map((element) => element.sentiment);
        const sentimentData = { neg: 0, neu: 0, pos: 0 };
        let tempData = [];
        sentimentResult.forEach((e) => {
          e.forEach((sentiment) => {
            tempData.push(sentiment);
          });
        });

        sentimentData.neg = tempData.filter((e) => e < 0.0).length;
        sentimentData.neu = tempData.filter(
          (e) => e == 0.0 || e == null
        ).length;
        sentimentData.pos = tempData.filter((e) => e > 0.0).length;

        resolve(sentimentData);
      },
      error: function (error) {
        console.log(error);
        resolve();
      },
    });
  });
}

/**
 * API Abfrage zu Sentiment pro Sprecher*in.
 * @param id
 * @returns {Promise<unknown>}
 */
function getSentimentSpeaker(id) {
  return new Promise((resolve) => {
    $.ajax({
      method: "GET",
      dataType: "json",
      url: "http://localhost:4567/sentiment?speakerID=" + id,
      success: function (data) {
        const sentimentResult = data.result.map(
          (element) => element.sentiment
        )[0];
        const sentimentData = { neg: 0, neu: 0, pos: 0 };

        sentimentData.neg = sentimentResult.filter((e) => e < 0.0).length;
        sentimentData.neu = sentimentResult.filter(
          (e) => e == 0.0 || e == null
        ).length;
        sentimentData.pos = sentimentResult.filter((e) => e > 0.0).length;

        resolve(sentimentData);
      },
      error: function (error) {
        console.log(error);
        resolve();
      },
    });
  });
}

/**
 * API Abfrage zu Sentiment pro Sprecher*in.
 * @param id
 * @returns {Promise<unknown>}
 */
function getSentimentParty(id) {
  return new Promise((resolve) => {
    $.ajax({
      method: "GET",
      dataType: "json",
      url: "http://localhost:4567/sentiment?party=" + id,
      success: function (data) {
        const sentimentResult = data.result.map((element) => element.sentiment);
        const sentimentData = { neg: 0, neu: 0, pos: 0 };
        let tempData = [];
        sentimentResult.forEach((e) => {
          e.forEach((sentiment) => {
            tempData.push(sentiment);
          });
        });

        sentimentData.neg = tempData.filter((e) => e < 0.0).length;
        sentimentData.neu = tempData.filter(
          (e) => e == 0.0 || e == null
        ).length;
        sentimentData.pos = tempData.filter((e) => e > 0.0).length;

        resolve(sentimentData);
      },
      error: function (error) {
        console.log(error);
        resolve();
      },
    });
  });
}

async function getSentimentData(type, id) {
  switch (type) {
    case 0:
      return await getSentimentAll();
      break;
    case 1:
      return await getSentimentSpeaker(id);
      break;
    case 2:
      return await getSentimentParty(id);
      break;
    default:
      return undefined;
  }
}

/*
Plottet den Graphen zu Sentiment (Sprecher*in).
 */
async function plotSentimentAll(
  id = "all",
  canvasID = "myRadarChartSentiment",
  type = 0
) {
  const data = await getSentimentData(type, id);
  const ctx = document.getElementById(canvasID);
  const myRadarChartSentiment = new Chart(ctx, {
    type: "radar",
    data: {
      labels: ["Positiv", "Negativ", "Neutral"],
      datasets: [
        {
          data: [data.pos, data.neg, data.neu],
          backgroundColor: ["#3dc88a", "#e74a3a", "#858796"],
          hoverBackgroundColor: ["#3a805b", "#7e281d", "#4c4e56"],
          hoverBorderColor: "rgba(234, 236, 244, 1)",
          pointRadius: 5,
        },
      ],
    },
    options: {
      plugins: {
        legend: {
          display: false,
        },
      },
    },
  });
}
