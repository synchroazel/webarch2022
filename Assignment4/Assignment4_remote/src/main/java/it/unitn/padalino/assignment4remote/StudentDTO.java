package it.unitn.padalino.assignment4remote;

import java.io.Serializable;
import java.util.List;

public class StudentDTO implements Serializable {

    private String name;

    public void setName(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }


    private String surname;

    public void setSurname(String surname) {
        this.surname = surname;
    }

    public String getSurname() {
        return surname;
    }


    public List courses;

    public void setCourses(List courses) {
        this.courses = courses;
    }

    public List getCourses() {
        return courses;
    }


    public List grades;

    public void setGrades(List grades) {
        this.grades = grades;
    }

    public List getGrades() {
        return grades;
    }


    private List teachers;

    public void setTeachers(List teachers) {
        this.teachers = teachers;
    }

    public List getTeachers() {
        return teachers;
    }

}