package ipass.server.api.exceptionHandler;

import javax.ws.rs.ext.ExceptionMapper;
import javax.ws.rs.ext.Provider;
import javax.ws.rs.ForbiddenException;
import javax.ws.rs.NotAuthorizedException;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.Response.Status;;

@Provider
public class ForbiddenExceptionMapper implements ExceptionMapper<ForbiddenException>{

	public Response toResponse(NotAuthorizedException exception) {
		// TODO Auto-generated method stub
		return Response.status(Response.Status.UNAUTHORIZED).
			    entity(new Error(Status.UNAUTHORIZED.getStatusCode(), Status.UNAUTHORIZED, exception.getMessage())).
			    build();		
		}

	@Override
	public Response toResponse(ForbiddenException exception) {
		// TODO Auto-generated method stub
		return Response.status(Response.Status.FORBIDDEN).
			    entity(new Error(Status.FORBIDDEN.getStatusCode(), Status.FORBIDDEN, "Log alsjeblieft eerst in")).
			    build();
	}

}
