package ipass.server.domain;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import com.owlike.genson.annotation.JsonIgnore;

import org.hibernate.annotations.Cascade;
import org.hibernate.annotations.DynamicUpdate;
import org.hibernate.annotations.NamedQueries;
import org.hibernate.annotations.NamedQuery;
import org.hibernate.validator.constraints.Email;

import javax.persistence.*;
import javax.validation.constraints.Digits;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;

@DynamicUpdate
@Entity
@Table(name = "users")
@NamedQueries({
	@NamedQuery(
			name="test", 
			query="SELECT u FROM UserEntity u WHERE u.email = :email AND u.password = :password")
})
public class UserEntity implements Serializable{

	/**
	 * 
	 */
	private static final long serialVersionUID = 1307203626745175042L;
	public static final String FIND_BY_LOGIN_PASSWORD = "UserEntity.findByLoginAndPassword";
	
	@Id
	@Column(name = "id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;
	
	@NotNull(message="{user.firstname.required}")
	@Pattern(regexp="^[A-Za-z0-9]*$", message="{user.firstname.pattern}")
	@Column(name = "firstname", nullable = false)
	private String firstname;
	
	@NotNull(message="{user.lastname.required}")
	@Pattern(regexp="^[A-Za-z0-9]*$", message="{user.lastname.pattern}")
	@Column(name = "lastname", nullable = false)
	private String lastname;
	
	@NotNull(message="{user.address.required}")
	@Pattern(regexp="^[A-Za-z0-9]*$", message="{user.address.pattern}")
	@Column(name = "address", nullable = false)
	private String address;
	
	@NotNull(message="{user.residence.required}")
	@Pattern(regexp="^[A-Za-z0-9]*$", message="{user.residence.pattern}")
	@Column(name = "residence", nullable = false)
	private String residence;
	
	@Email
	@NotNull(message="{user.email.required}")
	@Column(name = "email", nullable = false)
	private String email;
	
	@NotNull(message="{user.password.required}")
	@Pattern(regexp="^[A-Za-z0-9]*$", message="{user.password.pattern}")
	@Column(name = "password", nullable = false)
	private String password;
	
	@Digits(fraction = 0, integer = 10)
	@Column(name = "phone", nullable = false)
	private int phone;
	
	@Column(name = "latitude", nullable = false)
	private double latitude;
	
	@Column(name = "longitude", nullable = false)
	private double longitude;
	
	@Column(name ="aandoening", nullable = false)
	private String aandoening;


    public String getAandoening() {
		return aandoening;
	}

	public void setAandoening(String aandoening) {
		this.aandoening = aandoening;
	}
	@OneToMany (mappedBy = "user")
	private Set<AlarmEntity> alarms;
	
	@OrderBy(value="trigger_date DESC, handled ASC")
	@ManyToMany(cascade={CascadeType.REFRESH, CascadeType.DETACH})
	@JoinTable(name="user_alerts",
			joinColumns={@JoinColumn(name="users_id")},
			inverseJoinColumns={@JoinColumn(name="alerts_id")})
	private Set<AlertEntity> alerts;

	@ManyToMany(cascade={CascadeType.ALL})
	@JoinTable(name="user_patients",
		joinColumns={@JoinColumn(name="user_id")},
		inverseJoinColumns={@JoinColumn(name="user_patients_id")})
	private Set<UserEntity> patients = new HashSet<UserEntity>();
    
	@ManyToMany(cascade={CascadeType.ALL})
	@JoinTable(name="user_patients",
		joinColumns={@JoinColumn(name="user_patients_id")},
		inverseJoinColumns={@JoinColumn(name="user_id")})    
    private Set<UserEntity> zorgdragers = new HashSet<UserEntity>();
    
    @JsonIgnore
    public Set<AlertEntity> getAlerts() {
        return alerts;
    }

    @JsonIgnore
    public Set<UserEntity> getPatients() {
		return patients;
	}
	public void setPatients(Set<UserEntity> patients) {
		this.patients = patients;
	}
    @JsonIgnore
    public Set<AlarmEntity> getAlarms() {
        return alarms;
    }
    public void setAlarms(Set<AlarmEntity> alarms) {
		this.alarms = alarms;
	}

	public void setAlerts(Set<AlertEntity> alerts) {
		this.alerts = alerts;
	}

	@JsonIgnore
    public Set<UserEntity> getZorgdragers(){
    	return this.zorgdragers;
    }
    public void setZorgdragers(Set<UserEntity> zorgdragers){
    	this.zorgdragers = zorgdragers;
    }
    @JsonIgnore
	public String getPassword() {
		return password;
	}

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getFirstname() {
		return firstname;
	}

	public void setFirstname(String firstname) {
		this.firstname = firstname;
	}

	public String getLastname() {
		return lastname;
	}

	public void setLastname(String lastname) {
		this.lastname = lastname;
	}

	public String getAddress() {
		return address;
	}

	public void setAddress(String address) {
		this.address = address;
	}

	public String getResidence() {
		return residence;
	}

	public void setResidence(String recidence) {
		this.residence = recidence;
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	public int getPhone() {
		return phone;
	}

	public void setPhone(int phone) {
		this.phone = phone;
	}

	public double getLatitude() {
		return latitude;
	}

	public void setLatitude(double latitude) {
		this.latitude = latitude;
	}

	public double getLongitude() {
		return longitude;
	}

	public void setLongitude(double longitude) {
		this.longitude = longitude;
	}
	public UserEntity(){
		this.firstname = null;
		this.lastname = null;
		this.address = null;
		this.residence = null;
		this.email = null;
		this.password = null;
		this.latitude = 0;
		this.longitude = 0;
	}
	public UserEntity(
			String fn, 
			String ln, 
			String ad, 
			String res, 
			String e, 
			String pass, 
			int phone, 
			double latitude, 
			double longitude
	){
		this.firstname = fn;
		this.lastname = ln;
		this.address = ad;
		this.residence = res;
		this.email = e;
		this.password = pass;
		this.latitude = latitude;
		this.longitude = longitude;
	}
	//@JsonIgnore

	public String getRole(){
		return  this.patients.size() > 0 ? "zorgdrager" : "patient";
	}
	public String toString(){
		return this.id + this.password;
	}
}
