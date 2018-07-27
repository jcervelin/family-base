package io.jcervelin.familybase.gateways.h2;

import io.jcervelin.familybase.domains.Parent;
import org.springframework.data.repository.CrudRepository;

import java.util.Collection;

/**
 * @author Juliano Cervelin on 26/07/2018
 */
public interface ParentsRepository extends CrudRepository<Parent, Long> {

    @Override
    Collection<Parent> findAll();
}
