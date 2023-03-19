package top.naccl;

import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import top.naccl.util.HashUtils;

@SpringBootTest
class BlogApiApplicationTests {

	@Test
	void contextLoads() {
	}

	@Test
	void test() {
		System.out.println(HashUtils.getBC("pgmerxw@blogAdmin"));
	}

}
