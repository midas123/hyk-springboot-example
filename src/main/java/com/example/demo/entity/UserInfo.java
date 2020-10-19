package com.example.demo.entity;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;

import com.example.demo.dto.UserDto;

import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter @Setter
@NoArgsConstructor
@Entity
public class UserInfo {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;
	private String userName;
	private String password;
	
	
	@Builder
	public UserInfo(String userName, String password) {
		this.userName = userName;
		this.password = password;
	}
	
	public static UserInfo dtoToEntity(UserDto dto) {
		return UserInfo.builder()
				.userName(dto.getUsername())
				.password(dto.getPassword())
				.build();
	}
}
