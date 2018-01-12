package ipass.server.api;

import java.security.Key;


import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import javax.inject.Inject;
import javax.validation.Valid;
import javax.ws.rs.Consumes;
import javax.ws.rs.DELETE;
import javax.ws.rs.FormParam;
import javax.ws.rs.GET;
import javax.ws.rs.NotAuthorizedException;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.SecurityContext;
import javax.ws.rs.core.Response.Status;


import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.impl.crypto.MacProvider;
import ipass.server.domain.*;
import ipass.server.service.AlarmService;
import ipass.server.service.UserService;

import javax.annotation.security.RolesAllowed;
import javax.crypto.spec.SecretKeySpec;

import javax.ws.rs.core.UriInfo;
import ipass.server.service.*;
@Path(value = "users")
public class UserResource {
	@Context
	UriInfo uriInfo;

	@Inject
	private UserService userService;
	
	@Inject
	private AlertService alertService;
	
	@Inject AlarmService alarmService;

	public UserResource() {
		this.userService = ServiceProvider.getUserservice();
		this.alertService = ServiceProvider.getAlertservice();
		this.alarmService = ServiceProvider.getAlarmservice();
	}

	final public static Key key = MacProvider.generateKey();
	/**
	 * Geef een gebruiker terug
	 * 
	 * @param id
	 *            De ID van de gebruiker die ingeladen moet worden.
	 * @return
	 */
	@GET
	@Path(value = "/{id}")
	@Produces(MediaType.APPLICATION_JSON)
	public Response getUser(@PathParam("id") Long id) {
		UserEntity entity = this.userService.read(id);
		Success<UserEntity> message = new Success<UserEntity>("Gebruiker met success opgehaald", Response.Status.OK, entity);
		return Response.ok(message, MediaType.APPLICATION_JSON).build();
	}


	/**
	 * Haal patiënten op van een zorgdrager
	 * @param id
	 * @return
	 */
	@GET
	@Path(value = "/")
	@RolesAllowed("zorgdrager")
	@Produces(MediaType.APPLICATION_JSON)
	public Set<UserEntity> getUserPatients(@Context SecurityContext sc) {
		UserEntity entity = this.userService.read(Long.parseLong(sc.getUserPrincipal().getName()));
		Set<UserEntity> patients = this.userService.getPatientsByUser(entity);
		return patients;
	}

	/**
	 * Update latitude / longitude (Momenteel is alleen dit ondersteunt).
	 * 
	 * @param latitude
	 * @param longitude
	 * @return
	 */
	@POST
	@Path(value = "/location")
	@RolesAllowed("patient")
	@Produces(MediaType.APPLICATION_JSON)
	@Consumes(MediaType.APPLICATION_JSON)
	public Response updateLocation(@Context SecurityContext sc, UserEntity userEntity) {

		// throw new WebApplicationException();
		UserEntity user = userService.read(Long.parseLong(sc.getUserPrincipal().getName()));
		user.setLatitude(userEntity.getLatitude());
		user.setLongitude(userEntity.getLongitude());
		userService.update(user);
		Success<UserEntity> success = new Success<UserEntity>("Patiënt is met succes ge-update.", Response.Status.OK, user);
		return Response.ok(success).build();

	}

	@POST
	@Path("/login")
	@Produces(MediaType.APPLICATION_JSON)
	@Consumes(MediaType.APPLICATION_FORM_URLENCODED)
	public Response authenticateUser(@FormParam("email") String email, @FormParam("password") String password) {
		HashMap<String, Object> map = new HashMap<String, Object>();

		try {
			UserEntity user = userService.loginUser(email, password);
			// Issue a token for the user
			String token = issueToken(user.getId(), user.getRole());
			map.put("role", user.getRole());
			map.put("user", user);
			map.put("token", token);
			map.put("zorgdragers", user.getZorgdragers()); 
			Success<HashMap<String, Object>> responseWithToken = new Success<HashMap<String, Object>>("Je bent met succes ingelogd.", Response.Status.OK, map);

			// Return the token on the response
			return Response.ok(responseWithToken, MediaType.APPLICATION_JSON).build();
		} catch (Exception e) {
			e.printStackTrace();
			return Response.status(Status.UNAUTHORIZED).build();
		}
		// Authenticate the user using the credentials provided

	}
	@GET
	@Path("/authenticated")
	@RolesAllowed({"patient", "zorgdrager"})
	@Produces(MediaType.APPLICATION_JSON)
	public Response isAuthenticated(@Context SecurityContext sc){
		HashMap<String, Object> map = new HashMap<String, Object>();
		try {
			UserEntity user = userService.read(Long.parseLong(sc.getUserPrincipal().getName()));
			map.put("role", sc.isUserInRole("zorgdrager") ? "zorgdrager" : "patient");
			map.put("user", user);
			Success<HashMap<String, Object>>successMessage = new Success<HashMap<String, Object> >("Je bent ingelogd", Response.Status.OK, map);
			return Response.ok(successMessage, MediaType.APPLICATION_JSON).build();
		} catch(NotAuthorizedException e){
			e.printStackTrace();
			return Response.status(Status.UNAUTHORIZED).build();
		}
	}
	/**
	 * Geef een JSON webtoken terug
	 * - Header
	 * - Payload -> claim, de rol die de gebruiker krijgt toegewezen en subject is de gebruikersnaam van de gebruiker.
	 * - Signature -> Versleuteling van de data met de secret (UserResource.key)
	 * @param login de id waar de claim aan wordt gekoppeld (Subject)
	 * @param isZorgdrager Geef een boolean mee of de gebruiker een zorgdrager is of patient.
	 * (isZorgdrager) ? rol=zorgdrager : rol = patient;
	 * @return Geef de token terug als string.
	 */
	private String issueToken(Long userId, String role) {
		Key key = UserResource.key;
		String jwtToken = Jwts.builder().setSubject(userId+"").claim("role", role).setIssuer(this.uriInfo.getAbsolutePath().toString())
				.setIssuedAt(new Date()).setExpiration(toDate(LocalDateTime.now().plusMinutes(15L)))
				.signWith(SignatureAlgorithm.HS512, key).compact();
		return jwtToken;
	}

	private Date toDate(LocalDateTime localDateTime) {
		return Date.from(localDateTime.atZone(ZoneId.systemDefault()).toInstant());
	}
	
	/**
	 * 1. Haal alle zorgdragers op van een patiënt.
	 * 2. Loop door alle zorgdragers heen van deze patiënt. 
	 * 3. Maak voor iedere zorgdrager een Alert aan met het de meegegeven Alarm.
	 * * Let op, op dit moment zijn er Native al SMS berichten gestuurd! 
	 * @param sc
	 * @return
	 */
	@POST
	@Path("/makealerts/{alarmId}")
	@RolesAllowed("patient")
	@Produces(MediaType.APPLICATION_JSON)
	public Response makeAlertsForZorgdragers(@Context SecurityContext sc, @PathParam("alarmId") Long id) {
		UserEntity associatedUser = userService.read(Long.parseLong(sc.getUserPrincipal().getName())); // haal de ingelogde gebruiker op.
		Set<UserEntity> zorgdragers = new HashSet<UserEntity>(associatedUser.getZorgdragers()); // haal zorgdragers op
		AlarmEntity alarm = alarmService.read(id);
		AlertEntity alert = new AlertEntity(false, alarm); // maak eerst een nieuwe alert aan
		alertService.create(alert); // maak deze alert aan
		for(UserEntity entity : zorgdragers){ // maak voor iedere zorgdrager een alert aan
			entity.getAlerts().add(alert); // koppel de nieuwe alert eraan vast 
			userService.update(entity); // sla de zorgdrager op
		}
		
		Success<Object> success = new Success<Object>("Meldingen met success aangemaakt", Status.OK, null);
		
		return Response.ok(success, MediaType.APPLICATION_JSON).build();
		
	}
/*	
    @DELETE
	@RolesAllowed("patient") // alleen patiënt mag aanpassen
    @Path(value = "/alarms/{id}")
    @Produces(MediaType.APPLICATION_JSON)
    public Response deleteCustomer(@Context SecurityContext sc,@PathParam("id") Long id) {
    	AlarmEntity entity = alarmService.read(id);
    	UserEntity user = userService.read(Long.parseLong(sc.getUserPrincipal().getName()));
    	user.getAlarms().remove(entity);
    	alarmService.delete(entity);
    	userService.update(user);
    	Success<AlarmEntity> message = new Success<AlarmEntity>("Wekker met success verwijderd", Response.Status.OK, entity);
        return Response.ok(message, MediaType.APPLICATION_JSON).build();
    }
    
    */
	/*
	 * Delete is momenteel niet van belang.
	 * 
	 * @DELETE
	 * 
	 * @Path(value = "/delete/{id}")
	 * 
	 * @Produces(MediaType.TEXT_PLAIN) public Response
	 * deleteUser(@PathParam("id") Long id) { AlarmEntity entity =
	 * alarmService.read(id); alarmService.delete(entity);
	 * 
	 * return
	 * Response.status(Status.OK).entity("Wekker is met succes verwijderd.").
	 * build(); } Ook de update functionaliteit staat momenteel uit.
	 * 
	 * @PUT
	 * 
	 * @Path(value = "/")
	 * 
	 * @Produces(MediaType.TEXT_PLAIN) public Response createAlarm(
	 * 
	 * @FormParam("firstname") String fn,
	 * 
	 * @FormParam("lastname") String ln,
	 * 
	 * @FormParam("address") String ad,
	 * 
	 * @FormParam("residence") String res,
	 * 
	 * @FormParam("email") String email,
	 * 
	 * @FormParam("password") String password,
	 * 
	 * @FormParam("confirm_password") String confpass,
	 * 
	 * @FormParam("phone") int phone,
	 * 
	 * @FormParam("latitude") int latitude,
	 * 
	 * @FormParam("longitude") int longitude ){ try { UserEntity user = new
	 * UserEntity(); userService.create(user); return
	 * Response.status(Status.OK).entity("Gebruiker is met succes aangemaakt.").
	 * build(); } catch(IllegalArgumentException e){ return
	 * Response.status(Status.BAD_REQUEST).
	 * entity("De gegevens die zijn meegegeven zijn niet juist").build(); }
	 * 
	 * }
	 */
	/*
	 * @POST
	 * 
	 * @Path(value = "/update")
	 * 
	 * @Produces(MediaType.APPLICATION_JSON) public Response saveCustomer(
	 * 
	 * @FormParam("active") int active,
	 * 
	 * @FormParam("keeps_running") int keepsRunning,
	 * 
	 * @FormParam("day") String day,
	 * 
	 * @FormParam("time") String time,
	 * 
	 * @FormParam("user_id") Long userId ) { AlarmEntity alarm = new
	 * AlarmEntity(day, time, active, keepsRunning, userId);
	 * alarmService.update(alarm); return
	 * Response.status(Status.OK).entity("Wekker is met succes opgeslagen").type
	 * (MediaType.APPLICATION_JSON).build(); }
	 */
}
