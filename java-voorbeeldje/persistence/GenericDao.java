package ipass.server.persistence;

import java.util.List;
import java.util.Set;

public interface GenericDao<T> {
	T read(Long id);
	void delete(T id);
	T update (T entity);
	T create (T entity);
	Set<T> list();
}
