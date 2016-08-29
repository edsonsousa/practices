package br.edson.sousa.model;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;

import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import javax.validation.constraints.NotNull;
import javax.xml.bind.annotation.XmlRootElement;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

/**
 * Entity represents the customer parking register.
 *
 * @author Edson Sousa
 *
 */
@Entity
@XmlRootElement
@Table(name = "ParkingRegister")
public class ParkingRegister implements Serializable {

	/**
	 *
	 */
	private static final long serialVersionUID = -5589701567962916771L;

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;

	@NotNull
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "id_customer", referencedColumnName = "id", nullable = false)
	private Customer customer;

	@NotNull
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "id_company", referencedColumnName = "id", nullable = false)
	private ParkingCompany company;

	private Date startParking;

	private Date finishParking;

	private BigDecimal parkingValueCalculated;

	private Date dateValueCalculated;

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "id_invoice", referencedColumnName = "id", nullable = false)
	private ParkingInvoice invoice;

	@Override
	public String toString() {
		ObjectMapper mapper = new ObjectMapper();

		try {
			return mapper.writeValueAsString(this);
		} catch (JsonProcessingException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		return "";
	}

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public Customer getCustomer() {
		return customer;
	}

	public void setCustomer(Customer customer) {
		this.customer = customer;
	}

	public BigDecimal getParkingValueCalculated() {
		return parkingValueCalculated;
	}

	public void setParkingValueCalculated(BigDecimal parkingValueCalculated) {
		this.parkingValueCalculated = parkingValueCalculated;
	}

	public Date getDateValueCalculated() {
		return dateValueCalculated;
	}

	public void setDateValueCalculated(Date dateValueCalculated) {
		this.dateValueCalculated = dateValueCalculated;
	}

	public Date getStartParking() {
		return startParking;
	}

	public void setStartParking(Date startParking) {
		this.startParking = startParking;
	}

	public Date getFinishParking() {
		return finishParking;
	}

	public void setFinishParking(Date finishParking) {
		this.finishParking = finishParking;
	}

	public ParkingCompany getCompany() {
		return company;
	}

	public void setCompany(ParkingCompany company) {
		this.company = company;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((id == null) ? 0 : id.hashCode());
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj) {
			return true;
		}
		if (obj == null) {
			return false;
		}
		if (getClass() != obj.getClass()) {
			return false;
		}
		ParkingRegister other = (ParkingRegister) obj;
		if (id == null) {
			if (other.id != null) {
				return false;
			}
		} else if (!id.equals(other.id)) {
			return false;
		}
		return true;
	}

	public ParkingInvoice getInvoice() {
		return invoice;
	}

	public void setInvoice(ParkingInvoice invoice) {
		this.invoice = invoice;
	}

}
