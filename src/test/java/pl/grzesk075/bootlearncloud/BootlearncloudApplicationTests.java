package pl.grzesk075.bootlearncloud;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.ApplicationContext;
import org.springframework.http.MediaType;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.web.servlet.MockMvc;
import pl.grzesk075.bootlearncloud.model.Student;

import static org.hamcrest.Matchers.is;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
@TestPropertySource("classpath:application-integrationtest.properties")
class BootlearncloudApplicationTests {

	private static final Student STUDENT_1 = Student.builder()
			.firstName("Jan")
			.lastName("Kowalski")
			.build();

	private static final Student STUDENT_2 = Student.builder()
			.firstName("Anna")
			.lastName("Jasna")
			.build();

	@Autowired
	private MockMvc mockMvc;

	@Autowired
	private ObjectMapper objectMapper;

	@Value("${properties.test.value}")
	private Integer propertiesTestValue;

	@Test
	void contextLoads(ApplicationContext appCtx) {
		assertNotNull(appCtx);
	}

	@Test
	void propertiesTest() {
		assertEquals(5, propertiesTestValue);
	}

	@Test
	void studentIntegrationTest() throws Exception {

		addStudent(STUDENT_1, "1");
		addStudent(STUDENT_2, "2");
		addGrades();
		getStudents();
		findGrades();
	}

	private void addStudent(Student student, String expectedReturnedId) throws Exception {

		mockMvc.perform(post("/student/addStudent")
						.contentType(MediaType.APPLICATION_JSON)
						.content(objectMapper.writeValueAsString(student))
				)
				.andExpect(status().isOk())
				.andExpect(content().string(is(expectedReturnedId)));
	}

	private void addGrades() {

	}

	private void getStudents() {

	}

	private void findGrades() {

	}
}
