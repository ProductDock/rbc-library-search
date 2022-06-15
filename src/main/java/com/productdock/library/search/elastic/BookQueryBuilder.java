package com.productdock.library.search.elastic;

import com.productdock.library.search.book.BookSearchFields;
import org.elasticsearch.index.query.BoolQueryBuilder;
import org.elasticsearch.index.query.QueryBuilders;

import java.util.List;
import java.util.Optional;

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

    public BookQueryBuilder andSearchByTitleAndAuthor(String searchText) {
        if (searchText != null && !searchText.isEmpty()) {
            var searchByTitleAndAuthorCriteria = new BoolQueryBuilder();
            searchByTitleAndAuthorCriteria.should(QueryBuilders.fuzzyQuery(BookSearchFields.TITLE.label, searchText).maxExpansions(10));
            searchByTitleAndAuthorCriteria.should(QueryBuilders.fuzzyQuery(BookSearchFields.AUTHOR.label, searchText).maxExpansions(10));
            and(searchByTitleAndAuthorCriteria);
        }
        return this;
    }

    private void and(BoolQueryBuilder andBuilder) {
        builder.must(andBuilder);
    }

    public BoolQueryBuilder build() {
        return builder;
    }

}
