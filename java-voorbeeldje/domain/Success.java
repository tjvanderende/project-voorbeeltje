package ipass.server.domain;

import java.io.Serializable;

import javax.ws.rs.core.Response.Status;

/**
 * Dit is geen database object. 
 * De structuur van deze object wordt aangehouden om een consistent succes-bericht te sturen.
 * @author tjvan
 *
 */
public class Success<T extends Object> implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 7708026800413514601L;
	
	private String message;
	private Status status;
	private T data;
	public Success(String m, Status s) {
		this(m, s, null);
	}
	public Success(String m, Status s, T entity){
		this.message = m;
		this.status = s;
		this.setData(entity);
	}
	public Status getStatus() {
		return status;
	}
	public void setStatus(Status status) {
		this.status = status;
	}
	public String getMessage() {
		return message;
	}
	public void setMessage(String message) {
		this.message = message;
	}
	public T getData() {
		return data;
	}
	public void setData(T data) {
		this.data = data;
	}
	
}
