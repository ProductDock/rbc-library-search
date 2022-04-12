package com.productdock.library.search;

import lombok.Getter;
import lombok.NonNull;
import org.elasticsearch.index.query.BoolQueryBuilder;
import org.elasticsearch.index.query.QueryBuilders;

import java.util.List;

public class QueryBuilderDecorator extends BoolQueryBuilder {

    @NonNull
    @Getter
    private BoolQueryBuilder builder;

    public QueryBuilderDecorator() {
        builder = new BoolQueryBuilder();
    }

    public void addTopicsCriteria(List<String> topics) {
        for (String topic: topics) {
            builder.should(QueryBuilders.matchQuery(SearchFields.TOPICS_NAME, topic));
        }
    }
}
