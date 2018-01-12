package ipass.server.service;


import java.util.HashSet;
import java.util.List;
import java.util.Set;

import javax.inject.Singleton;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;

import ipass.server.domain.AlarmEntity;
import ipass.server.persistence.AlarmDao;
import ipass.server.persistence.GenericDaoImpl;
@Singleton

public class AlarmService extends GenericDaoImpl<AlarmEntity> implements AlarmDao {
	@SuppressWarnings("unchecked")
	public Set<AlarmEntity> filterByUser(Long user_id){
	//	CriteriaBuilder builder = this.entityManager.getCriteriaBuilder();
	//	CriteriaQuery<AlarmEntity> criteria = builder.createCriteria(AlarmEntity.class);
		List<AlarmEntity> query = (List<AlarmEntity>) entityManager.createQuery("FROM AlarmEntity where user_id = :user_id order by day").setParameter("user_id", user_id).getResultList();
		Set<AlarmEntity> set = new HashSet(query); 
		return set;
	}
}
