package ipass.server.api.exceptionHandler;

import javax.ws.rs.core.Response.Status;

public class Error {
	public int code;
	public Status status;
	public String message;
	
	public Error(int code, Status notFound, String message){
		this.code = code;
		this.message = message;
		this.status = notFound;
	}
}
