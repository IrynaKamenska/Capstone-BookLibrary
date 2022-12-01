import {BookState} from "./BookState";

export type GoogleModel =
    {
        "id": string,
        "volumeInfo": {
            "title": string,
            "authors": [],
            "industryIdentifiers": [
                {
                    "type": string,
                    "identifier": string
                }
            ],
            "imageLinks": {
                "thumbnail": string
            },
            "previewLink": string
        },
        bookState: BookState,
    }
