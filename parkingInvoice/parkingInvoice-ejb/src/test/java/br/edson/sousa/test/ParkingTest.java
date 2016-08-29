package br.edson.sousa.test;

import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.util.ArrayList;
import java.util.List;
import java.util.logging.Logger;

import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;

import br.edson.sousa.data.CompanyDao;
import br.edson.sousa.data.CustomerDao;
import br.edson.sousa.data.ParkingDao;
import br.edson.sousa.exception.ParkingException;
import br.edson.sousa.model.ParkingRegister;
import br.edson.sousa.service.ParkingService;

@RunWith(MockitoJUnitRunner.class)
public class ParkingTest {

	@InjectMocks
	ParkingService parkingService;

	@Mock
	Logger log;

	@Mock
	CompanyDao companyDao;

	@Mock
	CustomerDao customerDao;

	@Mock
	ParkingDao parkingDao;

	@Rule
	public ExpectedException exception = ExpectedException.none();

	@Test
	public void testInsertParkingWithoutCustomer() throws ParkingException {
		List<ParkingRegister> listParking = new ArrayList<ParkingRegister>();
		listParking.add(TestUtil.createParkingRegister(true, 30));

		exception.expect(ParkingException.class);
		exception.expectMessage("Parking Register failed: Customer null");

		listParking.get(0).setCustomer(null);
		when(companyDao.findByName(listParking.get(0).getCompany().getName()))
				.thenReturn(listParking.get(0).getCompany());

		parkingService.addListParkingRegister(listParking);

		verify(companyDao).findByName(listParking.get(0).getCompany().getName());
	}

	@Test
	public void testInsertParkingWithoutCompany() throws ParkingException {
		List<ParkingRegister> listParking = new ArrayList<ParkingRegister>();
		listParking.add(TestUtil.createParkingRegister(true, 30));

		exception.expect(ParkingException.class);
		exception.expectMessage("Parking Register failed: Parking House null");
		when(customerDao.findByName(listParking.get(0).getCustomer().getName()))
				.thenReturn(listParking.get(0).getCustomer());
		listParking.get(0).setCompany(null);
		parkingService.addListParkingRegister(listParking);

		verify(customerDao).findByName(listParking.get(0).getCustomer().getName());
	}

	@Test
	public void testInsertParkingSucess() throws ParkingException {
		List<ParkingRegister> listParking = new ArrayList<ParkingRegister>();
		listParking.add(TestUtil.createParkingRegister(true, 30));

		when(customerDao.findByName(listParking.get(0).getCustomer().getName()))
				.thenReturn(listParking.get(0).getCustomer());
		when(companyDao.findByName(listParking.get(0).getCompany().getName()))
				.thenReturn(listParking.get(0).getCompany());
		parkingService.addListParkingRegister(listParking);

		verify(customerDao).findByName(listParking.get(0).getCustomer().getName());
		verify(companyDao).findByName(listParking.get(0).getCompany().getName());
		verify(parkingDao).registerParking(listParking.get(0));
	}

}
