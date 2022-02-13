// Speichert den Graphen zu Named Entities (Sprecher*in).
let namedEntitiesPerSpeakerChart = undefined;

const btnNamedEntitiesPerSpeaker = document.querySelector("#buttonNamedEntitiesPerSpeaker");

// Click Event Listener für den Suchbutton.
btnNamedEntitiesPerSpeaker.addEventListener("click", function () {
    const input = document.getElementById("inputNamedEntitiesPerSpeaker");
    try {
        const name = input.value.split(", ");
        const firstname = name[0];
        const suffix = name[1].split(" (");
        const lastname = suffix[0];
        const party = suffix[1].substring(0, suffix[1].length-1);
        const speaker =  speakers.filter((speaker)=> speaker.firstname == firstname & speaker.name == lastname & speaker.party == party);
        if (speaker.length != 0) {
            document.getElementById("NamedEntities Speaker Name").innerHTML = "Laden...";
            plotNamedEntitiesPerSpeakerChart(speaker[0].id);
        };
    } catch (e) {
        // pass
    } finally {
        input.value = "";
    };
});

/*
API Abfrage zu Named Entities pro Sprecher*in.
 */
function getNamedEntitiesOfSpeaker(id) {
    return new Promise(async resolve => {
        await $.ajax({
            method: "GET",
            dataType: "json",
            url: "http://api.prg2021.texttechnologylab.org/namedEntities?user=" + id,
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
Plottet den Graphen zu Named Entities (Sprecher*in).
 */
async function plotNamedEntitiesPerSpeakerChart(id) {
    const data = await getNamedEntitiesOfSpeaker(id);
    const divSpeakername = document.getElementById("NamedEntities Speaker Name");
    const speakername = speakers.filter((speaker)=> speaker.id == id)[0];
    if (namedEntitiesPerSpeakerChart == undefined) {
        const ctx = document.getElementById("myAreaChart NamedEntities Speaker");
        namedEntitiesPerSpeakerChart = new Chart(ctx, {
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
        divSpeakername.innerHTML = speakername.firstname + " " + speakername.name;
    } else {
        namedEntitiesPerSpeakerChart.data = {
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
                backgroundColor: [
                    'rgba(255, 99, 132)',
                    'rgba(255, 159, 64)',
                    'rgba(255, 205, 86)',
                ],
                borderColor: "rgb(28,64,173)",
            }],
        };
        namedEntitiesPerSpeakerChart.update();
        divSpeakername.innerHTML = speakername.firstname + " " + speakername.name;
    }
};