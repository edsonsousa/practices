
package br.edson.sousa.service;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;

import javax.ejb.Stateless;
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

	private static final int MONTH_FEE_PREMIUM = 20;

	private static final BigDecimal MAX_VALUE_INVOICE_PREMIUM = new BigDecimal(300);

	private static final int UNIT_REFERENCE = 30;

	private static final float VALUE_REGULAR_DAYNIGHT = 1.00f;

	private static final float VALUE_PREMIUM_DAYNIGHT = 0.75f;

	private static final float VALUE_REGULAR_DAYLIGHT = 1.50f;

	private static final float VALUE_PREMIUM_DAYLIGHT = 1.00f;

	@Inject
	private InvoiceDao invoiceDao;

	@Inject
	ParkingDao parkingDao;

	public List<ParkingInvoice> generateInvoiceCustomer(Customer customer) throws ParkingException {

		List<ParkingRegister> parkingList = parkingDao.findAllRegitersWithoutInvoiceByCustomer(customer);
		List<ParkingInvoice> invoices = new ArrayList<ParkingInvoice>();
		if (parkingList != null && !parkingList.isEmpty()) {

			// create lists per company
			HashMap<ParkingCompany, List<ParkingRegister>> mapInvoicesCompany = createParkingListPerCompany(
					parkingList);

			ParkingInvoice invoice;

			for (ParkingCompany company : mapInvoicesCompany.keySet()) {
				// Isolates Parking Registers per month
				HashMap<String, List<ParkingRegister>> invoicesPerMonth = returnParkingLogPerMonth(
						mapInvoicesCompany.get(company));
				for (String monthYearInvoice : invoicesPerMonth.keySet()) {
					// Generate invoice of company per Month
					invoice = generateInvoice(customer, invoicesPerMonth.get(monthYearInvoice));
					invoice.setCustomer(customer);
					invoice.setMonthYearReference(monthYearInvoice);
					invoice.setCompany(company);
					invoiceDao.registerInvoice(invoice);
					invoices.add(invoice);
				}
			}

			return invoices;

		} else {
			throw new ParkingException("Customer has no pending Parking.");
		}
	}

	/**
	 * Generic method to generate Invoice using parameter.
	 *
	 * @param customer
	 * @param listParking
	 * @return {@link ParkingInvoice}
	 */
	private ParkingInvoice generateInvoice(Customer customer, List<ParkingRegister> listParking) {
		BigDecimal totalInvoice = new BigDecimal(0);
		Date invoiceDate = new Date();
		ParkingInvoice invoice = new ParkingInvoice();

		for (ParkingRegister parkingRegister : listParking) {
			parkingRegister.setParkingValueCalculated(calculateParkingRegister(parkingRegister));
			parkingRegister.setDateValueCalculated(invoiceDate);
			totalInvoice = totalInvoice.add(parkingRegister.getParkingValueCalculated());
			parkingRegister.setInvoice(invoice);
		}
		BigDecimal monthFee = customer.getPremium() ? new BigDecimal(MONTH_FEE_PREMIUM) : new BigDecimal(0);
		totalInvoice = totalInvoice.add(monthFee);

		if (totalInvoice.compareTo(MAX_VALUE_INVOICE_PREMIUM) == 1) {
			totalInvoice = MAX_VALUE_INVOICE_PREMIUM;
		}

		invoice.setDateGenerated(invoiceDate);
		invoice.setTotalInvoice(totalInvoice);

		return invoice;

	}

	/**
	 * Generates a HashMap with list of ParkingRegisters per Month/Year
	 *
	 * @param parkingList
	 * @return HashMap with ParkingRegisters per Month/Year
	 */
	private HashMap<String, List<ParkingRegister>> returnParkingLogPerMonth(List<ParkingRegister> parkingList) {
		HashMap<String, List<ParkingRegister>> invoicesPerMonth = new HashMap<String, List<ParkingRegister>>();
		String keyMonth;
		Calendar cal = Calendar.getInstance();
		for (ParkingRegister parkingRegister : parkingList) {
			// TODO Considering only startDate. Could be improved.
			cal.setTime(parkingRegister.getStartParking());
			keyMonth = cal.get(Calendar.MONTH) + 1 + "/" + cal.get(Calendar.YEAR);
			if (!invoicesPerMonth.containsKey(keyMonth)) {
				List<ParkingRegister> parkingListMonthYear = new ArrayList<ParkingRegister>();
				parkingListMonthYear.add(parkingRegister);
				invoicesPerMonth.put(keyMonth, parkingListMonthYear);
			} else {
				invoicesPerMonth.get(keyMonth).add(parkingRegister);
			}
		}

		return invoicesPerMonth;
	}

	/**
	 * Generates a HashMap with list of ParkingRegisters per Company
	 *
	 * @param parkingList
	 * @return HashMap with ParkingRegisters per Company
	 */
	private HashMap<ParkingCompany, List<ParkingRegister>> createParkingListPerCompany(
			List<ParkingRegister> parkingList) {
		HashMap<ParkingCompany, List<ParkingRegister>> mapInvoicesCompany = new HashMap<ParkingCompany, List<ParkingRegister>>();
		for (ParkingRegister parkingRegister : parkingList) {

			if (!mapInvoicesCompany.containsKey(parkingRegister.getCompany())) {
				List<ParkingRegister> parkingListCompany = new ArrayList<ParkingRegister>();
				parkingListCompany.add(parkingRegister);
				mapInvoicesCompany.put(parkingRegister.getCompany(), parkingListCompany);
			} else {
				mapInvoicesCompany.get(parkingRegister.getCompany()).add(parkingRegister);
			}
		}

		return mapInvoicesCompany;
	}

	public BigDecimal calculateParkingRegister(ParkingRegister parkingRegister) {

		if (parkingRegister.getFinishParking().after(parkingRegister.getStartParking())) {

			if (verifyStartAndFinishSameDay(parkingRegister)) {
				// TODO Here we have fixed values of 7AM and 7PM but we could
				// personalize
				// the calculation receiving as parameters coming from company
				Calendar zeroAM = Calendar.getInstance();
				zeroAM.setTime(parkingRegister.getStartParking());
				zeroAM.set(Calendar.HOUR, 0);
				zeroAM.set(Calendar.MINUTE, 0);
				zeroAM.set(Calendar.AM_PM, Calendar.AM);

				Calendar twentyThreeFiftyNine = Calendar.getInstance();
				twentyThreeFiftyNine.setTime(parkingRegister.getStartParking());
				twentyThreeFiftyNine.set(Calendar.HOUR, 11);
				twentyThreeFiftyNine.set(Calendar.MINUTE, 59);
				twentyThreeFiftyNine.set(Calendar.AM_PM, Calendar.PM);

				Calendar sevenAM = Calendar.getInstance();
				sevenAM.setTime(parkingRegister.getStartParking());
				sevenAM.set(Calendar.HOUR, 7);
				sevenAM.set(Calendar.MINUTE, 0);
				sevenAM.set(Calendar.AM_PM, Calendar.AM);

				Calendar sevenPM = Calendar.getInstance();
				sevenPM.setTime(parkingRegister.getStartParking());
				sevenPM.set(Calendar.HOUR, 7);
				sevenPM.set(Calendar.MINUTE, 0);
				sevenPM.set(Calendar.AM_PM, Calendar.PM);

				// The minutes of DayNight is the sum of minutes between
				// Midnight to 7AM and 7PM until 11:59PM
				int minutesDayNight = calculateMinutesParking(parkingRegister.getStartParking(),
						parkingRegister.getFinishParking(), zeroAM, sevenAM)
						+ calculateMinutesParking(parkingRegister.getStartParking(), parkingRegister.getFinishParking(),
								sevenPM, twentyThreeFiftyNine);
				// The minutes of DayLight are the minutes between 7AM and 7PM
				int minutesDayLight = calculateMinutesParking(parkingRegister.getStartParking(),
						parkingRegister.getFinishParking(), sevenAM, sevenPM);

				if (minutesDayLight > 0 || minutesDayNight > 0) {

					// TODO The values of prices per half hour could be
					// personalized per company
					Float valueMinuteDayLight = parkingRegister.getCustomer().getPremium() ? VALUE_PREMIUM_DAYLIGHT
							: VALUE_REGULAR_DAYLIGHT;
					Float valueMinuteDayNight = parkingRegister.getCustomer().getPremium() ? VALUE_PREMIUM_DAYNIGHT
							: VALUE_REGULAR_DAYNIGHT;

					int quantityUnitReferenceDayLight = minutesDayLight % UNIT_REFERENCE > 0
							? (minutesDayLight / UNIT_REFERENCE) + 1 : minutesDayLight / UNIT_REFERENCE;
					int quantityUnitReferenceDayNight = minutesDayNight % UNIT_REFERENCE > 0
							? (minutesDayNight / UNIT_REFERENCE) + 1 : minutesDayNight / UNIT_REFERENCE;

					BigDecimal result = new BigDecimal((quantityUnitReferenceDayLight * valueMinuteDayLight)
							+ (quantityUnitReferenceDayNight * valueMinuteDayNight));

					return result;
				}
			} else {
				// TODO Calculates when register finish in another day.
				return null;
			}
		}
		return new BigDecimal(0);
	}

	/**
	 *
	 * Calculate how much minutes of parking are between initPeriod and
	 * endPeriod
	 *
	 * @param startParking
	 * @param finishParking
	 * @param initPeriod
	 * @param endPeriod
	 * @return Number of minutes of parking are between initPeriod and endPeriod
	 */
	private int calculateMinutesParking(Date startParking, Date finishParking, Calendar initPeriod,
			Calendar endPeriod) {
		Date minorStart = null;
		Date minorFinish = null;
		long divisorMinutes = 60 * 1000;

		// checks if startParking or finishParking is between Period
		if (((startParking.equals(initPeriod.getTime()) || startParking.after(initPeriod.getTime()))
				&& startParking.before(endPeriod.getTime()))
				|| (finishParking.equals(endPeriod.getTime()) || finishParking.before(endPeriod.getTime()))
						&& finishParking.after(initPeriod.getTime())) {

			// if startParking hour is early than initPeriod
			minorStart = startParking.before(initPeriod.getTime()) ? initPeriod.getTime() : startParking;
			// if finishParking hour is early than endPeriod
			minorFinish = finishParking.before(endPeriod.getTime()) ? finishParking : endPeriod.getTime();
			return new Long(((minorFinish.getTime() - minorStart.getTime()) / divisorMinutes)).intValue();
		}
		return 0;

	}

	private boolean verifyStartAndFinishSameDay(ParkingRegister parkingRegister) {
		Calendar cStart = Calendar.getInstance();
		cStart.setTime(parkingRegister.getStartParking());

		Calendar cFinish = Calendar.getInstance();
		cFinish.setTime(parkingRegister.getFinishParking());

		return cStart.get(Calendar.DAY_OF_MONTH) == cFinish.get(Calendar.DAY_OF_MONTH)
				&& cStart.get(Calendar.YEAR) == cFinish.get(Calendar.YEAR);
	}

}
