package it.unitn.padalino.assignment4remote.ejb;

import it.unitn.padalino.assignment4remote.StudentDTO;
import it.unitn.padalino.assignment4remote.StudentEntity;

import javax.ejb.Remote;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;
import java.util.List;

@Stateless
@Remote(StudentHandlerBeanIf.class)
public class StudentHandlerBean implements StudentHandlerBeanIf {

    @PersistenceContext(unitName = "default")
    private EntityManager entityManager;


    public StudentDTO getStudent(int i) {

        StudentDTO studentDTO = new StudentDTO();

        String name = getName(i);
        String surname = getSurname(i);
        List courses = getCourses(i);
        List grades = getGrades(i);
        List teachers = getTeachers(i);

        studentDTO.setName(name);
        studentDTO.setSurname(surname);
        studentDTO.setCourses(courses);
        studentDTO.setGrades(grades);
        studentDTO.setTeachers(teachers);

        return studentDTO;
    }

    @Override
    public String getName(int i) {

        Query q = entityManager.createQuery("From StudentEntity where MATRICULATION = " + i);
        StudentEntity studentEntity = (StudentEntity) q.getSingleResult();
        return studentEntity.getName();
    }

    @Override
    public String getSurname(int i) {

        Query q = entityManager.createQuery("From StudentEntity where MATRICULATION = " + i);
        StudentEntity studentEntity = (StudentEntity) q.getSingleResult();
        return studentEntity.getSurname();
    }

    @Override
    public List<String> getCourses(int i) {

        Query q = entityManager.createNativeQuery(
                """
                     SELECT COURSE.NAME FROM COURSE
                     INNER JOIN ATTENDANCY ON COURSE.ID=ATTENDANCY.COURSE_ID
                     WHERE ATTENDANCY.STUDENT_ID =
                    """ + i
        );

        List<String> courses = q.getResultList();

        return courses;

    }

    @Override
    public List<Integer> getGrades(int i) {

        Query q = entityManager.createNativeQuery(
                """
                     SELECT ATTENDANCY.GRADE FROM COURSE
                     INNER JOIN ATTENDANCY ON COURSE.ID=ATTENDANCY.COURSE_ID
                     WHERE ATTENDANCY.STUDENT_ID =
                    """ + i
        );

        List<Integer> grades = q.getResultList();

        return grades;

    }

    @Override
    public List<String> getTeachers(int i) {

        Query q = entityManager.createNativeQuery(
                """
                    SELECT CONCAT(TEACHER.NAME, ' ', TEACHER.SURNAME)
                    FROM TEACHER
                    INNER JOIN ATTENDANCY ON TEACHER.ID=ATTENDANCY.COURSE_ID
                    WHERE ATTENDANCY.STUDENT_ID =
                    """ + i
        );

        List<String> teachers = q.getResultList();

        return teachers;

    }

}