import {Availability} from "./Availability";

export type BookModel = {
    id: string,
    cover: string,
    title: string,
    author: string,
    isbn: string,
    availability: Availability,
    rentedBy: string
}