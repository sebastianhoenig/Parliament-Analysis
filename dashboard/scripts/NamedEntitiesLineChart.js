function getAllNamedEntities() {
  $.ajax({
    url: "http://localhost:4567/namedEntities?entities=persons",
    method: "GET",
    dataType: "json",
    success: function (data) {
      const dataPersons = [];
      const labelPersons = [];
      for (let i = 0; i < 40; i++) {
        dataPersons.push(data.result[i].count);
        labelPersons.push(data.result[i].persons);
      }

      $.ajax({
        url: "http://localhost:4567/namedEntities?entities=locations",
        method: "GET",
        dataType: "json",
        success: function (data) {
          const dataLocations = [];
          const labelLocations = [];
          for (let j = 0; j < 40; j++) {
            dataLocations.push(data.result[j].count);
            labelLocations.push(data.result[j].locations);
          }

          $.ajax({
            url: "http://localhost:4567/namedEntities?entities=organisations",
            method: "GET",
            dataType: "json",
            success: function (data) {
              const dataOrganisations = [];
              const labelOrganisations = [];
              for (let k = 0; k < 40; k++) {
                dataOrganisations.push(data.result[k].count);
                labelOrganisations.push(data.result[k].organisations);
              }

              createMultipleLineChart(
                dataPersons,
                dataLocations,
                dataOrganisations,
                labelPersons,
                labelLocations,
                labelOrganisations
              );
            },
            error: function () {
              console.log("Geht nicht... :");
            },
          });
        },
        error: function () {
          console.log("Geht nicht... :");
        },
      });
    },
    error: function () {
      console.log("Geht nicht... :");
    },
  });
}

function getAllNamedEntitiesPerson(id, neID) {
  $.ajax({
    url:
      "http://localhost:4567/namedEntities?speakerID=" +
      id +
      "&entities=persons",
    method: "GET",
    dataType: "json",
    success: function (data) {
      const dataPersons = [];
      const labelPersons = [];
      for (let i = 0; i < 40; i++) {
        dataPersons.push(data.result[i].count);
        labelPersons.push(data.result[i].persons);
      }

      $.ajax({
        url:
          "http://localhost:4567/namedEntities?speakerID=" +
          id +
          "&entities=locations",
        method: "GET",
        dataType: "json",
        success: function (data) {
          const dataLocations = [];
          const labelLocations = [];
          for (let j = 0; j < 40; j++) {
            dataLocations.push(data.result[j].count);
            labelLocations.push(data.result[j].locations);
          }

          $.ajax({
            url:
              "http://localhost:4567/namedEntities?speakerID=" +
              id +
              "&entities=organisations",
            method: "GET",
            dataType: "json",
            success: function (data) {
              const dataOrganisations = [];
              const labelOrganisations = [];
              for (let k = 0; k < 40; k++) {
                dataOrganisations.push(data.result[k].count);
                labelOrganisations.push(data.result[k].organisations);
              }

              createMultipleLineChartPerson(
                dataPersons,
                dataLocations,
                dataOrganisations,
                labelPersons,
                labelLocations,
                labelOrganisations,
                neID
              );
            },
            error: function () {
              console.log("Geht nicht... :");
            },
          });
        },
        error: function () {
          console.log("Geht nicht... :");
        },
      });
    },
    error: function () {
      console.log("Geht nicht... :");
    },
  });
}

function getAllNamedEntitiesParty(id, neID) {
  $.ajax({
    url:
      "http://localhost:4567/namedEntities?party=" + id + "&entities=persons",
    method: "GET",
    dataType: "json",
    success: function (data) {
      const dataPersons = [];
      const labelPersons = [];
      for (let i = 0; i < 40; i++) {
        dataPersons.push(data.result[i].count);
        labelPersons.push(data.result[i].persons);
      }

      $.ajax({
        url:
          "http://localhost:4567/namedEntities?party=" +
          id +
          "&entities=locations",
        method: "GET",
        dataType: "json",
        success: function (data) {
          const dataLocations = [];
          const labelLocations = [];
          for (let j = 0; j < 40; j++) {
            dataLocations.push(data.result[j].count);
            labelLocations.push(data.result[j].locations);
          }

          $.ajax({
            url:
              "http://localhost:4567/namedEntities?party=" +
              id +
              "&entities=organisations",
            method: "GET",
            dataType: "json",
            success: function (data) {
              const dataOrganisations = [];
              const labelOrganisations = [];
              for (let k = 0; k < 40; k++) {
                dataOrganisations.push(data.result[k].count);
                labelOrganisations.push(data.result[k].organisations);
              }

              createMultipleLineChartPerson(
                dataPersons,
                dataLocations,
                dataOrganisations,
                labelPersons,
                labelLocations,
                labelOrganisations,
                neID
              );
            },
            error: function () {
              console.log("Geht nicht... :");
            },
          });
        },
        error: function () {
          console.log("Geht nicht... :");
        },
      });
    },
    error: function () {
      console.log("Geht nicht... :");
    },
  });
}

function createMultipleLineChartPerson(
  dataPersons,
  dataLocations,
  dataOrganisations,
  labelPersons,
  labelLocations,
  labelOrganisations,
  neID
) {
  var ctx = document.getElementById(neID);
  var myLineChartNamedEntitites = new Chart(ctx, {
    type: "line",
    plot: {
      tooltip: {},
    },
    data: {
      labels: [
        0, 1, 2, 3, 4, 5, 6, 7, 8, 9, 10, 11, 12, 13, 14, 15, 16, 17, 18, 19,
        20, 21, 22, 23, 24, 25, 26, 27, 28, 29, 30, 31, 32, 33, 34, 35, 36, 37,
        38, 39, 40,
      ],
      datasets: [
        {
          label: "persons",
          labels: labelPersons,
          data: dataPersons,
          backgroundColor: "#36b9cc",
          borderColor: "#36b9cc",
          pointBorderColor: "#36b9cc",
          fill: false,
        },
        {
          label: "locations",
          labels: labelLocations,
          data: dataLocations,
          backgroundColor: "#f6c23e",
          borderColor: "#f6c23e",
          pointBorderColor: "#f6c23e",
          fill: false,
        },
        {
          label: "organisation",
          labels: labelOrganisations,
          data: dataOrganisations,
          backgroundColor: "#e74a3b",
          borderColor: "#e74a3b",
          pointBorderColor: "#e74a3b",
          fill: false,
        },
      ],
    },
    options: {
      plugins: {
        tooltip: {
          callbacks: {
            label: function (tooltipItem) {
              console.log(tooltipItem);
              return tooltipItem.dataset.labels[tooltipItem.parsed.x];
            },
          },
        },
      },
    },
  });
}

function createMultipleLineChart(
  dataPersons,
  dataLocations,
  dataOrganisations,
  labelPersons,
  labelLocations,
  labelOrganisations
) {
  var ctx = document.getElementById("myLineChartNamedEntities");
  var myLineChartNamedEntitites = new Chart(ctx, {
    type: "line",
    plot: {
      tooltip: {},
    },
    data: {
      labels: [
        0, 1, 2, 3, 4, 5, 6, 7, 8, 9, 10, 11, 12, 13, 14, 15, 16, 17, 18, 19,
        20, 21, 22, 23, 24, 25, 26, 27, 28, 29, 30, 31, 32, 33, 34, 35, 36, 37,
        38, 39, 40,
      ],
      datasets: [
        {
          label: "persons",
          labels: labelPersons,
          data: dataPersons,
          backgroundColor: "#36b9cc",
          borderColor: "#36b9cc",
          pointBorderColor: "#36b9cc",
          fill: false,
        },
        {
          label: "locations",
          labels: labelLocations,
          data: dataLocations,
          backgroundColor: "#f6c23e",
          borderColor: "#f6c23e",
          pointBorderColor: "#f6c23e",
          fill: false,
        },
        {
          label: "organisation",
          labels: labelOrganisations,
          data: dataOrganisations,
          backgroundColor: "#e74a3b",
          borderColor: "#e74a3b",
          pointBorderColor: "#e74a3b",
          fill: false,
        },
      ],
    },
    options: {
      plugins: {
        tooltip: {
          callbacks: {
            label: function (tooltipItem) {
              console.log(tooltipItem);
              return tooltipItem.dataset.labels[tooltipItem.parsed.x];
            },
          },
        },
      },
    },
  });
}
