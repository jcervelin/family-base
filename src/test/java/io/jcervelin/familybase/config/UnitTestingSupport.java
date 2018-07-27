package io.jcervelin.familybase.config;

import org.junit.BeforeClass;
import org.junit.Rule;
import org.junit.rules.ExpectedException;
import org.junit.runner.RunWith;
import org.mockito.runners.MockitoJUnitRunner;

import static br.com.six2six.fixturefactory.loader.FixtureFactoryLoader.loadTemplates;

@RunWith(MockitoJUnitRunner.class)
public class UnitTestingSupport {

    private static final String TEMPLATE_PACKAGE = "io.jcervelin.familybase.templates";

    @Rule
    public final ExpectedException thrown = ExpectedException.none();

    @BeforeClass
    public static void setup() {
        loadTemplates(TEMPLATE_PACKAGE);
    }


}