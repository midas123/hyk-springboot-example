package com.example.demo.entity;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.ManyToMany;
import javax.persistence.OneToMany;
import javax.persistence.Table;

import org.hibernate.annotations.Cascade;
import org.hibernate.annotations.CascadeType;

import com.example.demo.dto.UserDto;

import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter @Setter
@NoArgsConstructor
@Entity
@Table(name="user_info")
public class UserInfo {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;
	private String userName;
	private String password;
	
	@OneToMany
//	@Cascade(CascadeType.SAVE_UPDATE)
	@Cascade(CascadeType.MERGE)
	@JoinColumn(name="user_id")
	private List<Address> addresses = new ArrayList<Address>();
	
	
	@OneToMany(fetch = FetchType.EAGER)
	@Cascade(CascadeType.DELETE)
	@JoinColumn(name="user_id")
	private List<OrderBasket> orders = new ArrayList<>();
	
//	@OneToMany(mappedBy = "user", targetEntity = UserAuthMap.class, fetch = FetchType.EAGER)
//	private List<UserAuthMap> userAuthMaps = new ArrayList<>();

	@ManyToMany(targetEntity = UserAuth.class, fetch = FetchType.EAGER)
	@Cascade(CascadeType.SAVE_UPDATE)
	@JoinTable(name="user_auth_map", joinColumns={ @JoinColumn(name="user_id") }, inverseJoinColumns={ @JoinColumn(name="auth_id") })		
	private java.util.Set<UserAuth> auths = new HashSet<>();

	@Builder
	public UserInfo(String userName, String password) {
		this.userName = userName;
		this.password = password;
	}
	
	public void addAddress(Address address) {
		this.addresses.add(address);
	}
	
	public void addAuth(UserAuth auth) {
		this.auths.add(auth);
	}
	
	public void addOrder(OrderBasket order) {
		this.orders.add(order);
	}
	
	public static UserInfo dtoToEntity(UserDto dto) {
		return UserInfo.builder()
				.userName(dto.getUsername())
				.password(dto.getPassword())
				.build();
	}
}
