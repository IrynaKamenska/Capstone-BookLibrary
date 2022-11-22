import React from 'react';
import './css/App.css';
import CreateBook from "./CreateBook";
import BookOverview from "./BookOverview";

function App() {

    return <>

        <h1>Book Library</h1>
        <main>
            <BookOverview/>
            <CreateBook/>
        </main>
    </>;

}

export default App;
