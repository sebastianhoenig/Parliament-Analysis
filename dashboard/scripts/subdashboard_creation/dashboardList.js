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
    items.splice(index - 2, 1);
  };

  const setIds = () => {
    for (let i = 0; i < items.length; i++) {
      items[i].setId(2 + i);
    }
  };

  return { getDashboardList, addToDashboardList, deleteDashboard, setIds };
})();

export { dashboardList };