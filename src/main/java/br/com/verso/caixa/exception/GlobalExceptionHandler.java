package br.com.verso.caixa.exception;

import jakarta.validation.ConstraintViolation;
import jakarta.validation.ConstraintViolationException;
import jakarta.ws.rs.core.Response;
import jakarta.ws.rs.ext.ExceptionMapper;
import jakarta.ws.rs.ext.Provider;
import org.jboss.logging.Logger;
import java.time.LocalDateTime;
import java.util.stream.Collectors;

@Provider
public class GlobalExceptionHandler implements ExceptionMapper<Exception> {

    private static final Logger LOG = Logger.getLogger(GlobalExceptionHandler.class);

    @Override
    public Response toResponse(Exception exception) {
        LOG.errorf("Exception caught: %s", exception.getMessage(), exception);

        if (exception instanceof BusinessException businessException) {
            return buildErrorResponse(400, "Bad Request", businessException.getMessage());
        }

        if (exception instanceof NotFoundException notFoundException) {
            return buildErrorResponse(404, "Not Found", notFoundException.getMessage());
        }

        if (exception instanceof ConstraintViolationException violationException) {
            String message = violationException.getConstraintViolations()
                    .stream()
                    .map(ConstraintViolation::getMessage)
                    .collect(Collectors.joining(", "));
            return buildErrorResponse(400, "Validation Error", message);
        }

        return buildErrorResponse(500, "Internal Server Error", "Um erro inesperado ocorreu");
    }

    private Response buildErrorResponse(int status, String error, String message) {
        ErrorResponse errorResponse = new ErrorResponse(
                LocalDateTime.now(),
                status,
                error,
                message,
                ""
        );

        return Response.status(status)
                .entity(errorResponse)
                .build();
    }
}

