package io.jcervelin.familybase.domains;

import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * @author Juliano Cervelin on 25/07/2018
 */
@AllArgsConstructor
@Getter
public enum GenderType {
    MALE("MALE"),
    FEMALE("FEMALE");

    private String type;
}
