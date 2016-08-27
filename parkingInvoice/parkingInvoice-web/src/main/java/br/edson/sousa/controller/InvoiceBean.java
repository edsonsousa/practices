package br.edson.sousa.controller;

import java.util.ArrayList;
import java.util.List;

import javax.annotation.PostConstruct;
import javax.enterprise.inject.Model;
import javax.faces.application.FacesMessage;
import javax.faces.context.FacesContext;
import javax.inject.Inject;

import br.edson.sousa.model.ParkingRegister;
import br.edson.sousa.service.InvoiceService;

@Model
public class InvoiceBean {

	@Inject
	InvoiceService invoiceService;

	@Inject
	private FacesContext facesContext;

	private List<ParkingRegister> parkingList;

	public void calculateInvoice() throws Exception {
		try {
			invoiceService.generateInvoice(parkingList);
			facesContext.addMessage(null,
					new FacesMessage(FacesMessage.SEVERITY_INFO, "Generated!", "Generation successful"));
		} catch (Exception e) {
			FacesMessage m = new FacesMessage(FacesMessage.SEVERITY_ERROR, "Generation failed.",
					"Generation Unsuccessful");
			facesContext.addMessage(null, m);
		}
	}

	@PostConstruct
	public void initNewMember() {
		parkingList = new ArrayList<ParkingRegister>();
	}

	public List<ParkingRegister> getParkingList() {
		return parkingList;
	}

	public void setParkingList(List<ParkingRegister> parkingList) {
		this.parkingList = parkingList;
	}
}
