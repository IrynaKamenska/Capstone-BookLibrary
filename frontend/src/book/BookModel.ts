import {Availability} from "./Availability";
import {RentBookInfo} from "./RentBookInfo";
import {Isbn} from "./Isbn";

export type BookModel = {
    id: string,
    cover: string,
    title: string,
    author: string,
    isbn: Isbn[],
    availability: Availability,
    rentBookInfo: RentBookInfo
}