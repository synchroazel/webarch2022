package it.unitn.padalino.assignment4remote.ejb;

import it.unitn.padalino.assignment4remote.StudentDTO;

import java.util.List;

public interface StudentHandlerBeanIf {

    StudentDTO getStudent(int i);

    String getName(int i);

    String getSurname(int i);

    List getCourses(int i);

    List getGrades(int i);

    List getTeachers(int i);
}
