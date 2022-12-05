import React from 'react';
import "../css/AddBookManually.css";

type addBookProps = {
    openModal: () => void
}

function AddBookManually(props: addBookProps) {

    return (
        <><h2>Add new Books</h2><p>Add book manually via form: </p>
            <button className="button-add" onClick={props.openModal}>Manually add new Book</button>
        </>
    )
        ;
}

export default AddBookManually;