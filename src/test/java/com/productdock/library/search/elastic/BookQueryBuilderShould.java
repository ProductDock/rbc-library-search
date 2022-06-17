package com.productdock.library.search.elastic;

import com.jayway.jsonpath.JsonPath;
import org.junit.jupiter.api.Test;

import java.util.List;
import java.util.Optional;

import static com.productdock.library.search.elastic.BookQueryBuilder.bookQueryBuilder;
import static java.util.stream.Collectors.toList;
import static java.util.stream.Stream.of;
import static org.assertj.core.api.Assertions.assertThat;

class BookQueryBuilderShould {

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
                .read("$['bool']['must'][*]['bool']['should'][*]['match']['topics.name']['operator']");
        assertThat(topicOperators).containsAll(expectedOperators);

    }

    private void assertThatTopicsAreMatching(List<String> topics, String queryString) {
        List<String> topicQueries = JsonPath.parse(queryString)
                .read("$['bool']['must'][*]['bool']['should'][*]['match']['topics.name']['query']");
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

        assertThat(read).isNotEmpty();
        assertThat(read).doesNotContain("should");
    }

    @Test
    void buildQueryWhenSearchingByTopics() {
        var searchText = Optional.of("SEARCH TEXT");

        var query = bookQueryBuilder().andSearchByTitleAndAuthor(searchText).build().toString();

        assertThatTitleIsMatching(searchText.get(), query);
        assertThatAuthorIsMatching(searchText.get(), query);
    }

    private void assertThatTitleIsMatching(String searchText, String queryString) {
        List<String> titleQuery = JsonPath.parse(queryString)
                .read("$['bool']['must'][*]['bool']['should'][*]['fuzzy']['title']['value']");
        assertThat(titleQuery).contains(searchText);
    }

    private void assertThatAuthorIsMatching(String searchText, String queryString) {
        List<String> authorQuery = JsonPath.parse(queryString)
                .read("$['bool']['must'][*]['bool']['should'][*]['fuzzy']['author']['value']");
        assertThat(authorQuery).contains(searchText);
    }
}
