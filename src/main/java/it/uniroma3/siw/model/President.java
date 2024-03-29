package it.uniroma3.siw.model;

import java.time.LocalDate;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.OneToOne;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

import org.springframework.format.annotation.DateTimeFormat;

@Entity
public class President {
	
	@Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

	@OneToOne
    @JoinColumn(name = "team_id", nullable=true)
	private Team team;
	
    @NotBlank
    private String fiscalCode;

    @NotNull
    @DateTimeFormat(pattern = "yyyy-MM-dd")
    private LocalDate birthDate;

    @NotBlank
    private String placeOfBirth;
    
	@NotBlank
	private String name;
	
	@NotBlank
	private String surname;
	
	@NotBlank
	private String sport;
	
	@OneToOne
	private User user;
	
	@NotBlank
	private String code;
	
	
	 /*setter e getter password presidente*/
		public void setCode(String code) {
			this.code=code;
		}
		public String getCode(){
			return this.code;
		}
	
	public void setUser(User user) {
		this.user=user;
	}
	
	public User getUser() {
		return this.user;
	}
	/*setter e getter id*/
    public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}
	
	/*setter e getter nome*/
	public String getName() {
		return name;
	}
	
	public void setName(String name) {
		this.name = name;
	}
	
	/*setter e getter cognome*/
	public String getSurname() {
		return surname;
	}
	
	public void setSurname(String surname) {
		this.surname = surname;
	}
	
	/*setter e getter sport*/
	public String getSport() {
		return sport;
	}
	
	public void setSport(String sport) {
		this.sport = sport;
	}
	
	/*setter e getter squadra*/
    public void setTeam(Team team) {
        this.team = team;
    }
 	
    public Team getTeam() {
        return this.team;
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
    public President getPlayer() {
    	return this;
    }
}

