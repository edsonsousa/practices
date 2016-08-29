package br.edson.sousa.test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;
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

		ParkingInvoice invoice = TestUtil.createInvoice();
		when(invoiceDao.registerInvoice(invoice)).thenReturn(invoice);
		List<ParkingInvoice> listInvoice = invoiceService.generateInvoiceCustomer(customer);
		assertNotNull(listInvoice);
		assertTrue(listInvoice.size() == 1);
		assertNotNull(listParking.get(0).getDateValueCalculated());
		assertNotNull(listParking.get(0).getParkingValueCalculated());
		assertNotNull(listInvoice.get(0).getDateGenerated());
		assertNotNull(listInvoice.get(0).getTotalInvoice());
		assertEquals(new BigDecimal(1.5), listInvoice.get(0).getTotalInvoice());
	}

	@Test
	public void testGenerateInvoice1ParkingRegisterEqualUnitReferencePremium() throws ParkingException {

		List<ParkingRegister> listParking = new ArrayList<ParkingRegister>();

		listParking.add(TestUtil.createParkingRegister(true, UNIT_REFERENCE));

		Customer customer = listParking.get(0).getCustomer();
		customer.setPremium(true);
		when(parkingDao.findAllRegitersWithoutInvoiceByCustomer(customer)).thenReturn(listParking);

		ParkingInvoice invoice = TestUtil.createInvoice();
		when(invoiceDao.registerInvoice(invoice)).thenReturn(invoice);
		List<ParkingInvoice> listInvoice = invoiceService.generateInvoiceCustomer(customer);
		assertNotNull(listInvoice);
		assertTrue(listInvoice.size() == 1);
		assertNotNull(listParking.get(0).getDateValueCalculated());
		assertNotNull(listParking.get(0).getParkingValueCalculated());
		assertNotNull(listInvoice.get(0).getDateGenerated());
		assertNotNull(listInvoice.get(0).getTotalInvoice());
		assertEquals(new BigDecimal(21), listInvoice.get(0).getTotalInvoice());
	}

	@Test
	public void testGenerateInvoice1inParkingRegisterLessThanUnitReference() throws ParkingException {
		List<ParkingRegister> listParking = new ArrayList<ParkingRegister>();

		listParking.add(TestUtil.createParkingRegister(true, UNIT_REFERENCE-1));
		Customer customer = listParking.get(0).getCustomer();
		when(parkingDao.findAllRegitersWithoutInvoiceByCustomer(customer)).thenReturn(listParking);

		ParkingInvoice invoice = TestUtil.createInvoice();
		when(invoiceDao.registerInvoice(invoice)).thenReturn(invoice);
		List<ParkingInvoice> listInvoice = invoiceService.generateInvoiceCustomer(customer);
		assertNotNull(listInvoice);
		assertTrue(listInvoice.size() == 1);
		assertNotNull(listParking.get(0).getDateValueCalculated());
		assertNotNull(listParking.get(0).getParkingValueCalculated());
		assertNotNull(listInvoice.get(0).getDateGenerated());
		assertNotNull(listInvoice.get(0).getTotalInvoice());
		assertEquals(new BigDecimal(1.5), listInvoice.get(0).getTotalInvoice());
	}

	@Test
	public void testGenerateInvoice1ParkingRegister31Min() throws ParkingException {
		List<ParkingRegister> listParking = new ArrayList<ParkingRegister>();

		listParking.add(TestUtil.createParkingRegister(true, UNIT_REFERENCE+1));
		Customer customer = listParking.get(0).getCustomer();
		when(parkingDao.findAllRegitersWithoutInvoiceByCustomer(customer)).thenReturn(listParking);

		ParkingInvoice invoice = TestUtil.createInvoice();
		when(invoiceDao.registerInvoice(invoice)).thenReturn(invoice);
		List<ParkingInvoice> listInvoice = invoiceService.generateInvoiceCustomer(customer);
		assertNotNull(listInvoice);
		assertTrue(listInvoice.size() == 1);
		assertNotNull(listParking.get(0).getDateValueCalculated());
		assertNotNull(listParking.get(0).getParkingValueCalculated());
		assertNotNull(listInvoice.get(0).getDateGenerated());
		assertNotNull(listInvoice.get(0).getTotalInvoice());
		assertEquals(new BigDecimal(3), listInvoice.get(0).getTotalInvoice());
	}

	@Test
	public void testGenerateInvoice2ParkingRegister() throws ParkingException {
		List<ParkingRegister> listParking = new ArrayList<ParkingRegister>();

		listParking.add(TestUtil.createParkingRegister(true, UNIT_REFERENCE+1));
		listParking.add(TestUtil.createParkingRegister(true, UNIT_REFERENCE+1));
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

		ParkingInvoice invoice = TestUtil.createInvoice();
		when(invoiceDao.registerInvoice(invoice)).thenReturn(invoice);
		List<ParkingInvoice> listInvoice = invoiceService.generateInvoiceCustomer(customer);
		assertNotNull(listInvoice);
		assertTrue(listInvoice.size() == 1);
		assertNotNull(listParking.get(0).getDateValueCalculated());
		assertNotNull(listParking.get(0).getParkingValueCalculated());
		assertNotNull(listInvoice.get(0).getDateGenerated());
		assertNotNull(listInvoice.get(0).getTotalInvoice());
		assertEquals(new BigDecimal(3), listInvoice.get(0).getTotalInvoice());
	}

	@Test
	public void testGenerateInvoice2ParkingRegisterPremium() throws ParkingException {
		List<ParkingRegister> listParking = new ArrayList<ParkingRegister>();

		listParking.add(TestUtil.createParkingRegister(true, UNIT_REFERENCE+1));
		listParking.add(TestUtil.createParkingRegister(true, UNIT_REFERENCE+1));
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

		ParkingInvoice invoice = TestUtil.createInvoice();
		when(invoiceDao.registerInvoice(invoice)).thenReturn(invoice);
		List<ParkingInvoice> listInvoice = invoiceService.generateInvoiceCustomer(customer);
		assertNotNull(listInvoice);
		assertTrue(listInvoice.size() == 1);
		assertNotNull(listParking.get(0).getDateValueCalculated());
		assertNotNull(listParking.get(0).getParkingValueCalculated());
		assertNotNull(listInvoice.get(0).getDateGenerated());
		assertNotNull(listInvoice.get(0).getTotalInvoice());
		assertEquals(new BigDecimal(23), listInvoice.get(0).getTotalInvoice());
	}

	@Test
	public void testGenerateInvoiceParkingMidNightUntilPosSevenAM() throws ParkingException {
		//06:50 - 09:45
	}

	@Test
	public void testGenerateInvoiceParkingSevenAMUntilPosSevenPM() throws ParkingException {
		//08:50 - 20:45
	}

	@Test
	public void testGenerateInvoiceParkingOnlyPosSevenPM() throws ParkingException {
		//19:50 - 22:45
	}

	@Test
	public void testGenerateInvoiceParkingOnlyBeforeSevenAM() throws ParkingException {
		//04:47 - 07:59
	}

	@Test
	public void testGenerateInvoiceParkingHomeAssignment1() throws ParkingException {
		//		1. Regular customer parks twice a month
		//08:12 – 10:45 (6 * 1.50 = 9.00 EUR)
		//19:40 – 20:35 (2 * 1.00 = 2.00 EUR)
		//		Total invoice: 9.00 + 2.00 = 11.00 EUR
	}

	@Test
	public void testGenerateInvoiceParkingHomeAssignment2() throws ParkingException {
		//		2. Premium customer parks four times a month
		//		08:12 – 10:45 (6 * 1.00 = 6.00 EUR)
		//		07:02 – 11:56 (10 * 1.00 = 10.00 EUR)
		//		22:10 – 22:35 (1 * 0.75 = 0.75 EUR)
		//		19:40 – 20:35 (2 * 0.75 = 1.50 EUR)
		//		Total invoice: 6.00 + 10.00 + 0.75 + 1.50 + 20.00 = 38.25 EUR
	}

	@Test
	public void testGenerateInvoiceParkingGreaterThan300Premium() throws ParkingException {
	}
	@Test
	public void testGenerateInvoiceParkingGreaterThan300NoPremium() throws ParkingException {
	}
}