package org.example.application;

import org.example.domain.program.command.AddCourseCommand;
import org.example.domain.program.command.CreateProgramCommand;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping(value = "api/", produces = MediaType.APPLICATION_JSON_VALUE)
public class CommandController {
    @Autowired
    private ApplicationEventPublisher publisher;


    @PostMapping("/createProgram")
    @ResponseStatus(HttpStatus.OK)
    public void createProgram(@RequestBody CreateProgramCommand command) {
        publisher.publishEvent(command);
    }


    @PostMapping("/addCourse")
    @ResponseStatus(HttpStatus.OK)
    public void createProgram(@RequestBody AddCourseCommand command) {
        publisher.publishEvent(command);
    }
}
