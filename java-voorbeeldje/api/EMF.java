package ipass.server.api;

import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;

public final class EMF {
    private static final EntityManagerFactory emfInstance = Persistence.createEntityManagerFactory("persist-unit");

    private EMF() {}

    public static EntityManagerFactory get() {
        return emfInstance;
    }
}