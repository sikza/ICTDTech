package birthtech.entities;

import java.io.Serializable;
import java.lang.String;

 




import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.xml.bind.annotation.XmlRootElement;

 
@Entity
@XmlRootElement
public class Martenal implements Serializable {  
	 
	@Id
	@GeneratedValue(strategy=GenerationType.AUTO)
	private int pid;
	private String names;
	private String surname;
	private int idnumber;
	private static final long serialVersionUID = 1L;

	public Martenal() {
		super();
	}   
	public int getPid() {
		return this.pid;
	}

	public void setPid(int pid) {
		this.pid = pid;
	}   
	public String getNames() {
		return this.names;
	}

	public void setNames(String names) {
		this.names = names;
	}   
	public String getSurname() {
		return this.surname;
	}

	public void setSurname(String surname) {
		this.surname = surname;
	}   
	public int getIdnumber() {
		return this.idnumber;
	}

	public void setIdnumber(int idnumber) {
		this.idnumber = idnumber;
	}
   
}
