package com.sense.mos.base;

import com.sense.mos.utils.SpringApplicationContext;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;

@ActiveProfiles("test")
@SpringBootTest
@ExtendWith({SpringExtension.class})
public abstract class IntegrationTest {

    protected MockMvc mockMvc;

    @Autowired
    private WebApplicationContext context;

    @Autowired
    private TruncateDatabaseService truncateDatabaseService;

    @Autowired
    private SpringApplicationContext springApplicationContext;

    @BeforeEach
    protected void setUp() throws Exception {
        this.mockMvc = MockMvcBuilders.webAppContextSetup(this.context).build();
        try {
            truncateDatabaseService.restartIdWith(1, true, null);
        } finally {
            truncateDatabaseService.closeResource();
        }
        springApplicationContext.setApplicationContext(context);
    }
}
