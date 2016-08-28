package br.edson.sousa.controller;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import javax.annotation.PostConstruct;
import javax.enterprise.context.ConversationScoped;
import javax.faces.application.FacesMessage;
import javax.faces.context.FacesContext;
import javax.inject.Inject;
import javax.inject.Named;

import br.edson.sousa.data.InvoiceDao;
import br.edson.sousa.model.Customer;
import br.edson.sousa.model.ParkingInvoice;
import br.edson.sousa.model.ParkingRegister;
import br.edson.sousa.service.InvoiceService;

@Named
@ConversationScoped
public class InvoiceBean implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 5013811333931499032L;

	@Inject
	InvoiceService invoiceService;
	
	@Inject
	InvoiceDao invoiceDao;

	@Inject
	private FacesContext facesContext;
	
	private List<ParkingInvoice> invoiceList;

	public String calculateInvoice(Customer customer) {
		try {
			invoiceService.generateInvoice(customer);
			facesContext.addMessage(null,
					new FacesMessage(FacesMessage.SEVERITY_INFO, "Generated!", "Generation successful"));
		} catch (Exception e) {
			FacesMessage m = new FacesMessage(FacesMessage.SEVERITY_ERROR, "Generation failed.",
					"Generation Unsuccessful");
			facesContext.addMessage(null, m);
		}
		return findInvoicesCustomer(customer);
	}
	
	public String findInvoicesCustomer(Customer customer) {
		if (customer != null) {
			invoiceList = invoiceDao.findAllRegitersByCustomer(customer);

			if(invoiceList.isEmpty()){
				facesContext.addMessage(null,
						new FacesMessage(FacesMessage.SEVERITY_INFO, "No Data for Selected User.", ""));
			}
			return "invoices";
		} else {
			facesContext.addMessage(null,
					new FacesMessage(FacesMessage.SEVERITY_ERROR, "User Invalid. Please select a User", ""));
		}

		return "";
	}

	public List<ParkingInvoice> getInvoiceList() {
		return invoiceList;
	}

	public void setInvoiceList(List<ParkingInvoice> invoiceList) {
		this.invoiceList = invoiceList;
	}
}
