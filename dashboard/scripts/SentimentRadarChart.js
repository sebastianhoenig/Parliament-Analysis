/**
 * API requests for Sentiment for all speeches.
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
 * API requests for Sentiment per speaker.
 * @param id
 * @returns {Promise<unknown>}
 */
function getSentimentSpeaker(id, startDate, endDate) {
  return new Promise((resolve) => {
    $.ajax({
      method: "GET",
      dataType: "json",
      url: "http://localhost:4567/sentiment?speakerID=" + id + "&beginDate=" + startDate + "&endDate=" + endDate,
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
 * API requests for Sentiment per party.
 * @param id
 * @returns {Promise<unknown>}
 */
function getSentimentParty(id, startDate, endDate) {
  return new Promise((resolve) => {
    $.ajax({
      method: "GET",
      dataType: "json",
      url: "http://localhost:4567/sentiment?party=" + id + "&beginDate=" + startDate + "&endDate=" + endDate,
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

/**
 * This methode is for switch through the right data.
 * @param type
 * @param id
 * @returns {Promise<*>}
 */
async function getSentimentData(type, id, startDate, endDate) {
  switch (type) {
    case 0:
      return await getSentimentAll();
      break;
    case 1:
      return await getSentimentSpeaker(id, startDate, endDate);
      break;
    case 2:
      return await getSentimentParty(id, startDate, endDate);
      break;
    default:
      return undefined;
  }
}

/**
 * The Methode is plotting for a given canvasID or speakerID a Chart.
 * @param id
 * @param canvasID
 * @param type
 * @returns {Promise<void>}
 */
async function plotSentimentAll(
  id = "all",
  canvasID = "myRadarChartSentiment",
  type = 0,
  startDate = "2017-10-26",
  endDate = "2022-02-11"
) {
  const data = await getSentimentData(type, id, startDate, endDate);
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
