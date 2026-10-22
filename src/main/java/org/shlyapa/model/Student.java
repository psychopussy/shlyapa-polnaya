package org.shlyapa.model;

import jakarta.persistence.*;

@Entity
@Table(name = "students")
public class Student {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String name;

    @Column(nullable = false)
    private Integer course;

    @Column(name = "has_debt", nullable = false)
    private Boolean hasDebt;

    public Student() {
    }

    public Student(String name, Integer course, Boolean hasDebt) {
        this.name = name;
        this.course = course;
        this.hasDebt = hasDebt;
    }

    public Student(Long id, String name, Integer course, Boolean hasDebt) {
        this.id = id;
        this.name = name;
        this.course = course;
        this.hasDebt = hasDebt;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Integer getCourse() {
        return course;
    }

    public void setCourse(Integer course) {
        this.course = course;
    }

    public Boolean getHasDebt() {
        return hasDebt;
    }

    public void setHasDebt(Boolean hasDebt) {
        this.hasDebt = hasDebt;
    }
}
