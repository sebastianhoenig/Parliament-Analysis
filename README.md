<div align="center">
  <h1>German parliament analysis</h1>
  <br />
  A group project from Goethe-University's practial programming course
  <br />
</div>

<div align="center">
<br />


</div>

<details open="open">
<summary>Table of Contents</summary>

- [About](#about)
  - [Built With](#built-with)
- [Usage](#usage)
- [Authors & contributors](#authors--contributors)
- [License](#license)
- [Acknowledgements](#acknowledgements)

</details>

---

## About

This project's aim is to analyze the german parliament with modern NLP techniques, store these results in a database and visualizing the results using a REST-API and Javascripts chart.js library.


### Built With

Technologies used: Java, Javascript, Rest-API's and MongoDB.

For a full breakdown of programming languages used, see [the graphs page](https://gitlab.texttechnologylab.org/seb/gruppe_8_4_parliamentsentimentradar/-/graphs/master/charts)

## Getting Started

### Demonstration

![](showcase.gif)

## Usage

To use the project, please make sure you set up your own connection to a MongoDB database. Afterwards, run the ParliamentSentimentRadar.java main function to download the XML files and store it's analyzed content in the database.
This may take serveral days. Once it's finished loading, run the ApiConnection.java main function to set up the API and open the index.html page in the dashboard-folder. 


## Authors & contributors

The original setup of this repository is by Emmelina Garde, Sebastian Hönig, Vanessa Rosenbaum and Ben Werner.

For a full list and breakdown of all authors and contributors, see [the contributors page](https://gitlab.texttechnologylab.org/seb/gruppe_8_4_parliamentsentimentradar/-/graphs/master).


## License

This project is licensed under the **MIT license**.

See [LICENSE](LICENSE) for more information.

## Acknowledgements

We would like to thank Giuseppe Abrami from Goethe-University for his lectures and support through out the project as well as his team of student tutors that were always open to answer questions.



