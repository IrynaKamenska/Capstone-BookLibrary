import {BookState} from "./BookState";

export type BookModel = {
    id: string,
    title: string,
    author: string,
    isbn: string,
    bookState: BookState
}