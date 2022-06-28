package com.productdock.library.search.adapter.in.web.dto;

import com.productdock.library.search.domain.BookStatus;
import lombok.ToString;

import java.util.List;

@ToString
public class BookDto {

    public String id;
    public String title;
    public String author;
    public String cover;
    public List<Record> records;
    public Rating rating = new Rating();

    @ToString
    public static class Record {

        public String email;
        public BookStatus status;
    }

    @ToString
    public static class Rating {

        public Double score;
        public Integer count;
    }
}
