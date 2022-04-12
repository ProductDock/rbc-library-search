package com.productdock.library.search;

import lombok.NonNull;
import org.elasticsearch.index.query.BoolQueryBuilder;
import org.elasticsearch.index.query.QueryBuilders;

import java.util.List;

public class QueryBuilderDecorator {

    @NonNull
    private BoolQueryBuilder builder;

    public QueryBuilderDecorator(BoolQueryBuilder builder) {
        this.builder = builder;
    }

    public void addTopicsCriteria(List<String> topics) {
        for (String topic : topics) {
            builder.should(QueryBuilders.matchQuery(SearchFields.TOPICS_NAME, topic));
        }
    }
}
