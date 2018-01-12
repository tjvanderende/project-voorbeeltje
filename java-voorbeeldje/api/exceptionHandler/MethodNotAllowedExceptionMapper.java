package ipass.server.api.exceptionHandler;

import javax.ws.rs.NotAllowedException;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.Response.Status;
import javax.ws.rs.ext.ExceptionMapper;
import javax.ws.rs.ext.Provider;

@Provider
public class MethodNotAllowedExceptionMapper implements ExceptionMapper<NotAllowedException> {

	public Response toResponse(NotAllowedException exception) {
		// TODO Auto-generated method stub
		return Response.status(Response.Status.METHOD_NOT_ALLOWED).
	    entity(new Error(405, Status.METHOD_NOT_ALLOWED, exception.getMessage())).
	    build();	
	}
}
