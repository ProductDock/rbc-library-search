package com.productdock.library.search.config;

import org.testcontainers.elasticsearch.ElasticsearchContainer;
import org.testcontainers.utility.DockerImageName;

public class ElasticsearchTestContainer extends ElasticsearchContainer {

    private static final String ELASTICSEARCH_DOCKER_IMAGE = "docker.elastic.co/elasticsearch/elasticsearch";
    private static final String ELASTICSEARCH_DOCKER_TAG = "7.17.2";
    private static final String CLUSTER_NAME = "books-test-cluster";
    private static final String ELASTIC_SEARCH = "elasticsearch";

    public ElasticsearchTestContainer() {
        super(DockerImageName
                .parse(ELASTICSEARCH_DOCKER_IMAGE)
                .withTag(ELASTICSEARCH_DOCKER_TAG));

        this.withExposedPorts(9200);
        this.addFixedExposedPort(9222, 9200);
        this.addEnv(CLUSTER_NAME, ELASTIC_SEARCH);
    }
}
