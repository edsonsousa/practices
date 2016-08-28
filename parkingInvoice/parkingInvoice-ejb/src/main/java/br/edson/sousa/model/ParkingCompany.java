package br.edson.sousa.model;

import java.io.Serializable;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.persistence.UniqueConstraint;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;
import javax.validation.constraints.Size;
import javax.xml.bind.annotation.XmlRootElement;

import org.hibernate.validator.constraints.Email;
import org.hibernate.validator.constraints.NotEmpty;

/**
 * Entity represents the customer
 *
 * @author Edson Sousa
 *
 */
@Entity
@XmlRootElement
@Table(name = "ParkingRegister", uniqueConstraints = @UniqueConstraint(columnNames = "name"))
public class ParkingCompany implements Serializable {

	/**
	 *
	 */
	private static final long serialVersionUID = 3640024931872003031L;

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;

	@NotNull
	@Size(min = 1, max = 25)
	@Pattern(regexp = "[^0-9]*", message = "Must not contain numbers")
	private String name;

	@NotNull
	@NotEmpty
	@Email
	private String emailCompany;

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getEmailCompany() {
		return emailCompany;
	}

	public void setEmailCompany(String emailCompany) {
		this.emailCompany = emailCompany;
	}

}
