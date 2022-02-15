

function test1(){
    $.ajax({
        url: "http://localhost:4567/namedEntities?entities=persons",
        method: 'GET',
        dataType: 'json',
        success: function (data) {
            const dataPersons = [];
            const labelPersons = [];
            for(let i = 0; i < 40; i++){
                dataPersons.push(data.result[i].count);
                console.log(data.result[i].count);
                labelPersons.push(data.result[i].persons);
                console.log(data.result[i].persons);
            }
            console.log("test:"+dataPersons);
            return console.log("test1 done");
        },
        error: function () {
            console.log("Geht nicht... :")
        }
    });
}

function test2(){
    $.ajax({
        url: "http://localhost:4567/namedEntities?entities=locations",
        method: 'GET',
        dataType: 'json',
        success: function (data) {
            const dataLocations = [];
            const labelLocations = [];
            for(let j = 0; j < 40; j++){
                dataLocations.push(data.result[j].count);
                labelLocations.push(data.result[j].locations);
            }
            console.log("test2:"+dataLocations);
            return dataLocations;
        },
        error: function () {
            console.log("Geht nicht... :")
        }
    });
}

function test5(){
    $.ajax({
        url: "http://localhost:4567/namedEntities?entities=organisations",
        method: 'GET',
        dataType: 'json',
        success: function (data) {
            const dataOrganisations = [];
            const labelOrganisations = [];
            for(let k = 0; k < 40; k++){
                dataOrganisations.push(data.result[k].count);
                labelOrganisations.push(data.result[k].organisations)
            }
            console.log("test3:"+dataOrganisations);
            return dataOrganisations;
        },
        error: function () {
            console.log("Geht nicht... :")
        }
    });
}

getAllNamedEntities();
function getAllNamedEntities(){
    $.ajax({
        url: "http://localhost:4567/namedEntities?entities=persons",
        method: 'GET',
        dataType: 'json',
        success: function (data) {
            const dataPersons = [];
            const labelPersons = [];
            for(let i = 0; i < 40; i++){
                dataPersons.push(data.result[i].count);
                labelPersons.push(data.result[i].persons);
            }

            $.ajax({
                url: "http://localhost:4567/namedEntities?entities=locations",
                method: 'GET',
                dataType: 'json',
                success: function (data) {
                    const dataLocations = [];
                    const labelLocations = [];
                    for(let j = 0; j < 40; j++){
                        dataLocations.push(data.result[j].count);
                        labelLocations.push(data.result[j].locations);
                    }

                    $.ajax({
                        url: "http://localhost:4567/namedEntities?entities=organisations",
                        method: 'GET',
                        dataType: 'json',
                        success: function (data) {
                            const dataOrganisations = [];
                            const labelOrganisations = [];
                            for(let k = 0; k < 40; k++){
                                dataOrganisations.push(data.result[k].count);
                                labelOrganisations.push(data.result[k].organisations)
                            }

                            createMultipleLineChart(dataPersons, dataLocations, dataOrganisations,
                                labelPersons, labelLocations, labelOrganisations);
                        },
                        error: function () {
                            console.log("Geht nicht... :")
                        }
                    });
                },
                error: function () {
                    console.log("Geht nicht... :")
                }
            });

        },
        error: function () {
            console.log("Geht nicht... :")
        }
    });
}


function getAllNamedEntities2(){
    $.ajax({
        url: "http://localhost:4567/namedEntities?entities=persons",
        method: 'GET',
        dataType: 'json',
        success: function (data) {
            const dataPersons = [];
            for(let i = 0; i < 40; i++){
                const newDict = {};
                newDict["y"]=data.result[i].count;
                newDict["plabel"]=data.result[i].persons;
                dataPersons.push(newDict);
            }

            $.ajax({
                url: "http://localhost:4567/namedEntities?entities=locations",
                method: 'GET',
                dataType: 'json',
                success: function (data) {
                    const dataLocations = [];
                    for(let j = 0; j < 40; j++){
                        const newDict = {};
                        newDict["y"]=data.result[i].count;
                        newDict["plabel"]=data.result[i].persons;
                        dataLocations.push(newDict);
                    }

                    $.ajax({
                        url: "http://localhost:4567/namedEntities?entities=organisations",
                        method: 'GET',
                        dataType: 'json',
                        success: function (data) {
                            const dataOrganisations = [];
                            for(let k = 0; k < 40; k++){
                                const newDict = {};
                                newDict["y"]=data.result[i].count;
                                newDict["plabel"]=data.result[i].persons;
                                dataOrganisations.push(newDict);
                            }

                            createMultipleLineChart(dataPersons,dataLocations,dataOrganisations);
                        },
                        error: function () {
                            console.log("Geht nicht... :")
                        }
                    });
                },
                error: function () {
                    console.log("Geht nicht... :")
                }
            });

        },
        error: function () {
            console.log("Geht nicht... :")
        }
    });
}


function createMultipleLineChart(dataPersons, dataLocations, dataOrganisations,
                                 labelPersons, labelLocations, labelOrganisations){
    var ctx = document.getElementById("myLineChartNamedEntities");
    var myLineChartNamedEntitites = new Chart(ctx,
        {
            type: 'line',
            plot: {
                tooltip: {

                }
            },
            data: {
                labels: [0, 1, 2, 3, 4, 5, 6, 7, 8, 9, 10, 11, 12, 13, 14, 15, 16, 17, 18, 19, 20, 21, 22, 23, 24, 25, 26, 27, 28, 29, 30, 31, 32, 33, 34, 35,
                    36, 37, 38, 39, 40],
                datasets: [{
                    label: "persons",
                    labels: labelPersons,
                    data: dataPersons,
                    backgroundColor: "#36b9cc",
                    borderColor: "#36b9cc",
                    pointBorderColor: "#36b9cc",
                    fill: false,
                }, {
                    label: "locations",
                    labels: labelLocations,
                    data: dataLocations,
                    backgroundColor: "#f6c23e",
                    borderColor: "#f6c23e",
                    pointBorderColor: "#f6c23e",
                    fill: false,
                }, {
                    label: "organisation",
                    labels: labelOrganisations,
                    data: dataOrganisations,
                    backgroundColor: "#e74a3b",
                    borderColor: "#e74a3b",
                    pointBorderColor: "#e74a3b",
                    fill: false,
                }]

            },
            options: {
                plugins:{
                    tooltip: {
                        callbacks:{
                            label: function (tooltipItem) {
                                console.log(tooltipItem);
                                return tooltipItem.dataset.labels[tooltipItem.parsed.x];
                            }
                        }
                    },
                },

            }
        });

}