<div >
  <h2>Book Library</h2>
  <p>A Book Library Website where can search books of your favourite authors. </p>
</div>

### API

- [Google Books API](https://developers.google.com/books/docs/v1/using)

**Link to github issues:**
https://github.com/IrynaKamenska/Capstone-BookLibrary/issues

### Clone this repository

```bash
git clone https://github.com/IrynaKamenska/Capstone-BookLibrary.git
```

### Installation

- Open project in an IDE <br>
- Run `npm install` in frontend folder <br>
- Allow installation of backend packages <br>
- Start backend application <br>
- Start frontend application (with start-button in package.json)<br>
- Open http://localhost:3000 in a browser <br>
- To start the application for the first time a user with a role Librarian must be stored in the MongoDB beforehand:<br>
- Open MongoDB-Compass and connect to mongodb://localhost:27017<br>
- Create a database named "book-library"<br>
- Create a collection named "appUser"<br>
- Create a Librarian as follows:<br>

``` _id: 6390cfae035b1f6382de17b7
  username: "user"
  rawPassword: ""
  passwordBcrypt:"$3b$20$rYQarIu23k3fcYqlF/aFSuxynzgoHiXpM82g9rpE5oxXJ18PayyUO"
  role: "LIBRARIAN"`
  ```

<br>
  For password-hashing you can use e.g. https://bcrypt-generator.com/<br>



