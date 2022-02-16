// Speichert den Graphen zu Sentiment (Fraktionen).
let myBarChartSpeakerDistribution = undefined;

let speakerPicture = undefined;

/**
 * API Abfrage zu Sentiment pro Sprecher*in.
 * @param id
 * @returns {Promise<unknown>}
 */
function getSpeakerDistribution() {
  return new Promise((resolve) => {
    $.ajax({
      method: "GET",
      dataType: "json",
      url: "http://localhost:4567/speaker",
      success: function (data) {
        var speakers = data.speakers;
        speakers.sort(function (sp1, sp2) {
          if (sp1.allSpeeches > sp2.allSpeeches) {
            return -1;
          } else {
            return 1;
          }
        });
        const result = speakers.filter((speaker) => speaker.allSpeeches > 50);
        speakerPicture = speakers.map((speaker) => {
          let rObj = {};
          rObj["name"] = speaker.name + " " + speaker.surname;
          rObj["picture"] = speaker.picture;
          return rObj;
        });
        resolve(result);
      },
      error: function (error) {
        console.log(error);
        resolve();
      },
    });
  });
}
plotSpeakerDistribution();

async function plotSpeakerDistribution() {
  const data = await getSpeakerDistribution();
  const nameDiv = document.getElementById("SpeakerDistributionNameField");
  if (myBarChartSpeakerDistribution == undefined) {
    const ctx = document.getElementById("myBarChartSpeakerDistribution");
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
    nameDiv.innerHTML = "All";
  } else {
    // TODO: Wenn Chart neu geladen wird.
  }
}

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
      const tr = document.createElement("tr");
      tr.style.backgroundColor = "inherit";
      tr.style.borderWidth = 0;

      const td = document.createElement("td");
      td.style.borderWidth = 0;

      const img = document.createElement("img");
      img.src = speakerPicture.filter(
        (speaker) => speaker["name"] == tooltip.title
      )[0]["picture"];

      td.appendChild(img);
      tr.appendChild(td);
      tableBody.appendChild(tr);
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
