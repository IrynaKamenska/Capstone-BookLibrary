import React from 'react';
import './css/App.css';
import BookOverview from "./BookOverview";
import {BrowserRouter, Route, Routes} from "react-router-dom";

function App() {

    return <>
        <h1>Book Library</h1>
        <main>
            <BrowserRouter>
                <Routes>
                    <Route path={"/api/books"} element={<BookOverview/>}></Route>
                </Routes>
            </BrowserRouter>
        </main>
    </>;

}

export default App;
