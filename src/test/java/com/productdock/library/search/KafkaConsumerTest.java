package com.productdock.library.search;


import com.productdock.library.search.config.ElasticsearchTestContainer;
import com.productdock.library.search.cosumer.KafkaConsumer;
import com.productdock.library.search.cosumer.messages.BookTopic;
import com.productdock.library.search.cosumer.messages.InsertBook;
import com.productdock.library.search.data.provider.KafkaTestProducer;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.elasticsearch.core.ElasticsearchOperations;
import org.springframework.data.elasticsearch.core.IndexOperations;
import org.springframework.kafka.test.context.EmbeddedKafka;
import org.springframework.test.web.servlet.MockMvc;
import org.testcontainers.elasticsearch.ElasticsearchContainer;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;

import java.util.ArrayList;
import java.util.List;

import static com.productdock.library.search.data.provider.BookIndexMother.defaultBookBuilder;
import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertTrue;

@Testcontainers
@SpringBootTest
@AutoConfigureMockMvc
@EmbeddedKafka(partitions = 1, brokerProperties = { "listeners=PLAINTEXT://localhost:9091", "port=9091" })
public class KafkaConsumerTest {
    
    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private KafkaTestProducer producer;

    @Autowired
    private BookIndexRepository bookIndexRepository;

    @Value("${spring.kafka.topic.insert-book-topic}")
    private String topic;

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

    @Test
    void shouldSaveBookIndex_whenMessageReceived() {
        BookTopic bt = BookTopic.builder().id("122").name("MARKETING").build();
        List<BookTopic> btl = new ArrayList<>();
        btl.add(bt);
        InsertBook insertBook = InsertBook.builder().id("123").title("Book title").author("Book author").cover("Cover").topics(btl).build();
        System.out.println(topic);
        var topic1 = new Topic();
        topic1.setName("LA");
        var book = defaultBookBuilder().title("PA").topic(topic1).build();
        bookIndexRepository.save(book);
        producer.send(topic, insertBook);

        bookIndexRepository.findAll().forEach(a -> System.out.println("JEDNOM"));
        assertThat(bookIndexRepository.findById("123").get().getAuthor()).isEqualTo("Book author");
    }


}
