package io.jcervelin.familybase.gateways.h2;

import io.jcervelin.familybase.domains.Child;
import org.springframework.data.repository.CrudRepository;

import java.util.Collection;

/**
 * @author Juliano Cervelin on 26/07/2018
 */
public interface ChildrenRepository extends CrudRepository<Child, Long> {
    @Override
    Collection<Child> findAll();
}
