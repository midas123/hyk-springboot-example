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
@Table(name="users")
public class User {
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
	
	@OneToMany(mappedBy = "user", targetEntity = UserAuthMap.class, fetch = FetchType.EAGER)
	@org.hibernate.annotations.Cascade({org.hibernate.annotations.CascadeType.SAVE_UPDATE})
	private Set<UserAuthMap> userAuthMaps = new HashSet<>();

	@Builder
	public User(String userName, String password) {
		this.userName = userName;
		this.password = password;
	}
	
	public void addAddress(Address address) {
		this.addresses.add(address);
	}
	
	public void addAuthMapping(UserAuthMap authMap) {
		this.userAuthMaps.add(authMap);
	}
	
	public void addOrder(OrderBasket order) {
		this.orders.add(order);
	}
	
	public static User dtoToEntity(UserDto dto) {
		return User.builder()
				.userName(dto.getUsername())
				.password(dto.getPassword())
				.build();
	}
}
