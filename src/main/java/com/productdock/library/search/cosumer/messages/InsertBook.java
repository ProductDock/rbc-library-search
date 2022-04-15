package com.productdock.library.search.cosumer.messages;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.NoArgsConstructor;

import java.util.List;

@AllArgsConstructor
@NoArgsConstructor
@Builder
public class InsertBook {

    public String id;
    public String title;
    public String cover;
    public String author;
    public List<BookTopic> topics;
}

