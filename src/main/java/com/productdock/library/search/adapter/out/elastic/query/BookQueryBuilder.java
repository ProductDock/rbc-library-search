package com.productdock.library.search.adapter.out.elastic.query;

import org.elasticsearch.index.query.BoolQueryBuilder;
import org.elasticsearch.index.query.MultiMatchQueryBuilder;
import org.elasticsearch.index.query.QueryBuilders;

import java.util.List;
import java.util.Optional;

import static org.elasticsearch.index.query.QueryBuilders.multiMatchQuery;

public class BookQueryBuilder {

    private BoolQueryBuilder builder = new BoolQueryBuilder();


    public static BookQueryBuilder bookQueryBuilder() {
        return new BookQueryBuilder();
    }

    public BookQueryBuilder withTopicsCriteria(Optional<List<String>> topicsFilter) {
        topicsFilter.ifPresent(this::addTopicsCriteria);
        return this;
    }

    private void addTopicsCriteria(List<String> topics) {
        var topicsCriteria = new BoolQueryBuilder();

        for (String topic : topics) {
            topicsCriteria.should(QueryBuilders.matchQuery(BookSearchFields.TOPICS.label, topic));
        }
        and(topicsCriteria);
    }

    public BookQueryBuilder andRecommendation(boolean recommended) {
        if (recommended) {
            var recommendCriteria = new BoolQueryBuilder();
            recommendCriteria.must(QueryBuilders.matchQuery(BookSearchFields.RECOMMENDED.label, true));
            and(recommendCriteria);
        }
        return this;
    }

    public BookQueryBuilder andSearchByTitleAndAuthor(Optional<String> searchText) {
        searchText.ifPresent(this::addSearchByTitleAndAuthorCriteria);
        return this;
    }

    public void addSearchByTitleAndAuthorCriteria(String searchText) {
        var searchByTitleAndAuthorCriteria = new BoolQueryBuilder();
        searchByTitleAndAuthorCriteria.should(multiMatchQuery(searchText)
                .field(BookSearchFields.TITLE.label)
                .field(BookSearchFields.AUTHOR.label)
                .type(MultiMatchQueryBuilder.Type.PHRASE_PREFIX)
                .slop(15));
        and(searchByTitleAndAuthorCriteria);
    }

    private void and(BoolQueryBuilder andBuilder) {
        builder.must(andBuilder);
    }

    public BoolQueryBuilder build() {
        return builder;
    }

}
