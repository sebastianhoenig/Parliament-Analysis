// Speichert den Graphen zu Token (Fraktionen).
let tokenPerFractionChart = undefined;

const btnTokenPerFraction = document.querySelector("#buttonTokenPerFraction");

// Click Event Listener für den Suchbutton.
btnTokenPerFraction.addEventListener("click", function () {
    const input = document.getElementById("inputTokenPerFraction");
    const name = input.value;
    const fraction =  fractions.filter((fraction)=> fraction == name);
    if (name == fraction[0]){
        document.getElementById("Token Fraction Name").innerHTML = "Laden...";
        plotTokenPerFractionChart(fraction[0]);
    };
    input.value = "";
});

/*
API Abfrage zu Token pro Fraktion.
 */
function getTokenOfFraction(fraction) {
    return new Promise(async resolve => {
        await $.ajax({
            method: "GET",
            dataType: "json",
            url: "http://api.prg2021.texttechnologylab.org/tokens?minimum=1500&fraction=" + fraction,
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
Plottet den Graphen zu Token (Fraktion).
 */
async function plotTokenPerFractionChart(fraction) {
    const data = await getTokenOfFraction(fraction);
    const divFractionname = document.getElementById("Token Fraction Name");
    if (tokenPerFractionChart == undefined) {
        const ctx = document.getElementById("myAreaChart Token Fraction");
        tokenPerFractionChart = new Chart(ctx, {
            type: 'bar',
            data: {
                labels: data.map((token)=> token.token),
                datasets: [{
                    data: data.map((token)=> token.count),
                    backgroundColor: "rgba(78,115,223)",
                    hoverBackgroundColor: "rgb(28,64,173)",
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
        tokenPerFractionChart.data = {
            labels: data.map((token)=> token.token),
            datasets: [{
                data: data.map((token)=> token.count),
                backgroundColor: "rgba(78,115,223)",
                hoverBackgroundColor: "rgb(28,64,173)",
            }],
        };
        tokenPerFractionChart.update();
        divFractionname.innerHTML = fraction;
    }
};