package com.productdock.library.search.integration;

import com.productdock.library.search.book.BookDocumentRepository;
import com.productdock.library.search.elastic.document.BookDocument;
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
class BookSearchApiTest extends IntegrationTestBase {

    public static final int RESULTS_PAGE_SIZE = 19;
    public static final String FIRST_PAGE = "0";
    public static final String SECOND_PAGE = "1";
    public static final boolean RECOMMENDED_BOOK = true;
    public static final boolean NOT_RECOMMENDED_BOOK = false;

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private BookDocumentRepository bookDocumentRepository;


    @Nested
    class SearchWithTopics {

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

        private List<BookDocument.Topic> createTopicEntitiesWithNames(String... topicNames) {
            return stream(topicNames)
                    .map(topicName -> BookDocument.Topic.builder().name(topicName).build())
                    .toList();
        }
    }

    @Nested
    class SearchWithRecommendations {

        @Test
        void getFirstPage_whenThereAreResults() throws Exception {
            givenBook("Recommended book", RECOMMENDED_BOOK);
            givenBook("Not recommended book", NOT_RECOMMENDED_BOOK);

            mockMvc.perform(get("/api/search")
                            .param("page", FIRST_PAGE)
                            .param("recommended", "true"))
                    .andExpect(status().isOk())
                    .andExpect(jsonPath("$.count").value(1))
                    .andExpect(jsonPath("$.books").value(hasSize(1)))
                    .andExpect(jsonPath("$.books[0].title").value("Recommended book"));
        }

        private void givenBook(String title, boolean recommended) {
            var book = defaultBookDocumentBuilder()
                    .title(title)
                    .recommended(recommended)
                    .build();
            bookDocumentRepository.save(book);
        }
    }

    @Nested
    class SearchWithFilters {

        @Test
        void getFirstPage_whenThereAreResults() throws Exception {
            givenABookWithTopicAndRecommendation("Title Product", "PRODUCT", NOT_RECOMMENDED_BOOK);
            givenABookWithTopicAndRecommendation("Title Marketing", "MARKETING", NOT_RECOMMENDED_BOOK);
            givenABookWithTopicAndRecommendation("Title Design", "DESIGN", RECOMMENDED_BOOK);
            givenABookWithTopicAndRecommendation("Title Another Product", "PRODUCT", RECOMMENDED_BOOK);

            mockMvc.perform(get("/api/search")
                            .param("page", FIRST_PAGE)
                            .param("topics", "DESIGN")
                            .param("topics", "MARKETING")
                            .param("recommended", "true"))
                    .andExpect(status().isOk())
                    .andExpect(jsonPath("$.count").value(1))
                    .andExpect(jsonPath("$.books").value(hasSize(1)))
                    .andExpect(jsonPath("$.books[0].title").value("Title Design"));
        }

        private void givenABookWithTopicAndRecommendation(String title, String topicName, boolean recommended) {
            var topic = BookDocument.Topic.builder().name(topicName).build();
            var book = defaultBookDocumentBuilder()
                    .title(title)
                    .topics(List.of(topic))
                    .recommended(recommended)
                    .build();
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
            var book = defaultBookDocument();
            bookDocumentRepository.save(book);
        }

        private void givenSecondPageOfResults() {
            var book = defaultBookDocumentBuilder().title("Second Page Title").build();
            bookDocumentRepository.save(book);
        }

    }

    @Nested
    class SearchWithText {

        @Test
        void getBookSuggestions_whenThereAreSearchResults() throws Exception {
            givenABook("Title Product", "Product Guru");
            givenABook("Title Marketing", "John Doe");
            givenABook("Title Design", "Product Guru");
            givenABook("Title Another Product", "Product Guru");

            mockMvc.perform(get("/api/search/suggestions")
                            .param("searchText", "product"))

                    .andExpect(status().isOk())
                    .andExpect(jsonPath("$").value(hasSize(3)));
        }

        @Test
        void getBookSuggestionsForPartialTitleSearchText_whenThereAreSearchResults() throws Exception {
            givenABook("Title Product", "John Doe");
            givenABook("Title Marketing", "John Doe");

            mockMvc.perform(get("/api/search/suggestions")
                            .param("searchText", "prod"))

                    .andExpect(status().isOk())
                    .andExpect(jsonPath("$").value(hasSize(1)));
        }

        @Test
        void getBookSuggestionsForPartialAuthorSearchText_whenThereAreSearchResults() throws Exception {
            givenABook("Title Product", "John Doe");
            givenABook("Title Marketing", "John Doe");

            mockMvc.perform(get("/api/search/suggestions")
                            .param("searchText", "joh"))

                    .andExpect(status().isOk())
                    .andExpect(jsonPath("$").value(hasSize(2)));
        }

        @Test
        void getZeroBookSuggestions_whenThereAreNoSearchResults() throws Exception {
            givenABook("Title Product", "Product Guru");
            givenABook("Title Marketing", "John Doe");
            givenABook("Title Design", "Product Guru");
            givenABook("Title Another Product", "Product Guru");

            mockMvc.perform(get("/api/search/suggestions")
                            .param("searchText", "Software"))

                    .andExpect(status().isOk())
                    .andExpect(jsonPath("$").value(hasSize(0)));
        }

        private void givenABook(String title, String author) {
            var book = defaultBookDocumentBuilder()
                    .title(title)
                    .author(author)
                    .build();
            bookDocumentRepository.save(book);
        }

        @Test
        void getRecommendedBooks_whenThereAreSearchResultsWithFilters() throws Exception {
            givenABookWithTopicAndRecommendation("Title Product", "John Doe", "PRODUCT", NOT_RECOMMENDED_BOOK);
            givenABookWithTopicAndRecommendation("Title Marketing", "John Doe", "MARKETING", NOT_RECOMMENDED_BOOK);
            givenABookWithTopicAndRecommendation("Title Design", "John Doe", "DESIGN", RECOMMENDED_BOOK);
            givenABookWithTopicAndRecommendation("Title Another Product", "John Doe", "PRODUCT", RECOMMENDED_BOOK);

            mockMvc.perform(get("/api/search")
                            .param("page", FIRST_PAGE)
                            .param("topics", "DESIGN")
                            .param("topics", "MARKETING")
                            .param("recommended", "true")
                            .param("searchText", "Design"))
                    .andExpect(status().isOk())
                    .andExpect(jsonPath("$.count").value(1))
                    .andExpect(jsonPath("$.books").value(hasSize(1)))
                    .andExpect(jsonPath("$.books[0].title").value("Title Design"));
        }

        @Test
        void getNoRecommendedBooks_whenThereAreNoSearchResultsWithFilters() throws Exception {
            givenABookWithTopicAndRecommendation("Title Product", "John Doe", "PRODUCT", NOT_RECOMMENDED_BOOK);
            givenABookWithTopicAndRecommendation("Title Marketing", "John Doe", "MARKETING", NOT_RECOMMENDED_BOOK);
            givenABookWithTopicAndRecommendation("Title Design", "John Doe", "DESIGN", RECOMMENDED_BOOK);
            givenABookWithTopicAndRecommendation("Title Another Product", "John Doe", "PRODUCT", RECOMMENDED_BOOK);

            mockMvc.perform(get("/api/search")
                            .param("page", FIRST_PAGE)
                            .param("topics", "DESIGN")
                            .param("topics", "MARKETING")
                            .param("recommended", "true")
                            .param("searchText", "Software"))
                    .andExpect(status().isOk())
                    .andExpect(jsonPath("$.count").value(0))
                    .andExpect(jsonPath("$.books").value(hasSize(0)));
        }

        @Test
        void getFirstPage_whenThereAreSearchResults() throws Exception {
            givenABookWithTopicAndRecommendation("Title Product", "Product Guru", "PRODUCT", NOT_RECOMMENDED_BOOK);
            givenABookWithTopicAndRecommendation("Title Marketing", "Product Guru", "MARKETING", NOT_RECOMMENDED_BOOK);
            givenABookWithTopicAndRecommendation("Title Design", "John Doe", "DESIGN", RECOMMENDED_BOOK);
            givenABookWithTopicAndRecommendation("Title Design", "John Doe", "PRODUCT", RECOMMENDED_BOOK);

            mockMvc.perform(get("/api/search")
                            .param("page", FIRST_PAGE)
                            .param("searchText", "Product"))
                    .andExpect(status().isOk())
                    .andExpect(jsonPath("$.count").value(2))
                    .andExpect(jsonPath("$.books").value(hasSize(2)))
                    .andExpect(jsonPath("$.books[0].title").value("Title Product"))
                    .andExpect(jsonPath("$.books[0].author").value("Product Guru"));
        }

        @Test
        void getFirstPage_whenThereAreNoSearchResults() throws Exception {
            givenABookWithTopicAndRecommendation("Title Product", "Product Guru", "PRODUCT", NOT_RECOMMENDED_BOOK);
            givenABookWithTopicAndRecommendation("Title Marketing", "Product Guru", "MARKETING", NOT_RECOMMENDED_BOOK);
            givenABookWithTopicAndRecommendation("Title Design", "John Doe", "DESIGN", RECOMMENDED_BOOK);
            givenABookWithTopicAndRecommendation("Title Design", "John Doe", "PRODUCT", RECOMMENDED_BOOK);

            mockMvc.perform(get("/api/search")
                            .param("page", FIRST_PAGE)
                            .param("searchText", "Software"))
                    .andExpect(status().isOk())
                    .andExpect(jsonPath("$.count").value(0))
                    .andExpect(jsonPath("$.books").value(hasSize(0)));
        }

        private void givenABookWithTopicAndRecommendation(String title, String author, String topicName, boolean recommended) {
            var topic = BookDocument.Topic.builder().name(topicName).build();
            var book = defaultBookDocumentBuilder()
                    .title(title)
                    .author(author)
                    .topics(List.of(topic))
                    .recommended(recommended)
                    .build();
            bookDocumentRepository.save(book);
        }

    }
}
