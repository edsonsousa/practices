package br.edson.sousa.controller;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import javax.enterprise.context.ConversationScoped;
import javax.enterprise.context.SessionScoped;
import javax.enterprise.inject.Model;
import javax.faces.application.FacesMessage;
import javax.faces.context.FacesContext;
import javax.inject.Inject;

import br.edson.sousa.data.ParkingDao;
import br.edson.sousa.model.Customer;
import br.edson.sousa.model.ParkingRegister;
import br.edson.sousa.service.ParkingService;

@Model
@SessionScoped
public class ParkingBean implements Serializable{

	/**
	 * 
	 */
	private static final long serialVersionUID = 3355878650765828923L;

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

			if(parkingLog.isEmpty()){
				facesContext.addMessage(null,
						new FacesMessage(FacesMessage.SEVERITY_INFO, "No Data for Selected User.", ""));
			}
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
