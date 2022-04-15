package com.productdock.library.search.elastic.document;

import lombok.NoArgsConstructor;
import org.springframework.data.elasticsearch.annotations.Field;
import org.springframework.data.elasticsearch.annotations.FieldType;

@NoArgsConstructor
public class Topic {

    @Field(type = FieldType.Text)
    public String id;

    @Field(type = FieldType.Text)
    public String name;
}
