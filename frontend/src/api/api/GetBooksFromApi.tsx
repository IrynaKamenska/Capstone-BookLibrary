import React, {FormEvent, useState} from 'react';
import axios from "axios";
import BookCard from "../components/BookCard";
import "../../css/BookGallery.css";
import {Availability} from "../model/Availability";
import {BookModel} from "../model/BookModel";

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
        "isbn": "",
        "availability": Availability.AVAILABLE
    }

    const [result, setResult] = useState<BookModel>(initialData);
    const [searchBy, setSearchBy] = useState<"isbn" | "text">()

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


    function handleChange(event: any) {
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
        <div>
            <form onSubmit={handleSubmit}>
                <input className={"search-input"} type="text"
                       placeholder={"Search for Books"}
                       autoComplete={"off"}
                       onChange={handleChange}
                />
                <button type={"submit"}>Search</button>
            </form>
            <button onClick={handleClick}>SearchBy: {searchBy}</button>

            <div className={"book-cards"}>
                {Array.isArray(result)
                    ? result.map((current, index) =>
                        <div className={"book-card"}>
                            <BookCard key={index} book={current} reloadAllBooks={props.reloadAllBooks}/>
                        </div>)
                    : ""}
            </div>
        </div>
    );

}

export default GetBooksFromApi;