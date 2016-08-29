
package br.edson.sousa.service;

import java.util.Date;
import java.util.List;
import java.util.logging.Logger;

import javax.ejb.Stateless;
import javax.inject.Inject;

import br.edson.sousa.data.CompanyDao;
import br.edson.sousa.data.CustomerDao;
import br.edson.sousa.data.ParkingDao;
import br.edson.sousa.exception.ParkingException;
import br.edson.sousa.model.Customer;
import br.edson.sousa.model.ParkingCompany;
import br.edson.sousa.model.ParkingRegister;

@Stateless
public class ParkingService {

	private static final String PARKING_REGISTER_FAILED = "Parking Register failed: ";

	@Inject
	private Logger log;

	@Inject
	private ParkingDao parkingDao;

	@Inject
	private CustomerDao customerDao;

	@Inject
	private CompanyDao companyDao;

	@Inject
	private CustomerService customerService;

	@Inject
	private CompanyService companyService;

	public void registerParking(ParkingRegister parkingRegister) throws ParkingException {

		log.info("Registering " + parkingRegister.toString());
		parkingRegister.setCustomer(findOrinsertCustomer(parkingRegister.getCustomer()));
		parkingRegister.setCompany(findOrinsertCompany(parkingRegister.getCompany()));
		validateParkingRegister(parkingRegister);
		parkingDao.registerParking(parkingRegister);

	}

	private void validateParkingRegister(ParkingRegister parkingRegister) throws ParkingException {
		if(parkingRegister == null){
			throw new ParkingException(PARKING_REGISTER_FAILED + "Register null");
		}

		if(parkingRegister.getCustomer() == null || parkingRegister.getCustomer().getId() == null){
			throw new ParkingException(PARKING_REGISTER_FAILED + "Customer null");
		}

		if(parkingRegister.getStartParking() == null || parkingRegister.getStartParking().after(new Date())){
			throw new ParkingException(PARKING_REGISTER_FAILED + "Start date invalid");
		}

		if(parkingRegister.getStartParking() == null || parkingRegister.getFinishParking().before(parkingRegister.getStartParking())){
			throw new ParkingException(PARKING_REGISTER_FAILED + "Finish date invalid");
		}
		if(parkingRegister.getCompany() == null){
			throw new ParkingException(PARKING_REGISTER_FAILED + "Parking House null");
		}

		if(parkingRegister.getDateValueCalculated() != null){
			throw new ParkingException(PARKING_REGISTER_FAILED + "Register invalid");
		}
		
		//Could we make more validations here
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
	private Customer findOrinsertCustomer(Customer customer) throws ParkingException {
		Customer result = customer;
		if (customer != null && customer.getName() != null) {
			result = customerDao.findByName(customer.getName());
			if (result == null) {
				return customerService.registerCustomer(customer);
			}
		}
		return result;
	}


	private ParkingCompany findOrinsertCompany(ParkingCompany company) throws ParkingException {
		ParkingCompany result = company;
		if (company != null && company.getName() != null) {
			result = companyDao.findByName(company.getName());
			if (result == null) {
				return companyService.registerCompany(company);
			}
		}
		return result;
	}
}
