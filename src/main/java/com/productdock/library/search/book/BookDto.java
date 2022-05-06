package com.productdock.library.search.book;

import java.util.List;

public class BookDto {

    public String id;
    public String title;
    public String author;
    public String cover;
    public List<Record> records;

    public static class Record {

        public String email;
        public BookStatus status;
    }
}
