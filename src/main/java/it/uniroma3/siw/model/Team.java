package it.uniroma3.siw.model;

import java.util.List;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import javax.persistence.OneToOne;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

@Entity
public class Team {
	
	    @Id
        @GeneratedValue(strategy = GenerationType.AUTO)
        private Long id;
	   
	    @NotBlank
	    private String name;

	    @NotNull
	    private Integer year;

	    @NotBlank
	    private String address;
	    
	    private String image;
	    
	    @NotBlank
	    private String sport;

	    @OneToOne(mappedBy = "team")
	    private President president;

	    @OneToMany(mappedBy="team")
	    private List<Player> players;
	    
	    /*setter e getter id*/
		public void setId(Long id) {
			this.id=id;
		}
		
		public Long getId() {
			return this.id;
		}
		
		/*setter e getter sport*/
		public void setImage(String img) {
			this.image=img;
		}
		
		public String getImage() {
			return this.image;
		}
		
		/*setter e getter sport*/
		public void setSport(String s) {
			this.sport=s;
		}
		
		public String getSport() {
			return this.sport;
		}
		
		/*setter e getter nome*/
	    public void setName(String name) {
	        this.name = name;
	    }	
	    
	    public String getName() {
	        return this.name;
	    }

	    /*setter e getter anno fondazione*/
	    public void setYear(Integer year) {
	        this.year = year;
	    }	
	    
	    public Integer getYear() {
	        return this.year;
	    }

	    /*setter e getter indirizzo sede*/
	    public void setAddress(String address) {
	        this.address = address;
	    }
	 	    
	    public String getAddress() {
	        return this.address;
	    }

	    /*setter e getter presidente*/
	    public void setPresident(President president) {
	        this.president = president;
	    }

	    public President getPresident() {
	        return this.president;
	    }

       /*setter e getter giocatori*/
	    public void setPlayers(List<Player> players) {
	        this.players = players;
	    }
	  	    
	    public List<Player> getPlayers() {
	        return players;
	    }

  public Team getTeam() {
	  return this;
  }
}
