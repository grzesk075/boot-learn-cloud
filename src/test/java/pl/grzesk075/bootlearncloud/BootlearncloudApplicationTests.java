package pl.grzesk075.bootlearncloud;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.Import;
import org.springframework.http.MediaType;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.web.servlet.MockMvc;
import pl.grzesk075.bootlearncloud.configuration.GradeStatusTestConsumer;
import pl.grzesk075.bootlearncloud.model.Grade;
import pl.grzesk075.bootlearncloud.model.GradeValue;
import pl.grzesk075.bootlearncloud.model.Student;
import pl.grzesk075.bootlearncloud.model.Subject;
import pl.grzesk075.bootlearncloud.queue.GradeMessageQueueProducer;
import pl.grzesk075.bootlearncloud.queue.message.GradeMessage;
import pl.grzesk075.bootlearncloud.queue.message.GradeStatus;

import java.util.List;
import java.util.UUID;
import java.util.concurrent.TimeUnit;

import static org.awaitility.Awaitility.await;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.is;
import static org.hamcrest.Matchers.nullValue;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
@AutoConfigureMockMvc
@TestPropertySource("classpath:application-integrationtest.properties")
@Import(GradeStatusTestConsumer.class)
class BootlearncloudApplicationTests {

	private static final Student STUDENT_1 = Student.builder()
			.firstName("Jan")
			.lastName("Kowalski")
			.build();

	private static final Student STUDENT_2 = Student.builder()
			.firstName("Anna")
			.lastName("Jasna")
			.build();
	private static final String STUDENT_ID_1 = "1";

	private static final String STUDENT_ID_2 = "2";

	private static final GradeMessage GRADE_MESSAGE_1 = GradeMessage.builder()
			.uuid(new UUID(73954672L, 6147526725L))
			.studentId(Long.valueOf(STUDENT_ID_1))
			.grade(Grade.builder().gradeValue(GradeValue.E).subject(Subject.ENGLISH).build())
			.build();

	private static final GradeMessage GRADE_MESSAGE_2 = GradeMessage.builder()
			.uuid(new UUID(54885L, 326578L))
			.studentId(Long.valueOf(STUDENT_ID_2))
			.grade(Grade.builder().gradeValue(GradeValue.E).subject(Subject.MATHEMATICS).build())
			.build();

	@Autowired
	private GradeMessageQueueProducer gradeMessageQueueProducer;

	@Autowired
	private MockMvc mockMvc;

	@Autowired
	private ObjectMapper objectMapper;

	@Autowired
	private GradeStatusTestConsumer gradeStatusTestConsumer;

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

		addStudent(STUDENT_1, STUDENT_ID_1);
		addStudent(STUDENT_2, STUDENT_ID_2);
		addGrade(Subject.MATHEMATICS, GradeValue.C, STUDENT_ID_1, "1");
		addGrade(Subject.CHEMISTRY, GradeValue.B, STUDENT_ID_1, "2");
		addGrade(Subject.PHYSICS, GradeValue.A, STUDENT_ID_2, "3");
		addGrade(Subject.ENGLISH, GradeValue.D, STUDENT_ID_2, "4");
		getStudents();
		findGrades();
		sendGradesToQueue();
	}

	private void addStudent(Student student, String expectedReturnedId) throws Exception {

		mockMvc.perform(post("/student/addStudent")
						.contentType(MediaType.APPLICATION_JSON)
						.content(objectMapper.writeValueAsString(student))
				)
				.andExpect(status().isOk())
				.andExpect(content().string(is(expectedReturnedId)));
	}

	private void addGrade(Subject subject,
						  GradeValue gradeValue,
						  String studentId,
						  String expectedReturnedId) throws Exception {

		Grade grade = Grade.builder().subject(subject).gradeValue(gradeValue).build();
		mockMvc.perform(post("/student/addGrade/" + studentId)
						.contentType(MediaType.APPLICATION_JSON)
						.content(objectMapper.writeValueAsString(grade))
				)
				.andExpect(status().isOk())
				.andExpect(content().string(is(expectedReturnedId)));
	}

	private void getStudents() throws Exception {
		mockMvc.perform(get("/student/getStudent?studentId=" + STUDENT_ID_1))
				.andExpect(status().isOk())
				.andExpect(jsonPath("id", is(Integer.valueOf(STUDENT_ID_1))))
				.andExpect(jsonPath("firstName", is(STUDENT_1.getFirstName())))
				.andExpect(jsonPath("grades[1].subject", is(Subject.CHEMISTRY.name())))
				.andExpect(jsonPath("grades[1].gradeValue", is(GradeValue.B.name())));

		mockMvc.perform(get("/student/getStudent?studentId=" + STUDENT_ID_2))
				.andExpect(status().isOk())
				.andExpect(jsonPath("id", is(Integer.valueOf(STUDENT_ID_2))))
				.andExpect(jsonPath("firstName", is(STUDENT_2.getFirstName())))
				.andExpect(jsonPath("grades[1].subject", is(Subject.ENGLISH.name())))
				.andExpect(jsonPath("grades[1].gradeValue", is(GradeValue.D.name())));
	}

	private void findGrades() throws Exception {
		String uri = String.format(
				"/student/getGrades?subjectFilter=%s&studentLastNameFilter=%s",
				Subject.MATHEMATICS.name(),
				STUDENT_1.getLastName());
		mockMvc.perform(get(uri))
				.andExpect(status().isOk())
				.andExpect(jsonPath("[0].subject", is(Subject.MATHEMATICS.name())))
				.andExpect(jsonPath("[0].gradeValue", is(GradeValue.C.name())))
				.andExpect(jsonPath("length()", is(1)));
	}

	private void sendGradesToQueue() throws Exception {
		gradeMessageQueueProducer.send(GRADE_MESSAGE_1);
		gradeMessageQueueProducer.send(GRADE_MESSAGE_2);

		await().atMost(30L, TimeUnit.SECONDS).until(
				() -> gradeStatusTestConsumer.getGradeStatusList().size() == 2);

		List<GradeStatus> gradeStatusList = gradeStatusTestConsumer.getGradeStatusList();
		assertThat(gradeStatusList.get(0).getUuid(), is(GRADE_MESSAGE_1.getUuid()));
		assertThat(gradeStatusList.get(1).getUuid(), is(GRADE_MESSAGE_2.getUuid()));
		assertThat(gradeStatusList.get(0).getError(), nullValue());
		assertThat(gradeStatusList.get(1).getError(), nullValue());

		mockMvc.perform(get("/student/getStudent?studentId=" + STUDENT_ID_1))
				.andExpect(status().isOk())
				.andExpect(jsonPath("grades[2].subject", is(Subject.ENGLISH.name())))
				.andExpect(jsonPath("grades[2].gradeValue", is(GradeValue.E.name())));

		mockMvc.perform(get("/student/getStudent?studentId=" + STUDENT_ID_2))
				.andExpect(status().isOk())
				.andExpect(jsonPath("grades[2].subject", is(Subject.MATHEMATICS.name())))
				.andExpect(jsonPath("grades[2].gradeValue", is(GradeValue.E.name())));
	}
}
