package com.productdock.library.search;

import lombok.NonNull;
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
        topicsFilter.ifPresent(list -> addTopicsCriteria(list));
        return this;
    }

    private void addTopicsCriteria(List<String> topics) {
        for (String topic : topics) {
            builder.should(QueryBuilders.matchQuery(SearchFields.TOPICS_NAME.label, topic));
        }
    }

    public BoolQueryBuilder build() {
        return builder;
    }
}