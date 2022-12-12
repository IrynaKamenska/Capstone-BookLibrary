import React, {FormEvent} from 'react';
import "./css/BookCard.css";
import {Availability} from "./Availability";
import axios from "axios";
import {BookModel} from "./BookModel";


type BookCardProps = {
    book: BookModel;
    reloadAllBooks: () => void;

}

function BookCard(props: BookCardProps) {
    const [newBook, setNewBook] = React.useState(props.book);


    const addNewBook = (event: FormEvent<HTMLFormElement>) => {
        event.preventDefault()

        axios.post("/api/books", newBook)
            .then(props.reloadAllBooks)
            .catch((e) => console.log("POST Error: " + e))
        setNewBook({
            id: "",
            cover: "",
            title: "",
            author: "",
            isbn: "",
            availability: Availability.AVAILABLE,
            rentBookInfo: {
                rentByUsername: "",
                rentUntil: Date.now().toLocaleString()
            }
        });
    }

    function handleAddApiBook(event: any) {
        setNewBook({
            ...newBook,
            [event.target.name]: event.target.value
        })
    }

    return <>
        <form onSubmit={addNewBook}>
            <div className={"book-card-inner"}>
                <div className="book-cover-div">
                    <img className="book-cover" src={props.book.cover} alt={props.book.title}/>
                </div>
                <div className="book-info-div">
                    <h3 className="book-title">{props.book.title}</h3>
                    <p className="book-author">by {props.book.author}</p>
                    <p className="book-isbn">ISBN {props.book.isbn}</p>
                </div>
                <div className="div-form">
                    <br/>
                    <label>
                        Cover:
                        <input className="input-text" type="text"
                               id={"cover" + props.book.cover}
                               name="cover"
                               value={props.book.cover}
                               placeholder="cover"
                               onChange={handleAddApiBook}
                        />
                    </label>
                    <br/>
                    <label>
                        Title:
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
                        Author:
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
                        ISBN:
                        <input className="input-text" type="text"
                               id={"isbn" + props.book.id}
                               name="isbn"
                               value={props.book.isbn}
                               placeholder="isbn"
                               onChange={handleAddApiBook}
                        />
                    </label>
                    <br/>
                    <label htmlFor="availability">New Book State:</label>
                    <select name="availability" id="availability">
                        <option value={Availability.AVAILABLE}>AVAILABLE</option>
                        <option value={Availability.NOT_AVAILABLE}>NOT_AVAILABLE</option>
                    </select>
                    <br/><br/>
                </div>
            </div>
            <div className="div-button">
                <button className="button button-add-book">Add Book</button>
            </div>
        </form>


    </>
}

export default BookCard;
