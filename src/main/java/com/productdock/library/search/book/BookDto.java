package com.productdock.library.search.book;

import java.io.Serializable;
import java.util.List;

public class BookDto implements Serializable {

    public String id;
    public String title;
    public String author;
    public String cover;
    public List<RecordDto> records;

}
