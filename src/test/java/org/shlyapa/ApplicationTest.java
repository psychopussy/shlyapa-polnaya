package org.shlyapa;

import org.junit.jupiter.api.Test;
import org.shlyapa.model.Student;
import org.shlyapa.service.StudentService;
import org.shlyapa.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.mock.web.MockHttpSession;
import org.springframework.test.web.servlet.MockMvc;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
@AutoConfigureMockMvc
public class ApplicationTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private UserService userService;

    @Autowired
    private StudentService studentService;

    @Test
    void contextLoads() {
    }

    @Test
    void testUserRegistrationAndAuthentication() {
        boolean registered = userService.register("testuser", "password123");
        assertTrue(registered);

        boolean duplicate = userService.register("testuser", "different");
        assertFalse(duplicate);

        boolean authenticated = userService.authenticate("testuser", "password123");
        assertTrue(authenticated);

        boolean wrongPassword = userService.authenticate("testuser", "wrong");
        assertFalse(wrongPassword);
    }

    @Test
    void testUnauthorizedRedirect() throws Exception {
        mockMvc.perform(get("/students"))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/login"));

        mockMvc.perform(get("/students/add"))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/login"));
    }

    @Test
    void testStudentFlowWithSession() throws Exception {
        MockHttpSession session = new MockHttpSession();
        session.setAttribute("user", "admin");

        mockMvc.perform(get("/students").session(session))
                .andExpect(status().isOk())
                .andExpect(view().name("students-list"))
                .andExpect(model().attributeExists("students"));

        mockMvc.perform(post("/students/add")
                        .session(session)
                        .param("name", "Иван Иванов")
                        .param("course", "3")
                        .param("hasDebt", "true"))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/students"));

        boolean studentExists = studentService.getAllStudents().stream()
                .anyMatch(s -> "Иван Иванов".equals(s.getName()) && s.getCourse() == 3 && Boolean.TRUE.equals(s.getHasDebt()));
        assertTrue(studentExists);
    }
}
