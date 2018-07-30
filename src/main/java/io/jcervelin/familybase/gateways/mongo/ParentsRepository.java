package io.jcervelin.familybase.gateways.mongo;

import io.jcervelin.familybase.domains.mongo.ParentDocument;
import org.bson.types.ObjectId;
import org.springframework.data.mongodb.repository.MongoRepository;

import java.util.Optional;

/**
 * @author Juliano Cervelin on 26/07/2018
 */
public interface ParentsRepository extends MongoRepository<ParentDocument, ObjectId> {
    Optional<ParentDocument> findByChildrenId(final ObjectId id);
}
