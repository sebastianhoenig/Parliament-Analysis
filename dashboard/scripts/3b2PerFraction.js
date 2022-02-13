// Speichert den Graphen zu POS (Fraktionen).
let posPerFractionChart = undefined;

const btnPOSPerFraction = document.querySelector("#buttonPOSPerFraction");

// Click Event Listener für den Suchbutton.
btnPOSPerFraction.addEventListener("click", function () {
    const input = document.getElementById("inputPOSPerFraction");
    const name = input.value;
    const fraction =  fractions.filter((fraction)=> fraction == name);
    if (name == fraction[0]){
        document.getElementById("POS Fraction Name").innerHTML = "Laden...";
        plotPOSPerFractionChart(fraction[0]);
    };
    input.value = "";
});

/*
API Abfrage zu POS pro Fraktion.
 */
function getPOSOfFraction(fraction) {
    return new Promise(async resolve => {
        await $.ajax({
            method: "GET",
            dataType: "json",
            url: "http://api.prg2021.texttechnologylab.org/pos?minimum=1000&fraction=" + fraction,
            success: function (data) {
                resolve(data.result);
            },
            error: function (error) {
                console.log(error);
                resolve();
            }
        });
    });
};

/*
Plottet den Graphen zu POS (Fraktion).
 */
async function plotPOSPerFractionChart(fraction) {
    const data = await getPOSOfFraction(fraction);
    const divFractionname = document.getElementById("POS Fraction Name");
    if (posPerFractionChart == undefined) {
        const ctx = document.getElementById("myAreaChart POS Fraction");
        posPerFractionChart = new Chart(ctx, {
            type: 'bar',
            data: {
                labels: data.map((pos)=> pos.pos),
                datasets: [{
                    data: data.map((pos)=> pos.count),
                    backgroundColor: "rgba(231,74,59)",
                    hoverBackgroundColor: "rgb(108,46,39)",
                }],
            },
            options: {
                legend: {
                    display: false
                },
            },
        });
        divFractionname.innerHTML = fraction;
    } else {
        posPerFractionChart.data = {
            labels: data.map((pos)=> pos.pos),
            datasets: [{
                data: data.map((pos)=> pos.count),
                backgroundColor: "rgba(231,74,59)",
                hoverBackgroundColor: "rgb(108,46,39)",
            }],
        };
        posPerFractionChart.update();
        divFractionname.innerHTML = fraction;
    }
};