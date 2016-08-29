package br.edson.sousa.test;

import java.math.BigDecimal;
import java.util.Calendar;
import java.util.Date;

import br.edson.sousa.model.Customer;
import br.edson.sousa.model.ParkingCompany;
import br.edson.sousa.model.ParkingInvoice;
import br.edson.sousa.model.ParkingRegister;

public class TestUtil {

	public static ParkingRegister createParkingRegister(Boolean daylight, Integer minutesParking) {
		ParkingRegister parkingRegister = new ParkingRegister();
		parkingRegister.setId(1L);
		parkingRegister.setCustomer(createCustomer());

		Date date = new Date();
		Calendar calendar = Calendar.getInstance();
		calendar.setTime(date);
		calendar.set(Calendar.HOUR, 8);
		calendar.set(Calendar.AM_PM, Calendar.AM);
		if (!daylight) {
			calendar.add(Calendar.HOUR, 12);
		}
		calendar.add(Calendar.DAY_OF_YEAR, -100);

		parkingRegister.setStartParking(calendar.getTime());
		calendar.add(Calendar.MINUTE, minutesParking);

		parkingRegister.setFinishParking(calendar.getTime());
		parkingRegister.setCompany(createCompany());
		return parkingRegister;
	}

	public static Customer createCustomer() {
		Customer customer = new Customer();
		customer.setId(1L);
		customer.setName("nameCustomer");
		customer.setEmail("asd@ghl.com");
		customer.setPremium(false);
		return customer;
	}
	
	public static ParkingCompany createCompany() {
		ParkingCompany company = new ParkingCompany();
		company.setId(1L);
		company.setName("nameCustomer");
		return company;
	}

	public static ParkingInvoice createInvoice() {
		ParkingInvoice invoice = new ParkingInvoice();
		invoice.setId(1L);
		invoice.setCompany(createCompany());
		invoice.setCustomer(createCustomer());
		invoice.setDateGenerated(new Date());
		invoice.setMonthYearReference("09/15");
		invoice.setTotalInvoice(new BigDecimal(0));
		return invoice;
	}
}
