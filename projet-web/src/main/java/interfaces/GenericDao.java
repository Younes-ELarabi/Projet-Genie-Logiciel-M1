package interfaces;

import java.util.List;

import elements.Reservation;

/**
 * 
 * functions that needed for all daos
 *
 * @param <E>
 */
public interface GenericDao<E> {

	
	public String addElement(E e) throws Exception;
	
	public E modifyElement(String id,E element) throws Exception;
	
	public boolean deleteElement(E e) throws Exception;
	
	public boolean deleteElement(String id);
	
	public List<E> consultElement() throws Exception;

	
}
