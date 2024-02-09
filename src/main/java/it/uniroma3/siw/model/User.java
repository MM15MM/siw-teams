package it.uniroma3.siw.model;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.OneToOne;
import javax.persistence.Table;
import javax.validation.constraints.NotBlank;

@Entity
@Table(name = "users")
public class User {
	
	@Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

	
	@OneToOne
	@JoinColumn(name = "president_id")
	private President president;
	
	@NotBlank
	private String code;
	
    @NotBlank
    private String fiscalCode;

	

    /*setter e getter password presidente*/
	public void setCode(String code) {
		this.code=code;
	}
	public String getCode(){
		return this.code;
	}
    
    
    
    /*setter e getter presidente*/
	public void setPresident(President president) {
		this.president=president;
	}
	public President getPresident(){
		return this.president;
	}
	/*setter e getter id*/
    public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}
	
	/*setter e getter codice fiscale*/
    public void setFiscalCode(String fiscalCode) {
        this.fiscalCode = fiscalCode;
    }
 
    public String getFiscalCode() {
        return this.fiscalCode;
    }
	
	

}

