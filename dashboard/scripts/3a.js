/*
Implementiert alle Einzelergebnisse wird aus Main aufgerufen.
 */
function getSingleResults() {
    // a.1
    const speakerWithMostSpeeches = statisticsOfSpeaker[0];
    const speakerContainer = document.getElementById("Speaker");
    getSpeakerName(speakerWithMostSpeeches.id).then((name) => speakerContainer.innerHTML = name + " (" + speakerWithMostSpeeches.count + ")");

    // a.2
    const longestSpeech = statisticsOfSpeeches[0];
    const longestSpeechContainer = document.getElementById("longestSpeech");
    getSpeakerIDOfSpeech(longestSpeech.id).then((id) => getSpeakerName(id).then((name) =>
        longestSpeechContainer.innerHTML = name + " (" + longestSpeech.length + ")"));

    // a.3
    const totalLength = statisticsOfSpeeches.map((speeches)=> speeches.length)
    let sum = 0;
    const counter = totalLength.length;
    for (let i = 0; i < counter; i++) {
        sum += totalLength[i];
    };
    const avg = Math.round(sum / counter * 100) / 100;

    const speakerLengthContainer = document.getElementById("Speech length");
    speakerLengthContainer.innerHTML = avg;

    // a.4
    const speechContainer = document.getElementById("Speeches");
    speechContainer.innerHTML = statisticsOfSpeeches.length;

    // a.5
    const commentContainer = document.getElementById("Comments");
    commentContainer.innerHTML = statisticsOfComments.length;
}