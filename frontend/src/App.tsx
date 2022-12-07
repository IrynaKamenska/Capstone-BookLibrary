import React, {useEffect, useState} from 'react';
import './css/App.css';
import BookOverview from "./components/BookOverview";
import GetBooksFromApi from "./service/GetBooksFromApi";
import axios from "axios";
import {BookModel} from "./model/BookModel";
import LoginPage from "./security/LoginPage";
import RegisterPage from "./security/RegisterPage";
import SecuredPage from "./security/SecuredPage";
import {BrowserRouter, Route, Routes} from "react-router-dom";
import {AppUser} from "./security/model/AppUser";


function App() {
    const [books, setBooks] = useState<BookModel[]>([]);
    const fetchAllBooks = () => {
        axios.get("/api/books")
            .then(response => response.data)
            .catch(error => console.error("GET Error: " + error))
            .then(setBooks)
    }
    useEffect(fetchAllBooks, [])

    const [username, setUsername] = useState<string>();

    const initialData: AppUser = {
        "id": "",
        "username": "",
        "rawPassword": "",
        "role": ""
    }
    const [appUser, setAppUser] = useState<AppUser>(initialData);

    const fetchUser = () => {
        axios.get("/api/app-users/user")
            .then(response => response.data)
            .then(setAppUser)
    }
    useEffect(fetchUser, [])


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
            <BrowserRouter>
                <Routes>
                    <Route path={"/*"}
                           element={<LoginPage fetchUser={fetchUser} fetchUsername={fetchUsername}/>}></Route>
                    <Route path={"/register"} element={<RegisterPage/>}></Route>
                </Routes>
            </BrowserRouter>
        </>
    }

    return <>
        <SecuredPage fetchUsername={fetchUsername} appUser={appUser} fetchUser={fetchUser} setUsername={setUsername}/>
        <BookOverview books={books} fetchAllBooks={fetchAllBooks}/>
        <GetBooksFromApi reloadAllBooks={fetchAllBooks}/>
    </>;

}

export default App;
