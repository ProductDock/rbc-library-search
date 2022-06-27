package com.productdock.library.search.clean;

import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

import java.util.List;

@Data
public class Book {
    private String id;
    private String title;
    private String author;
    private String cover;
    private List<Book.Record> records;
    private Book.Rating rating = new Book.Rating();

    @Data
    public static class Record {

        private String email;
        private BookStatus status;
    }

    @Data
    public static class Rating {

        private Double score;
        private Integer count;
    }
}
