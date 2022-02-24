const searchSpeakerButton = document.getElementById("searchSpeaker");
const searchPartyButton = document.getElementById("searchParty");

searchSpeakerButton.addEventListener("click", () => {
  let targetDataList = document.getElementById("SpeakerName");
  let currInput = document.getElementById("speakerInput");
  let currId = document.querySelector(
    '#SpeakerName [value="' + currInput.value + '"]'
  ).dataset.value;
  let check = false;
  for (let i = 0; i < targetDataList.childNodes.length; i++) {
    if (currInput.value == targetDataList.childNodes[i].value) {
      check = true;
    }
  }
  if (check) {
    let dashboards = document.getElementById("speaker-dashboards");
    let newDashboard = new SpeakerDashboard(
      currInput.value.replace(/\s/g, "_"),
      0,
      currId
    );
    dashboardListSpeaker.addToDashboardList(newDashboard);
    dashboardListSpeaker.setIds();
    createDashboard(newDashboard, dashboards);
  } else {
    console.log("Entered value not in dropdown");
  }
});

searchPartyButton.addEventListener("click", () => {
  let party = document.getElementById("partyInput").value;
  let dashboards = document.getElementById("party-dashboards");
  let newDashboard = new PartyDashboard(party, 0);
  dashboardListParty.addToDashboardList(newDashboard);
  dashboardListParty.setIds();
  createPartyDashboard(newDashboard, dashboards);
});

function createPartyDashboard(element, dashboards) {
  let dashboard = document.createElement("div");
  dashboard.classList.add("dashboard-nav-item");
  dashboard.id = element.getId();
  let dashboardTextDiv = document.createElement("div");
  dashboardTextDiv.classList.add("dashboard-text");
  let idName = element.getName();
  dashboardTextDiv.textContent = idName;
  dashboardTextDiv.addEventListener("click", (e) => {
    changeActivePartyDashboard(e, dashboards, element);
  });
  let dashboardDeleteDiv = document.createElement("div");
  dashboardDeleteDiv.classList.add("dashboard-del");
  let dashboardDeleteButton = document.createElement("button");
  dashboardDeleteButton.innerHTML = '<i class="fas fa-times"></i>';
  dashboardDeleteButton.addEventListener("click", (e) => {
    deletePartyDashboard(e, dashboardListParty);
  });
  dashboardDeleteDiv.appendChild(dashboardDeleteButton);
  dashboard.append(dashboardTextDiv, dashboardDeleteDiv);
  let hr = document.createElement("hr");
  hr.classList.add("sidebar-divider");
  hr.classList.add("newtopmargin");
  hr.classList.add("my-0");
  dashboards.append(hr, dashboard);
}

function createDashboard(element, dashboards) {
  if (element.getName() == "all") {
    let dashboard = document.createElement("div");
    dashboard.classList.add("dashboard-nav-item");
    dashboard.id = 0;
    let dashboardTextDiv = document.createElement("div");
    dashboardTextDiv.classList.add("dashboard-text");
    dashboardTextDiv.textContent = "German parliament";
    dashboardTextDiv.addEventListener("click", (e) => {
      changeActiveDashboard(e, dashboards, element, 0);
    });
    dashboard.appendChild(dashboardTextDiv);
    let hr = document.createElement("hr");
    hr.classList.add("sidebar-divider");
    hr.classList.add("my-0");
    dashboards.append(dashboard, hr);
    renderDefaultPageContent();
  } else {
    let speakerID = element.getSpeakerID();
    let dashboard = document.createElement("div");
    dashboard.classList.add("dashboard-nav-item");
    dashboard.id = element.getId();
    let dashboardTextDiv = document.createElement("div");
    dashboardTextDiv.classList.add("dashboard-text");
    let idName = element.getName();
    let modified = idName.replace(/_/g, " ");
    let final = modified.replace(/ *\([^)]*\) */g, "");
    dashboardTextDiv.textContent = final;
    dashboardTextDiv.addEventListener("click", (e) => {
      changeActiveDashboard(e, dashboards, element, speakerID);
    });
    let dashboardDeleteDiv = document.createElement("div");
    dashboardDeleteDiv.classList.add("dashboard-del");
    let dashboardDeleteButton = document.createElement("button");
    dashboardDeleteButton.innerHTML = '<i class="fas fa-times"></i>';
    dashboardDeleteButton.addEventListener("click", (e) => {
      deleteDashboard(e, dashboardListSpeaker);
    });
    dashboardDeleteDiv.appendChild(dashboardDeleteButton);
    dashboard.append(dashboardTextDiv, dashboardDeleteDiv);
    let hr = document.createElement("hr");
    hr.classList.add("sidebar-divider");
    hr.classList.add("newtopmargin");
    hr.classList.add("my-0");
    dashboards.append(hr, dashboard);
  }
}

function deleteDashboard(e, dashboardListSpeaker) {
  let index = e.target.parentElement.parentElement.parentElement.id;
  console.log(index);
  console.log(dashboardListSpeaker.getDashboardList());
  dashboardListSpeaker.deleteDashboard(index);
  console.log(dashboardListSpeaker.getDashboardList());
  renderDashboards(dashboardListSpeaker);
  e.preventDefault();
  renderDefaultPageContent();
}

function deletePartyDashboard(e, dashboardListParty) {
  let index = e.target.parentElement.parentElement.parentElement.id;
  dashboardListParty.deleteDashboard(index);
  renderPartyDashboards(dashboardListParty);
  e.preventDefault();
  renderDefaultPageContent();
}

function renderPartyDashboards(dashboardListParty) {
  dashboardListParty.setIds();
  let currDashboards = dashboardListParty.getDashboardList();
  let dashboards = document.getElementById("party-dashboards");
  dashboards.innerHTML = '<div class="headline">Party Analysis</div>';
  currDashboards.forEach((element) => {
    createPartyDashboard(element, dashboards);
  });
}

function renderDashboards(dashboardListSpeaker) {
  dashboardListSpeaker.setIds();
  let currDashboards = dashboardListSpeaker.getDashboardList();
  let dashboards = document.getElementById("speaker-dashboards");
  dashboards.innerHTML = '<div class="headline">Speaker Analysis</div>';
  currDashboards.slice(1).forEach((element) => {
    createDashboard(element, dashboards);
  });
}

function changeActiveDashboard(e, dashboards, element, speakerID) {
  let speakerDashboard = document.getElementById("speaker-dashboards");
  let partyDashboard = document.getElementById("party-dashboards");
  for (let item of speakerDashboard.children) {
    item.classList.remove("active");
  }
  for (let item of partyDashboard.children) {
    item.classList.remove("active");
  }
  let dashboardID = e.target.parentElement.id;
  e.target.parentElement.classList.add("active");
  if (element.getName() == "all") {
    renderDefaultPageContent();
  } else {
    let name = element.getName();
    let modified = name.replace(/_/g, " ");
    let final = modified.replace(/ *\([^)]*\) */g, "");
    renderPageContent(
      dashboardListSpeaker.getDashboardList[dashboardID],
      name,
      speakerID,
      final
    );
  }
}

function changeActivePartyDashboard(e, dashboards, element) {
  let speakerDashboard = document.getElementById("speaker-dashboards");
  let partyDashboard = document.getElementById("party-dashboards");
  for (let item of speakerDashboard.children) {
    item.classList.remove("active");
  }
  for (let item of partyDashboard.children) {
    item.classList.remove("active");
  }
  let dashboardID = e.target.parentElement.id;
  e.target.parentElement.classList.add("active");
  let name = element.getName();
  let modified = name.replace(/./g, " ");
  renderPartyPageContent(
    dashboardListSpeaker.getDashboardList[dashboardID],
    name,
    modified
  );
}

function setupPage() {
  let newDashboard = new SpeakerDashboard("all", 0, 0);
  dashboardListSpeaker.addToDashboardList(newDashboard);
  let dashboards = document.getElementById("main-dashboard");
  createDashboard(newDashboard, dashboards);
}

function renderDefaultPageContent() {
  const container = document.getElementById("content");
  container.innerHTML = `
  <!-- Page Divider (Übersicht) -->
  <hr class="my-3 border" />

  <div
    class="d-sm-flex align-items-center justify-content-between mb-3"
  >
    <h1 class="h2 mb-0 text-info mb-2 text-center w-100">
      Übersicht
    </h1>
  </div>

  <!-- Content Row Tokens -->
  <div class="row">
    <!-- Line Chart Token -->
    <div class="col-xl-6 col-lg-6">
      <div class="card shadow mb-4">
        <!-- Card Header - Searchbar and datalist for all others Charts-->
        <div
          class="card-header py-3 d-flex flex-row align-items-center justify-content-between"
        >
          <h6 class="m-3 font-weight-bold text-info">Token</h6>
        </div>
        <!-- Card Body -->
        <div class="card-body">
          <div
            class="text-info text-center w-100"
          >
            German Parliament
          </div>
          <div class="fa-chart pt-4 pb-2">
            <canvas id="myLineChartAllToken"></canvas>
          </div>
        </div>
      </div>
    </div>

    <!-- Bar Chart POS -->
    <div class="col-xl-6 col-lg-6">
      <div class="card shadow mb-4">
        <!-- Card Header - Searchbar and datalist for all others Charts -->
        <div
          class="card-header py-3 d-flex flex-row align-items-center justify-content-between"
        >
          <h6 class="m-3 font-weight-bold text-info">POS</h6>
        </div>
        <!-- Card Body -->
        <div class="card-body">
          <div
            class="text-info text-center w-100"
          >
            German Parliament
          </div>
          <div class="fa-chart pt-4 pb-2">
            <canvas id="myBarChartAllPos"></canvas>
          </div>
        </div>
      </div>
    </div>
  </div>

  <!-- Content Row Sentiment and Speaker -->
  <div class="row">
    <!-- Bar-Chart Speaker -->
    <div class="col-8 col-8">
      <div class="card shadow mb-4">
        <!-- Card Header - with Searchbar -->
        <div
          class="card-header py-3 d-flex flex-row align-items-center justify-content-between"
        >
          <h6 class="m-0 font-weight-bold text-danger">
            Speaker Verteilung
          </h6>
          <div class="form-inline"></div>
        </div>
        <!-- Card Body -->
        <div class="card-body">
          <div
            class="text-danger text-center w-100"
          >
            German Parliament
          </div>
          <div class="fa-chart pt-4 pb-2">
            <canvas id="myBarChartSpeakerDistribution"></canvas>
          </div>
        </div>
      </div>
    </div>

    <!-- Radar-Chart Sentiment -->
    <div class="col-4 col-4">
      <div class="card shadow mb-4">
        <!-- Card Header - with Searchbar -->
        <div
          class="card-header py-3 d-flex flex-row align-items-center justify-content-between"
        >
          <h6 class="m-0 font-weight-bold text-danger">Sentiment</h6>
          <div class="form-inline"></div>
        </div>
        <!-- Card Body -->
        <div class="card-body">
          <div
            class="text-danger text-center w-100"
          >
            German Parliament
          </div>
          <div class="fa-chart pt-4 pb-2">
            <canvas id="myRadarChartSentiment"></canvas>
          </div>
        </div>
      </div>
    </div>
  </div>

  <!-- Content Row NamedEntities -->
  <div class="row">
    <!-- Multiple Axes Line Chart NamedEntities -->
    <div class="col-12 col-12">
      <div class="card shadow mb-4">
        <!-- Card Header - with Searchbar -->
        <div
          class="card-header py-3 d-flex flex-row align-items-center justify-content-between"
        >
          <h6 class="m-0 font-weight-bold text-primary">
            NamedEntities
          </h6>
          <div class="form-inline"></div>
        </div>
        <!-- Card Body -->
        <div class="card-body">
          <div
            class="text-primary text-center w-100"
          >
            German Parliament
          </div>
          <div class="fa-chart pt-4 pb-2">
            <canvas id="myLineChartNamedEntities"></canvas>
          </div>
        </div>
      </div>
    </div>
  </div>

  <!-- Page Divider (Named Entities) -->
  <hr class="my-5 border" />

  <div
    class="d-sm-flex align-items-center justify-content-between mb-3"
  >
    <h1 class="h2 mb-0 text-warning mb-2 text-center w-100">
      Volltext Suche
    </h1>
  </div>

  <!-- Content Row Named Entities -->
  <div class="row">
    <!-- Area Chart Named Entities Speaker -->
    <div class="col-12 col-12">
      <div class="card shadow mb-4">
        <!-- Card Header - with Searchbar -->
        <div
          class="card-header py-3 d-flex flex-row align-items-center justify-content-between"
        >
          <h6 class="m-0 font-weight-bold text-warning">
            Häufigkeitsverteilung der Named Entities pro Sprecher*in
          </h6>
          <div class="form-inline"></div>
        </div>
        <!-- Card Body -->
        <div class="card-body">
          <div
            class="text-warning text-center w-100"
          >
            German Parliament
          </div>
          <div class="fa-chart pt-4 pb-2">
            <canvas id="myAreaChart NamedEntities Speaker"></canvas>
          </div>
        </div>
      </div>
    </div>
  </div>
  `;
  getAllToken();
  getAllPos();
  getIndividuals();
  getParties();
  plotSentimentAll();
  plotSpeakerDistribution();
  getAllNamedEntities();
}

function renderPageContent(id, name, speakerID, finalName) {
  const container = document.getElementById("content");
  container.innerHTML =
    `
  <div class="container-fluid">
            <!-- Page Divider (Übersicht) -->
            <hr class="my-3 border" />

            <div
              class="d-sm-flex align-items-center justify-content-between mb-3"
            >
              <h1 class="h2 mb-0 text-info mb-2 text-center w-100">
                Übersicht
              </h1>
            </div>
            <!-- Content Row Tokens -->
            <div class="row">
              <!-- Line Chart Token -->
              <div class="col-xl-6 col-lg-6">
                <div class="card shadow mb-4">
                    <!-- Card Header - Searchbar and datalist for all others Charts-->
                    <div class="card-header py-3 d-flex flex-row align-items-center justify-content-between">
                        <h6 class="m-3 font-weight-bold text-info">Token</h6>
                    </div>
                    <!-- Card Body -->
                    <div class="card-body">
                        <div class=" text-info text-center w-100" >` +
    finalName +
    `</div>
                        <div class="fa-chart">
                            <canvas id=` +
    "myLineChartToken" +
    name +
    "" +
    `></canvas>
                        </div>
                    </div>
                </div>
              </div>
              <!-- Bar Chart POS -->
              <div class="col-xl-6 col-lg-6">
                <div class="card shadow mb-4">
                    <!-- Card Header - Searchbar and datalist for all others Charts -->
                    <div class="card-header py-3 d-flex flex-row align-items-center justify-content-between">
                        <h6 class="m-3 font-weight-bold text-info">POS</h6>
                    </div>
                    <!-- Card Body -->
                    <div class="card-body">
                        <div class=" text-info text-center w-100" >` +
    finalName +
    `</div>
                        <div class="fa-chart">
                            <canvas id=` +
    "myBarChartPos" +
    name +
    "" +
    `></canvas>
                        </div>
                    </div>
                </div>
            </div>
          </div>
            <!-- Content Row POS -->
            <div class="row">
              <!-- Area Chart POS Speaker -->
              <div class="col-6 col-6">
                <div class="card shadow mb-4">
                  <!-- Card Header - with Searchbar -->
                  <div
                    class="card-header py-3 d-flex flex-row align-items-center justify-content-between"
                  >
                    <h6 class="m-0 font-weight-bold text-danger">Sentiment</h6>
                    <div class="form-inline"></div>
                  </div>
                  <!-- Card Body -->
                  <div class="card-body">
                  <div
                    class="text-danger text-center w-100"
                  >
                    ` +
    finalName +
    `
                  </div>
                  <div class="fa-chart pt-4 pb-2">
                    <canvas id=` +
    "myRadarChartSentiment" +
    name +
    `></canvas>
                </div>
              </div>
                </div>
              </div>

              <!-- Area Chart POS Fraction -->
              <div class="col-6 col-6">
                <div class="card shadow mb-4">
                  <!-- Card Header - with Searchbar -->
                  <div
                    class="card-header py-3 d-flex flex-row align-items-center justify-content-between"
                  >
                    <h6 class="m-0 font-weight-bold text-danger">
                      Speaker Verteilung
                    </h6>
                    <div class="form-inline"></div>
                  </div>
                  <!-- Card Body -->
                  <div class="card-body">
                    <div
                      class="text-danger text-center w-100"
                    >
                      ` +
    finalName +
    `
                    </div>
                    <div class="fa-chart">
                      <canvas id=` +
    "myAreaChart" +
    name +
    "" +
    `></canvas>
                    </div>
                  </div>
                </div>
              </div>
            </div>
            <!-- Content Row Token -->
            <div class="row">
              <!-- Area Chart Token Speaker -->
              <div class="col-12 col-12">
                <div class="card shadow mb-4">
                  <!-- Card Header - with Searchbar -->
                  <div
                    class="card-header py-3 d-flex flex-row align-items-center justify-content-between"
                  >
                    <h6 class="m-0 font-weight-bold text-primary">
                      NamedEntities
                    </h6>
                    <div class="form-inline"></div>
                  </div>
                  <!-- Card Body -->
                  <div class="card-body">
                    <div
                      class="text-primary text-center w-100"
                    >
                      ` +
    finalName +
    `
                    </div>
                    <div class="fa-chart">
                      <canvas id=` +
    "myLineChart" +
    name +
    "" +
    `></canvas>
                    </div>
                  </div>
                </div>
              </div>
            </div>`;
  let tokenID = "myLineChartToken" + name;
  let posID = "myBarChartPos" + name;
  let namedEntitiesID = "myLineChart" + name;
  let sentimentID = "myRadarChartSentiment" + name;
  let speakerGraphID = "myAreaChart" + name;
  const tokenChart = document.getElementById(tokenID);
  const posChart = document.getElementById(posID);
  const namedEntities = document.getElementById(namedEntitiesID);
  getTokenBySpeaker(speakerID, tokenID);
  getPosBySpeaker(speakerID, posID);
  getAllNamedEntitiesPerson(speakerID, namedEntitiesID);
  plotSentimentAll(speakerID, sentimentID, 1);
  plotSpeakerDistribution(speakerID, speakerGraphID, 1);
}

function renderPartyPageContent(id, name, finalName) {
  const container = document.getElementById("content");
  container.innerHTML =
    `
  <div class="container-fluid">
            <!-- Page Divider (Übersicht) -->
            <hr class="my-3 border" />

            <div
              class="d-sm-flex align-items-center justify-content-between mb-3"
            >
              <h1 class="h2 mb-0 text-info mb-2 text-center w-100">
                Übersicht
              </h1>
            </div>
            <!-- Content Row Tokens -->
            <div class="row">
              <!-- Line Chart Token -->
              <div class="col-xl-6 col-lg-6">
                <div class="card shadow mb-4">
                    <!-- Card Header - Searchbar and datalist for all others Charts-->
                    <div class="card-header py-3 d-flex flex-row align-items-center justify-content-between">
                        <h6 class="m-3 font-weight-bold text-info">Token</h6>
                    </div>
                    <!-- Card Body -->
                    <div class="card-body">
                        <div class=" text-info text-center w-100" >` +
    finalName +
    `</div>
                        <div class="fa-chart">
                            <canvas id=` +
    "myLineChartToken" +
    name +
    "" +
    `></canvas>
                        </div>
                    </div>
                </div>
              </div>
              <!-- Bar Chart POS -->
              <div class="col-xl-6 col-lg-6">
                <div class="card shadow mb-4">
                    <!-- Card Header - Searchbar and datalist for all others Charts -->
                    <div class="card-header py-3 d-flex flex-row align-items-center justify-content-between">
                        <h6 class="m-3 font-weight-bold text-info">POS</h6>
                    </div>
                    <!-- Card Body -->
                    <div class="card-body">
                        <div class=" text-info text-center w-100" >` +
    finalName +
    `</div>
                        <div class="fa-chart">
                            <canvas id=` +
    "myBarChartPos" +
    name +
    "" +
    `></canvas>
                        </div>
                    </div>
                </div>
            </div>
          </div>
            <!-- Content Row POS -->
            <div class="row">
              <!-- Area Chart POS Speaker -->
              <div class="col-6 col-6">
                <div class="card shadow mb-4">
                  <!-- Card Header - with Searchbar -->
                  <div
                    class="card-header py-3 d-flex flex-row align-items-center justify-content-between"
                  >
                    <h6 class="m-0 font-weight-bold text-danger">Sentiment</h6>
                    <div class="form-inline"></div>
                  </div>
                  <!-- Card Body -->
                  <div class="card-body">
                  <div
                    class="text-danger text-center w-100"
                  >
                    ` +
    finalName +
    `
                  </div>
                  <div class="fa-chart pt-4 pb-2">
                    <canvas id=` +
    "myRadarChartSentiment" +
    name +
    `></canvas>
                </div>
              </div>
                </div>
              </div>

              <!-- Area Chart POS Fraction -->
              <div class="col-6 col-6">
                <div class="card shadow mb-4">
                  <!-- Card Header - with Searchbar -->
                  <div
                    class="card-header py-3 d-flex flex-row align-items-center justify-content-between"
                  >
                    <h6 class="m-0 font-weight-bold text-danger">
                      Speaker Verteilung
                    </h6>
                    <div class="form-inline"></div>
                  </div>
                  <!-- Card Body -->
                  <div class="card-body">
                    <div
                      class="text-danger text-center w-100"
                    >
                      ` +
    finalName +
    `
                    </div>
                    <div class="fa-chart">
                      <canvas id=` +
    "myAreaChart" +
    name +
    "" +
    `></canvas>
                    </div>
                  </div>
                </div>
              </div>
            </div>
            <!-- Content Row Token -->
            <div class="row">
              <!-- Area Chart Token Speaker -->
              <div class="col-12 col-12">
                <div class="card shadow mb-4">
                  <!-- Card Header - with Searchbar -->
                  <div
                    class="card-header py-3 d-flex flex-row align-items-center justify-content-between"
                  >
                    <h6 class="m-0 font-weight-bold text-primary">
                      NamedEntities
                    </h6>
                    <div class="form-inline"></div>
                  </div>
                  <!-- Card Body -->
                  <div class="card-body">
                    <div
                      class="text-primary text-center w-100"
                    >
                      ` +
    finalName +
    `
                    </div>
                    <div class="fa-chart">
                      <canvas id=` +
    "myLineChart" +
    name +
    "" +
    `></canvas>
                    </div>
                  </div>
                </div>
              </div>
            </div>`;
  let tokenID = "myLineChartToken" + name;
  let posID = "myBarChartPos" + name;
  let namedEntitiesID = "myLineChart" + name;
  let sentimentID = "myRadarChartSentiment" + name;
  let speakerGraphID = "myAreaChart" + name;
  const tokenChart = document.getElementById(tokenID);
  const posChart = document.getElementById(posID);
  const namedEntities = document.getElementById(namedEntitiesID);
  getTokenByParty(name, tokenID);
  getPosByParty(name, posID);
  getAllNamedEntitiesParty(name, namedEntitiesID);
  plotSentimentAll(name, sentimentID, 2);
  plotSpeakerDistribution(name, speakerGraphID, 2);
}

const dashboardListSpeaker = (() => {
  let items = [];

  const getDashboardList = () => {
    return items;
  };

  const addToDashboardList = (newDashboard) => {
    items.push(newDashboard);
  };

  const deleteDashboard = (index) => {
    items.splice(index - 1, 1);
  };

  const setIds = () => {
    for (let i = 0; i < items.length; i++) {
      items[i].setId(1 + i);
    }
  };

  return { getDashboardList, addToDashboardList, deleteDashboard, setIds };
})();

const dashboardListParty = (() => {
  let items = [];

  const getDashboardList = () => {
    return items;
  };

  const addToDashboardList = (newDashboard) => {
    items.push(newDashboard);
  };

  const deleteDashboard = (index) => {
    items.splice(index - 1, 1);
  };

  const setIds = () => {
    for (let i = 0; i < items.length; i++) {
      items[i].setId(1 + i);
    }
  };

  return { getDashboardList, addToDashboardList, deleteDashboard, setIds };
})();

class SpeakerDashboard {
  constructor(name, id, speakerID) {
    this.name = name;
    this.id = id;
    this.speakerID = speakerID;
  }

  setName(name) {
    this.name = name;
  }

  getName() {
    return this.name;
  }

  setId(i) {
    this.id = i;
  }

  getId() {
    return this.id;
  }

  getSpeakerID() {
    return this.speakerID;
  }
}

class PartyDashboard {
  constructor(name, id) {
    this.name = name;
    this.id = id;
  }

  setName(name) {
    this.name = name;
  }

  getName() {
    return this.name;
  }

  setId(i) {
    this.id = i;
  }

  getId() {
    return this.id;
  }
}

setupPage();
