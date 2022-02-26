/* Fills the datalists for the selection process */

function getParties() {
  $.ajax({
    method: "GET",
    dataType: "json",
    contentType: "text/plain",
    url: "http://localhost:4567/party",
    success: function (data) {
      let targetDataList = document.getElementById("PartyName");
      data["result"].forEach((element) => {
        let newOption = document.createElement("option");
        newOption.value = element.id;
        newOption.setAttribute("data-value", element.id);
        targetDataList.appendChild(newOption);
      });
    },
    error: function () {
      console.log("Doesnt work..");
    },
  });
}

function getIndividuals() {
  $.ajax({
    method: "GET",
    dataType: "json",
    contentType: "text/plain",
    url: "http://localhost:4567/speakers",
    success: function (data) {
      let targetDataList = document.getElementById("SpeakerName");
      data["result"].forEach((element) => {
        let newOption = document.createElement("option");
        let firstname = element.name;
        let name = element.surname;
        let party = "";
        if (element.party !== null) {
          party = "(" + element.party + ")".replace(/\./g, "");
        }
        newOption.value = firstname + " " + name + " " + party;
        newOption.setAttribute("data-value", element.id);
        if (party !== "(undefined)") {
          targetDataList.appendChild(newOption);
        }
      });
    },
    error: function () {
      console.log("Doesnt work..");
    },
  });
}
getIndividuals();
getParties();
