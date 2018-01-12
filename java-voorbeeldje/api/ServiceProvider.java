package ipass.server.api;
import ipass.server.service.AlertService;
import ipass.server.service.AlarmService;
import ipass.server.service.UserService;

public class ServiceProvider {
	private static AlarmService alarmService;
	private static UserService userService;
	private static AlertService alertService;
	
	public static AlarmService getAlarmservice(){
		alarmService = new AlarmService();
		return alarmService;
	}
	
	public static UserService getUserservice(){
		userService = new UserService();
		return userService;
	}
	
	public static AlertService getAlertservice(){
		alertService = new AlertService();
		return alertService;
	}
}
