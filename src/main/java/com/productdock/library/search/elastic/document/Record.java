package com.productdock.library.search.elastic.document;

import com.productdock.library.search.book.BookStatus;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.elasticsearch.annotations.Field;
import org.springframework.data.elasticsearch.annotations.FieldType;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Record {

    @Field(type = FieldType.Text)
    private String email;

    @Field(type = FieldType.Text)
    private BookStatus status;
}
