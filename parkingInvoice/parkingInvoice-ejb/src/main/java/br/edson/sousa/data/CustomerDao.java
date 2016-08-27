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

@ApplicationScoped
public class CustomerDao {

	List<Customer> customerList = new ArrayList<Customer>();

	public Customer findById(Long id) {
		return customerList.get(findIndexById(id));
	}

	public List<Customer> getAll() {

		return customerList;
	}

	public Customer findByName(String nome) {
		return customerList.get(findIndexByName(nome));
	}

	public int findIndexByName(final String name) {
		int index = 0;
		for (int i = 0; i < customerList.size(); i++) {
			if (customerList.get(i).getName().contains(name)) {
				index = i;
			}
		}
		return index;
	}

	public Customer registerCustomer(Customer customer) throws ParkingException {
		Long id = 0L;
		if (customerList.size() > 0) {
			id = customerList.get(customerList.size() - 1).getId();
			id++;
		}
		customer.setId(Long.valueOf(id));
		customerList.add(customer);
		return customer;
	}

	public int findIndexById(final Long integer) {
		int index = 0;
		for (int i = 0; i < customerList.size(); i++) {
			if (customerList.get(i).getId() == integer) {
				index = i;
			}
		}
		return index;
	}
}
