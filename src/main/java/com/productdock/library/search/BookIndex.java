package com.productdock.library.search;

import lombok.*;
import org.springframework.data.annotation.Id;
import org.springframework.data.elasticsearch.annotations.Document;
import org.springframework.data.elasticsearch.annotations.Field;
import org.springframework.data.elasticsearch.annotations.FieldType;

import java.util.Set;

@Builder
@Getter
@Setter
@AllArgsConstructor
@Document(indexName = "book_index")
public class BookIndex {

    @Id
    @Field(type = FieldType.Text)
    private String id;

    @Field(type = FieldType.Text)
    private String title;

    @Field(type = FieldType.Text)
    private String author;

    @Field(type = FieldType.Text)
    private String cover;

    @Singular
    @Field(type = FieldType.Nested, includeInParent = true)
    private Set<Topic> topics;
}
