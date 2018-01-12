package ipass.server.api.exceptionHandler;

import javax.ws.rs.NotFoundException;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.Response.Status;
import javax.ws.rs.ext.ExceptionMapper;
import javax.ws.rs.ext.Provider;
@Provider

public class NotFoundExceptionMapper implements ExceptionMapper<NotFoundException>{

	public Response toResponse(NotFoundException exception) {
		// TODO Auto-generated method stub
		return Response.status(Response.Status.NOT_FOUND).
			    entity(new Error(404, Status.NOT_FOUND, exception.getMessage())).
			    build();
	}

}
