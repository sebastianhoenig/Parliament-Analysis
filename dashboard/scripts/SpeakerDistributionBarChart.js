let myBarChartSpeakerDistribution = undefined;

let speakerPicture = undefined;

/**
 * This is to map the picture array just ones.
 */
$.ajax({
  method: "GET",
  dataType: "json",
  url: "http://localhost:4567/speaker",
  success: function (data) {
    var speakers = data.speakers;
    speakerPicture = speakers.map((speaker) => {
      let rObj = {};
      rObj["name"] = speaker.name + " " + speaker.surname;
      rObj["picture"] = speaker.picture;
      return rObj;
    });
  },
  error: function (error) {
    console.log(error);
  },
});

/**
 * API requests for speakerDistribution for all speaker.
 * @param id
 * @returns {Promise<unknown>}
 */
function getSpeakerDistributionAll() {
  return new Promise((resolve) => {
    $.ajax({
      method: "GET",
      dataType: "json",
      url: "http://localhost:4567/speaker",
      success: function (data) {
        const speakers = data.speakers;
        speakers.sort(function (sp1, sp2) {
          if (sp1.allSpeeches > sp2.allSpeeches) {
            return -1;
          } else {
            return 1;
          }
        });
        const result = speakers.filter((speaker) => speaker.allSpeeches > 50);
        console.log(result);
        resolve(result);
      },
      error: function (error) {
        console.log(error);
        resolve();
      },
    });
  });
}

/**
 * API requests for speakerDistribution per speaker.
 * @param id
 * @returns {Promise<unknown>}
 */
function getSpeakerDistributionSpeaker(id, startDate, endDate) {
  return new Promise((resolve) => {
    if (id == "all") {
      id = "11001478";
    }
    $.ajax({
      method: "GET",
      dataType: "json",
      url: "http://localhost:4567/speaker?id=" + id + "&beginDate=" + startDate + "&endDate=" + endDate,
      success: function (data) {
        // let speaker = data;
        // speaker.allSpeeches = speaker.allSpeeches.length;
        resolve([data]);
      },
      error: function (error) {
        console.log(error);
        resolve();
      },
    });
  });
}

/**
 * API requests for speakerDistribution per party.
 * @param id
 * @returns {Promise<unknown>}
 */
function getSpeakerDistributionParty(id) {
  return new Promise((resolve) => {
    $.ajax({
      method: "GET",
      dataType: "json",
      url: "http://localhost:4567/speaker",
      success: function (data) {
        const speakers = data.speakers.filter((speaker) => speaker.party == id);
        speakers.sort(function (sp1, sp2) {
          if (sp1.allSpeeches > sp2.allSpeeches) {
            return -1;
          } else {
            return 1;
          }
        });
        const result = speakers.filter((speaker) => speaker.allSpeeches > 10);
        resolve(result);
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
async function getSpeakerDistributionData(type, id, startDate, endDate) {
  switch (type) {
    case 0:
      return await getSpeakerDistributionAll();
      break;
    case 1:
      return await getSpeakerDistributionSpeaker(id, startDate, endDate);
      break;
    case 2:
      return await getSpeakerDistributionParty(id);
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
async function plotSpeakerDistribution(
  id = "all",
  canvasID = "myBarChartSpeakerDistribution",
  type = 0,
  startDate = "2017-10-20",
  endDate = "2022-02-11"
) {
  const data = await getSpeakerDistributionData(type, id, startDate, endDate);
  const ctx = document.getElementById(canvasID);
  myBarChartSpeakerDistribution = new Chart(ctx, {
    type: "bar",
    plot: {
      tooltip: {},
    },
    data: {
      labels: data.map((speaker) => speaker.name + " " + speaker.surname),
      datasets: [
        {
          axis: "x",
          barThickness: 2,
          data: data.map((speaker) => speaker.allSpeeches),
          backgroundColor: "#dc0101",
        },
      ],
    },
    options: {
      legend: {
        display: false,
      },
      scales: {
        xAxes: {
          display: false,
        },
      },
      plugins: {
        legend: false,
        tooltip: {
          enabled: false,
          position: "nearest",
          external: externalTooltipHandler,
        },
      },
    },
  });
}

/**
 * The methode is to create the tooltip for the Chart.
 * @param chart
 * @returns {HTMLDivElement}
 */
const getOrCreateTooltip = (chart) => {
  let tooltipEl = chart.canvas.parentNode.querySelector("div");

  if (!tooltipEl) {
    tooltipEl = document.createElement("div");
    tooltipEl.style.background = "rgba(0, 0, 0, 0.7)";
    tooltipEl.style.borderRadius = "3px";
    tooltipEl.style.color = "white";
    tooltipEl.style.opacity = 1;
    tooltipEl.style.pointerEvents = "none";
    tooltipEl.style.position = "absolute";
    tooltipEl.style.transform = "translate(0%, 0)";
    tooltipEl.style.transition = "all .1s ease";
    tooltipEl.style.zIndex = 99;

    const table = document.createElement("table");
    table.style.margin = "0px";

    tooltipEl.appendChild(table);
    chart.canvas.parentNode.appendChild(tooltipEl);
  }

  return tooltipEl;
};

/**
 * This param is creating the html stuff for the tooltip.
 * @param context
 */
const externalTooltipHandler = (context) => {
  // Tooltip Element
  const { chart, tooltip } = context;
  const tooltipEl = getOrCreateTooltip(chart);

  // Hide if no tooltip
  if (tooltip.opacity === 0) {
    tooltipEl.style.opacity = 0;
    return;
  }

  // Set Text
  if (tooltip.body) {
    const titleLines = tooltip.title || [];
    const bodyLines = tooltip.body.map((b) => b.lines);

    const tableHead = document.createElement("thead");

    titleLines.forEach((title) => {
      const tr = document.createElement("tr");
      tr.style.borderWidth = 0;

      const th = document.createElement("th");
      th.style.borderWidth = 0;
      const text = document.createTextNode(title);

      th.appendChild(text);
      tr.appendChild(th);
      tableHead.appendChild(tr);
    });

    const tableBody = document.createElement("tbody");
    bodyLines.forEach((body, i) => {
      const tr2 = document.createElement("tr");
      tr2.style.backgroundColor = "inherit";
      tr2.style.borderWidth = 0;

      const td2 = document.createElement("td");
      td2.style.borderWidth = 0;

      const tr1 = document.createElement("tr");
      tr1.style.backgroundColor = "inherit";
      tr1.style.borderWidth = 0;

      const td1 = document.createElement("td");
      td1.style.borderWidth = 0;

      const text = document.createTextNode("Total Speeches: " + body);

      const img = document.createElement("img");
      img.src = speakerPicture.filter(
        (speaker) => speaker["name"] == tooltip.title
      )[0]["picture"];

      td1.appendChild(text);
      tr1.appendChild(td1);
      td2.appendChild(img);
      tr2.appendChild(td2);
      tableBody.appendChild(tr1);
      tableBody.appendChild(tr2);
    });

    const tableRoot = tooltipEl.querySelector("table");

    // Remove old children
    while (tableRoot.firstChild) {
      tableRoot.firstChild.remove();
    }

    // Add new children
    tableRoot.appendChild(tableHead);
    tableRoot.appendChild(tableBody);
  }

  const { offsetLeft: positionX, offsetTop: positionY } = chart.canvas;

  // Display, position, and set styles for font
  tooltipEl.style.opacity = 1;
  tooltipEl.style.left = positionX + tooltip.caretX + "px";
  tooltipEl.style.top = positionY + tooltip.caretY + "px";
  tooltipEl.style.font = tooltip.options.bodyFont.string;
  tooltipEl.style.padding =
    tooltip.options.padding + "px " + tooltip.options.padding + "px";
};
