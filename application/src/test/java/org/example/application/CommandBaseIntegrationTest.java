package org.example.application;


import com.google.gson.Gson;
import io.restassured.RestAssured;
import io.restassured.builder.RequestSpecBuilder;
import io.restassured.specification.RequestSpecification;
import org.example.generic.DomainEvent;
import org.example.generic.EventBus;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Captor;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.SpyBean;
import org.springframework.boot.web.server.LocalServerPort;
import org.springframework.restdocs.RestDocumentationContextProvider;
import org.springframework.restdocs.RestDocumentationExtension;
import org.springframework.restdocs.operation.OperationRequest;
import org.springframework.restdocs.operation.OperationResponse;
import org.springframework.restdocs.operation.OperationResponseFactory;
import org.springframework.restdocs.operation.preprocess.OperationPreprocessor;
import org.springframework.restdocs.payload.RequestFieldsSnippet;
import org.springframework.restdocs.restassured3.RestAssuredRestDocumentation;
import org.springframework.restdocs.restassured3.RestDocumentationFilter;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import static org.mockito.Mockito.verify;
import static org.springframework.restdocs.operation.preprocess.Preprocessors.*;
import static org.springframework.restdocs.restassured3.RestAssuredRestDocumentation.documentationConfiguration;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT, classes = Application.class)
@ExtendWith({RestDocumentationExtension.class, SpringExtension.class})
public abstract class CommandBaseIntegrationTest {

    @SpyBean
    private EventBus bus;
    @Captor
    private ArgumentCaptor<DomainEvent> eventArgumentCaptor;

    protected RequestSpecification documentationSpec;

    @BeforeAll
    static void cleanAll() {
    }

    @BeforeEach
    public void setUp(RestDocumentationContextProvider restDocumentation) {
        this.documentationSpec = new RequestSpecBuilder()
                .addFilter(documentationConfiguration(restDocumentation))
                .build();

    }

    @LocalServerPort
    private void initRestAssured(final int localPort) {
        RestAssured.port = localPort;
        RestAssured.baseURI = "http://localhost";
    }

    protected RestDocumentationFilter getSpecDoc(String name, RequestFieldsSnippet requestFieldsSnippet) {
        return RestAssuredRestDocumentation.document(name,
                preprocessRequest(prettyPrint()),
                preprocessResponse(new OperationPreprocessor() {
                    @Override
                    public OperationRequest preprocess(OperationRequest operationRequest) {
                        return operationRequest;
                    }

                    @Override
                    public OperationResponse preprocess(OperationResponse operationResponse) {
                        verify(bus).publish(eventArgumentCaptor.capture());
                        return new OperationResponseFactory().create(
                                200,
                                operationResponse.getHeaders(),
                                new Gson().toJson(eventArgumentCaptor.getAllValues()).getBytes()
                        );
                    }
                }, prettyPrint()),
                requestFieldsSnippet
        );
    }


}