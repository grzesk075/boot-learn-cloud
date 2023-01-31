package pl.grzesk075.bootlearncloud;

import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.ApplicationContext;

import static org.junit.jupiter.api.Assertions.assertNotNull;

@SpringBootTest
class BootlearncloudApplicationTests {

	@Test
	void contextLoads(ApplicationContext appCtx) {
		assertNotNull(appCtx);
	}

}
