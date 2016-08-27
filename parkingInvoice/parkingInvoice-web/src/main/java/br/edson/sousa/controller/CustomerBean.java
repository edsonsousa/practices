package br.edson.sousa.controller;

import javax.enterprise.inject.Model;
import javax.faces.context.FacesContext;
import javax.inject.Inject;

import br.edson.sousa.model.Customer;
import br.edson.sousa.service.InvoiceService;

@Model
public class CustomerBean {

	@Inject
	InvoiceService invoiceService;

	@Inject
	private FacesContext facesContext;

	private Customer customerSelected;

	public Customer getCustomerSelected() {
		return customerSelected;
	}

	public void setCustomerSelected(Customer customerSelected) {
		this.customerSelected = customerSelected;
	}

}
