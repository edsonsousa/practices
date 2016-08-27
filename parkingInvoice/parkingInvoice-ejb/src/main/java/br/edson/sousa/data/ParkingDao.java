/*
 * JBoss, Home of Professional Open Source
 * Copyright 2013, Red Hat, Inc. and/or its affiliates, and individual
 * contributors by the @authors tag. See the copyright.txt in the
 * distribution for a full listing of individual contributors.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 * http://www.apache.org/licenses/LICENSE-2.0
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package br.edson.sousa.data;

import java.util.ArrayList;
import java.util.List;

import javax.enterprise.context.ApplicationScoped;

import br.edson.sousa.exception.ParkingException;
import br.edson.sousa.model.Customer;
import br.edson.sousa.model.ParkingRegister;

@ApplicationScoped
public class ParkingDao {

	List<ParkingRegister> parkingList = new ArrayList<ParkingRegister>();

	public ParkingRegister findById(Long id) {
		return parkingList.get(findIndexById(id));
	}

	public List<ParkingRegister> findAllRegitersByCustomer(Customer customer) {
		List<ParkingRegister> result = new ArrayList<ParkingRegister>();
		for (ParkingRegister parkingRegister : parkingList) {
			if (parkingRegister.getCustomer().getName().equals(customer.getName())) {
				result.add(parkingRegister);
			}
		}
		return result;

	}

	public List<Customer> findAllCustomers() {
		List<Customer> result = new ArrayList<Customer>();
		for (ParkingRegister parkingRegister : parkingList) {
			result.add(parkingRegister.getCustomer());
		}
		return result;

	}

	public List<ParkingRegister> getAll() {

		return parkingList;
	}

	public void registerParking(ParkingRegister parkingRegister) throws ParkingException {
		Long id = 0L;
		if (parkingList.size() > 0) {
			id = parkingList.get(parkingList.size() - 1).getId();
			id++;
		}
		parkingRegister.setId(Long.valueOf(id));
		parkingList.add(parkingRegister);

	}

	public int findIndexById(final Long integer) {
		int index = 0;
		for (int i = 0; i < parkingList.size(); i++) {
			if (parkingList.get(i).getId() == integer) {
				index = i;
			}
		}
		return index;
	}
}
