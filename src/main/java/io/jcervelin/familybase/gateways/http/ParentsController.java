package io.jcervelin.familybase.gateways.http;

import io.jcervelin.familybase.domains.Parent;
import io.jcervelin.familybase.usecases.ManageParentsUseCase;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Collection;

import static io.jcervelin.familybase.domains.Endpoints.PARENTS;
import static org.springframework.http.MediaType.APPLICATION_JSON_VALUE;

/**
 * @author Juliano Cervelin on 25/07/2018
 */

@RestController
@RequestMapping(PARENTS)
@Api(value = PARENTS, description = "Operations pertaining to parents")
@RequiredArgsConstructor
@Slf4j
public class ParentsController {

    private final ManageParentsUseCase useCase;

    @GetMapping(produces = APPLICATION_JSON_VALUE)
    @ApiOperation("Find all parents")
    public ResponseEntity<Collection<Parent>> findAll() {

        final Collection<Parent> parents = useCase.findAll();

        return new ResponseEntity<>(parents, HttpStatus.OK);
    }

    @GetMapping(value = "{id}", produces = APPLICATION_JSON_VALUE)
    @ApiOperation("Find parent by id")
    public ResponseEntity<Parent> findAll(@PathVariable final Integer id) {
        final Parent parent = useCase.findById(Long.valueOf(id));
        log.info("Found parent: {}", parent);
        return new ResponseEntity<>(parent, HttpStatus.OK);
    }

    @PostMapping(produces = APPLICATION_JSON_VALUE)
    @ApiOperation("Save parent")
    @ResponseStatus(value = HttpStatus.CREATED)
    public void save(@RequestBody final Parent parent) {
        log.info("Starting to save parent: {}", parent);
        useCase.save(parent);
    }

}
