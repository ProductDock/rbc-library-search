package com.productdock.library.search.elastic.document;

import lombok.*;
import org.springframework.data.annotation.Id;
import org.springframework.data.elasticsearch.annotations.Document;
import org.springframework.data.elasticsearch.annotations.Field;
import org.springframework.data.elasticsearch.annotations.FieldType;

import java.util.ArrayList;
import java.util.Set;

@Getter
@Setter
@AllArgsConstructor
@Builder
@Document(indexName = "book_index")
public class BookDocument {

    @Id
    @Field(type = FieldType.Text)
    private String bookId;

    @Field(type = FieldType.Text)
    private String title;

    @Field(type = FieldType.Text)
    private String author;

    @Field(type = FieldType.Text)
    private String cover;

    @Singular
    @Field(type = FieldType.Nested, includeInParent = true)
    private Set<Topic> topics;

    @Field(type = FieldType.Nested, includeInParent = true)
    private BookStatusWrapper bookStatusWrapper;

    public BookDocument() {
        bookStatusWrapper = new BookStatusWrapper(0, new ArrayList<>());
    }

}
