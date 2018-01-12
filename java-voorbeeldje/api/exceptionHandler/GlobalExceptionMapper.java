package ipass.server.api.exceptionHandler;

import javax.validation.ValidationException;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.Response.Status;
import javax.ws.rs.ext.ExceptionMapper;

public class GlobalExceptionMapper implements ExceptionMapper<ValidationException> {

	public Response toResponse(ValidationException exception) {
		// TODO Auto-generated method stub
		return Response.status(Response.Status.BAD_REQUEST).
			    entity(new Error(400, Status.BAD_REQUEST, exception.getMessage())).
			    build();		}

}
