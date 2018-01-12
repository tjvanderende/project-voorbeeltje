package ipass.server.config;

import javax.inject.Singleton;


import org.glassfish.hk2.utilities.binding.AbstractBinder;

import ipass.server.service.AlarmService;
import ipass.server.service.AlertService;
import ipass.server.service.UserService;

public class ApplicationBinder extends AbstractBinder {
	
    @Override
    protected void configure() {
    	// services
        //bind(AlarmServiceImpl.class).to(AlarmServiceOld.class).in(Singleton.class);
        
        // dao
    //	bind(GenericDaoImpl.class).to(GenericDao.class).in(Singleton.class);
    	bind(AlertService.class).to(AlertService.class).in(Singleton.class);
        bind(AlarmService.class).to(AlarmService.class).in(Singleton.class);
        bind(UserService.class).to(UserService.class).in(Singleton.class);
    }
}