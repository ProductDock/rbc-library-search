package com.productdock.library.search.domain;

import lombok.*;

import java.util.List;
import java.util.Set;

@Builder
@ToString
@Getter
@Setter
@AllArgsConstructor
public class Book {
    private String id;
    private String title;
    private String author;
    private String cover;
    private Book.RentalState rentalState;
    @Builder.Default
    private Book.Rating rating = new Book.Rating();
    @Singular
    private Set<Topic> topics;
    private boolean recommended;

    public void update(BookChanges changes) {
        switch (changes.getField()){
            case RENTAL_RECORDS -> rentalState.setRecords((List<RentalState.Record>) changes.getValue());
            case AVAILABLE_BOOK_COUNT -> rentalState.setAvailableBooksCount((Integer) changes.getValue());
            case RATING -> setRating((Rating) changes.getValue());
            case RECOMMENDED -> setRecommended((Boolean) changes.getValue());
        }
    }

    @Data
    @Builder
    public static class RentalState {
        private Integer availableBooksCount;
        private List<Record> records;

        @Data
        @RequiredArgsConstructor
        @NoArgsConstructor
        @AllArgsConstructor
        @Builder
        public static class Record {

            private String email;

            @NonNull
            private BookStatus status;
        }
    }

    @Data
    @AllArgsConstructor
    @NoArgsConstructor
    @Builder
    public static class Rating {

        private Double score;
        private Integer count;
    }

    @Data
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class Topic {

        private String id;

        private String name;
    }
}
