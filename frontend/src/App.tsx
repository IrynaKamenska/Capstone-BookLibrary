import React, {useCallback, useEffect, useState} from 'react';
import './book/css/App.css';
import BookOverview from "./book/BookOverview";
import GetBooksFromApi from "./api/GetBooksFromApi";
import axios from "axios";
import {BookModel} from "./book/BookModel";
import LoginPage from "./security/LoginPage";
import RegisterPage from "./security/RegisterPage";
import SecuredPage from "./security/SecuredPage";
import {BrowserRouter, Route, Routes} from "react-router-dom";
import GetAllUsernames from "./security/GetAllUsernames";


function App() {
    const [books, setBooks] = useState<BookModel[]>([]);

    const fetchAllBooks = useCallback(() => {
        axios.get("/api/books")
            .then(response => response.data)
            .catch(error => console.error("GET Error: " + error))
            .then(setBooks)
    }, [])

    useEffect(fetchAllBooks, [fetchAllBooks])


    const [username, setUsername] = useState<string>();
    const fetchUsername = useCallback(() => {
        axios.get("/api/app-users/me")
            .then(response => response.data)
            .then(setUsername)
    }, [])
    useEffect(fetchUsername, [fetchUsername])


    if (username === undefined) {
        return <>Loading...</>
    }
    if (username === 'anonymousUser') {
        return <>
            <BrowserRouter>
                <Routes>
                    <Route path={"/"}
                           element={<LoginPage fetchUsername={fetchUsername}/>}></Route>
                    <Route path={"/register"} element={<RegisterPage/>}></Route>
                </Routes>
            </BrowserRouter>
        </>
    }

    return <>
        <SecuredPage fetchUsername={fetchUsername} setUsername={setUsername}/>
        <BookOverview books={books} fetchAllBooks={fetchAllBooks}/>
        <GetBooksFromApi reloadAllBooks={fetchAllBooks}/>
        <GetAllUsernames></GetAllUsernames>
    </>;

}

export default App;
