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
        Optional<List<String>> topics = Optional.of(
                of("MARKETING", "DEVELOPMENT").collect(toList())
        );

        var queryString = bookQueryBuilder().withTopicsCriteria(topics).build().toString();

        assertThatTopicsAreMatching(topics, queryString);
        assertThatOperatorsAreMatching(of("OR", "OR").toList(), queryString);
    }

    private void assertThatOperatorsAreMatching(List<String> expectedOperators, String queryString) {
        List<String> topicOperators = JsonPath.parse(queryString)
                .read("$['bool']['should'][*]['match']['topics.name']['operator']");
        assertThat(topicOperators).containsAll(expectedOperators);

    }

    private void assertThatTopicsAreMatching(Optional<List<String>> topics, String queryString) {
        List<String> topicQueries = JsonPath.parse(queryString)
                .read("$['bool']['should'][*]['match']['topics.name']['query']");
        assertThat(topicQueries).containsAll(topics.get());
    }

    @Test
    void buildQueryWithoutTopics() {
        Optional<List<String>> topics = Optional.ofNullable(null);

        var queryString = bookQueryBuilder().withTopicsCriteria(topics).build().toString();
        assertThatNoFiltersExist(queryString);
    }

    private void assertThatNoFiltersExist(String queryString) {
        List<String> read = JsonPath.parse(queryString).read("$['bool'][*]");
        assertThat(read).doesNotContain("should");
    }
}
