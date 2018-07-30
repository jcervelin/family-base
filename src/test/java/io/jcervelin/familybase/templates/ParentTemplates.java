package io.jcervelin.familybase.templates;

import br.com.six2six.fixturefactory.Fixture;
import br.com.six2six.fixturefactory.Rule;
import br.com.six2six.fixturefactory.loader.TemplateLoader;
import io.jcervelin.familybase.domains.Child;
import io.jcervelin.familybase.domains.Parent;

import java.time.LocalDate;

import static io.jcervelin.familybase.templates.ChildTemplates.CHILD_WITHOUT_DATE_OF_BIRTH;
import static io.jcervelin.familybase.templates.ChildTemplates.VALID_CHILD;

public class ParentTemplates implements TemplateLoader {
    public static final String VALID_PARENT = "Valid parent";
    public static final String PARENT_WITHOUT_ID = "Parent without id";
    public static final String PARENT_WITHOUT_DATE_OF_BIRTH = "Parent without date of birth";

    @Override
    public void load() {
        Fixture.of(Parent.class).addTemplate(VALID_PARENT, new Rule() {{
            add("id", "5b5e30a04caf45731516eb66");
            add("title", "Mr.");
            add("firstName", "Antonius");
            add("lastName", "Cervelin");
            add("dateOfBirth", LocalDate.of(1950, 9, 12));
            add("emailAddress", "anto@gmail.com");
            add("gender", "male");
            add("secondName", null);
            add("children", has(2).of(Child.class, VALID_CHILD, CHILD_WITHOUT_DATE_OF_BIRTH));

        }});

        Fixture.of(Parent.class).addTemplate(PARENT_WITHOUT_ID).inherits(VALID_PARENT, new Rule() {{
            add("id", null);
        }});

        Fixture.of(Parent.class).addTemplate(PARENT_WITHOUT_DATE_OF_BIRTH).inherits(VALID_PARENT, new Rule() {{
            add("id", "5b5e30a04caf45731516eb67");
            add("dateOfBirth", null);
        }});
    }

}

