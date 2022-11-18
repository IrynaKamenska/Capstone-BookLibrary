package de.neuefische.booklibrary.backend;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/books")
public class BookLibraryController {

    @GetMapping
    String book (){
        return "My book library!";
    }
}
