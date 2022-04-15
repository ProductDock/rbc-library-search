package com.productdock.library.search;

import com.productdock.library.search.config.ElasticsearchTestContainer;
import org.junit.jupiter.api.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.elasticsearch.core.ElasticsearchOperations;
import org.springframework.data.elasticsearch.core.IndexOperations;
import org.springframework.kafka.test.context.EmbeddedKafka;
import org.springframework.test.web.servlet.MockMvc;
import org.testcontainers.elasticsearch.ElasticsearchContainer;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;

import static com.productdock.library.search.data.provider.BookIndexMother.defaultBook;
import static com.productdock.library.search.data.provider.BookIndexMother.defaultBookBuilder;
import static org.hamcrest.Matchers.hasSize;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;

@Testcontainers
@SpringBootTest
@AutoConfigureMockMvc
@EmbeddedKafka(partitions = 1, brokerProperties = { "listeners=PLAINTEXT://localhost:9091", "port=9091" })
public class SearchApiTest {

    public static final int RESULTS_PAGE_SIZE = 19;
    public static final String FIRST_PAGE = "0";
    public static final String SECOND_PAGE = "1";

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private BookService bookService;

    @Autowired
    private BookIndexRepository bookIndexRepository;

    @Autowired
    private ElasticsearchOperations elasticsearchOperations;

    @Container
    private static ElasticsearchContainer elasticsearchContainer = new ElasticsearchTestContainer();

    @BeforeAll
    static void setUp() {
        elasticsearchContainer.start();
    }

    @AfterAll
    static void destroy() {
        elasticsearchContainer.stop();
    }

    @BeforeEach
    void testIsContainerRunning() {
        assertTrue(elasticsearchContainer.isRunning());
        recreateIndex();
    }

    private void recreateIndex() {
        IndexOperations indexOperations = elasticsearchOperations.indexOps(BookIndex.class);
        if (indexOperations.exists()) {
            indexOperations.delete();
        }
        indexOperations.createWithMapping();
        indexOperations.refresh();
    }

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
            topic.setName(topicName);
            var book = defaultBookBuilder().title(title).topic(topic).build();

            bookService.save(book);
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
            bookIndexRepository.save(book);
        }

        private void givenSecondPageOfResults() {
            var book = defaultBookBuilder().title("Second Page Title").build();
            bookIndexRepository.save(book);
        }

    }
}
