package ipass.server.api;

import java.util.List;
import java.util.Set;

import javax.annotation.security.RolesAllowed;
import javax.inject.Inject;
import javax.validation.Valid;
import javax.validation.constraints.NotNull;
import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
import javax.ws.rs.NotAuthorizedException;
import javax.ws.rs.POST;
import javax.ws.rs.PUT;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.SecurityContext;
import javax.ws.rs.core.Response.Status;

import ipass.server.domain.*;
import ipass.server.service.AlertService;
import ipass.server.service.UserService;

@Path(value = "alerts")
public class AlertResource {
	private final AlertService alertService;
	private final UserService userService;
	@Inject
	public AlertResource() {
		this.alertService = ServiceProvider.getAlertservice();
		this.userService = ServiceProvider.getUserservice();
	}
	
	
    @GET
	@Produces(MediaType.APPLICATION_JSON)
    @RolesAllowed("zorgdrager")
	public Response getAlerts(@Context SecurityContext sc) {
    	Long userId = Long.parseLong(sc.getUserPrincipal().getName());
		return Response.ok().entity(this.userService.getAlertsByUser(userService.read(userId))).build();

	}
   
    @GET
	@Path(value = "/{id}")
	@Produces(MediaType.APPLICATION_JSON)
	public Response getAlert(@PathParam("id") Long id) {
		AlertEntity entity = (AlertEntity) alertService.read(id);
		Success<AlertEntity> message = new Success<AlertEntity>("AlertEntity met success opgehaald", Response.Status.OK, entity);

		return Response.ok(message, MediaType.APPLICATION_JSON).build();
	}
    
    @PUT
    @Produces(MediaType.TEXT_PLAIN)
    @Consumes(MediaType.APPLICATION_JSON)
    public Response createAlert(
    	@Valid AlertEntity alert
    ){
		alertService.create(alert);
		
    	return Response.status(Status.OK).entity("Wekker is met succes aangemaakt.").build();
    }
    
    @POST
    @Path(value = "/{id}")
    @RolesAllowed("zorgdrager")
    @Produces(MediaType.APPLICATION_JSON)
    @Consumes(MediaType.APPLICATION_JSON)
    public Response updateAlert(
    		@Context SecurityContext sc,
    		@PathParam("id") Long id,
    		@Valid AlertEntity alertEntity
    ){
    	AlertEntity alert = alertService.read(id);
    	if(alert.getHandled()){
			throw new NotAuthorizedException("Dit alarm is al afgehandeld!");
    	}
    	UserEntity user = userService.read(Long.parseLong(sc.getUserPrincipal().getName()));
    	alert.setHandled(alertEntity.getHandled());
    	alert.setHandledBy(user);
    	Success<AlertEntity> success = new Success<AlertEntity>("Melding met success aangepast", Response.Status.OK, alert);
    	//alert.setHandled(handled);
    	alertService.update(alertEntity);
    	return Response.ok(success, MediaType.APPLICATION_JSON).build();
    }
    
}
