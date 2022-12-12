import {Availability} from "./Availability";
import {RentBookInfo} from "./RentBookInfo";

export type BookModel = {
    id: string,
    cover: string,
    title: string,
    author: string,
    isbn: string,
    availability: Availability,
    rentBookInfo: RentBookInfo
}