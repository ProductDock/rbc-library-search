package com.productdock.library.search.elastic;

import com.productdock.library.search.book.BookSearchFields;
import org.elasticsearch.index.query.BoolQueryBuilder;
import org.elasticsearch.index.query.QueryBuilders;

import java.util.List;
import java.util.Optional;

public class BookQueryBuilder {

    private final BoolQueryBuilder builder = new BoolQueryBuilder();


    public static BookQueryBuilder bookQueryBuilder() {
        return new BookQueryBuilder();
    }

    public BookQueryBuilder withTopicsCriteria(Optional<List<String>> topicsFilter) {
        topicsFilter.ifPresent(this::addTopicsCriteria);
        return this;
    }

    private void addTopicsCriteria(List<String> topics) {
        for (String topic : topics) {
            builder.should(QueryBuilders.matchQuery(BookSearchFields.TOPICS_NAME.label, topic));
        }
    }

    public BookQueryBuilder withRecommendation(boolean recommendation) {
        if (recommendation) {
            builder.must(QueryBuilders.matchQuery(BookSearchFields.RECOMMENDATION_NAME.label, true));
        }
        return this;
    }

    public BoolQueryBuilder build() {
        return builder;
    }
}
