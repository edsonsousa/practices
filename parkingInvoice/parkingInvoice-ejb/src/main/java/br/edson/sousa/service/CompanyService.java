
package br.edson.sousa.service;

import javax.ejb.Stateless;
import javax.inject.Inject;

import br.edson.sousa.data.CompanyDao;
import br.edson.sousa.data.CustomerDao;
import br.edson.sousa.exception.ParkingException;
import br.edson.sousa.model.Customer;
import br.edson.sousa.model.ParkingCompany;

@Stateless
public class CompanyService {

	@Inject
	private CompanyDao companyDao;
	
	public ParkingCompany registerCompany(ParkingCompany company) throws ParkingException{
		
		//validate
		
		companyDao.registerCompany(company);
		return company;
	}

}
