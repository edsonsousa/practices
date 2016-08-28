
package br.edson.sousa.service;

import javax.ejb.Stateless;
import javax.inject.Inject;

import br.edson.sousa.data.CustomerDao;
import br.edson.sousa.exception.ParkingException;
import br.edson.sousa.model.Customer;

@Stateless
public class CustomerService {

	@Inject
	private CustomerDao customerDao;
	
	public Customer registerCustomer(Customer customer) throws ParkingException{
		
		//validate
		
		customerDao.registerCustomer(customer);
		return customer;
	}

}
