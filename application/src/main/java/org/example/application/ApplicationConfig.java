package org.example.application;

import org.example.business.usecases.AddCourseUseCase;
import org.example.business.usecases.CreateProgramUseCase;
import org.example.generic.EventStoreRepository;
import org.springframework.amqp.core.TopicExchange;
import org.springframework.amqp.rabbit.core.RabbitAdmin;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class ApplicationConfig {
    public static final String EXCHANGE = "scoreextraction";

    @Bean
    public CreateProgramUseCase createProgramUseCase() {
        return new CreateProgramUseCase();
    }

    @Bean
    public AddCourseUseCase addCourseUseCase(EventStoreRepository repository) {
        return new AddCourseUseCase(repository);
    }

//    @Bean("queries")
//    public MongoTemplate mongoTemplateQueries(@Value("${spring.queries.uri}") String uri) {
//        ConnectionString connectionString = new ConnectionString(uri);
//        return new MongoTemplate(new SimpleMongoClientDatabaseFactory(connectionString));
//    }
//
//    @Bean("commands")
//    @Primary
//    public MongoTemplate mongoTemplateCommands(@Value("${spring.commands.uri}") String uri) {
//        ConnectionString connectionString = new ConnectionString(uri);
//        return new MongoTemplate(new SimpleMongoClientDatabaseFactory(connectionString));
//    }

    @Bean
    public RabbitAdmin rabbitAdmin(RabbitTemplate rabbitTemplate) {
        var rabbitAdmin = new RabbitAdmin(rabbitTemplate);
        rabbitAdmin.declareExchange(new TopicExchange(EXCHANGE));
        return rabbitAdmin;
    }

}
