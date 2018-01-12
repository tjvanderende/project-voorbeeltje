package ipass.server.persistence;

import com.mysql.jdbc.log.Log;
import ipass.server.api.EMF;

import java.lang.reflect.ParameterizedType;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Set;
import java.util.logging.Level;

import javax.inject.Singleton;
import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;
import javax.persistence.Table;
import javax.validation.*;
import javax.ws.rs.NotAllowedException;
import javax.ws.rs.NotFoundException;

@Singleton
public class GenericDaoImpl<T extends Object> implements GenericDao<T> {
	
	@PersistenceContext
	public EntityManager entityManager = EMF.get().createEntityManager();
	
    protected Class<T> entityClass;
	private boolean constraintValidationsDetected(T entity) {
		ValidatorFactory factory = Validation.buildDefaultValidatorFactory();
		Validator validator = factory.getValidator();
		Set<ConstraintViolation<T>> constraintViolations = validator.validate(entity);
		if (constraintViolations.size() > 0) {
			Iterator<ConstraintViolation<T>> iterator = constraintViolations.iterator();
			while (iterator.hasNext()) {
				ConstraintViolation<T> cv = iterator.next();
				System.err.println(cv.getRootBeanClass().getName() + "." + cv.getPropertyPath() + " " + cv.getMessage());

				//JsfUtil.addErrorMessage(cv.getRootBeanClass().getSimpleName() + "." + cv.getPropertyPath() + " " + cv.getMessage());
			}
			return true;
		}
		else {
			return false;
		}
	}

	@SuppressWarnings("unchecked")
	public GenericDaoImpl (){
    	ParameterizedType genericSuperclass = (ParameterizedType) getClass()
                .getGenericSuperclass();
           this.entityClass = (Class<T>) genericSuperclass
                .getActualTypeArguments()[0];
          
        this.entityManager =  EMF.get().createEntityManager();
    }
    /**
     * Lees een entiteit uit met een meegegeven ID
     * @param Long id , de Primary key van het object die je wilt inlezen.
     * @throws NotFoundException als de entiteit niet is gevonden.
     */
	public T read(Long id) {
		// TODO Auto-generated method stub
		T entity = entityManager.find(this.entityClass, id);
		if(entity == null)
			throw new NotFoundException("Helaas is de entiteit niet gevonden");
		
		return entity;	
	}

	public void delete(T t) {
		if(!this.entityManager.getTransaction().isActive())
			this.entityManager.getTransaction().begin();
		
		t = this.entityManager.merge(t);
		this.entityManager.remove(t);
		this.entityManager.getTransaction().commit();
		
	}

	public T update(T entity) {
		if(!this.entityManager.getTransaction().isActive())
			this.entityManager.getTransaction().begin();
		
		this.entityManager.merge(entity);
        this.entityManager.getTransaction().commit();

		return entity;

	}

	public T create(T entity) {
		if(!this.entityManager.getTransaction().isActive())
			this.entityManager.getTransaction().begin();
		
		this.entityManager.persist(entity);
		this.entityManager.getTransaction().commit();

		return entity;
	}

	@SuppressWarnings("unchecked")
	public Set<T> list() {
		List<T> query = (List<T>) entityManager.createQuery("FROM "+this.entityClass.getName()).getResultList();
		Set<T> set = new HashSet(query);
		
		// TODO Auto-generated method stub
		return set;
	}

	@SuppressWarnings("unchecked")
	public List<T> getPaginationList(int page, int size) {
	    return entityManager.createQuery("SELECT a FROM "+this.entityClass.getAnnotation(Table.class).name()+" a")
	        .setFirstResult(page * size)
	        .setMaxResults(size)
	        .getResultList();
	}
}
