package br.edson.sousa.test;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

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

	public static List<ParkingRegister> generateListParking() {
		Customer customer = TestUtil.createCustomer();
		customer.setPremium(true);
		List<ParkingRegister> listParking = new ArrayList<ParkingRegister>();

		listParking.add(TestUtil.createParkingRegister(true, 30 - 1));
		listParking.add(TestUtil.createParkingRegister(true, 30));
		listParking.add(TestUtil.createParkingRegister(true, 30));
		listParking.add(TestUtil.createParkingRegister(true, 30));
		listParking.get(1).setCompany(listParking.get(0).getCompany());
		listParking.get(2).setCompany(listParking.get(0).getCompany());
		listParking.get(3).setCompany(listParking.get(0).getCompany());

		listParking.get(0).setCustomer(customer);
		listParking.get(1).setCustomer(customer);
		listParking.get(2).setCustomer(customer);
		listParking.get(3).setCustomer(customer);

		// 08:12 – 10:45 (6 * 1.00 = 6.00 EUR)
		Calendar calendar = Calendar.getInstance();
		calendar.setTime(listParking.get(0).getStartParking());
		calendar.set(Calendar.HOUR, 8);
		calendar.set(Calendar.MINUTE, 12);
		calendar.set(Calendar.AM_PM, Calendar.AM);

		listParking.get(0).setStartParking(calendar.getTime());

		calendar.setTime(listParking.get(0).getFinishParking());
		calendar.set(Calendar.HOUR, 10);
		calendar.set(Calendar.MINUTE, 45);
		calendar.set(Calendar.AM_PM, Calendar.AM);

		listParking.get(0).setFinishParking(calendar.getTime());
		System.out.println(listParking.get(0).getStartParking() + " - " + listParking.get(0).getFinishParking());

		// 07:02 – 11:56 (10 * 1.00 = 10.00 EUR)
		calendar = Calendar.getInstance();
		calendar.setTime(listParking.get(1).getStartParking());
		calendar.set(Calendar.HOUR, 7);
		calendar.set(Calendar.MINUTE, 2);
		calendar.set(Calendar.AM_PM, Calendar.AM);

		listParking.get(1).setStartParking(calendar.getTime());

		calendar.setTime(listParking.get(1).getFinishParking());
		calendar.set(Calendar.HOUR, 11);
		calendar.set(Calendar.MINUTE, 56);
		calendar.set(Calendar.AM_PM, Calendar.AM);

		listParking.get(1).setFinishParking(calendar.getTime());
		System.out.println(listParking.get(1).getStartParking() + " - " + listParking.get(1).getFinishParking());

		// 22:10 – 22:35 (1 * 0.75 = 0.75 EUR)
		calendar = Calendar.getInstance();
		calendar.setTime(listParking.get(2).getStartParking());
		calendar.set(Calendar.HOUR, 10);
		calendar.set(Calendar.MINUTE, 10);
		calendar.set(Calendar.AM_PM, Calendar.PM);

		listParking.get(2).setStartParking(calendar.getTime());

		calendar.setTime(listParking.get(2).getFinishParking());
		calendar.set(Calendar.HOUR, 10);
		calendar.set(Calendar.MINUTE, 35);
		calendar.set(Calendar.AM_PM, Calendar.PM);

		listParking.get(2).setFinishParking(calendar.getTime());
		System.out.println(listParking.get(2).getStartParking() + " - " + listParking.get(2).getFinishParking());

		// 19:40 – 20:35 (2 * 0.75 = 1.50 EUR)
		calendar = Calendar.getInstance();
		calendar.setTime(listParking.get(3).getStartParking());
		calendar.set(Calendar.HOUR, 7);
		calendar.set(Calendar.MINUTE, 40);
		calendar.set(Calendar.AM_PM, Calendar.PM);

		listParking.get(3).setStartParking(calendar.getTime());

		calendar.setTime(listParking.get(3).getFinishParking());
		calendar.set(Calendar.HOUR, 8);
		calendar.set(Calendar.MINUTE, 35);
		calendar.set(Calendar.AM_PM, Calendar.PM);

		listParking.get(3).setFinishParking(calendar.getTime());
		System.out.println(listParking.get(3).getStartParking() + " - " + listParking.get(3).getFinishParking());

		return listParking;
	}
}
