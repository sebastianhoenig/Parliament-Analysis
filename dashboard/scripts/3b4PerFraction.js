// Speichert den Graphen zu Named Entities (Fraktionen).
let namedEntitiesPerFractionChart = undefined;

const btnNamedEntitiesPerFraction = document.querySelector("#buttonNamedEntitiesPerFraction");

// Click Event Listener für den Suchbutton.
btnNamedEntitiesPerFraction.addEventListener("click", function () {
    const input = document.getElementById("inputNamedEntitiesPerFraction");
    const name = input.value;
    const fraction =  fractions.filter((fraction)=> fraction == name);
    if (name == fraction[0]){
        document.getElementById("NamedEntities Fraction Name").innerHTML = "Laden...";
        plotNamedEntitiesPerFractionChart(fraction[0]);
    };
    input.value = "";
});

/*
API Abfrage zu Named Entities pro Fraktion.
 */
function getNamedEntitiesOfFraction(fraction) {
    return new Promise(async resolve => {
        await $.ajax({
            method: "GET",
            dataType: "json",
            url: "http://api.prg2021.texttechnologylab.org/namedEntities?fraction=" + fraction,
            success: function (data) {
                const dataResult = data.result;
                const resolveData = {persons: 0, organisations: 0, locations: 0};
                const tmpDataPersons = dataResult[0].persons.map((elements)=> elements.count);
                const tmpDataOrganisations = dataResult[1].organisations.map((elements)=> elements.count);
                const tmpDataLocations = dataResult[2].locations.map((elements)=> elements.count);

                for (let i = 0; i < tmpDataPersons.length; i++) {
                    resolveData.persons += tmpDataPersons[i];
                };

                for (let i = 0; i < tmpDataOrganisations.length; i++) {
                    resolveData.organisations += tmpDataOrganisations[i];
                };

                for (let i = 0; i < tmpDataLocations.length; i++) {
                    resolveData.locations += tmpDataLocations[i];
                };
                const total = resolveData.persons + resolveData.organisations + resolveData.locations;
                resolveData.persons = (resolveData.persons / total) * 100;
                resolveData.organisations = (resolveData.organisations / total) * 100;
                resolveData.locations = (resolveData.locations / total) * 100;

                resolve(resolveData);
            },
            error: function (error) {
                console.log(error);
                resolve();
            }
        });
    });
};

/*
Plottet den Graphen zu Named Entities (Fraktion).
 */
async function plotNamedEntitiesPerFractionChart(fraction) {
    const data = await getNamedEntitiesOfFraction(fraction);
    const divFractionname = document.getElementById("NamedEntities Fraction Name");
    if (namedEntitiesPerFractionChart == undefined) {
        const ctx = document.getElementById("myAreaChart NamedEntities Fraction");
        namedEntitiesPerFractionChart = new Chart(ctx, {
            type: 'bar',
            data: {
                labels: ["Persons", "Organisations", "Locations"].reverse(),
                datasets: [
                    {
                        stack: "combined",
                        type: "line",
                        data: [namedEntitiesAvg.persons, namedEntitiesAvg.organisations, namedEntitiesAvg.locations].reverse(),
                        fill: false,
                        borderColor: "rgb(78,115,223)",
                    },
                    {
                    type: "bar",
                    data: [data.persons, data.organisations, data.locations].reverse(),
                    backgroundColor: [
                        'rgba(255, 99, 132)',
                        'rgba(255, 159, 64)',
                        'rgba(255, 205, 86)',
                    ],
                    borderColor: "rgb(28,64,173)",
                    }],
            },
            options: {
                legend: {
                    display: false
                },
                scales: {
                    yAxes: [{
                        ticks: {
                            min: 0,
                            max: 100,
                            callback: function(value){return value+ "%"}
                        },
                        scaleLabel: {
                            display: true,
                            labelString: "Percentage"
                        }
                    }]
                }
            },
        });
        divFractionname.innerHTML = fraction;
    } else {
        namedEntitiesPerFractionChart.data = {
            labels: ["Persons", "Organisations", "Locations"].reverse(),
            datasets: [
                {
                    stack: "combined",
                    type: "line",
                    data: [namedEntitiesAvg.persons, namedEntitiesAvg.organisations, namedEntitiesAvg.locations].reverse(),
                    fill: false,
                    borderColor: "rgb(78,115,223)",
                },
                {
                data: [data.persons, data.organisations, data.locations].reverse(),
                type: "bar",
                backgroundColor: [
                    'rgba(255, 99, 132)',
                    'rgba(255, 159, 64)',
                    'rgba(255, 205, 86)',
                ],
                borderColor: "rgb(28,64,173)",
                }],
        };
        namedEntitiesPerFractionChart.update();
        divFractionname.innerHTML = fraction;
    }
};