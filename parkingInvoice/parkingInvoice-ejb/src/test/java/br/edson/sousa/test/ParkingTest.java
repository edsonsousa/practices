package br.edson.sousa.test;

import java.util.ArrayList;
import java.util.List;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;

import br.edson.sousa.model.ParkingRegister;
import br.edson.sousa.service.ParkingService;

@RunWith(MockitoJUnitRunner.class)
public class ParkingTest {

	@Mock
	ParkingService parkingService;

	@Test
	public void testInsertParkingLessWithCustomer() throws Exception {
		List<ParkingRegister> listParking = new ArrayList<ParkingRegister>();
		listParking.add(TestUtil.createParkingRegister(true, 30));
	}

	@Test
	public void testInsertParkingLessWithoutCustomer() throws Exception {
		List<ParkingRegister> listParking = new ArrayList<ParkingRegister>();
		listParking.add(TestUtil.createParkingRegister(true, 30));
	}

}
