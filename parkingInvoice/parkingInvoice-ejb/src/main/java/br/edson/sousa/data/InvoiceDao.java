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
import br.edson.sousa.model.ParkingInvoice;

@ApplicationScoped
public class InvoiceDao {

	List<ParkingInvoice> invoices = new ArrayList<ParkingInvoice>();

	public ParkingInvoice findById(Long id) {
		return invoices.get(findIndexById(id));
	}

	public List<ParkingInvoice> findAllRegitersByCustomer(Customer customer) {
		List<ParkingInvoice> result = new ArrayList<ParkingInvoice>();
		for (ParkingInvoice invoice : invoices) {
			if (invoice.getCustomer().getName().equals(customer.getName())) {
				result.add(invoice);
			}
		}
		return result;

	}

	public List<ParkingInvoice> getAll() {

		return invoices;
	}

	public void registerInvoice(ParkingInvoice invoice) throws ParkingException {
		Long id = 0L;
		if (invoices.size() > 0) {
			id = invoices.get(invoices.size() - 1).getId();
			id++;
		}
		invoice.setId(Long.valueOf(id));
		invoices.add(invoice);

	}

	public int findIndexById(final Long integer) {
		int index = 0;
		for (int i = 0; i < invoices.size(); i++) {
			if (invoices.get(i).getId() == integer) {
				index = i;
			}
		}
		return index;
	}

}
