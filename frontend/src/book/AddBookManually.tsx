import React from 'react';
import "./css/AddBookManually.css";

type addBookProps = {
    openModal: () => void
}

function AddBookManually(props: addBookProps) {

    return (
        <>
            <div className="search-main-div">
                <h2>Add new Books to Library</h2><p>Add book manually via form: </p>
                <button className="button button-add-manually" onClick={props.openModal}>Manually add new Book</button>
            </div>
        </>
    )
        ;
}

export default AddBookManually;