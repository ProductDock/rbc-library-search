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

    public void update(BookChanges updater) {
        switch (updater.getField()){
            case RECORDS -> rentalState.setRecords((List<RentalState.Record>) updater.getValue());
            case AVAILABLE_BOOK_COUNT -> rentalState.setAvailableBooksCount((Integer) updater.getValue());
            case RATING -> setRating((Rating) updater.getValue());
            case RECOMMENDED -> setRecommended((Boolean) updater.getValue());
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
