package org.example.domain.program;



import org.example.domain.program.event.CourseAssigned;
import org.example.domain.program.event.ProgramCreated;
import org.example.domain.program.event.ScoreAssigned;
import org.example.generic.DomainEvent;
import org.example.generic.AggregateRoot;

import java.util.Date;
import java.util.List;
import java.util.Map;


public class Program extends AggregateRoot {
    protected Map<String, Course> courses;
    protected Map<String, Score> scores;
    protected String name;

    public Program(String programId, String name){
        super(programId);
        subscribe(new ProgramEventChange(this));
        appendChange(new ProgramCreated(name)).apply();
    }


    public void addCourse(String courseId, String name, List<String> categories){
        appendChange(new CourseAssigned(courseId, name, categories)).apply();
    }

    public void assignScore(String user, String courseId, String category, String value, Date date){
        appendChange(new ScoreAssigned(user, courseId, category, value, date)).apply();
    }


    private Program(String id){
        super(id);
        subscribe(new ProgramEventChange(this));
    }


    public static Program from(String id, List<DomainEvent> events){
        var program = new Program(id);
        events.forEach(program::applyEvent);
        return program;
    }

    public String name() {
        return name;
    }

    public Course getCoursesById(String courseId) {
        return courses.get(courseId);
    }

    public Score getScoreByCourseIdAndCategoryAndUser(String courseId, String category, String user){
        return this.scores.get(courseId+category+user);
    }

    public Map<String, Score> scores() {
        return scores;
    }
}