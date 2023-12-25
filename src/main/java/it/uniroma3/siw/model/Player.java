package it.uniroma3.siw.model;

import java.time.LocalDate;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.validation.constraints.NotBlank;
import org.springframework.format.annotation.DateTimeFormat;

@Entity
public class Player {
	
	@Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;
	
	@ManyToOne
	@JoinColumn(name = "team_id")
    private Team team;
	
	@NotBlank
    private String name;
	
	@NotBlank
    private String surname;
	
	@DateTimeFormat(pattern = "yyyy-MM-dd")
    private LocalDate birthDate;
	
	@NotBlank
    private String placeOfBirth;
	
	@NotBlank
    private String role;
	
	@Column(name = "membership_start_date")
	 private LocalDate membershipStartDate;

	 @Column(name = "membership_end_date")
	 private LocalDate membershipEndDate;
	
	/*setter e getter id*/
	public void setId(Long id) {
		this.id=id;
	}
	
	public Long getId() {
		return this.id;
	}
    
	/* getter e setter team*/
    public void setTeam(Team team) {
        this.team = team;
    }

    public Team getTeam() {
        return this.team;
    }
    
    /*getter e setter nome*/
    public void setName(String name) {
    	this.name=name;
    }
    
    public String getName() {
    	return this.name;
    }
    
    /*getter e setter cognome*/
     public void setSurname(String surname) {
        this.surname = surname;
    }

    public String getSurname() {
        return this.surname;
    }

   /*setter e getter data nascita*/   
    public void setBirthDate(LocalDate birthDate) {
    	this.birthDate=birthDate;
    }
    
    public LocalDate getBirthDate() {
        return this.birthDate;
    }

   /*setter e getter luogo di nascita*/
    public void setPlaceOfBirth(String placeOfBirth) {
        this.placeOfBirth = placeOfBirth;
    }
    
    public String getPlaceOfBirth() {
        return this.placeOfBirth;
    }

    /*setter e getter ruolo*/

    public String getRole() {
        return role;
    }

    public void setRole(String role) {
        this.role = role;
    }
    
    /*setter e getter inizio tesseramento*/
    public void setMembershipStartDate(LocalDate membershipStartDate) {
        this.membershipStartDate = membershipStartDate;
    } 
    
    public LocalDate getMembershipStartDate() {
        return this.membershipStartDate;
    }

    /*getter e setter data termine tesseramento*/
     public void setMembershipEndDate(LocalDate membershipEndDate) {
        this.membershipEndDate = membershipEndDate;
    }
	   
    public LocalDate getMembershipEndDate() {
        return this.membershipEndDate;
    }


}
