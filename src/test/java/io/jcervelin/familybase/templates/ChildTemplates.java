package io.jcervelin.familybase.templates;

import br.com.six2six.fixturefactory.Fixture;
import br.com.six2six.fixturefactory.Rule;
import br.com.six2six.fixturefactory.loader.TemplateLoader;
import io.jcervelin.familybase.domains.Child;

import java.time.LocalDate;

public class ChildTemplates implements TemplateLoader {
    public static final String VALID_CHILD = "Valid child";
    public static final String MID_CHILD = "Mid child";
    public static final String YOUNG_CHILD = "Young child";
    public static final String CHILD_WITHOUT_ID = "Child without id";
    public static final String CHILD_WITHOUT_DATE_OF_BIRTH = "Child without date of birth";

    @Override
    public void load() {
        Fixture.of(Child.class).addTemplate(VALID_CHILD, new Rule() {{
            add("id", "5b5e30a04caf45731516eb76");
            add("firstName", "Juliano");
            add("lastName", "Cervelin");
            add("dateOfBirth", LocalDate.of(1980, 9, 12));
            add("emailAddress", "juliano.cervelin@gmail.com");
            add("gender", "MALE");
            add("secondName", "de Sousa");
        }});

        Fixture.of(Child.class).addTemplate(MID_CHILD, new Rule() {{
            add("id", "5b5e30a04caf45731516eb77");
            add("firstName", "Daniel");
            add("lastName", "Cervelin");
            add("dateOfBirth", LocalDate.of(1985, 2, 5));
            add("emailAddress", "daniel.cervelin@gmail.com");
            add("gender", "Male");
            add("secondName", "de Sousa");
        }});

        Fixture.of(Child.class).addTemplate(YOUNG_CHILD, new Rule() {{
            add("id", "5b5e30a04caf45731516eb78");
            add("firstName", "Carol");
            add("lastName", "Cervelin");
            add("dateOfBirth", LocalDate.of(1992, 1, 30));
            add("emailAddress", "carol.cervelin@gmail.com");
            add("gender", "female");
            add("secondName", "de Sousa");
        }});

        Fixture.of(Child.class).addTemplate(CHILD_WITHOUT_ID).inherits(VALID_CHILD, new Rule() {{
            add("id", null);
        }});

        Fixture.of(Child.class).addTemplate(CHILD_WITHOUT_DATE_OF_BIRTH).inherits(VALID_CHILD, new Rule() {{
            add("id", "5b5e30a04caf45731516eb79");
            add("dateOfBirth", null);
        }});


    }

}

