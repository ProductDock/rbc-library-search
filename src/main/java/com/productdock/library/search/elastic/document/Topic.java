package com.productdock.library.search.elastic.document;

import lombok.*;
import org.springframework.data.elasticsearch.annotations.Field;
import org.springframework.data.elasticsearch.annotations.FieldType;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Topic {

    @Field(type = FieldType.Text)
    private String id;

    @Field(type = FieldType.Text)
    private String name;
}
