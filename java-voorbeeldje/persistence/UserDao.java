package ipass.server.persistence;

import ipass.server.domain.AlarmEntity;
import ipass.server.domain.AlertEntity;
import ipass.server.domain.UserEntity;

import java.util.List;
import java.util.Set;

public interface UserDao extends GenericDao<UserEntity> {
	public Set<AlarmEntity> getAlarmsByUser(UserEntity user);
	public Set<AlertEntity> getAlertsByUser(UserEntity user);
	public Set<UserEntity> getPatientsByUser(UserEntity user);
	public UserEntity loginUser(String email, String password);
}
