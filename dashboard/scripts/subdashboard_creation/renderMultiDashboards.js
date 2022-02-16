const searchSpeacherButton = document.getElementById("searchSpeaker");
const searchPartyButton = document.getElementById("searchParty");

searchSpeacherButton.addEventListener("click", () => {
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
    let dashboards = document.getElementById("dashboards");
    let newDashboard = new Dashboard(currInput.value.replace(/\s/g, ""), 0);
    dashboardList.addToDashboardList(newDashboard);
    dashboardList.setIds();
    createDashboard(newDashboard, dashboards, currId);
  } else {
    console.log("Entered value not in dropdown");
  }
});

function createDashboard(element, dashboards, speakerID) {
  let dashboard = document.createElement("div");
  dashboard.classList.add("project-nav");
  dashboard.id = element.getId();
  let dashboardTextDiv = document.createElement("div");
  dashboardTextDiv.classList.add("project-text");
  dashboardTextDiv.textContent = element.getName();
  dashboardTextDiv.addEventListener("click", (e) => {
    changeActiveDashboard(e, dashboards, element, speakerID);
  });
  let dashboardDeleteDiv = document.createElement("div");
  dashboardDeleteDiv.classList.add("project-del");
  let dashboardDeleteButton = document.createElement("button");
  dashboardDeleteButton.innerHTML = '<i class="fas fa-times"></i>';
  dashboardDeleteButton.addEventListener("click", (e) => {
    deleteDashboard(e, dashboardList);
  });
  dashboardDeleteDiv.appendChild(dashboardDeleteButton);
  dashboard.append(dashboardTextDiv, dashboardDeleteDiv);
  dashboards.appendChild(dashboard);
}

function deleteDashboard(e, dashboardList) {
  let index = e.target.parentElement.parentElement.parentElement.id;
  dashboardList.deleteDashboard(index);
  renderDashboards(dashboardList);
}

function renderDashboards(dashboardList) {
  dashboardList.setIds();
  let currDashboards = dashboardList.getDashboardList;
  let dashboards = document.getElementById("dashboards");
  dashboards.textContent = "";
  currDashboards.forEach((element) => {
    createDashboard(element, dashboards);
  });
}

function addProject(
  value,
  projectList,
  addProjectButton,
  addProjectInput,
  addProjectInputButton,
  cancelProjectInputButton,
  projects
) {
  if (value !== "") {
    let newProject = new Project(value, [], 0);
    projectList.addToProjectList(newProject);
    projectList.setIds();
    createProject(newProject, projects);
    addProjectButton.classList.remove("deactivated");
    addProjectInput.classList.add("deactivated");
    addProjectInputButton.classList.add("deactivated");
    cancelProjectInputButton.classList.add("deactivated");
    addProjectInput.value = "";
  }
}

function changeActiveDashboard(e, dashboards, element, speakerID) {
  for (let item of dashboards.children) {
    item.classList.remove("active");
  }
  let dashboardID = e.target.parentElement.id;
  e.target.parentElement.classList.add("active");
  let name = element.getName();
  renderPageContent(
    dashboardList.getDashboardList[dashboardID],
    name,
    speakerID
  );
}

function renderPageContent(id, name, speakerID) {
  const topbar = document.getElementById("top-navbar");
  topbar.innerHTML = "";
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
    name +
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
    name +
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
    name +
    `
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
    name +
    `
                    </div>
                    <div class="fa-chart-area">
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
    name +
    `
                    </div>
                    <div class="fa-chart-area">
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
  const tokenChart = document.getElementById(tokenID);
  const posChart = document.getElementById(posID);
  const namedEntities = document.getElementById(namedEntitiesID);
  getTokenBySpeaker(speakerID, tokenID);
  getPosBySpeaker(speakerID, posID);
  getAllNamedEntitiesPerson(speakerID, namedEntitiesID);
}

const dashboardList = (() => {
  let items = [];

  // Create an example Project with ToDo
  // let exampleProject = createNewProject("The Odin Project", [], 0);
  // items.push(exampleProject);
  // let exampleToDo = createNewTodo(
  //   "Finish the Todo-List Application",
  //   new Date(),
  //   0,
  //   false
  // );

  // let exampleToDo2 = createNewTodo(
  //   "Finish the Odin Project Fullstack JS",
  //   parseISO("2022-04-01"),
  //   0,
  //   false
  // );

  // exampleProject.addToDo(exampleToDo);
  // exampleProject.addToDo(exampleToDo2);

  const getDashboardList = () => {
    return items;
  };

  const addToDashboardList = (newDashboard) => {
    items.push(newDashboard);
  };

  const deleteDashboard = (index) => {
    items.splice(index, 1);
  };

  const setIds = () => {
    for (let i = 0; i < items.length; i++) {
      items[i].setId(i);
    }
  };

  return { getDashboardList, addToDashboardList, deleteDashboard, setIds };
})();

class Dashboard {
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
