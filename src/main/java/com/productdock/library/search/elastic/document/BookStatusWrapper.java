package com.productdock.library.search.elastic.document;

import lombok.*;
import org.springframework.data.elasticsearch.annotations.Field;
import org.springframework.data.elasticsearch.annotations.FieldType;

import java.util.List;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class BookStatusWrapper {

    @Field(type = FieldType.Integer)
    private int availableBooksCount;

    @Singular
    @Field(type = FieldType.Nested, includeInParent = true)
    private List<Record> records;
}
