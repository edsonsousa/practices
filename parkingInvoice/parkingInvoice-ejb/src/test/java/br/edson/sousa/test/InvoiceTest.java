package br.edson.sousa.test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Logger;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;

import br.edson.sousa.model.ParkingInvoice;
import br.edson.sousa.model.ParkingRegister;
import br.edson.sousa.service.InvoiceService;

@RunWith(MockitoJUnitRunner.class)
public class InvoiceTest {

	@Mock
	InvoiceService invoiceService;

	@Mock
	Logger log;

	@Test
	public void testGenerateInvoice1ParkingRegister30Min() throws Exception {
		List<ParkingRegister> listParking = new ArrayList<ParkingRegister>();
		listParking.add(TestUtil.createParkingRegister(true, 30));
		List<ParkingInvoice> listInvoice = invoiceService.generateInvoice(listParking);
		assertNotNull(listInvoice);
		assertTrue(listInvoice.size() == 1);
		assertNotNull(listParking.get(0).getDateValueCalculated());
		assertNotNull(listParking.get(0).getParkingValueCalculated());
		assertNotNull(listInvoice.get(0).getDateGenerated());
		assertNotNull(listInvoice.get(0).getTotalInvoice());
		assertEquals(listInvoice.get(0).getTotalInvoice(), new BigDecimal(1.5));
		log.info("Invoice persisted with id " + listInvoice.get(0).getId());
	}

	@Test
	public void testGenerateInvoice1inParkingRegister29M() throws Exception {
		List<ParkingRegister> listParking = new ArrayList<ParkingRegister>();
		listParking.add(TestUtil.createParkingRegister(true, 29));
		List<ParkingInvoice> listInvoice = invoiceService.generateInvoice(listParking);
		assertNotNull(listInvoice);
		assertTrue(listInvoice.size() == 1);
		assertNotNull(listParking.get(0).getDateValueCalculated());
		assertNotNull(listParking.get(0).getParkingValueCalculated());
		assertNotNull(listInvoice.get(0).getDateGenerated());
		assertNotNull(listInvoice.get(0).getTotalInvoice());
		assertEquals(listInvoice.get(0).getTotalInvoice(), new BigDecimal(1.5));
		log.info("Invoice persisted with id " + listInvoice.get(0).getId());
	}

	@Test
	public void testGenerateInvoice1ParkingRegister31Min() throws Exception {
		List<ParkingRegister> listParking = new ArrayList<ParkingRegister>();
		listParking.add(TestUtil.createParkingRegister(true, 31));
		List<ParkingInvoice> listInvoice = invoiceService.generateInvoice(listParking);
		assertNotNull(listInvoice);
		assertTrue(listInvoice.size() == 1);
		assertNotNull(listParking.get(0).getDateValueCalculated());
		assertNotNull(listParking.get(0).getParkingValueCalculated());
		assertNotNull(listInvoice.get(0).getDateGenerated());
		assertNotNull(listInvoice.get(0).getTotalInvoice());
		assertEquals(listInvoice.get(0).getTotalInvoice(), new BigDecimal(1.5));
		log.info("Invoice persisted with id " + listInvoice.get(0).getId());
	}

	@Test
	public void testGenerateInvoice2ParkingRegister() throws Exception {
		List<ParkingRegister> listParking = new ArrayList<ParkingRegister>();
		listParking.add(TestUtil.createParkingRegister(true, 31));
		listParking.add(TestUtil.createParkingRegister(true, 31));
		List<ParkingInvoice> listInvoice = invoiceService.generateInvoice(listParking);
		assertNotNull(listInvoice);
		assertTrue(listInvoice.size() == 1);
		assertNotNull(listParking.get(0).getDateValueCalculated());
		assertNotNull(listParking.get(0).getParkingValueCalculated());
		assertNotNull(listInvoice.get(0).getDateGenerated());
		assertNotNull(listInvoice.get(0).getTotalInvoice());
		assertEquals(listInvoice.get(0).getTotalInvoice(), new BigDecimal(1.5));
		log.info("Invoice persisted with id " + listInvoice.get(0).getId());
	}

}
