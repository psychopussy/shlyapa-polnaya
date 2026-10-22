package org.shlyapa.controller;

import jakarta.servlet.http.HttpSession;
import org.shlyapa.model.Student;
import org.shlyapa.service.StudentService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/students")
public class StudentController {

    private final StudentService studentService;

    public StudentController(StudentService studentService) {
        this.studentService = studentService;
    }

    private boolean isUserLoggedIn(HttpSession session) {
        return session.getAttribute("user") != null;
    }

    @GetMapping
    public String getAllStudents(HttpSession session, Model model) {
        if (!isUserLoggedIn(session)) {
            return "redirect:/login";
        }
        model.addAttribute("currentUser", session.getAttribute("user"));
        model.addAttribute("students", studentService.getAllStudents());
        return "students-list";
    }

    @GetMapping("/add")
    public String showAddStudentForm(HttpSession session, Model model) {
        if (!isUserLoggedIn(session)) {
            return "redirect:/login";
        }
        Student student = new Student();
        student.setHasDebt(false);
        model.addAttribute("currentUser", session.getAttribute("user"));
        model.addAttribute("student", student);
        return "students-form";
    }

    @PostMapping("/add")
    public String addStudent(@ModelAttribute("student") Student student, HttpSession session) {
        if (!isUserLoggedIn(session)) {
            return "redirect:/login";
        }
        if (student.getHasDebt() == null) {
            student.setHasDebt(false);
        }
        studentService.saveStudent(student);
        return "redirect:/students";
    }
}
