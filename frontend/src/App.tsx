import React, {useEffect, useState} from 'react';
import './css/App.css';
import BookOverview from "./components/BookOverview";
import GetBooksFromApi from "./service/GetBooksFromApi";
import axios from "axios";
import {BookModel} from "./model/BookModel";
import LoginPage from "./security/LoginPage";
import RegisterPage from "./security/RegisterPage";
import SecuredPage from "./security/SecuredPage";

function App() {
    const [books, setBooks] = useState<BookModel[]>([]);
    const [username, setUsername] = useState<string>();

    const fetchAllBooks = () => {
        axios.get("/api/books")
            .then(response => response.data)
            .catch(error => console.error("GET Error: " + error))
            .then(setBooks)
    }
    useEffect(fetchAllBooks, [])

    const fetchUsername = () => {
        axios.get("/api/app-users/me")
            .then(response => response.data)
            .then(setUsername)
    }
    useEffect(fetchUsername, [])

    if (username === undefined) {
        return <>Loading...</>
    }
    if (username === 'anonymousUser') {
        return <>
            <LoginPage onLogin={fetchUsername}></LoginPage>
            <RegisterPage/>
        </>
    }

    return <>
        <SecuredPage onLogout={fetchUsername} username={username}/>
        <BookOverview books={books} fetchAllBooks={fetchAllBooks}/>
        <GetBooksFromApi reloadAllBooks={fetchAllBooks}/>
    </>;


}

export default App;
