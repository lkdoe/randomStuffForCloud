package com.fdmgroup.soloproject2;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.ApplicationContext;

@SpringBootTest
class SpringBootAppTest {

	@Test
	void testContextLoads(ApplicationContext context) {
		assertThat(context).isNotNull();
	}

}
