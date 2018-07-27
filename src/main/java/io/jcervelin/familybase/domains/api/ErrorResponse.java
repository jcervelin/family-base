package io.jcervelin.familybase.domains.api;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.http.HttpStatus;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class ErrorResponse implements Serializable {

    private static final long serialVersionUID = 1L;

    private HttpStatus status;
    private List<MessageError> errors = new ArrayList<>();

    public void addError(final String error) {
        errors.add(MessageError.builder().message(error).build());
    }

}