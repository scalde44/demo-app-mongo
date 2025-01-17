package org.example.application.handles;

import org.example.business.usecases.CreateProgramUseCase;
import org.example.domain.program.command.CreateProgramCommand;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.event.EventListener;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;


@Component
public class CreateProgramHandle extends HandleBase {

    @Autowired
    private CreateProgramUseCase createProgramUseCase;


    @Async
    @EventListener
    public void handleCreateProgram(CreateProgramCommand command) {
        System.out.println(command);
        var events = createProgramUseCase.apply(command);
        emit(events);
    }

}
