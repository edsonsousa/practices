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
package br.edson.sousa.rest;

import java.io.InputStream;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.logging.Logger;

import javax.enterprise.context.RequestScoped;
import javax.inject.Inject;
import javax.persistence.NoResultException;
import javax.validation.ConstraintViolation;
import javax.validation.ConstraintViolationException;
import javax.validation.ValidationException;
import javax.validation.Validator;
import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.WebApplicationException;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import br.edson.sousa.data.CustomerDao;
import br.edson.sousa.data.ParkingDao;
import br.edson.sousa.exception.ParkingException;
import br.edson.sousa.model.Customer;
import br.edson.sousa.model.ParkingRegister;
import br.edson.sousa.service.ParkingService;

@Path("/customers")
@RequestScoped
public class ParkingResourceRESTService {
	@Inject
	private Logger log;

	@Inject
	private Validator validator;

	@Inject
	private ParkingDao parkingDao;

	@Inject
	private CustomerDao customerDao;

	@Inject
	private ParkingService parkingService;

	@GET
	@Produces(MediaType.APPLICATION_JSON)
	public List<Customer> listAllCustomers() {
		return customerDao.getAll();
	}

	@GET
	@Path("/customer/{name}")
	@Produces(MediaType.APPLICATION_JSON)
	public Customer lookupCustomerByName(@PathParam("name") String name) {
		Customer customer = customerDao.findByName(name);
		if (customer == null) {
			throw new WebApplicationException(Response.Status.NOT_FOUND);
		}
		return customer;
	}

	@GET
	@Path("/customerparkinglog/{name}")
	@Produces(MediaType.APPLICATION_JSON)
	public List<ParkingRegister> lookupParkingLogByCustomerName(@PathParam("name") String name) {
		Customer customer = customerDao.findByName(name);
		if(customer != null){
			List<ParkingRegister> parkingList = parkingDao.findAllRegitersByCustomer(customer);
			if (parkingList == null) {
				throw new WebApplicationException(Response.Status.NOT_FOUND);
			}
			return parkingList;
		}
		throw new WebApplicationException(Response.Status.NOT_FOUND);
	}

	/**
	 * Creates a new member from the values provided. Performs validation, and
	 * will return a JAX-RS response with either 200 ok, or with a map of
	 * fields, and related errors.
	 */
	@POST
	@Path("/registerParking/")
	@Consumes(MediaType.APPLICATION_JSON)
	@Produces(MediaType.APPLICATION_JSON)
	public Response registerParking(ParkingRegister parkingRegister) {

		Response.ResponseBuilder builder = null;

		try {
			// Validates using bean validation
			validateParkingRegister(parkingRegister);

			parkingService.registerParking(parkingRegister);

			builder = Response.ok();
		} catch (ConstraintViolationException ce) {
			builder = createViolationResponse(ce.getConstraintViolations());
		} catch (Exception e) {
			Map<String, String> responseObj = new HashMap<String, String>();
			responseObj.put("error", e.getMessage());
			builder = Response.status(Response.Status.BAD_REQUEST).entity(responseObj);
		}

		return builder.build();
	}

	/**
	 * Creates a JAX-RS "Bad Request" response including a map of all violation
	 * fields, and their message. This can then be used by clients to show
	 * violations.
	 *
	 * @param violations
	 *            A set of violations that needs to be reported
	 * @return JAX-RS response containing all violations
	 */
	private Response.ResponseBuilder createViolationResponse(Set<ConstraintViolation<?>> violations) {
		log.fine("Validation completed. violations found: " + violations.size());

		Map<String, String> responseObj = new HashMap<String, String>();

		for (ConstraintViolation<?> violation : violations) {
			responseObj.put(violation.getPropertyPath().toString(), violation.getMessage());
		}

		return Response.status(Response.Status.BAD_REQUEST).entity(responseObj);
	}

	/**
	 *
	 * @param parkingRegister
	 *            Parking Register to validate
	 * @throws ConstraintViolationException
	 *             If Bean Validation errors exist
	 * @throws ValidationException
	 *             If customer with the same name already exists
	 */
	private void validateParkingRegister(ParkingRegister parkingRegister)
			throws ConstraintViolationException, ValidationException {
		// Create a bean validator and check for issues.
		Set<ConstraintViolation<ParkingRegister>> violations = validator.validate(parkingRegister);

		if (!violations.isEmpty()) {
			throw new ConstraintViolationException(new HashSet<ConstraintViolation<?>>(violations));
		}
	}

	public boolean customerNameExists(String name) {
		Customer customer = null;
		try {
			customer = customerDao.findByName(name);
		} catch (NoResultException e) {
			// ignore
		}
		return customer != null;
	}

	@GET
	@Path("/verify")
	@Produces(MediaType.TEXT_PLAIN)
	public Response verifyRESTService(InputStream incomingData) {
		String result = "Successfully started..";

		// return HTTP response 200 in case of success
		return Response.status(200).entity(result).build();
	}

}
