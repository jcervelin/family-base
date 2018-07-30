package io.jcervelin.familybase.templates;

import br.com.six2six.fixturefactory.Fixture;
import br.com.six2six.fixturefactory.Rule;
import br.com.six2six.fixturefactory.loader.TemplateLoader;
import io.jcervelin.familybase.domains.mongo.ChildDocument;
import org.bson.types.ObjectId;

import java.time.LocalDate;

public class ChildDocumentTemplates implements TemplateLoader {
    public static final String VALID_CHILD_DOCUMENT = "Valid child document";
    public static final String MID_CHILD_DOCUMENT = "Mid child document";
    public static final String YOUNG_CHILD_DOCUMENT = "Young child document";
    public static final String CHILD_DOCUMENT_WITHOUT_ID = "Child document without id";
    public static final String CHILD_DOCUMENT_WITHOUT_DATE_OF_BIRTH = "Child document without date of birth";

    @Override
    public void load() {
        Fixture.of(ChildDocument.class).addTemplate(VALID_CHILD_DOCUMENT, new Rule() {{
            add("id", new ObjectId("5b5e30a04caf45731516eb76"));
            add("firstName", "Juliano");
            add("lastName", "Cervelin");
            add("dateOfBirth", LocalDate.of(1980, 9, 12));
            add("emailAddress", "juliano.cervelin@gmail.com");
            add("gender", "MALE");
            add("secondName", "de Sousa");
        }});

        Fixture.of(ChildDocument.class).addTemplate(MID_CHILD_DOCUMENT, new Rule() {{
            add("id", new ObjectId("5b5e30a04caf45731516eb77"));
            add("firstName", "Daniel");
            add("lastName", "Cervelin");
            add("dateOfBirth", LocalDate.of(1985, 2, 5));
            add("emailAddress", "daniel.cervelin@gmail.com");
            add("gender", "MALE");
            add("secondName", "de Sousa");
        }});

        Fixture.of(ChildDocument.class).addTemplate(YOUNG_CHILD_DOCUMENT, new Rule() {{
            add("id", new ObjectId("5b5e30a04caf45731516eb78"));
            add("firstName", "Carol");
            add("lastName", "Cervelin");
            add("dateOfBirth", LocalDate.of(1992, 1, 30));
            add("emailAddress", "carol.cervelin@gmail.com");
            add("gender", "FEMALE");
            add("secondName", "de Sousa");
        }});

        Fixture.of(ChildDocument.class).addTemplate(CHILD_DOCUMENT_WITHOUT_ID).inherits(VALID_CHILD_DOCUMENT, new Rule() {{
            add("id", null);
        }});

        Fixture.of(ChildDocument.class).addTemplate(CHILD_DOCUMENT_WITHOUT_DATE_OF_BIRTH).inherits(VALID_CHILD_DOCUMENT, new Rule() {{
            add("id", new ObjectId("5b5e30a04caf45731516eb79"));
            add("dateOfBirth", null);
        }});


    }

}

