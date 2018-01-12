package ipass.server.domain;

import javax.persistence.*;
import javax.validation.constraints.Digits;
import javax.validation.constraints.NotNull;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import com.owlike.genson.annotation.JsonIgnore;

import org.hibernate.annotations.DynamicUpdate;
import org.hibernate.annotations.Type;

import java.io.Serializable;
import java.util.Date;

@DynamicUpdate
@Entity
@Table(name = "alerts")
public class AlertEntity implements Serializable {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1367823791625693253L;

	/**
	 * 
	 */
	@Id
	@Column(name = "id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;
	
	@NotNull (message="{alert.handled.required}")
	@Column(name = "handled", nullable = false)
    @Type(type = "org.hibernate.type.BooleanType")

	private boolean handled;

	@NotNull
	@OneToOne
	@JoinColumn (name="alarm_id")
	private AlarmEntity alarm;

	public AlarmEntity getAlarm() {
		return alarm;
	}
	public void setAlarm(AlarmEntity alarm) {
		this.alarm = alarm;
	}



	
	public AlertEntity(){
		this.id = null;
		this.handled = false;
		this.alarm = null;
	}
	public AlertEntity(boolean handled, AlarmEntity alart){
		this.handled = handled;
		this.alarm = alart;
	}
	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public boolean getHandled() {
		return handled;
	}

	public void setHandled(boolean handled) {
		this.handled = handled;
	}

	
}
