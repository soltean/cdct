package com.so.cdct;

import com.jayway.restassured.module.mockmvc.RestAssuredMockMvc;
import org.junit.Before;
import org.junit.runner.RunWith;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.test.context.junit4.SpringRunner;

@RunWith(SpringRunner.class)
@WebMvcTest(ItemController.class)
public abstract class ItemBaseTest {

	@Before
	public void before() {
		RestAssuredMockMvc.standaloneSetup(new ItemController());
	}

}
