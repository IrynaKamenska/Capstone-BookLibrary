import React, {ChangeEvent, FormEvent, useState} from 'react';
import axios from "axios";
import BookCard from "../book/BookCard";
import "./css/GetBooksFromApi.css";
import {Availability} from "../book/Availability";
import {BookModel} from "../book/BookModel";

type GetBooksFromApiProps = {
    reloadAllBooks: () => void
}

function GetBooksFromApi(props: GetBooksFromApiProps) {
    const [text, setText] = useState<string>("")

    const initialData: BookModel = {
        "id": "",
        "cover": "",
        "title": "",
        "author": "",
        "isbn": [{type: "", identifier: ""}],
        "category": "",
        "printType": "",
        "pageCount": 0,
        "availability": Availability.AVAILABLE,
        "rentBookInfo": {rentByUsername: "", rentUntil: new Date()}
    }

    const [result, setResult] = useState<BookModel>(initialData);
    const [searchBy, setSearchBy] = useState<"isbn" | "text">("text")

    const isbnQuery = "isbn/" + text;
    const keyWordQuery = "search/" + text;
    const query = (searchBy === "isbn" ? isbnQuery : keyWordQuery)

    const getBooksFromApi = () => {
        axios.get("/api/books/" + query)
            .then(response =>
                response.data)
            .catch(error => console.error(error))
            .then(setResult)
    }


    function handleChange(event: ChangeEvent<HTMLInputElement>) {
        const text = event.target.value;
        setText(text);

    }

    function handleSubmit(event: FormEvent<HTMLFormElement>) {
        event.preventDefault();
        getBooksFromApi()

    }

    function handleClick() {
        const query = searchBy === "isbn" ? "text" : "isbn"
        setSearchBy(query)
    }


    return (
        <div className="search-main-div">
            <p>Semi-automatic via search in google database: </p>
            <form onSubmit={handleSubmit}>
                <input className={"search-input"} type="text"
                       placeholder={"Enter ISBN, author or title"}
                       autoComplete={"off"}
                       onChange={handleChange}
                />
                <button className="button button-search" type={"submit"}>Search</button>
                <button className="button button-search-by" onClick={handleClick}>Search by: {searchBy}</button>
            </form>


            <h2>Search results for {text}</h2>
            <div className={"book-cards"}>
                {Array.isArray(result)
                    ? result.map((current) =>
                        <div className={"book-card"}>
                            <BookCard key={current.id} book={current} reloadAllBooks={props.reloadAllBooks}/>
                        </div>)
                    : ""}
            </div>
        </div>
    );

}

export default GetBooksFromApi;