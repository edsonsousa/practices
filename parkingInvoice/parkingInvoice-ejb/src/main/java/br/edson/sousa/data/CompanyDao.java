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
import javax.inject.Inject;
import javax.persistence.EntityManager;

import br.edson.sousa.exception.ParkingException;
import br.edson.sousa.model.ParkingCompany;

@ApplicationScoped
public class CompanyDao {

	@Inject
	EntityManager em;

	List<ParkingCompany> companyList = new ArrayList<ParkingCompany>();


	public ParkingCompany findById(Long id) {
		return companyList.get(findIndexById(id));
	}

	public List<ParkingCompany> getAll() {
		if(companyList.isEmpty()){
			ParkingCompany c = new ParkingCompany();
			c.setName("companyA");
			c.setEmailCompany("fsdsfd@gmail.com");
			try {
				registerCompany(c);
			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		return companyList;
	}

	public ParkingCompany findByName(String name) {
//		CriteriaBuilder cb = em.getCriteriaBuilder();
//        CriteriaQuery<ParkingCompany> criteria = cb.createQuery(ParkingCompany.class);
//        Root<ParkingCompany> company = criteria.from(ParkingCompany.class);
//
//        criteria.select(company).where(cb.equal(company.get("name"), name));
//        try {
//        	 return em.createQuery(criteria).getSingleResult();
//		} catch (NoResultException e) {
//			return null;
//		}
		if(!companyList.isEmpty()){
			for (int i = 0; i < companyList.size(); i++) {
				if (companyList.get(i).getName().equals(name)) {
					return companyList.get(i);
				}
			}
		}
		return null;
	}

	public ParkingCompany registerCompany(ParkingCompany company) throws ParkingException {
		Long id = 0L;
		if (companyList.size() > 0) {
			id = companyList.get(companyList.size() - 1).getId();
			id++;
		}
		company.setId(Long.valueOf(id));
		companyList.add(company);
//		em.persist(company);
		return company;
	}

	public int findIndexById(final Long integer) {
		int index = 0;
		for (int i = 0; i < companyList.size(); i++) {
			if (companyList.get(i).getId() == integer) {
				index = i;
			}
		}
		return index;
	}
}
