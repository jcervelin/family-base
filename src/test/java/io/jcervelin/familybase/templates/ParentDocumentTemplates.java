package io.jcervelin.familybase.templates;

import br.com.six2six.fixturefactory.Fixture;
import br.com.six2six.fixturefactory.Rule;
import br.com.six2six.fixturefactory.loader.TemplateLoader;
import io.jcervelin.familybase.domains.mongo.ChildDocument;
import io.jcervelin.familybase.domains.mongo.ParentDocument;
import org.bson.types.ObjectId;

import java.time.LocalDate;

import static io.jcervelin.familybase.templates.ChildDocumentTemplates.CHILD_DOCUMENT_WITHOUT_DATE_OF_BIRTH;
import static io.jcervelin.familybase.templates.ChildDocumentTemplates.VALID_CHILD_DOCUMENT;

public class ParentDocumentTemplates implements TemplateLoader {
    public static final String VALID_PARENT_DOCUMENT = "Valid parent document";
    public static final String VALID_PARENT_DOCUMENT_WITH_ONE_CHILD = "Valid parent document with one child";
    public static final String PARENT_DOCUMENT_WITHOUT_ID = "Parent document without id";
    public static final String PARENT_DOCUMENT_WITHOUT_DATE_OF_BIRTH = "Parent document without date of birth";

    @Override
    public void load() {
        Fixture.of(ParentDocument.class).addTemplate(VALID_PARENT_DOCUMENT, new Rule() {{
            add("id", new ObjectId("5b5e30a04caf45731516eb66"));
            add("title", "Mr.");
            add("firstName", "Antonius");
            add("lastName", "Cervelin");
            add("dateOfBirth", LocalDate.of(1950, 9, 12));
            add("emailAddress", "anto@gmail.com");
            add("gender", "MALE");
            add("secondName", null);
            add("children", has(2).of(ChildDocument.class, VALID_CHILD_DOCUMENT, CHILD_DOCUMENT_WITHOUT_DATE_OF_BIRTH));

        }});

        Fixture.of(ParentDocument.class).addTemplate(PARENT_DOCUMENT_WITHOUT_ID).inherits(VALID_PARENT_DOCUMENT, new Rule() {{
            add("id", null);
        }});

        Fixture.of(ParentDocument.class).addTemplate(PARENT_DOCUMENT_WITHOUT_DATE_OF_BIRTH).inherits(VALID_PARENT_DOCUMENT, new Rule() {{
            add("id", new ObjectId("5b5e30a04caf45731516eb67"));
            add("dateOfBirth", null);
        }});

        Fixture.of(ParentDocument.class).addTemplate(VALID_PARENT_DOCUMENT_WITH_ONE_CHILD).inherits(VALID_PARENT_DOCUMENT, new Rule() {{
            add("children", has(1).of(ChildDocument.class, VALID_CHILD_DOCUMENT));
        }});

    }

}

