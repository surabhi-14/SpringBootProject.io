package com.wu;

import org.junit.Assert;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;


@SpringBootTest
class Trial1ApplicationTests {

	@Test
	void contextLoads() {
	}
	@Test
	public void test1() {
		//new codeCoverage();
		Assert.assertEquals(Trial1Application.start(), "start");
	}
	@Test
	public void test2() {
		//new codeCoverage();
		Assert.assertEquals(Trial1Application.stop(), "stop");
	}

}
