package com.productdock.library.search.book;

import com.productdock.library.search.elastic.document.BookDocument;
import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;


public interface BookDocumentRepository extends ElasticsearchRepository<BookDocument, String> {
}
