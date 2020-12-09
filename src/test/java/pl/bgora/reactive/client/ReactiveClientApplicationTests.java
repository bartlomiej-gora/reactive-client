package pl.bgora.reactive.client;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.web.reactive.function.client.WebClient;

@SpringBootTest
class ReactiveClientApplicationTests {

	@Test
	void contextLoads() {
	}

	@Autowired
	WebClient webClient;

}
