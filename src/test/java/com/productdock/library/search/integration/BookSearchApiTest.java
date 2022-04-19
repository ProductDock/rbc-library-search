package com.productdock.library.search.integration;

import com.productdock.library.search.book.BookDocumentRepository;
import com.productdock.library.search.elastic.document.Topic;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.web.servlet.MockMvc;

import java.util.List;

import static com.productdock.library.search.data.provider.BookDocumentMother.defaultBookDocument;
import static com.productdock.library.search.data.provider.BookDocumentMother.defaultBookDocumentBuilder;
import static java.util.Arrays.stream;
import static org.hamcrest.Matchers.containsInAnyOrder;
import static org.hamcrest.Matchers.hasSize;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
public class BookSearchApiTest extends IntegrationTestBase {

    public static final int RESULTS_PAGE_SIZE = 19;
    public static final String FIRST_PAGE = "0";
    public static final String SECOND_PAGE = "1";

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private BookDocumentRepository bookDocumentRepository;


    @Nested
    class SearchWithFilters {

        @Test
        void getSecondPage_whenEmptyResults() throws Exception {
            mockMvc.perform(get("/api/search")
                            .param("page", SECOND_PAGE)
                            .param("topics", "MARKETING")
                            .param("topics", "DESIGN"))
                    .andExpect(status().isOk())
                    .andExpect(content().json("{\"count\":0,\"books\":[]}"));
        }

        @Test
        void getFirstPage_whenThereAreResults() throws Exception {
            givenABookBelongingToTopic("Title Product", "PRODUCT");
            givenABookBelongingToTopic("Title Marketing", "MARKETING");
            givenABookBelongingToTopic("Title Design", "DESIGN");
            givenABookBelongingToTopic("Title Product & Marketing", "PRODUCT", "MARKETING");

            mockMvc.perform(get("/api/search")
                            .param("page", FIRST_PAGE)
                            .param("topics", "DESIGN")
                            .param("topics", "MARKETING"))
                    .andExpect(status().isOk())
                    .andExpect(jsonPath("$.count").value(3))
                    .andExpect(jsonPath("$.books").value(hasSize(3)))
                    .andExpect(jsonPath("$.books[*].title",
                            containsInAnyOrder("Title Marketing", "Title Design", "Title Product & Marketing")));
        }

        private void givenABookBelongingToTopic(String title, String... topicNames) {
            var topics = createTopicEntitiesWithNames(topicNames);
            var book = defaultBookDocumentBuilder().title(title).topics(topics).build();
            bookDocumentRepository.save(book);
        }

        private List<Topic> createTopicEntitiesWithNames(String... topicNames) {
            return stream(topicNames)
                    .map(topicName -> Topic.builder().name(topicName).build())
                    .toList();
        }
    }

    @Nested
    class GetBooksWithPagination {

        @Test
        void getFirstPage_whenEmptyResults() throws Exception {
            mockMvc.perform(get("/api/search").param("page", FIRST_PAGE))
                    .andExpect(status().isOk())
                    .andExpect(content().json("{\"count\":0,\"books\":[]}"));
        }

        @Test
        void getSecondPage_whenThereAreResults() throws Exception {
            givenFirstPageOfResults();
            givenSecondPageOfResults();

            mockMvc.perform(get("/api/search").param("page", SECOND_PAGE))
                    .andExpect(status().isOk())
                    .andExpect(jsonPath("$.count").value(19))
                    .andExpect(jsonPath("$.books").value(hasSize(1)))
                    .andExpect(jsonPath("$.books[0].id").exists())
                    .andExpect(jsonPath("$.books[0].title").value("Second Page Title"));
        }

        private void givenFirstPageOfResults() {
            for (int i = 0; i < RESULTS_PAGE_SIZE - 1; i++) {
                givenAnyBook();
            }
        }

        private void givenAnyBook() {
            var book = defaultBookDocument();
            bookDocumentRepository.save(book);
        }

        private void givenSecondPageOfResults() {
            var book = defaultBookDocumentBuilder().title("Second Page Title").build();
            bookDocumentRepository.save(book);
        }

    }
}