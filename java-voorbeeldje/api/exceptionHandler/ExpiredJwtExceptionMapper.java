package ipass.server.api.exceptionHandler;

import javax.ws.rs.core.Response;
import javax.ws.rs.core.Response.Status;
import javax.ws.rs.ext.ExceptionMapper;
import javax.ws.rs.ext.Provider;

import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.SignatureException;
@Provider
public class ExpiredJwtExceptionMapper implements ExceptionMapper<SignatureException> {

	@Override
	public Response toResponse(SignatureException exception) {
		// TODO Auto-generated method stub
		return Response.status(Response.Status.UNAUTHORIZED)
				.entity(new Error(Status.UNAUTHORIZED.getStatusCode(), Status.UNAUTHORIZED, "Er is iets fout gegaan met het authenticeren")).build();
	}

}
