package br.edson.sousa.controller;

import java.io.Serializable;

import javax.enterprise.context.SessionScoped;
import javax.enterprise.inject.Model;
import javax.enterprise.inject.Produces;
import javax.faces.component.UIOutput;
import javax.faces.context.FacesContext;
import javax.faces.event.AjaxBehaviorEvent;
import javax.inject.Inject;
import javax.inject.Named;

import br.edson.sousa.data.CustomerDao;
import br.edson.sousa.model.Customer;
import br.edson.sousa.service.InvoiceService;

@Named
@SessionScoped
public class CustomerBean implements Serializable{

	/**
	 * 
	 */
	private static final long serialVersionUID = 6956913726292252805L;

	@Inject
	InvoiceService invoiceService;
	
	@Inject
	CustomerDao customerDao;

	@Inject
	private FacesContext facesContext;

	
	private Customer customerSelected;


	public void valueChangedCustomerSelected(AjaxBehaviorEvent e){
		customerSelected = (Customer) ((UIOutput)e.getSource()).getValue();
	}
	
	@Produces
    @Named
	public Customer getCustomerSelected() {
		return customerSelected;
	}

	public void setCustomerSelected(Customer customerSelected) {
		this.customerSelected = customerSelected;
	}

}
