package ipass.server.domain;


import org.hibernate.annotations.DynamicUpdate;
import org.hibernate.annotations.FilterDef;
import org.hibernate.annotations.FilterDefs;
import org.hibernate.annotations.ParamDef;
import org.hibernate.annotations.Type;

import java.io.Serializable;
import java.sql.Time;
import java.util.Date;

import javax.persistence.*;
import javax.validation.constraints.Digits;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;
import javax.validation.constraints.Size;


@DynamicUpdate
@Entity
@Table(name = "alarms")

public class AlarmEntity implements Serializable {

	private static final long serialVersionUID = -5183069099661541090L;
	
	@Id
	@Column(name = "id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;
	
	@NotNull
	@Pattern(regexp="^[A-Za-z0-9]*$", message="{alarm.day.pattern}")
	@Size(max = 45, min = 1)
	@Column(name = "day", nullable = true)
	private String day;
	
	@NotNull (message="{alarm.time.required}")
	@Column(name = "time", nullable = true, columnDefinition= "TIMESTAMP WITHOUT TIME ZONE")
	private String time;

	@NotNull
	@Column(name = "active", nullable = true)
    @Type(type = "org.hibernate.type.BooleanType")
    private boolean active;

	@NotNull
    @Column(name="keeps_running", nullable = true)
    @Type(type = "org.hibernate.type.BooleanType")
	private boolean keepsRunning;
	
	
	/*@NotNull (message="{alarm.userId.required}")
	@Column(name="user_id", nullable = false)
	private Long userId;*/


    @ManyToOne
    @JoinColumn(name="user_id")
    private UserEntity user;


	public AlarmEntity() {
		this.id = null;
		this.day = null;
		this.time = null;
		this.active =  false;
		this.keepsRunning = false;
	}
	
	public AlarmEntity(String day, String date, boolean active, boolean keepsRunning, UserEntity user) {
		this.day = day;
		this.time = date;
		this.active = active;
		this.keepsRunning = keepsRunning;
		this.user = user;
		//this.userId = userId;
	}

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}
	public String getDay() {
		return day;
	}

	public void setDay(String day) {
		this.day = day;
	}

	public String getTime() {
		return time;
	}
	public UserEntity getUser(){
		return this.user;
	}
	public void setUser(UserEntity entity){
		this.user = entity;
	}
	public void setTime(String time) {
		this.time = time;
	}

	public boolean getActive() {
		return active;
	}

	public void setActive(boolean active) {
		this.active = active;
	}

	public boolean getKeepsRunning() {
		return keepsRunning;
	}

	public void setKeepsRunning(boolean keepsRunning) {
		this.keepsRunning = keepsRunning;
	}

	/*public Long getUserId() {
		return userId;
	}
	public void setUserId(Long userId) {
		this.userId = userId;
	}*/

	public static long getSerialversionuid() {
		return serialVersionUID;
	}
	public String toString(){
		return this.id+"";
	}


}
