package com.example.demo.entity;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.JoinColumns;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@Getter @Setter
@Entity
@Table(name = "user_auth_map")
@ToString
public class UserAuthMap {
	
	@Id
	@GeneratedValue
	private Long id;
	
	@ManyToOne(targetEntity = User.class, fetch = FetchType.LAZY)
	@JoinColumns(value={ @JoinColumn(name="user_id", referencedColumnName="id", nullable=false) })	
	private User user;

	@ManyToOne(targetEntity = UserAuth.class, fetch = FetchType.LAZY)
	@JoinColumns(value={ @JoinColumn(name="auth_id", referencedColumnName="id", nullable=false) })	
	private UserAuth auth;
	
	@Column(name = "IsEnable", nullable = false, length = 1)
	private boolean isEnable = false;
	
	public UserAuthMap() {
		
	}
	
	public UserAuthMap(User user, UserAuth auth, boolean isEnable) {
		this.user = user;
		this.auth = auth;
		this.isEnable = isEnable;
	}
}
