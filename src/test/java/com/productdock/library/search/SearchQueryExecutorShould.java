package com.productdock.library.search;

import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.elasticsearch.core.ElasticsearchOperations;
import org.springframework.data.elasticsearch.core.query.Query;

import java.util.List;
import java.util.Optional;
import java.util.stream.Stream;

import static java.util.stream.Stream.of;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;


import static java.util.stream.Collectors.toList;

@ExtendWith(MockitoExtension.class)
public class SearchQueryExecutorShould {

    @InjectMocks
    private SearchQueryExecutor searchQueryExecutor;

    @Mock
    private ElasticsearchOperations elasticsearchOperations;

    public static final int FIRST_PAGE = 0;

    @ParameterizedTest
    @MethodSource("parametersProvider")
    void executeSearchByTopics(Optional<List<String>> topics) {
        searchQueryExecutor.execute(topics, FIRST_PAGE);
        verify(elasticsearchOperations, times(1)).search(any(Query.class), any());
    }

    static Stream<Arguments> parametersProvider() {
        Optional<List<String>> topics = Optional.of(
                of("MARKETING", "DEVELOPMENT").collect(toList())
        );

        return Stream.of(
                Arguments.of(topics),
                Arguments.of(Optional.ofNullable(null)));
    }
}
