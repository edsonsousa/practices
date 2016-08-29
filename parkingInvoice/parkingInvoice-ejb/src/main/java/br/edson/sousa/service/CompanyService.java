
package br.edson.sousa.service;

import javax.ejb.Stateless;
import javax.inject.Inject;

import br.edson.sousa.data.CompanyDao;
import br.edson.sousa.exception.ParkingException;
import br.edson.sousa.model.ParkingCompany;

@Stateless
public class CompanyService {

	@Inject
	private CompanyDao companyDao;

	public ParkingCompany registerCompany(ParkingCompany company) throws ParkingException {

		// validate
		validateCompany(company);
		companyDao.registerCompany(company);
		return company;
	}

	private void validateCompany(ParkingCompany company) throws ParkingException {
		if (company == null) {
			throw new ParkingException("Company null");
		}

		if (company.getName() == null) {
			throw new ParkingException("Name Comapny null");
		}

	}

}
