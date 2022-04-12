package com.productdock.library.search;

import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;


public interface BookIndexRepository extends ElasticsearchRepository<BookIndex, String> {
}
