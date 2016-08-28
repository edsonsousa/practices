package br.edson.sousa.model;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;
import java.util.List;

import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import javax.validation.constraints.NotNull;
import javax.xml.bind.annotation.XmlRootElement;

/**
 * Entity represents the customer parking register.
 *
 * @author Edson Sousa
 *
 */
@Entity
@XmlRootElement
@Table(name = "ParkingInvoice")
public class ParkingInvoice implements Serializable {

	/**
	 *
	 */
	private static final long serialVersionUID = -5589701567962916771L;

	@Id
	@GeneratedValue
	private Long id;

	@NotNull
	@ManyToOne
	private Customer customer;

	@OneToMany
	private List<ParkingRegister> parkingRegisters;
	
	@NotNull
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "id_company", referencedColumnName = "id", nullable = false)
	private ParkingCompany company;

	@NotNull
	private Date dateGenerated;

	@NotNull
	private BigDecimal totalInvoice;

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

	public List<ParkingRegister> getParkingRegisters() {
		return parkingRegisters;
	}

	public void setParkingRegisters(List<ParkingRegister> parkingRegisters) {
		this.parkingRegisters = parkingRegisters;
	}

	public Date getDateGenerated() {
		return dateGenerated;
	}

	public void setDateGenerated(Date dateGenerated) {
		this.dateGenerated = dateGenerated;
	}

	public BigDecimal getTotalInvoice() {
		return totalInvoice;
	}

	public void setTotalInvoice(BigDecimal totalInvoice) {
		this.totalInvoice = totalInvoice;
	}

	public ParkingCompany getCompany() {
		return company;
	}

	public void setCompany(ParkingCompany company) {
		this.company = company;
	}

}
