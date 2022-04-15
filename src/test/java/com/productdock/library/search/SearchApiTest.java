package com.productdock.library.search;

import com.productdock.library.search.elastic.document.Topic;
import org.junit.jupiter.api.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.web.servlet.MockMvc;

import static com.productdock.library.search.data.provider.BookDocumentMother.defaultBook;
import static com.productdock.library.search.data.provider.BookDocumentMother.defaultBookBuilder;
import static org.hamcrest.Matchers.hasSize;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;

@SpringBootTest
public class SearchApiTest extends IntegrationTestBase {

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
            givenABookBelongingToTopic("PRODUCT", "Title Product");
            givenABookBelongingToTopic("MARKETING", "Title Marketing");
            givenABookBelongingToTopic("DESIGN", "Title Design");

            mockMvc.perform(get("/api/search")
                            .param("page", FIRST_PAGE)
                            .param("topics", "MARKETING")
                            .param("topics", "DESIGN"))
                    .andExpect(status().isOk())
                    .andExpect(jsonPath("$.count").value(2))
                    .andExpect(jsonPath("$.books").value(hasSize(2)))
                    .andExpect(jsonPath("$.books[0].title").value("Title Marketing"))
                    .andExpect(jsonPath("$.books[1].title").value("Title Design"));
        }

        private void givenABookBelongingToTopic(String topicName, String title) {
            var topic = new Topic();
            topic.name = topicName;
            var book = defaultBookBuilder().title(title).topic(topic).build();

            bookDocumentRepository.save(book);
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
            var book = defaultBook();
            bookDocumentRepository.save(book);
        }

        private void givenSecondPageOfResults() {
            var book = defaultBookBuilder().title("Second Page Title").build();
            bookDocumentRepository.save(book);
        }

    }
}
