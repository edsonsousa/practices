package br.edson.sousa.controller;

import java.util.ArrayList;
import java.util.List;

import javax.enterprise.inject.Model;
import javax.faces.application.FacesMessage;
import javax.faces.context.FacesContext;
import javax.inject.Inject;

import br.edson.sousa.data.ParkingDao;
import br.edson.sousa.model.Customer;
import br.edson.sousa.model.ParkingRegister;
import br.edson.sousa.service.ParkingService;

@Model
public class ParkingBean {

	@Inject
	ParkingService parkingService;

	@Inject
	ParkingDao parkingDao;

	@Inject
	private FacesContext facesContext;

	private List<ParkingRegister> parkingLog = new ArrayList<ParkingRegister>();

	public String findParkingLogCustomer(Customer customer) {
		if (customer != null) {
			parkingLog = parkingDao.findAllRegitersByCustomer(customer);

			return "parkingLog";
		} else {
			facesContext.addMessage(null,
					new FacesMessage(FacesMessage.SEVERITY_ERROR, "User Invalid. Please select a User", ""));
		}

		return "";
	}

	public List<ParkingRegister> getParkingLog() {
		return parkingLog;
	}

	public void setParkingLog(List<ParkingRegister> parkingLog) {
		this.parkingLog = parkingLog;
	}

}
