
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

	public Customer registerCustomer(Customer customer) throws ParkingException {

		// validate
		validateCustomer(customer);
		customerDao.registerCustomer(customer);
		return customer;
	}

	private void validateCustomer(Customer customer) throws ParkingException {
		if (customer == null) {
			throw new ParkingException("Customer null");
		}

		if (customer.getName() == null) {
			throw new ParkingException("Name Customer null");
		}

		if (customer.getPremium() == null) {
			throw new ParkingException("Flag Customer Premium null");
		}

		if (customer.getEmail() == null) {
			throw new ParkingException("Email Customer null");
		}

	}

}
