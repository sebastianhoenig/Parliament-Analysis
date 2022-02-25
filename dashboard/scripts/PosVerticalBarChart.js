/**
 * API requests for POS for all speeches.
 * @author vanessa
 *
 */

function getAllPos() {
  $.ajax({
    url: "http://localhost:4567/pos",
    method: "GET",
    dataType: "json",
    success: function (data) {
      var allPos = [];
      var posCount = [];

      for (let i = 0; i < data.result.length; i++) {
        allPos.push(data.result[i].pos);
        posCount.push(data.result[i].count);
      }

      createBarChart(allPos, posCount);
    },
    error: function () {
      console.log("Geht nicht... :");
    },
  });
}

/**
 * API requests for POS by Speaker.
 * @param speakerID
 * @param posID
 * @param beginDate
 * @param endDate
 * modified by Ben
 */
function getPosBySpeaker(speakerID, posID, beginDate = "2017-10-20",endDate = "2022-02-11") {
  $.ajax({
    url: "http://localhost:4567/pos?speakerID=" + speakerID + "&beginDate=" + beginDate + "&endDate=" + endDate,
    method: "GET",
    dataType: "json",
    success: function (data) {
      var allPos = [];
      var posCount = [];

      for (let i = 0; i < data.result.length; i++) {
        allPos.push(data.result[i].pos);
        posCount.push(data.result[i].count);
      }

      createBarChartPerson(allPos, posCount, posID);
    },
    error: function () {
      console.log("Geht nicht... :");
    },
  });
}

function getPosParty() {
  document.querySelector("#btnParty").addEventListener("click", function () {
    const input = document.getElementById("inputParty");
    getPosByParty(input.value);
  });
}

/**
 * API requests for POS by Party.
 * @param party
 * @param posID
 * @param beginDate
 * @param endDate
 * modified by Ben
 */
function getPosByParty(party, posID, beginDate = "2017-10-20",endDate = "2022-02-11") {
  $.ajax({
    url: "http://localhost:4567/pos?party=" + party + "&beginDate=" + beginDate + "&endDate=" + endDate,
    method: "GET",
    dataType: "json",
    success: function (data) {
      var allPos = [];
      var posCount = [];

      for (let i = 0; i < data.result.length; i++) {
        allPos.push(data.result[i].pos);
        posCount.push(data.result[i].count);
      }

      createBarChartPerson(allPos, posCount, posID);
    },
    error: function () {
      console.log("Geht nicht... :");
    },
  });
}

/**
 * creates bar chart with flexible element id.
 * @param labelsInput
 * @param dataInput
 * @param posID
 * modified by Sebastian
 */
function createBarChartPerson(labelsInput, dataInput, posID) {
  var ctx = document.getElementById(posID);
  var myBarChart = new Chart(ctx, {
    type: "bar",
    data: {
      labels: labelsInput,
      datasets: [
        {
          barThickness: 6,
          data: dataInput,
          backgroundColor: "#36b9cc",
        },
      ],
    },
    options: {
      plugins: {
        legend: false, // Hide legend
      },
      scales: {
        x: {
          gridLines: {
            color: "rgba(0, 0, 0, 0)",
          },
        },
        y: {
          gridLines: {
            color: "rgba(0, 0, 0, 0)",
          },
          ticks: {
            beginAtZero: true,
          },
        },
      },
    },
  });
}

/**
 * creates bar chart for all pos.
 * @param labelsInput
 * @param dataInput
 *
 */
function createBarChart(labelsInput, dataInput) {
  var ctx = document.getElementById("myBarChartAllPos");
  var myBarChart = new Chart(ctx, {
    type: "bar",
    data: {
      labels: labelsInput,
      datasets: [
        {
          barThickness: 6,
          data: dataInput,
          backgroundColor: "#36b9cc",
        },
      ],
    },
    options: {
      plugins: {
        legend: false, // Hide legend
      },
      scales: {
        x: {
          gridLines: {
            color: "rgba(0, 0, 0, 0)",
          },
        },
        y: {
          gridLines: {
            color: "rgba(0, 0, 0, 0)",
          },
          ticks: {
            beginAtZero: true,
          },
        },
      },
    },
  });
}
