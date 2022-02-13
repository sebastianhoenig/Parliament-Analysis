let statisticsOfComments = {};
let statisticsOfSpeaker = {};
let statisticsOfSpeeches = {};

let namedEntitiesAvg = {};

let speakerIDs = [];
let speakers = [];

let fractions = [];


// API Abfrage und speichern der Daten
async function getFractions() {
    const data = await $.ajax({
        method: "GET",
        dataType: "json",
        url: "http://api.prg2021.texttechnologylab.org/fractions",
        error: function (error) {
            console.log(error);
        }
    });
    fractions = (data.result).map((fraction)=> fraction.id);
}


// API Abfrage der Statistics und Speicherung separiert.
function getStatistics() {
    return new Promise(resolve => {
        $.ajax({
            method: "GET",
            dataType: "json",
            url: "http://api.prg2021.texttechnologylab.org/statistic",
            success: function (data) {
                statisticsOfComments = data.result.comments;
                statisticsOfSpeaker = data.result.speakers;
                statisticsOfSpeeches = data.result.speeches;
                statisticsOfSpeeches.sort(function selfSort(a, b) {
                    return a.length < b.length ? 1 : -1;
                });
                resolve();
            },
            error: function (error) {
                console.log(error);
                resolve();
            }
        });
    })
}

// API Abfrage aller Sprecher Daten. Gespeichert und gefiltert nach undefined.
async function getSpeaker() {
    const data = await $.ajax({
        method: "GET",
        dataType: "json",
        url: "http://api.prg2021.texttechnologylab.org/speakers",
        error: function (error) {
            console.log(error);
        }
    });
    speakers = data.result.filter((speaker)=> speaker.party != undefined & speaker.id != undefined);
}

// Liefert den Namen eines Sprechers zurück.
async function getSpeakerName(id) {
    const sp = speakers.filter((speaker)=> speaker.id == id)[0];
    return sp.firstname + " " + sp.name
}

// Liefert eine Sprecher-ID von einer Rede.
function getSpeakerIDOfSpeech(id) {
    return new Promise(resolve => {
        $.ajax({
            method: "GET",
            dataType: "json",
            url: "http://api.prg2021.texttechnologylab.org/speech?id=" + id,
            success: function (data) {
                resolve(data.speaker);
            },
            error: function (error) {
                console.log(error);
                resolve();
            }
        });
    })
}

// Speichert alle Redner*innen IDs.
function getSpeakerIDs() {
    speakerIDs = speakers.map((speaker)=> speaker.id);
}

// Füllt die DatenListen für das Auswahl Dropdown in den Suchfeldern.
function fillDataList() {
    const dataListSpeaker = document.getElementById("SpeakerName");
    speakers.forEach(function(speaker) {
        const optionDataListSpeaker = document.createElement("option");
        optionDataListSpeaker.value = speaker.firstname + ", " + speaker.name + " (" + speaker.party + ")";
        dataListSpeaker.appendChild(optionDataListSpeaker);
    });

    const dataListFraction = document.getElementById("FractionList");
    fractions.forEach(function(fraction) {
        const optionDataListFraction = document.createElement("option");
        optionDataListFraction.value = fraction;
        dataListFraction.appendChild(optionDataListFraction);
    });
};

// Berechnet den Durchschnitt alle Named Entities.
async function getAvgNamedEntities() {
    const resolveData = {persons: 0, organisations: 0, locations: 0};

    for (let i = 0; i < fractions.length; i++) {
        await $.ajax({
            method: "GET",
            dataType: "json",
            url: "http://api.prg2021.texttechnologylab.org/namedEntities?fraction",
            success: function (data) {
                const dataResult = data.result;
                const tmpDataPersons = dataResult[0].persons.map((elements) => elements.count);
                const tmpDataOrganisations = dataResult[1].organisations.map((elements) => elements.count);
                const tmpDataLocations = dataResult[2].locations.map((elements) => elements.count);

                for (let i = 0; i < tmpDataPersons.length; i++) {
                    resolveData.persons += tmpDataPersons[i];
                };

                for (let i = 0; i < tmpDataOrganisations.length; i++) {
                    resolveData.organisations += tmpDataOrganisations[i];
                };

                for (let i = 0; i < tmpDataLocations.length; i++) {
                    resolveData.locations += tmpDataLocations[i];
                };
            },
            error: function (error) {
                console.log(error);
            }});
    };
    const total = resolveData.persons + resolveData.organisations + resolveData.locations;
    resolveData.persons = (resolveData.persons / total) * 100;
    resolveData.organisations = (resolveData.organisations / total) * 100;
    resolveData.locations = (resolveData.locations / total) * 100;
    namedEntitiesAvg = resolveData;
};

// Speichert die Daten die Mehrfach verwendet werden.
async function saveData() {
    await getStatistics();
    await getSpeaker();
    await getSpeakerIDs();
    await getFractions();
    await fillDataList();
};

// Schließt die Sidebar zu begin.
function toggelSidebar() {
    $("body").toggleClass("sidebar-toggled");
    $(".sidebar").toggleClass("toggled");
    if ($(".sidebar").hasClass("toggled")) {
        $('.sidebar .collapse').collapse('hide');
    };
}

// Initialisiert die Aufgabe 3b1
function threeBOne(){
    plotSentimentPerSpeakerChart(11001478);
    plotSentimentPerFractionChart("DIE LINKE");
    plotSentimentAllChart();
}

// Initialisiert die Aufgabe 3b2
function threeBTwo() {
    plotPOSAllChart();
    plotPOSPerFractionChart("DIE LINKE");
}

// Initialisiert die Aufgabe 3b3
function threeBThree() {
    plotTokenAllChart()
    //plotTokenPerSpeakerChart(11001478)
    plotTokenPerFractionChart("DIE LINKE")
}

// Initialisiert die Aufgabe 3b4
function threeBFour() {
    plotNamedEntitiesPerSpeakerChart(11001478);
    plotNamedEntitiesPerFractionChart("DIE LINKE")
}

// Startete die Initialisirung für die Seite. Und lässt alles nacheinander ausführen.
async function main() {
    await toggelSidebar();
    await saveData();
    await getSingleResults();

    await threeBOne();
    await getAvgNamedEntities();
    await threeBTwo();
    await threeBThree();
    await threeBFour();
}
// Führt main aus.
main();
