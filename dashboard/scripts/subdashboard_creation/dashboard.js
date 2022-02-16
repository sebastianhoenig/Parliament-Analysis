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

export { Dashboard };
