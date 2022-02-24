function getAllToken() {
  $.ajax({
    url: "http://localhost:4567/token",
    method: "GET",
    dataType: "json",
    success: function (data) {
      var allToken = [];
      var tokenCount = [];

      for (let i = 0; i < data.result.length; i++) {
        if (data.result[i].count > 100000) {
          allToken.push(data.result[i].token);
          tokenCount.push(data.result[i].count);
        }
      }

      var colorList = [];
      for (let j = 0; j < allToken.length; j++) {
        colorList.push("#1cc88a");
      }

      createLineChart(allToken, tokenCount);
    },
    error: function () {
      console.log("Geht nicht... :");
    },
  });
}

function getTokenBySpeaker(id, tokenID) {
  $.ajax({
    url: "http://localhost:4567/token?speakerID=" + id,
    method: "GET",
    dataType: "json",
    success: function (data) {
      var allToken = [];
      var tokenCount = [];

      for (let i = 0; i < data.result.length; i++) {
        if (data.result[i].count > 100) {
          allToken.push(data.result[i].token);
          tokenCount.push(data.result[i].count);
        }
      }

      createLineChartPerson(allToken, tokenCount, tokenID);
    },
    error: function () {
      console.log("Geht nicht... :");
    },
  });
}

function getTokenByParty(party, tokenID) {
  $.ajax({
    url: "http://localhost:4567/token?party=" + party,
    method: "GET",
    dataType: "json",
    success: function (data) {
      var allToken = [];
      var tokenCount = [];

      for (let i = 0; i < data.result.length; i++) {
        if (data.result[i].count > 5000) {
          allToken.push(data.result[i].token);
          tokenCount.push(data.result[i].count);
        }
      }

      createLineChartPerson(allToken, tokenCount, tokenID);
    },
    error: function () {
      console.log("Geht nicht... :");
    },
  });
}

function createLineChartPerson(labelsInput, dataInput, tokenID) {
  var ctx = document.getElementById(tokenID);
  var myBarChart = new Chart(ctx, {
    type: "line",
    data: {
      labels: labelsInput,
      datasets: [
        {
          fill: false,
          data: dataInput,
          borderColor: "#36b9cc",
          pointBorderColor: "#36b9cc",
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

function createLineChart(labelsInput, dataInput) {
  var ctx = document.getElementById("myLineChartAllToken");
  var myBarChart = new Chart(ctx, {
    type: "line",
    data: {
      labels: labelsInput,
      datasets: [
        {
          fill: false,
          data: dataInput,
          borderColor: "#36b9cc",
          pointBorderColor: "#36b9cc",
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
