package org.example.application.handles;

import org.example.business.usecases.AddCourseUseCase;
import org.example.domain.program.command.AddCourseCommand;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Component;

@Component
public class AddCourseHandle extends HandleBase{

    @Autowired
    private AddCourseUseCase addCourseUseCase;

    @EventListener
    public void handleAddCourse(AddCourseCommand command) {
        var events = addCourseUseCase.apply(command);
        emit(events);
    }

}
