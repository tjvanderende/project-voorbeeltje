package ipass.server.api;

import java.util.List;

import javax.annotation.security.RolesAllowed;
import javax.inject.Inject;
import javax.validation.Valid;
import javax.ws.rs.Consumes;
import javax.ws.rs.DELETE;
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

import ipass.server.domain.AlarmEntity;
import ipass.server.domain.Success;
import ipass.server.domain.UserEntity;
import ipass.server.service.AlarmService;
import ipass.server.service.UserService;

@Path(value = "alarms")
public class AlarmResource {

	private final AlarmService alarmService;
	private final UserService userService;

	@Inject
	public AlarmResource() {
		this.alarmService = ServiceProvider.getAlarmservice();
		this.userService = ServiceProvider.getUserservice();
	}

	@GET
	@RolesAllowed("patient") // alleen patiënt mag aanpassen
	@Produces(MediaType.APPLICATION_JSON)
	public Response getAlarms(@Context SecurityContext sc){
		Long userId = Long.parseLong(sc.getUserPrincipal().getName());
		System.out.print(userId);
		return Response.ok().entity(alarmService.filterByUser(userId)).build();
		//return Response.ok().entity(alarmService.list()).build();
	}
	
    @GET
	@Path(value = "/{id}")
	@RolesAllowed("patient") // alleen patiënt mag aanpassen
	@Produces(MediaType.APPLICATION_JSON)
	public Response getAlarm(
			@Context SecurityContext sc,
			@PathParam("id") Long id) {
    	try {
    		AlarmEntity entity = alarmService.read(id);
    		if(Long.parseLong(sc.getUserPrincipal().getName()) != entity.getUser().getId()) // check of de ingelogde gebruiker eigenaar is van de te bewerken alarm
				throw new NotAuthorizedException("Je bent geen eigenaar van dit alarm");
        	Success<AlarmEntity> message = new Success<AlarmEntity>("Wekker met success opgehaald", Response.Status.OK, entity);
    		return Response.ok(message, MediaType.APPLICATION_JSON).build();
    	} catch(NotAuthorizedException e) {
			return Response.status(Status.FORBIDDEN).entity(new ipass.server.api.exceptionHandler.Error(401, Status.FORBIDDEN, e.getMessage())).build();
		}

	}

    @DELETE
	@RolesAllowed("patient") // alleen patiënt mag aanpassen
    @Path(value = "/{id}")
    @Produces(MediaType.APPLICATION_JSON)
    public Response deleteCustomer(@PathParam("id") Long id) {
    	AlarmEntity entity = alarmService.read(id);
    	alarmService.delete(entity);
    	Success<AlarmEntity> message = new Success<AlarmEntity>("Wekker met success verwijderd", Response.Status.OK, entity);
        return Response.ok(message, MediaType.APPLICATION_JSON).build();
    }
	
    @PUT
	@RolesAllowed("patient") // alleen patiënt mag aanpassen
    @Produces(MediaType.APPLICATION_JSON)
    @Consumes(MediaType.APPLICATION_JSON)
    public Response createAlarm(
		@Context SecurityContext sc,
    	@Valid AlarmEntity alarmEntity
    ){
    	System.out.println(Long.parseLong(sc.getUserPrincipal().getName()));
    	UserEntity user = userService.read(Long.parseLong(sc.getUserPrincipal().getName()));
    	AlarmEntity newEntity = new AlarmEntity(alarmEntity.getDay(), alarmEntity.getTime(), alarmEntity.getActive(), alarmEntity.getKeepsRunning(), user);
    	alarmService.create(newEntity);
    	Success<AlarmEntity> message = new Success<AlarmEntity>("Wekker met success aangemaakt", Response.Status.OK, newEntity);
    	return Response.ok(message, MediaType.APPLICATION_JSON).build();
    }
    
    /**
     * Sla een nieuw alarm op -> alleen voor patiënt en als de user_id van de ingelogde patiënt is.
     * @param id , de ID van de alarm
     * @param alarmEntity, de attributen (time(optioneel), day(optioneel), active(optioneel), keepsrunning(optioneel), user_id(verplicht))
     * @return geef de geupdate AlarmEntity terug volgens het success response format.
     * @since v1.0
     * @see success response
     */
	@POST
	@Path(value = "/{id}")
	@RolesAllowed("patient") // alleen patiënt mag aanpassen
	@Produces(MediaType.APPLICATION_JSON)
	@Consumes(MediaType.APPLICATION_JSON)
	public Response updateAlarm(
			@Context SecurityContext sc,
			@PathParam("id") Long id,
			@Valid AlarmEntity alarmEntity
		) {
		try {
			
			if(Long.parseLong(sc.getUserPrincipal().getName()) != this.alarmService.read(id).getUser().getId()) // check of de ingelogde gebruiker eigenaar is van de te bewerken alarm
				throw new NotAuthorizedException("Je bent geen eigenaar van dit alarm");
			
			AlarmEntity editEntity = alarmService.read(id);
			editEntity.setActive(alarmEntity.getActive());
			editEntity.setDay(alarmEntity.getDay());
			editEntity.setKeepsRunning(alarmEntity.getKeepsRunning());
			editEntity.setTime(alarmEntity.getTime());
			alarmService.update(editEntity); // de nieuwe entiteit.
	    	Success<AlarmEntity> message = new Success<AlarmEntity>("Wekker met success aangepast", Response.Status.OK, editEntity);
			return Response.ok(message, MediaType.APPLICATION_JSON).build(); // geef de JSON terug.
		} catch(NotAuthorizedException e) {
			return Response.status(Status.FORBIDDEN).entity(new ipass.server.api.exceptionHandler.Error(401, Status.FORBIDDEN, e.getMessage())).build();
		}
		
	}     
}
