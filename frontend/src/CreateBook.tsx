
import React, {FormEvent} from "react";

import axios from "axios";
import {BookState} from "./BookState";

export default function CreateBook() {

    const [newBook, setNewBook] = React.useState(
        {
            title: "",
            author: "",
            isbn: "",
            bookState: BookState.AVAILABLE
        }
    );


    const addNewBook = (event: FormEvent<HTMLFormElement>) => {
        event.preventDefault()

        if (!newBook.title || !newBook.author || !newBook.isbn || !newBook.bookState) {
            alert(`Please fill book title, author, isbn and state`)
            return
        }

        axios.post("/api/books", newBook)
            .then(function (response) {
                console.log(response)
            })
            .catch((e) => console.error("POST ERROR: " + e))
        setNewBook({
            title: "",
            author: "",
            isbn: "",
            bookState: BookState.AVAILABLE
        });
    }

    function handleChange(event: any) {
        setNewBook({
            ...newBook,
            [event.target.name]: event.target.value
        })
    }

    return <>
        <form onSubmit={addNewBook}>
            <h3>Please fill the following fields: </h3>
            <br/>
            <label>
                Book title:
                <input type="text"
                       id="title"
                       name="title"
                       value={newBook.title}
                       onChange={handleChange}
                       placeholder="title"
                />
            </label>
            <br/>
            <label>
                Book author:
                <input type="text"
                       id="author"
                       name="author"
                       value={newBook.author}
                       onChange={handleChange}
                       placeholder="author"
                />
            </label>
            <br/>
            <label>
                Book isbn:
                <input type="isbn"
                       id="isbn"
                       name="isbn"
                       value={newBook.isbn}
                       onChange={handleChange}
                       placeholder="isbn"
                />
            </label>
            <br/>
            <label htmlFor="bookState">Book State:</label>
            <select name="bookState" id="bookState">
                <option value={BookState.AVAILABLE}>AVAILABLE</option>
                <option value={BookState.NOT_AVAILABLE}>NOT_AVAILABLE</option>
            </select>
            <br/><br/>
            <button>add new book</button>
        </form>

    </>
}