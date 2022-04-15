package com.productdock.library.search;

import com.productdock.library.search.elastic.document.BookDocument;
import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;


public interface BookDocumentRepository extends ElasticsearchRepository<BookDocument, String> {
}
