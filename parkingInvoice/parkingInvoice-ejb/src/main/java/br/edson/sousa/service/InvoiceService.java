
package br.edson.sousa.service;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;

import javax.ejb.Stateless;
import javax.enterprise.event.Event;
import javax.inject.Inject;

import br.edson.sousa.data.InvoiceDao;
import br.edson.sousa.data.ParkingDao;
import br.edson.sousa.exception.ParkingException;
import br.edson.sousa.model.Customer;
import br.edson.sousa.model.ParkingCompany;
import br.edson.sousa.model.ParkingInvoice;
import br.edson.sousa.model.ParkingRegister;

@Stateless
public class InvoiceService {

	@Inject
	private InvoiceDao invoiceDao;

	@Inject ParkingDao parkingDao;

	@Inject
	private Event<List<ParkingInvoice>> invoiceEvent;

	public List<ParkingInvoice> generateInvoice(Customer customer) throws ParkingException {

		List<ParkingRegister> parkingList = parkingDao.findAllRegitersWithoutInvoiceByCustomer(customer);
		List<ParkingInvoice> invoices = new ArrayList<ParkingInvoice>();
		if(parkingList != null && !parkingList.isEmpty()){

			//create lists per company
			HashMap<ParkingCompany, List<ParkingRegister>> mapInvoicesCompany = createParkingListPerCompany(parkingList);

			BigDecimal totalInvoice = new BigDecimal(0);
			Date invoiceDate = new Date();
			ParkingInvoice invoice;
			
			for (ParkingCompany company : mapInvoicesCompany.keySet()) {
				for (ParkingRegister parkingRegister : mapInvoicesCompany.get(company)) {
					parkingRegister.setParkingValueCalculated(calculateParkingRegister(parkingRegister));
					parkingRegister.setDateValueCalculated(invoiceDate);
					totalInvoice.add(parkingRegister.getParkingValueCalculated());
				}
				invoice = new ParkingInvoice();
				invoice.setDateGenerated(invoiceDate);
				invoice.setTotalInvoice(totalInvoice);
				invoice.setCustomer(customer);
				invoice.setCompany(company);
				invoices.add(invoiceDao.registerInvoice(invoice));
			}

			return invoices;

		}else{
			throw new ParkingException("Customer has no pending Parking.");
		}
	}

	private HashMap<ParkingCompany, List<ParkingRegister>> createParkingListPerCompany(List<ParkingRegister> parkingList){
		HashMap<ParkingCompany, List<ParkingRegister>> mapInvoicesCompany = new HashMap<ParkingCompany, List<ParkingRegister>>();
		for (ParkingRegister parkingRegister : parkingList) {

			if(!mapInvoicesCompany.containsKey(parkingRegister.getCompany())){
				List<ParkingRegister> parkingListCompany = new ArrayList<ParkingRegister>();
				parkingListCompany.add(parkingRegister);
				mapInvoicesCompany.put(parkingRegister.getCompany(),parkingListCompany);
			}else{
				mapInvoicesCompany.get(parkingRegister.getCompany()).add(parkingRegister);
			}
		}

		return mapInvoicesCompany;
	}

	private BigDecimal calculateParkingRegister(ParkingRegister parkingRegister) {

		long time = parkingRegister.getFinishParking().getTime() - parkingRegister.getStartParking().getTime();
		long diffMinutes = time / (60 * 1000) % 60;
		return new BigDecimal((diffMinutes*.75)+5);
	}

	// não tá considerando o dia dos objetos abaixo com os passados
	// no parametro. "zerar" o dia antes de comparar
	private final long divisorMinutes = (60 * 1000) % 60;

	public TempoCalculado calculaTempo(Date init, Date finish) {
		TempoCalculado t = new TempoCalculado();
		Calendar midnight = Calendar.getInstance();
		midnight.set(Calendar.HOUR_OF_DAY, 0);
		midnight.set(Calendar.AM_PM, Calendar.AM);

		Calendar sevenAM = Calendar.getInstance();
		sevenAM.set(Calendar.HOUR_OF_DAY, 7);
		sevenAM.set(Calendar.AM_PM, Calendar.AM);

		Calendar sevenPM = Calendar.getInstance();
		sevenPM.set(Calendar.HOUR_OF_DAY, 7);
		sevenPM.set(Calendar.AM_PM, Calendar.PM);
		Date minor = null;
		if (finish.after(init)) {
			/* Init Hour */
			// 00:00 - 07:00
			if (init.after(midnight.getTime()) && init.before(sevenAM.getTime())) {
				// if finish hour is early than 07:00
				minor = finish.before(sevenAM.getTime()) ? finish : sevenAM.getTime();
				t.setMinutosDayNight(new Long(((minor.getTime() - init.getTime()) / divisorMinutes)).intValue());
			}
			// 19:00 - 00:00
			if (init.after(sevenPM.getTime()) && init.before(midnight.getTime())) {
				// if finish hour is early than 00:00
				minor = finish.before(midnight.getTime()) ? finish : midnight.getTime();
				t.setMinutosDayNight(new Long(((minor.getTime() - init.getTime()) / divisorMinutes)).intValue());
			}
			// 07:00 - 19:00
			if (init.after(sevenAM.getTime()) && init.before(sevenPM.getTime())) {
				// if finish hour is early than 19:00
				minor = finish.before(sevenPM.getTime()) ? finish : sevenPM.getTime();
				t.setMinutosDayLight(new Long(((minor.getTime() - init.getTime()) / divisorMinutes)).intValue());
			}

			// passou de uma faixa pra outra
			if (minor != null) {
				if (minor.equals(sevenPM.getTime())) {
					t.setMinutosDayNight(t.getMinutosDayNight()
							+ new Long(((finish.getTime() - minor.getTime()) / divisorMinutes)).intValue());
				}

				if (minor.equals(sevenAM.getTime())) {
					t.setMinutosDayLight(t.getMinutosDayLight()
							+ new Long(((finish.getTime() - minor.getTime()) / divisorMinutes)).intValue());
				}
			}

		} else {
			return null;
		}
		long diff = init.getTime() - finish.getTime();

		long diffMinutes = diff / (60 * 1000) % 60;
		long diffHours = diff / (60 * 60 * 1000) % 24;
		long diffDays = diff / (24 * 60 * 60 * 1000);

		if (diffHours < 12) {
			if (init.after(sevenAM.getTime()) && init.before(sevenPM.getTime())) {
				t.setMinutosDayLight(new Long(diffMinutes).intValue());

			}
		}
		return t;
	}

	private Integer diferenceTime(Date init, Date finish, Date value) {

		return 0;

	}

}
