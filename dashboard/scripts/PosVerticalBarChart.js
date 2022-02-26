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

function getPosBySpeaker(speakerID, posID, startDate, endDate) {

  $.ajax({
    url: "http://localhost:4567/pos?speakerID=" + speakerID + "&beginDate=" + startDate + "&endDate=" + endDate,
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

function getPosByParty(party, posID, startDate, endDate) {
  $.ajax({
    url: "http://localhost:4567/pos?party=" + party + "&beginDate=" + startDate + "&endDate=" + endDate,
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
