package com.productdock.library.search.adapter.out.elastic;

import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;

public interface BookDocumentElasticRepository extends ElasticsearchRepository<BookDocument, String> {
}
