package com.productdock.library.search;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.data.elasticsearch.annotations.Field;
import org.springframework.data.elasticsearch.annotations.FieldType;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class Topic {

    @Field(type = FieldType.Text)
    private String id;

    @Field(type = FieldType.Text)
    private String name;
}
