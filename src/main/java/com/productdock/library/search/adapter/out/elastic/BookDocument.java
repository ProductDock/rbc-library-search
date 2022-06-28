package com.productdock.library.search.adapter.out.elastic;

import com.productdock.library.search.domain.BookStatus;
import lombok.*;
import org.springframework.data.annotation.Id;
import org.springframework.data.elasticsearch.annotations.Document;
import org.springframework.data.elasticsearch.annotations.Field;
import org.springframework.data.elasticsearch.annotations.FieldType;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

@ToString
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
    private RentalState rentalState;

    @Field(type = FieldType.Nested, includeInParent = true)
    private Rating rating;

    @Field(type = FieldType.Boolean)
    private boolean recommended;

    public BookDocument() {
        rentalState = new RentalState(0, new ArrayList<>());
    }

    @Data
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class Rating {

        @Field(type = FieldType.Double)
        private double score;

        @Field(type = FieldType.Integer)
        private int count;
    }

    @Data
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class Topic {

        @Field(type = FieldType.Text)
        private String id;

        @Field(type = FieldType.Text)
        private String name;
    }

    @Data
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class RentalState {

        @Field(type = FieldType.Integer)
        private int availableBooksCount;

        @Singular
        @Field(type = FieldType.Nested, includeInParent = true)
        private List<Record> records;

        @Data
        @Builder
        @NoArgsConstructor
        @AllArgsConstructor
        @RequiredArgsConstructor
        public static class Record {

            @Field(type = FieldType.Text)
            private String email;

            @NonNull
            @Field(type = FieldType.Text)
            private BookStatus status;
        }
    }



}
