
package br.edson.sousa.service;

import java.util.List;
import java.util.logging.Logger;

import javax.ejb.Stateless;
import javax.inject.Inject;

import br.edson.sousa.data.CustomerDao;
import br.edson.sousa.data.ParkingDao;
import br.edson.sousa.exception.ParkingException;
import br.edson.sousa.model.Customer;
import br.edson.sousa.model.ParkingRegister;

@Stateless
public class ParkingService {

	@Inject
	private Logger log;

	@Inject
	private ParkingDao parkingDao;

	@Inject
	private CustomerDao customerDao;

	public void registerParking(ParkingRegister parkingRegister) throws Exception {

		log.info("Registering " + parkingRegister.getCustomer().getName());
		parkingRegister.setCustomer(insertCustomer(parkingRegister.getCustomer()));
		parkingDao.registerParking(parkingRegister);

	}

	public void addListParkingRegister(List<ParkingRegister> parkingList) throws Exception {
		log.info("Registering " + parkingList.size() + " registers");
		for (ParkingRegister parkingRegister : parkingList) {
			registerParking(parkingRegister);
		}

	}

	/**
	 * Insert customer only if don't exists. If exists, return complete object.
	 *
	 * @param customer
	 * @return Inserted customer or existent object or parameter if is invalid
	 *         value
	 * @throws ParkingException
	 */
	private Customer insertCustomer(Customer customer) throws ParkingException {
		Customer result = customer;
		if (customer != null && customer.getName() != null) {
			result = customerDao.findByName(customer.getName());
			if (result == null) {
				return customerDao.registerCustomer(customer);
			}
		}
		return result;
	}
}
