package org.dromara.test;

import org.dromara.onebot.client.OneBotClient;
import org.junit.jupiter.api.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

/**
 * 标签单元测试案例
 *
 * @author Lion Li
 */
@SpringBootTest
public class oneBotUnitTest {

    @Autowired
    private OneBotClient oneBotClient;

    @Test
    public void getVersionInfo() {

    }


    @BeforeEach
    public void testBeforeEach() {
        System.out.println("@BeforeEach ==================");
    }

    @AfterEach
    public void testAfterEach() {
        System.out.println("@AfterEach ==================");
    }


}
