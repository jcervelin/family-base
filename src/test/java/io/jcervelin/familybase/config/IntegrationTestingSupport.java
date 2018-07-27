package io.jcervelin.familybase.config;


import io.jcervelin.familybase.FamilyBaseApplication;
import lombok.RequiredArgsConstructor;
import org.junit.BeforeClass;
import org.junit.runner.RunWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import static br.com.six2six.fixturefactory.loader.FixtureFactoryLoader.loadTemplates;
import static org.springframework.boot.test.context.SpringBootTest.WebEnvironment.RANDOM_PORT;

@RunWith(SpringJUnit4ClassRunner.class)
@ActiveProfiles("integration")
@SpringBootTest(classes = {FamilyBaseApplication.class}, webEnvironment = RANDOM_PORT)
@ContextConfiguration
@RequiredArgsConstructor
public class IntegrationTestingSupport {

    private static final String TEMPLATE_PACKAGE = "io.jcervelin.familybase.templates";

    @BeforeClass
    public static void setup() {
        loadTemplates(TEMPLATE_PACKAGE);
    }

}
