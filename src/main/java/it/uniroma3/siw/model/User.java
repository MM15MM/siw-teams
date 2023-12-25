package it.uniroma3.siw.model;

import java.time.LocalDate;
import java.util.LinkedList;
import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToMany;
import javax.persistence.OneToMany;
import javax.persistence.OneToOne;
import javax.persistence.Table;
import javax.validation.constraints.NotBlank;

import org.springframework.format.annotation.DateTimeFormat;

@Entity
@Table(name = "users")
public class User {
	
	@Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

	@NotBlank
	private String name;
	
	@NotBlank
	private String surname;
	
	@OneToOne
	@JoinColumn(name = "president_id")
	private President president;
	
	
    @NotBlank
    private String fiscalCode;

    @DateTimeFormat(pattern = "yyyy-MM-dd")
    private LocalDate birthDate;

    @NotBlank
    private String placeOfBirth;
	

	
	public void setPresident(President president) {
		this.president=president;
	}
	public President getPresident(){
		return this.president;
	}

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
	
	
	public String getSurname() {
		return surname;
	}
	
	public void setSurname(String surname) {
		this.surname = surname;
	}

	/*setter e getter codice fiscale*/
    public void setFiscalCode(String fiscalCode) {
        this.fiscalCode = fiscalCode;
    }
 
    public String getFiscalCode() {
        return this.fiscalCode;
    }
	
	/*setter e getter data nascita*/
    public void setBirthDate(LocalDate birthDate) {
        this.birthDate = birthDate;
    }
 
    public LocalDate getBirthDate() {
        return this.birthDate;
    }
	
	/*setter e getter luogo nascita*/
    public void setPlaceOfBirth(String placeOfBirth) {
        this.placeOfBirth = placeOfBirth;
    }
    
    public String getPlaceOfBirth() {
        return this.placeOfBirth;
    }

}

