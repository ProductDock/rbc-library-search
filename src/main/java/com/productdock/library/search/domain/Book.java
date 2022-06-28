package com.productdock.library.search.domain;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;
import java.util.Set;

@Data
public class Book {
    private String id;
    private String title;
    private String author;
    private String cover;
    private Book.RentalState rentalState;
    private Book.Rating rating = new Book.Rating();
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
    public static class RentalState {
        private Integer availableBooksCount;
        private List<Record> records;

        @Data
        public static class Record {

            private String email;
            private BookStatus status;
        }
    }

    @Data
    @AllArgsConstructor
    @NoArgsConstructor
    public static class Rating {

        private Double score;
        private Integer count;
    }

    @Data
    public static class Topic {

        private String id;

        private String name;
    }
}
