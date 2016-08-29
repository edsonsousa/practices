package br.edson.sousa.test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.logging.Logger;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;

import br.edson.sousa.data.InvoiceDao;
import br.edson.sousa.data.ParkingDao;
import br.edson.sousa.exception.ParkingException;
import br.edson.sousa.model.Customer;
import br.edson.sousa.model.ParkingInvoice;
import br.edson.sousa.model.ParkingRegister;
import br.edson.sousa.service.InvoiceService;

@RunWith(MockitoJUnitRunner.class)
public class InvoiceTest {

	private static final int UNIT_REFERENCE = 30;

	@InjectMocks
	InvoiceService invoiceService;

	@Mock
	ParkingDao parkingDao;

	@Mock
	InvoiceDao invoiceDao;

	@Mock
	Logger log;

	@Test
	public void testGenerateInvoice1ParkingRegisterEqualUnitReference() throws ParkingException {

		List<ParkingRegister> listParking = new ArrayList<ParkingRegister>();

		listParking.add(TestUtil.createParkingRegister(true, UNIT_REFERENCE));

		Customer customer = listParking.get(0).getCustomer();
		when(parkingDao.findAllRegitersWithoutInvoiceByCustomer(customer)).thenReturn(listParking);

		List<ParkingInvoice> listInvoice = invoiceService.generateInvoiceCustomer(customer);
		assertNotNull(listInvoice);
		assertTrue(listInvoice.size() == 1);
		assertNotNull(listParking.get(0).getDateValueCalculated());
		assertNotNull(listParking.get(0).getParkingValueCalculated());
		assertNotNull(listInvoice.get(0).getDateGenerated());
		assertNotNull(listInvoice.get(0).getTotalInvoice());
		assertEquals(new BigDecimal(1.5), listInvoice.get(0).getTotalInvoice());

		verify(parkingDao).findAllRegitersWithoutInvoiceByCustomer(customer);

	}

	@Test
	public void testGenerateInvoice1ParkingRegisterEqualUnitReferencePremium() throws ParkingException {

		List<ParkingRegister> listParking = new ArrayList<ParkingRegister>();

		listParking.add(TestUtil.createParkingRegister(true, UNIT_REFERENCE));

		Customer customer = listParking.get(0).getCustomer();
		customer.setPremium(true);
		when(parkingDao.findAllRegitersWithoutInvoiceByCustomer(customer)).thenReturn(listParking);

		List<ParkingInvoice> listInvoice = invoiceService.generateInvoiceCustomer(customer);
		assertNotNull(listInvoice);
		assertTrue(listInvoice.size() == 1);
		assertNotNull(listParking.get(0).getDateValueCalculated());
		assertNotNull(listParking.get(0).getParkingValueCalculated());
		assertNotNull(listInvoice.get(0).getDateGenerated());
		assertNotNull(listInvoice.get(0).getTotalInvoice());
		assertEquals(new BigDecimal(21), listInvoice.get(0).getTotalInvoice());

		verify(parkingDao).findAllRegitersWithoutInvoiceByCustomer(customer);
	}

	@Test
	public void testGenerateInvoice1inParkingRegisterLessThanUnitReference() throws ParkingException {
		List<ParkingRegister> listParking = new ArrayList<ParkingRegister>();

		listParking.add(TestUtil.createParkingRegister(true, UNIT_REFERENCE - 1));
		Customer customer = listParking.get(0).getCustomer();
		when(parkingDao.findAllRegitersWithoutInvoiceByCustomer(customer)).thenReturn(listParking);

		List<ParkingInvoice> listInvoice = invoiceService.generateInvoiceCustomer(customer);
		assertNotNull(listInvoice);
		assertTrue(listInvoice.size() == 1);
		assertNotNull(listParking.get(0).getDateValueCalculated());
		assertNotNull(listParking.get(0).getParkingValueCalculated());
		assertNotNull(listInvoice.get(0).getDateGenerated());
		assertNotNull(listInvoice.get(0).getTotalInvoice());
		assertEquals(new BigDecimal(1.5), listInvoice.get(0).getTotalInvoice());

		verify(parkingDao).findAllRegitersWithoutInvoiceByCustomer(customer);
	}

	@Test
	public void testGenerateInvoice1ParkingRegister31Min() throws ParkingException {
		List<ParkingRegister> listParking = new ArrayList<ParkingRegister>();

		listParking.add(TestUtil.createParkingRegister(true, UNIT_REFERENCE + 1));
		Customer customer = listParking.get(0).getCustomer();
		when(parkingDao.findAllRegitersWithoutInvoiceByCustomer(customer)).thenReturn(listParking);

		List<ParkingInvoice> listInvoice = invoiceService.generateInvoiceCustomer(customer);
		assertNotNull(listInvoice);
		assertTrue(listInvoice.size() == 1);
		assertNotNull(listParking.get(0).getDateValueCalculated());
		assertNotNull(listParking.get(0).getParkingValueCalculated());
		assertNotNull(listInvoice.get(0).getDateGenerated());
		assertNotNull(listInvoice.get(0).getTotalInvoice());
		assertEquals(new BigDecimal(3), listInvoice.get(0).getTotalInvoice());

		verify(parkingDao).findAllRegitersWithoutInvoiceByCustomer(customer);
	}

	@Test
	public void testGenerateInvoice2ParkingRegister() throws ParkingException {
		List<ParkingRegister> listParking = new ArrayList<ParkingRegister>();

		listParking.add(TestUtil.createParkingRegister(true, UNIT_REFERENCE + 1));
		listParking.add(TestUtil.createParkingRegister(true, UNIT_REFERENCE + 1));
		listParking.get(1).setCompany(listParking.get(0).getCompany());

		Calendar calendar = Calendar.getInstance();
		calendar.setTime(listParking.get(1).getStartParking());
		calendar.add(Calendar.DAY_OF_MONTH, 7);

		listParking.get(1).setStartParking(calendar.getTime());

		calendar.setTime(listParking.get(1).getFinishParking());
		calendar.add(Calendar.DAY_OF_MONTH, 7);
		listParking.get(1).setFinishParking(calendar.getTime());

		Customer customer = listParking.get(0).getCustomer();
		when(parkingDao.findAllRegitersWithoutInvoiceByCustomer(customer)).thenReturn(listParking);

		List<ParkingInvoice> listInvoice = invoiceService.generateInvoiceCustomer(customer);
		assertNotNull(listInvoice);
		assertTrue(listInvoice.size() == 1);
		assertNotNull(listParking.get(0).getDateValueCalculated());
		assertNotNull(listParking.get(0).getParkingValueCalculated());
		assertNotNull(listInvoice.get(0).getDateGenerated());
		assertNotNull(listInvoice.get(0).getTotalInvoice());
		assertEquals(new BigDecimal(6), listInvoice.get(0).getTotalInvoice());

		verify(parkingDao).findAllRegitersWithoutInvoiceByCustomer(customer);
	}

	@Test
	public void testGenerateInvoice2ParkingRegisterPremium() throws ParkingException {
		List<ParkingRegister> listParking = new ArrayList<ParkingRegister>();

		listParking.add(TestUtil.createParkingRegister(true, UNIT_REFERENCE + 1));
		listParking.add(TestUtil.createParkingRegister(true, UNIT_REFERENCE + 1));
		listParking.get(1).setCompany(listParking.get(0).getCompany());

		Calendar calendar = Calendar.getInstance();
		calendar.setTime(listParking.get(1).getStartParking());
		calendar.add(Calendar.DAY_OF_MONTH, 7);

		listParking.get(1).setStartParking(calendar.getTime());

		calendar.setTime(listParking.get(1).getFinishParking());
		calendar.add(Calendar.DAY_OF_MONTH, 7);
		listParking.get(1).setFinishParking(calendar.getTime());

		Customer customer = listParking.get(0).getCustomer();
		customer.setPremium(true);
		when(parkingDao.findAllRegitersWithoutInvoiceByCustomer(customer)).thenReturn(listParking);

		List<ParkingInvoice> listInvoice = invoiceService.generateInvoiceCustomer(customer);
		assertNotNull(listInvoice);
		assertTrue(listInvoice.size() == 1);
		assertNotNull(listParking.get(0).getDateValueCalculated());
		assertNotNull(listParking.get(0).getParkingValueCalculated());
		assertNotNull(listInvoice.get(0).getDateGenerated());
		assertNotNull(listInvoice.get(0).getTotalInvoice());
		assertEquals(new BigDecimal(25), listInvoice.get(0).getTotalInvoice());

		verify(parkingDao).findAllRegitersWithoutInvoiceByCustomer(customer);
	}

	@Test
	public void testGenerateInvoiceParkingMidNightUntilPosSevenAM() throws ParkingException {
		// 06:50 - 09:45 = 1*1 + 6*1.5 = $10

		List<ParkingRegister> listParking = new ArrayList<ParkingRegister>();

		listParking.add(TestUtil.createParkingRegister(true, UNIT_REFERENCE - 1));

		System.out.println(listParking.get(0).getStartParking() + " - " + listParking.get(0).getFinishParking());
		Calendar calendar = Calendar.getInstance();
		calendar.setTime(listParking.get(0).getStartParking());
		calendar.set(Calendar.HOUR, 6);
		calendar.set(Calendar.MINUTE, 50);
		calendar.set(Calendar.AM_PM, Calendar.AM);

		listParking.get(0).setStartParking(calendar.getTime());

		calendar.setTime(listParking.get(0).getFinishParking());
		calendar.set(Calendar.HOUR, 9);
		calendar.set(Calendar.MINUTE, 45);
		calendar.set(Calendar.AM_PM, Calendar.AM);

		listParking.get(0).setFinishParking(calendar.getTime());
		System.out.println(listParking.get(0).getStartParking() + " - " + listParking.get(0).getFinishParking());
		Customer customer = listParking.get(0).getCustomer();
		when(parkingDao.findAllRegitersWithoutInvoiceByCustomer(customer)).thenReturn(listParking);

		List<ParkingInvoice> listInvoice = invoiceService.generateInvoiceCustomer(customer);
		assertNotNull(listInvoice);
		assertTrue(listInvoice.size() == 1);
		assertNotNull(listParking.get(0).getDateValueCalculated());
		assertNotNull(listParking.get(0).getParkingValueCalculated());
		assertNotNull(listInvoice.get(0).getDateGenerated());
		assertNotNull(listInvoice.get(0).getTotalInvoice());
		assertEquals(new BigDecimal(10), listInvoice.get(0).getTotalInvoice());

		verify(parkingDao).findAllRegitersWithoutInvoiceByCustomer(customer);
	}

	@Test
	public void testGenerateInvoiceParkingSevenAMUntilPosSevenPM() throws ParkingException {
		// 08:50 - 20:45 = 21*1.5 + 4*1 = 35.5

		List<ParkingRegister> listParking = new ArrayList<ParkingRegister>();

		listParking.add(TestUtil.createParkingRegister(true, UNIT_REFERENCE - 1));

		System.out.println(listParking.get(0).getStartParking() + " - " + listParking.get(0).getFinishParking());
		Calendar calendar = Calendar.getInstance();
		calendar.setTime(listParking.get(0).getStartParking());
		calendar.set(Calendar.HOUR, 8);
		calendar.set(Calendar.MINUTE, 50);
		calendar.set(Calendar.AM_PM, Calendar.AM);

		listParking.get(0).setStartParking(calendar.getTime());

		calendar.setTime(listParking.get(0).getFinishParking());
		calendar.set(Calendar.HOUR, 8);
		calendar.set(Calendar.MINUTE, 45);
		calendar.set(Calendar.AM_PM, Calendar.PM);

		listParking.get(0).setFinishParking(calendar.getTime());
		System.out.println(listParking.get(0).getStartParking() + " - " + listParking.get(0).getFinishParking());
		Customer customer = listParking.get(0).getCustomer();
		when(parkingDao.findAllRegitersWithoutInvoiceByCustomer(customer)).thenReturn(listParking);

		List<ParkingInvoice> listInvoice = invoiceService.generateInvoiceCustomer(customer);
		assertNotNull(listInvoice);
		assertTrue(listInvoice.size() == 1);
		assertNotNull(listParking.get(0).getDateValueCalculated());
		assertNotNull(listParking.get(0).getParkingValueCalculated());
		assertNotNull(listInvoice.get(0).getDateGenerated());
		assertNotNull(listInvoice.get(0).getTotalInvoice());
		assertEquals(new BigDecimal(35.5), listInvoice.get(0).getTotalInvoice());

		verify(parkingDao).findAllRegitersWithoutInvoiceByCustomer(customer);
	}

	@Test
	public void testGenerateInvoiceParkingOnlyPosSevenPM() throws ParkingException {
		// 19:50 - 22:45 = 6*1 = 6

		List<ParkingRegister> listParking = new ArrayList<ParkingRegister>();

		listParking.add(TestUtil.createParkingRegister(true, UNIT_REFERENCE - 1));

		System.out.println(listParking.get(0).getStartParking() + " - " + listParking.get(0).getFinishParking());
		Calendar calendar = Calendar.getInstance();
		calendar.setTime(listParking.get(0).getStartParking());
		calendar.set(Calendar.HOUR, 7);
		calendar.set(Calendar.MINUTE, 50);
		calendar.set(Calendar.AM_PM, Calendar.PM);

		listParking.get(0).setStartParking(calendar.getTime());

		calendar.setTime(listParking.get(0).getFinishParking());
		calendar.set(Calendar.HOUR, 10);
		calendar.set(Calendar.MINUTE, 45);
		calendar.set(Calendar.AM_PM, Calendar.PM);

		listParking.get(0).setFinishParking(calendar.getTime());
		System.out.println(listParking.get(0).getStartParking() + " - " + listParking.get(0).getFinishParking());
		Customer customer = listParking.get(0).getCustomer();
		when(parkingDao.findAllRegitersWithoutInvoiceByCustomer(customer)).thenReturn(listParking);

		List<ParkingInvoice> listInvoice = invoiceService.generateInvoiceCustomer(customer);
		assertNotNull(listInvoice);
		assertTrue(listInvoice.size() == 1);
		assertNotNull(listParking.get(0).getDateValueCalculated());
		assertNotNull(listParking.get(0).getParkingValueCalculated());
		assertNotNull(listInvoice.get(0).getDateGenerated());
		assertNotNull(listInvoice.get(0).getTotalInvoice());
		assertEquals(new BigDecimal(6), listInvoice.get(0).getTotalInvoice());

		verify(parkingDao).findAllRegitersWithoutInvoiceByCustomer(customer);
	}

	@Test
	public void testGenerateInvoiceParkingOnlyBeforeSevenAM() throws ParkingException {
		// 04:47 - 06:59 = 5*1 = 7

		List<ParkingRegister> listParking = new ArrayList<ParkingRegister>();

		listParking.add(TestUtil.createParkingRegister(true, UNIT_REFERENCE - 1));

		System.out.println(listParking.get(0).getStartParking() + " - " + listParking.get(0).getFinishParking());
		Calendar calendar = Calendar.getInstance();
		calendar.setTime(listParking.get(0).getStartParking());
		calendar.set(Calendar.HOUR, 4);
		calendar.set(Calendar.MINUTE, 47);
		calendar.set(Calendar.AM_PM, Calendar.AM);

		listParking.get(0).setStartParking(calendar.getTime());

		calendar.setTime(listParking.get(0).getFinishParking());
		calendar.set(Calendar.HOUR, 6);
		calendar.set(Calendar.MINUTE, 59);
		calendar.set(Calendar.AM_PM, Calendar.AM);

		listParking.get(0).setFinishParking(calendar.getTime());
		System.out.println(listParking.get(0).getStartParking() + " - " + listParking.get(0).getFinishParking());
		Customer customer = listParking.get(0).getCustomer();
		when(parkingDao.findAllRegitersWithoutInvoiceByCustomer(customer)).thenReturn(listParking);

		List<ParkingInvoice> listInvoice = invoiceService.generateInvoiceCustomer(customer);
		assertNotNull(listInvoice);
		assertTrue(listInvoice.size() == 1);
		assertNotNull(listParking.get(0).getDateValueCalculated());
		assertNotNull(listParking.get(0).getParkingValueCalculated());
		assertNotNull(listInvoice.get(0).getDateGenerated());
		assertNotNull(listInvoice.get(0).getTotalInvoice());
		assertEquals(new BigDecimal(5), listInvoice.get(0).getTotalInvoice());

		verify(parkingDao).findAllRegitersWithoutInvoiceByCustomer(customer);
	}

	@Test
	public void testGenerateInvoiceParkingHomeAssignment1() throws ParkingException {
		// 1. Regular customer parks twice a month
		// 08:12 – 10:45 (6 * 1.50 = 9.00 EUR)
		// 19:40 – 20:35 (2 * 1.00 = 2.00 EUR)
		// Total invoice: 9.00 + 2.00 = 11.00 EUR

		List<ParkingRegister> listParking = new ArrayList<ParkingRegister>();

		listParking.add(TestUtil.createParkingRegister(true, UNIT_REFERENCE - 1));
		listParking.add(TestUtil.createParkingRegister(true, UNIT_REFERENCE - 1));
		listParking.get(1).setCompany(listParking.get(0).getCompany());

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

		calendar = Calendar.getInstance();
		calendar.setTime(listParking.get(1).getStartParking());
		calendar.set(Calendar.HOUR, 7);
		calendar.set(Calendar.MINUTE, 40);
		calendar.set(Calendar.AM_PM, Calendar.PM);

		listParking.get(1).setStartParking(calendar.getTime());

		calendar.setTime(listParking.get(1).getFinishParking());
		calendar.set(Calendar.HOUR, 8);
		calendar.set(Calendar.MINUTE, 35);
		calendar.set(Calendar.AM_PM, Calendar.PM);

		listParking.get(1).setFinishParking(calendar.getTime());
		System.out.println(listParking.get(1).getStartParking() + " - " + listParking.get(1).getFinishParking());

		Customer customer = listParking.get(0).getCustomer();
		when(parkingDao.findAllRegitersWithoutInvoiceByCustomer(customer)).thenReturn(listParking);

		List<ParkingInvoice> listInvoice = invoiceService.generateInvoiceCustomer(customer);
		assertNotNull(listInvoice);
		assertTrue(listInvoice.size() == 1);
		assertNotNull(listParking.get(0).getDateValueCalculated());
		assertNotNull(listParking.get(0).getParkingValueCalculated());
		assertNotNull(listInvoice.get(0).getDateGenerated());
		assertNotNull(listInvoice.get(0).getTotalInvoice());
		assertEquals(new BigDecimal(11), listInvoice.get(0).getTotalInvoice());

		verify(parkingDao).findAllRegitersWithoutInvoiceByCustomer(customer);
	}

	@Test
	public void testGenerateInvoiceParkingHomeAssignment2() throws ParkingException {
		// 2. Premium customer parks four times a month
		// 08:12 – 10:45 (6 * 1.00 = 6.00 EUR)
		// 07:02 – 11:56 (10 * 1.00 = 10.00 EUR)
		// 22:10 – 22:35 (1 * 0.75 = 0.75 EUR)
		// 19:40 – 20:35 (2 * 0.75 = 1.50 EUR)
		// Total invoice: 6.00 + 10.00 + 0.75 + 1.50 + 20.00 = 38.25 EUR

		List<ParkingRegister> listParking = TestUtil.generateListParking();

		when(parkingDao.findAllRegitersWithoutInvoiceByCustomer(listParking.get(0).getCustomer()))
				.thenReturn(listParking);

		List<ParkingInvoice> listInvoice = invoiceService.generateInvoiceCustomer(listParking.get(0).getCustomer());
		assertNotNull(listInvoice);
		assertTrue(listInvoice.size() == 1);
		assertNotNull(listParking.get(0).getDateValueCalculated());
		assertNotNull(listParking.get(0).getParkingValueCalculated());
		assertNotNull(listInvoice.get(0).getDateGenerated());
		assertNotNull(listInvoice.get(0).getTotalInvoice());
		assertEquals(new BigDecimal(38.25), listInvoice.get(0).getTotalInvoice());

		verify(parkingDao).findAllRegitersWithoutInvoiceByCustomer(listParking.get(0).getCustomer());
	}

	@Test
	public void testGenerateInvoiceParkingGreaterThan300Premium() throws ParkingException {
	}

	@Test
	public void testGenerateInvoiceParkingGreaterThan300NoPremium() throws ParkingException {
	}

	@Test
	public void testGenerateInvoiceParkingMoreThan1Month() throws ParkingException {

		// Regular customer parks four times a month
		// 08:12 – 10:45 (6 * 1.5 = 9.00 EUR) - Company A
		// 07:02 – 11:56 (10 * 1.50 = 15.00 EUR) - Company B
		// 22:10 – 22:35 (1 * 1.0 = 1.00 EUR) - Company B
		// 19:40 – 20:35 (2 * 1.00 = 2.00 EUR) - Company B
		// Total invoice 1: 6.00 EUR
		// Total invoice 2: 15.00 + 1.0 + 2.0 = 18.00 EUR
		List<ParkingRegister> listParking = TestUtil.generateListParking();

		Calendar calendar = Calendar.getInstance();
		calendar.setTime(listParking.get(0).getStartParking());
		calendar.set(Calendar.MONTH, calendar.get(Calendar.MONTH) + 1);

		listParking.get(0).setStartParking(calendar.getTime());

		calendar = Calendar.getInstance();
		calendar.setTime(listParking.get(0).getFinishParking());
		calendar.set(Calendar.MONTH, calendar.get(Calendar.MONTH) + 1);

		listParking.get(0).setFinishParking(calendar.getTime());

		listParking.get(0).getCustomer().setPremium(false);

		when(parkingDao.findAllRegitersWithoutInvoiceByCustomer(listParking.get(0).getCustomer()))
				.thenReturn(listParking);

		List<ParkingInvoice> listInvoice = invoiceService.generateInvoiceCustomer(listParking.get(0).getCustomer());
		assertNotNull(listInvoice);
		assertTrue(listInvoice.size() == 2);
		assertNotNull(listParking.get(0).getDateValueCalculated());
		assertNotNull(listParking.get(0).getParkingValueCalculated());
		assertNotNull(listInvoice.get(0).getDateGenerated());
		assertNotNull(listInvoice.get(0).getTotalInvoice());

		assertNotNull(listParking.get(1).getDateValueCalculated());
		assertNotNull(listParking.get(1).getParkingValueCalculated());
		assertNotNull(listInvoice.get(1).getDateGenerated());
		assertNotNull(listInvoice.get(1).getTotalInvoice());
		assertTrue(new BigDecimal(9).equals(listInvoice.get(0).getTotalInvoice())
				|| new BigDecimal(9).equals(listInvoice.get(1).getTotalInvoice()));
		assertTrue(new BigDecimal(18).equals(listInvoice.get(0).getTotalInvoice())
				|| new BigDecimal(18).equals(listInvoice.get(1).getTotalInvoice()));

		verify(parkingDao).findAllRegitersWithoutInvoiceByCustomer(listParking.get(0).getCustomer());
	}

	@Test
	public void testGenerateInvoiceParking2Companies() throws ParkingException {

		// Regular customer parks four times a month
		// 08:12 – 10:45 (6 * 1.5 = 9.00 EUR) - Month A
		// 07:02 – 11:56 (10 * 1.50 = 15.00 EUR) - Month B
		// 22:10 – 22:35 (1 * 1.0 = 1.00 EUR) - Month B
		// 19:40 – 20:35 (2 * 1.00 = 2.00 EUR) - Month B
		// Total invoice 1: 6.00 EUR
		// Total invoice 2: 15.00 + 1.0 + 2.0 = 18.00 EUR
		List<ParkingRegister> listParking = TestUtil.generateListParking();

		listParking.get(0).setCompany(TestUtil.createCompany());
		listParking.get(0).getCustomer().setPremium(false);

		when(parkingDao.findAllRegitersWithoutInvoiceByCustomer(listParking.get(0).getCustomer()))
				.thenReturn(listParking);

		List<ParkingInvoice> listInvoice = invoiceService.generateInvoiceCustomer(listParking.get(0).getCustomer());
		assertNotNull(listInvoice);
		assertTrue(listInvoice.size() == 2);
		assertNotNull(listParking.get(0).getDateValueCalculated());
		assertNotNull(listParking.get(0).getParkingValueCalculated());
		assertNotNull(listInvoice.get(0).getDateGenerated());
		assertNotNull(listInvoice.get(0).getTotalInvoice());

		assertNotNull(listParking.get(1).getDateValueCalculated());
		assertNotNull(listParking.get(1).getParkingValueCalculated());
		assertNotNull(listInvoice.get(1).getDateGenerated());
		assertNotNull(listInvoice.get(1).getTotalInvoice());

		assertTrue(new BigDecimal(9).equals(listInvoice.get(0).getTotalInvoice())
				|| new BigDecimal(9).equals(listInvoice.get(1).getTotalInvoice()));
		assertTrue(new BigDecimal(18).equals(listInvoice.get(0).getTotalInvoice())
				|| new BigDecimal(18).equals(listInvoice.get(1).getTotalInvoice()));

		verify(parkingDao).findAllRegitersWithoutInvoiceByCustomer(listParking.get(0).getCustomer());
	}
}