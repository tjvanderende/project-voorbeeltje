package ipass.server.service;

import java.util.List;
import java.util.Set;

import javax.inject.Singleton;
import javax.persistence.Query;
import javax.persistence.TypedQuery;

import ipass.server.domain.AlarmEntity;
import ipass.server.domain.AlertEntity;
import ipass.server.domain.UserEntity;
import ipass.server.persistence.GenericDaoImpl;
import ipass.server.persistence.UserDao;

@Singleton
public class UserService extends GenericDaoImpl<UserEntity> implements UserDao {

	public Set<AlarmEntity> getAlarmsByUser(UserEntity user) {
		// TODO Auto-generated method stub
		return user.getAlarms();
	}

	public Set<AlertEntity> getAlertsByUser(UserEntity user) {
		// TODO Auto-generated method stub
		return user.getAlerts();
	}

	public Set<UserEntity> getPatientsByUser(UserEntity user) {
		// TODO Auto-generated method stub
		return user.getPatients();
	}

	public UserEntity loginUser(String email, String password) {
		// TODO fout oplossen met dat de query hardcoded moet zijn.
		try{
			Query query = this.entityManager.createNativeQuery("SELECT * FROM users u WHERE email = :email AND password = :password", UserEntity.class);
	        query.setParameter("email", email);
	        query.setParameter("password", password);
	        UserEntity user = (UserEntity) query.getSingleResult();
	        System.out.print(user.toString());
	        return user;

	   //     if (user == null)
	   //         throw new SecurityException("Invalid user/password");
		} catch(Exception e){
			e.printStackTrace();
			return null;
		}
		
	}
	
	
}
