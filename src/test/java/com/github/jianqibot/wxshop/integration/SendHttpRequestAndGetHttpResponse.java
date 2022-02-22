package com.github.jianqibot.wxshop.integration;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import org.flywaydb.core.Flyway;
import org.flywaydb.core.api.configuration.ClassicConfiguration;
import org.junit.jupiter.api.BeforeEach;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.env.Environment;

import java.io.IOException;
import java.net.CookieManager;
import java.net.CookiePolicy;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;


public class SendHttpRequestAndGetHttpResponse {
    private final ObjectMapper mapper = new ObjectMapper()
            .registerModule(new JavaTimeModule())
            .disable(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS);
    private final HttpClient client = HttpClient.newBuilder()
            .cookieHandler(new CookieManager(null, CookiePolicy.ACCEPT_ALL)).build();
    @Autowired
    Environment environment;

    @Value("${spring.datasource.url}")
    private String databaseUrl;
    @Value("${spring.datasource.username}")
    private String databaseUsername;
    @Value("${spring.datasource.password}")
    private String databasePassword;

    @BeforeEach
    public void initDatabase() {
        // 在每个测试开始前，执行一次flyway:clean flyway:migrate
        ClassicConfiguration conf = new ClassicConfiguration();
        conf.setDataSource(databaseUrl, databaseUsername, databasePassword);
        Flyway flyway = new Flyway(conf);
        flyway.clean();
        flyway.migrate();
    }

    public HttpResponse<String> sendHttpGetRequestAndGetHttpResponse(String apiName) throws IOException, InterruptedException {

        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create(getUrl(apiName)))
                .GET()
                .build();

        return this.client.send(request, HttpResponse.BodyHandlers.ofString());
    }


    public HttpResponse<String> sendHttpPostRequestAndGetHttpResponse(String apiName,
                                                                      Object requestBodyParam) throws IOException, InterruptedException {
        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create(getUrl(apiName)))
                .header("Content-Type", "application/json;charset=UTF-8")
                .POST(HttpRequest.BodyPublishers.ofString(new ObjectMapper().writeValueAsString(requestBodyParam)))
                .build();

        return this.client.send(request, HttpResponse.BodyHandlers.ofString());
    }

    public HttpResponse<String> sendHttpPutRequestAndGetHttpResponse(String apiName,
                                                                     Object requestBodyParam) throws IOException, InterruptedException {
        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create(getUrl(apiName)))
                .header("Content-Type", "application/json;charset=UTF-8")
                .PUT(HttpRequest.BodyPublishers.ofString(new ObjectMapper().writeValueAsString(requestBodyParam)))
                .build();

        return this.client.send(request, HttpResponse.BodyHandlers.ofString());
    }

    public HttpResponse<String> sendHttpDeleteRequestAndGetHttpResponse(String apiName) throws IOException, InterruptedException {

        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create(getUrl(apiName)))
                .DELETE()
                .build();

        return this.client.send(request, HttpResponse.BodyHandlers.ofString());
    }

    public <T> T parseJsonObject(String source, TypeReference<T> tTypeReference) throws IOException {
        return mapper.readValue(source, tTypeReference);
    }

    private String getUrl(String apiName) {
        return "http://localhost:" + environment.getProperty("local.server.port") + apiName;
    }
}
