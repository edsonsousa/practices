package br.edson.sousa.test;

import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;

import org.jboss.arquillian.container.test.api.Deployment;
import org.jboss.arquillian.junit.Arquillian;
import org.jboss.shrinkwrap.api.Archive;
import org.jboss.shrinkwrap.api.ShrinkWrap;
import org.jboss.shrinkwrap.api.asset.EmptyAsset;
import org.jboss.shrinkwrap.api.spec.WebArchive;
import org.junit.Test;
import org.junit.runner.RunWith;

import br.edson.sousa.model.Customer;
import br.edson.sousa.model.ParkingInvoice;
import br.edson.sousa.model.ParkingRegister;
import br.edson.sousa.service.InvoiceService;
import br.edson.sousa.service.TempoCalculado;
import br.edson.sousa.util.Resources;

@RunWith(Arquillian.class)
public class InvoiceTestArquillian {

	@Deployment
	public static Archive<?> createTestArchive() {
		return ShrinkWrap.create(WebArchive.class, "test.war")
				.addClasses(Customer.class, ParkingRegister.class, InvoiceService.class, ParkingInvoice.class,
						TestUtil.class, Resources.class, TempoCalculado.class)
				.addAsResource("META-INF/test-persistence.xml", "META-INF/persistence.xml")
				.addAsWebInfResource(EmptyAsset.INSTANCE, "beans.xml")
				// Deploy our test datasource
				.addAsWebInfResource("test-ds.xml", "test-ds.xml");
	}

	@Inject
	InvoiceService invoiceService;

	@Test
	public void testGenerateInvoice1ParkingRegister() throws Exception {
		List<ParkingRegister> listParking = new ArrayList<ParkingRegister>();
		ParkingRegister parkingRegister = TestUtil.createParkingRegister(false, 30);
		listParking.add(parkingRegister);
		List<ParkingInvoice> listInvoice = invoiceService.generateInvoice(listParking);
		assertNotNull(listInvoice);
		assertTrue(listInvoice.size() == 1);
		assertNotNull(listInvoice.get(0).getDateGenerated());
	}

}
