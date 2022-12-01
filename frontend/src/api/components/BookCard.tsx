import React, {FormEvent} from 'react';
import "./BookCard.css";

import {GoogleModel} from "../model/GoogleModel";
import {BookState} from "../../BookState";
import axios from "axios";


type BookCardProps = {
    book: GoogleModel;
    reloadAllBooks: () => void;

}

function BookCard(props: BookCardProps) {
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
            .catch((e) => console.log("POST Error: " + e))
            .then(props.reloadAllBooks)
        setNewBook({
            title: "",
            author: "",
            isbn: "",
            bookState: BookState.AVAILABLE
        });
    }

    function handleUpdateChange(event: any) {
        setNewBook({
            ...newBook,
            [event.target.name]: event.target.value
        })
    }

    return <>
        <div className={"book-card"}>
            {props.book.volumeInfo === undefined ? ""
                :
                <h3 className="book-title">{props.book.volumeInfo.title}</h3>}
            <p className="book-authors">{props.book.volumeInfo.authors}</p>
            <p className="book-authors">{props.book.volumeInfo.industryIdentifiers
                .map(({type, identifier}) => {
                    return <p key={props.book.id}>{type}{identifier}</p>
                })
            }</p>

            {props.book.volumeInfo.imageLinks === undefined ? ""
                :
                <img className="book-image" src={props.book.volumeInfo.imageLinks.thumbnail}
                     alt={props.book.volumeInfo.title}/>
            }
            {/*<a href={props.book.volumeInfo.previewLink} className={"link-details"} target="_blank" rel="noreferrer">More Info</a>*/}


            <form onSubmit={addNewBook}>
                <br/>
                <label>
                    New title:
                    <input className="input-text" type="text"
                           id={"title" + props.book.id}
                           name="title"
                           value={props.book.volumeInfo.title}
                           placeholder="title"
                           onChange={handleUpdateChange}
                    />
                </label>
                <br/>
                <label>
                    New Author:
                    <input className="input-text" type="text"
                           id={"author" + props.book.id}
                           name="author"
                           value={props.book.volumeInfo.authors}
                           placeholder="author"
                           onChange={handleUpdateChange}
                    />
                </label>
                <br/>
                <label>
                    New ISBN:
                    <input className="input-text" type="text"
                           id={"isbn" + props.book.id}
                           name="isbn"
                           value={props.book.volumeInfo.industryIdentifiers.filter(
                               current => current.type === "ISBN_13").map(
                               element => element.identifier
                           )}
                           placeholder="isbn"
                           onChange={handleUpdateChange}
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