<div >
  <h2>Book Library</h2>
  <p>A Book Library Website where can search books of your favorite authors. </p>
</div>

### API

- [Google Books API](https://developers.google.com/books/docs/v1/using)

**Link to github issues:**
https://github.com/IrynaKamenska/Capstone-BookLibrary/issues

### Clone this repository

```bash
git clone https://github.com/IrynaKamenska/Capstone-BookLibrary.git
```

### Deployed to Fly. io.
https://capstone-book-library.fly.dev


### Badges
Backend: <br>
[![Lines of Code](https://sonarcloud.io/api/project_badges/measure?project=irynakamenska-1_Capstone-BookLibrary-backend&metric=ncloc)](https://sonarcloud.io/summary/new_code?id=irynakamenska-1_Capstone-BookLibrary-backend)
[![Coverage](https://sonarcloud.io/api/project_badges/measure?project=irynakamenska-1_Capstone-BookLibrary-backend&metric=coverage)](https://sonarcloud.io/summary/new_code?id=irynakamenska-1_Capstone-BookLibrary-backend)
[![Security Rating](https://sonarcloud.io/api/project_badges/measure?project=irynakamenska-1_Capstone-BookLibrary-backend&metric=security_rating)](https://sonarcloud.io/summary/new_code?id=irynakamenska-1_Capstone-BookLibrary-backend)
[![Vulnerabilities](https://sonarcloud.io/api/project_badges/measure?project=irynakamenska-1_Capstone-BookLibrary-backend&metric=vulnerabilities)](https://sonarcloud.io/project/overview?id=irynakamenska-1_Capstone-BookLibrary-backend)
[![Code Smells](https://sonarcloud.io/api/project_badges/measure?project=irynakamenska-1_Capstone-BookLibrary-backend&metric=code_smells)](https://sonarcloud.io/summary/new_code?id=irynakamenska-1_Capstone-BookLibrary-backend)
[![Bugs](https://sonarcloud.io/api/project_badges/measure?project=irynakamenska-1_Capstone-BookLibrary-backend&metric=bugs)](https://sonarcloud.io/summary/new_code?id=irynakamenska-1_Capstone-BookLibrary-backend)
[![Reliability Rating](https://sonarcloud.io/api/project_badges/measure?project=irynakamenska-1_Capstone-BookLibrary-backend&metric=reliability_rating)](https://sonarcloud.io/summary/new_code?id=irynakamenska-1_Capstone-BookLibrary-backend)
[![Maintainability Rating](https://sonarcloud.io/api/project_badges/measure?project=irynakamenska-1_Capstone-BookLibrary-backend&metric=sqale_rating)](https://sonarcloud.io/summary/new_code?id=irynakamenska-1_Capstone-BookLibrary-backend)
<br>


Frontend:<br>
[![Lines of Code](https://sonarcloud.io/api/project_badges/measure?project=irynakamenska-1_Capstone-BookLibrary-frontend&metric=ncloc)](https://sonarcloud.io/summary/new_code?id=irynakamenska-1_Capstone-BookLibrary-frontend)
[![Code Smells](https://sonarcloud.io/api/project_badges/measure?project=irynakamenska-1_Capstone-BookLibrary-frontend&metric=code_smells)](https://sonarcloud.io/summary/new_code?id=irynakamenska-1_Capstone-BookLibrary-frontend)
[![Bugs](https://sonarcloud.io/api/project_badges/measure?project=irynakamenska-1_Capstone-BookLibrary-frontend&metric=bugs)](https://sonarcloud.io/summary/new_code?id=irynakamenska-1_Capstone-BookLibrary-frontend)
[![Maintainability Rating](https://sonarcloud.io/api/project_badges/measure?project=irynakamenska-1_Capstone-BookLibrary-frontend&metric=sqale_rating)](https://sonarcloud.io/summary/new_code?id=irynakamenska-1_Capstone-BookLibrary-frontend)
[![Reliability Rating](https://sonarcloud.io/api/project_badges/measure?project=irynakamenska-1_Capstone-BookLibrary-frontend&metric=reliability_rating)](https://sonarcloud.io/summary/new_code?id=irynakamenska-1_Capstone-BookLibrary-frontend)

### Installation

- Open project in an IDE <br>
- Run `npm install` in frontend folder <br>
- Allow installation of backend packages <br>
- Start backend application <br>
- Start frontend application (with start-button in package.json)<br>
- Open http://localhost:3000 in a browser <br>
- To start the application for the first time a user with a role Librarian must be stored in the MongoDB beforehand:<br>
- Open MongoDB-Compass and connect to mongodb://localhost:27017<br>
- Create a database named "library-db"<br>
- Create a collection named "book"<br>
- Create a collection named "appUser"<br>
- Create a Librarian in the "appUser" collection as follows:<br>

``` _id: 6390cfae035b1f6382de17b7
  username: "JohnDoe"
  rawPassword: ""
  passwordBcrypt:"$2a$10$yef/EG0medAcG7P0IHyVh.1xAiwa7DUOAEAN/Tve6cOeRjyi2CioK"
  role: "LIBRARIAN"`
  ```

<br>
  For password-hashing you can use e.g. https://bcrypt-generator.com/<br>



