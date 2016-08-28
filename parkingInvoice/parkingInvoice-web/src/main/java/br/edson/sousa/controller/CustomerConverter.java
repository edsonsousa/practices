package br.edson.sousa.controller;

import javax.enterprise.context.RequestScoped;
import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;
import javax.faces.convert.Converter;
import javax.inject.Inject;
import javax.inject.Named;

import br.edson.sousa.data.CustomerDao;
import br.edson.sousa.model.Customer;

@Named
@RequestScoped
public class CustomerConverter implements Converter {

    @Inject
    private CustomerDao customerDao;
    
	@Override
	public Object getAsObject(FacesContext context, UIComponent component, String value) {
		if(value == null){
			return value;
		}else{
			return customerDao.findByName(value);
		}
	}

	@Override
	public String getAsString(FacesContext context, UIComponent component, Object value) {
		if(value != null && !"".equals(value)){
			return ((Customer)value).getName();
		}

		return null;
	}

}
