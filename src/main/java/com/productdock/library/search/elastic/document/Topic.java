package com.productdock.library.search.elastic.document;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.NoArgsConstructor;
import org.springframework.data.elasticsearch.annotations.Field;
import org.springframework.data.elasticsearch.annotations.FieldType;

@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Topic {

    @Field(type = FieldType.Text)
    public String id;

    @Field(type = FieldType.Text)
    public String name;
}
