package io.jcervelin.familybase.gateways.http;

import io.jcervelin.familybase.domains.Child;
import io.jcervelin.familybase.usecases.ManageChildrenUseCase;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Collection;

import static io.jcervelin.familybase.domains.Endpoints.CHILDREN;
import static org.springframework.http.MediaType.APPLICATION_JSON_VALUE;

/**
 * @author Juliano Cervelin on 25/07/2018
 */

@RestController
@RequestMapping(CHILDREN)
@Api(value = CHILDREN, description = "Operations pertaining to children")
@RequiredArgsConstructor
@Slf4j
public class ChildrenController {
    @Autowired
    private final ManageChildrenUseCase useCase;

    @GetMapping(produces = APPLICATION_JSON_VALUE)
    @ApiOperation("Find all children of all parents")
    public ResponseEntity<Collection<Child>> findAll() {
        final Collection<Child> all = useCase.findAll();
        return new ResponseEntity<>(all, HttpStatus.OK);
    }

    @GetMapping(value = "{id}", produces = APPLICATION_JSON_VALUE)
    @ApiOperation("Find child by id")
    public ResponseEntity<Child> findById(@PathVariable final String id) {
        final Child child = useCase.findById(Long.valueOf(id));
        log.info("Found child: {}", child);
        return new ResponseEntity<>(child, HttpStatus.OK);
    }

    @PostMapping(produces = APPLICATION_JSON_VALUE)
    @ApiOperation("Save or update child")
    @ResponseStatus(value = HttpStatus.CREATED)
    public void saveOrUpdate(@RequestBody final Child child) {
        log.info("Starting to save child: {}", child);
        useCase.save(child);
    }

}
