package com.productdock.library.search;

import com.jayway.jsonpath.JsonPath;
import org.junit.jupiter.api.Test;

import java.util.List;
import java.util.Optional;

import static com.productdock.library.search.BookQueryBuilder.bookQueryBuilder;
import static java.util.stream.Stream.of;

import static org.assertj.core.api.Assertions.assertThat;

import static java.util.stream.Collectors.toList;

public class BookQueryBuilderShould {

    @Test
    void buildQueryWhenFilteringByTopics() {
        var topics = topicsOf("MARKETING", "DEVELOPMENT");

        var query = bookQueryBuilder().withTopicsCriteria(topics).build().toString();

        assertThatTopicsAreMatching(topics.get(), query);
        assertThatOperatorsAreMatching(of("OR", "OR").toList(), query);
    }

    private Optional<List<String>> topicsOf(String... topics) {
        return Optional.of(of(topics).collect(toList()));
    }

    private void assertThatOperatorsAreMatching(List<String> expectedOperators, String queryString) {
        List<String> topicOperators = JsonPath.parse(queryString)
                .read("$['bool']['should'][*]['match']['topics.name']['operator']");
        assertThat(topicOperators).containsAll(expectedOperators);

    }

    private void assertThatTopicsAreMatching(List<String> topics, String queryString) {
        List<String> topicQueries = JsonPath.parse(queryString)
                .read("$['bool']['should'][*]['match']['topics.name']['query']");
        assertThat(topicQueries).containsAll(topics);
    }

    @Test
    void buildQueryWithoutTopics() {
        Optional<List<String>> topics = Optional.ofNullable(null);

        var query = bookQueryBuilder().withTopicsCriteria(topics).build().toString();

        assertThatNoFiltersExist(query);
    }

    private void assertThatNoFiltersExist(String queryString) {
        List<String> read = JsonPath.parse(queryString).read("$['bool'][*]");
        assertThat(read).doesNotContain("should");
    }
}
