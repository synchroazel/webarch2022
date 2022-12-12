package it.unitn.padalino.assignment4remote;

import javax.persistence.*;
import java.io.Serializable;

@Entity
@Table(name = "STUDENT")
public class StudentEntity implements Serializable {
    private Integer matriculation;
    private String name;
    private String surname;

    public StudentEntity() {
    }

    public StudentEntity(String name, String surname) {
        this.name = name;
        this.surname = surname;
    }

    @Id
    @Column(name = "MATRICULATION", nullable = false)
    @GeneratedValue(strategy = GenerationType.IDENTITY, generator = "student_seq")
    @SequenceGenerator(name = "student_seq", sequenceName = "STUDENT_SEQ", allocationSize = 1)
    public Integer getMatriculation() {
        return matriculation;
    }

    public void setMatriculation(Integer matriculation) {
        this.matriculation = matriculation;
    }

    @Column(name = "NAME", nullable = true, length = 30)
    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    @Column(name = "SURNAME", nullable = true, length = 30)
    public String getSurname() {
        return surname;
    }

    public void setSurname(String surname) {
        this.surname = surname;
    }


    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        StudentEntity that = (StudentEntity) o;
        if (matriculation != null ? !matriculation.equals(that.matriculation) : that.matriculation != null) return false;
        if (name != null ? !name.equals(that.name) : that.name != null) return false;
        if (surname != null ? !surname.equals(that.surname) : that.surname != null) return false;
        return true;
    }

    @Override
    public int hashCode() {
        int result = matriculation != null ? matriculation.hashCode() : 0;
        result = 31 * result + (name != null ? name.hashCode() : 0);
        result = 31 * result + (surname != null ? surname.hashCode() : 0);
        return result;
    }

}