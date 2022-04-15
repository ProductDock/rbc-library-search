package com.productdock.library.search.elastic.document;

import lombok.*;
import org.springframework.data.annotation.Id;
import org.springframework.data.elasticsearch.annotations.Document;
import org.springframework.data.elasticsearch.annotations.Field;
import org.springframework.data.elasticsearch.annotations.FieldType;

import java.util.Set;

@Builder
@Document(indexName = "book_index")
public class BookDocument {

    @Id
    @Field(type = FieldType.Text)
    public String id;

    @Field(type = FieldType.Text)
    public String title;

    @Field(type = FieldType.Text)
    public String author;

    @Field(type = FieldType.Text)
    public String cover;

    @Singular
    @Field(type = FieldType.Nested, includeInParent = true)
    public Set<Topic> topics;
}
