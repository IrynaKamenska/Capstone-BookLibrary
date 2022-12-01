import React, {FormEvent, useState} from 'react';
import axios from "axios";
import BookCard from "../components/BookCard";
import "../components/BookGallery.css";
import {ResponseBookModel} from "../model/ResponseBookModel";
import {BookState} from "../model/BookState";

type GetBooksFromApi = {
    reloadAllBooks: () => void
}

function GetBooksFromApi(props: GetBooksFromApi) {
    const [text, setText] = useState<string>("")

    const initialData: ResponseBookModel = {
        "totalItems": 0,
        "items": [
            {
                "id": "",
                "volumeInfo": {
                    "title": "",
                    "authors": [],
                    "industryIdentifiers": [
                        {
                            "type": "",
                            "identifier": ""
                        }
                    ],
                    "imageLinks": {
                        "thumbnail": ""
                    },
                    "previewLink": "",
                }, bookState: BookState.AVAILABLE
            }
        ]
    }
    const [result, setResult] = useState<ResponseBookModel>(initialData);

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
            <button onClick={handleClick}>SearchByQuery: {searchBy}</button>

            <div className={"book-cards"}>

                {Array.isArray(result.items)
                    ? result.items.map((current, index) =>
                        <div className={"book-card"}>
                            <BookCard key={index} book={current} reloadAllBooks={props.reloadAllBooks}/>
                        </div>)
                    : ""}
            </div>

        </div>
    );

}

export default GetBooksFromApi;