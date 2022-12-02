import React, {FormEvent} from 'react';
import "./BookCard.css";
import {BookState} from "../../BookState";
import axios from "axios";
import {BookModel} from "../../BookModel";


type BookCardProps = {
    book: BookModel;
    reloadAllBooks: () => void;

}

function BookCard(props: BookCardProps) {
    const [newBook, setNewBook] = React.useState(
        {
            id: props.book.id,
            title: props.book.title,
            author: props.book.author,
            isbn: props.book.isbn,
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
            .catch((e) => console.log("POST Error: " + e))
            .then(props.reloadAllBooks)
        setNewBook({
            id: "",
            title: "",
            author: "",
            isbn: "",
            bookState: BookState.AVAILABLE
        });
    }

    function handleAddApiBook(event: any) {
        setNewBook({
            ...newBook,
            [event.target.name]: event.target.value
        })
    }

    return <>
        <div className={"book-card"}>
            <h3 className="book-title">{props.book.title}</h3>
            <p className="book-authors">{props.book.author}</p>
            <p className="book-authors">{props.book.isbn}</p>
            <form onSubmit={addNewBook}>
                <br/>
                <label>
                    New title:
                    <input className="input-text" type="text"
                           id={"title" + props.book.id}
                           name="title"
                           value={props.book.title}
                           placeholder="title"
                           onChange={handleAddApiBook}
                    />
                </label>
                <br/>
                <label>
                    New Author:
                    <input className="input-text" type="text"
                           id={"author" + props.book.id}
                           name="author"
                           value={props.book.author}
                           placeholder="author"
                           onChange={handleAddApiBook}
                    />
                </label>
                <br/>
                <label>
                    New ISBN:
                    <input className="input-text" type="text"
                           id={"isbn" + props.book.id}
                           name="isbn"
                           value={props.book.isbn}
                           placeholder="isbn"
                           onChange={handleAddApiBook}
                    />
                </label>
                <br/>
                <label htmlFor="bookState">New Book State:</label>
                <select name="bookState" id="bookState">
                    <option value={BookState.AVAILABLE}>AVAILABLE</option>
                    <option value={BookState.NOT_AVAILABLE}>NOT_AVAILABLE</option>
                </select>
                <br/><br/>
                <button>ADD</button>
            </form>
        </div>


    </>
}

export default BookCard;