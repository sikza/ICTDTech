package birthcertificate.entities;

import java.io.Serializable;

import javax.persistence.*;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlSeeAlso;

/**
 * Entity implementation class for Entity: Child
 *
 */
@XmlRootElement
@XmlSeeAlso(Patient.class)
@Entity
public class Child implements Serializable {

	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	private int childId;
	public int getMother() {
		return mother;
	}

	public void setMother(int mother) {
		this.mother = mother;
	}

	private String name;
	private String surname;
	 
	private String gender;
	private int mother;
	private static final long serialVersionUID = 1L;

	public Child() {
		super();
	}

	public int getChildId() {
		return this.childId;
	}

	public void setChildId(int childId) {
		this.childId = childId;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getSurname() {
		return surname;
	}

	public void setSurname(String surname) {
		this.surname = surname;
	}

 

	public String getGender() {
		return gender;
	}

	public void setGender(String gender) {
		this.gender = gender;
	}

}