package com.example.demo.entity;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.JoinTable;
import javax.persistence.ManyToMany;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import javax.persistence.JoinColumn;

import org.hibernate.annotations.Cascade;
import org.hibernate.annotations.CascadeType;

import com.sun.istack.NotNull;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.ToString;

@Entity
@Getter
@NoArgsConstructor
@Table(name = "user_auth")
@ToString
public class UserAuth {
	
	@Id
	@GeneratedValue
	private Long id;
	
    @Column(name = "auth_name", length = 50)
    @NotNull
    @Enumerated(EnumType.STRING)
	private AuthorityName authName;
    
    public UserAuth(AuthorityName authName) {
    	this.authName = authName;
    }
    
    public UserAuth(Long id, AuthorityName authName) {
    	this.id = id;
    	this.authName = authName;
    }
    
//    @OneToMany(mappedBy = "auth", targetEntity = UserAuthMap.class, fetch = FetchType.LAZY)
//	private List<UserAuthMap> userAuthMap = new ArrayList<>();
    
//	@ManyToMany(targetEntity=UserInfo.class, fetch= FetchType.LAZY)	
	@ManyToMany(mappedBy = "auths", fetch= FetchType.EAGER)	
	@org.hibernate.annotations.Cascade({org.hibernate.annotations.CascadeType.PERSIST})	
	//@org.hibernate.annotations.Cascade({org.hibernate.annotations.CascadeType.SAVE_UPDATE, org.hibernate.annotations.CascadeType.LOCK})	
	private java.util.Set<UserInfo> users = new java.util.HashSet<UserInfo>();
	
	public void addUser(UserInfo user) {
		this.users.add(user);
	}

	public UserAuth(long l) {
	}
}
