package ipass.server.service;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

import javax.inject.Singleton;

import ipass.server.domain.AlarmEntity;
import ipass.server.domain.AlertEntity;
import ipass.server.persistence.AlertDao;
import ipass.server.persistence.GenericDaoImpl;
@Singleton

public class AlertService extends GenericDaoImpl<AlertEntity> implements AlertDao {
	
}
