package ipass.server.api.exceptionHandler;

import javax.ws.rs.ext.ExceptionMapper;
import javax.ws.rs.ext.Provider;
import javax.ws.rs.NotAuthorizedException;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.Response.Status;;

@Provider
public class NotAuthorizedExceptionMapper implements ExceptionMapper<NotAuthorizedException>{

	public Response toResponse(NotAuthorizedException exception) {
		exception.printStackTrace();
		// TODO Auto-generated method stub
		return Response.status(Response.Status.UNAUTHORIZED).
			    entity(new Error(Status.UNAUTHORIZED.getStatusCode(), Status.UNAUTHORIZED, exception.getMessage())).
			    build();		
		}

}
