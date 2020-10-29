package com.example.demo.test;

import static org.junit.Assert.assertEquals;

import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.EntityTransaction;

import org.hibernate.Transaction;
import org.junit.Test;
import org.junit.jupiter.api.MethodOrderer.OrderAnnotation;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.TestMethodOrder;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.Rollback;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.transaction.annotation.Transactional;

import com.example.demo.entity.Address;
import com.example.demo.entity.AuthorityName;
import com.example.demo.entity.OrderBasket;
import com.example.demo.entity.UserAuth;
import com.example.demo.entity.UserInfo;
import com.example.demo.service.UserService;

@RunWith(SpringRunner.class) 
@SpringBootTest
@TestMethodOrder(OrderAnnotation.class)
public class UserAuthTest {
	@Autowired
	UserService userService; 
	
	@Autowired
	private EntityManagerFactory emf;
	
	@Test
	@Transactional
	@Rollback(false)
	@Order(3)
	public void userEntityTest() {
		EntityManager em = emf.createEntityManager();

		UserInfo user = UserInfo.builder()
		.userName("test")
		.password("1234")
		.build();
		
		
		UserAuth auth2 = new UserAuth(AuthorityName.ROLE_ADMIN);
		UserAuth auth = new UserAuth(AuthorityName.ROLE_USER);
		user.addAuth(auth);
		user.addAuth(auth2);
		
		em.getTransaction().begin();
		em.persist(auth);
		em.persist(auth2);
		em.persist(user);
		long userId = user.getId();
		UserInfo resultUserNull = userService.getUser("test");
		em.getTransaction().commit();
		
		UserInfo resultUser = em.find(UserInfo.class, userId);
		
		assertEquals("엔티티 트랙잭션 커밋전 사용자 조회 결과 Null 테스트", true, resultUserNull == null);
		assertEquals("사용자 조회 테스트", true, user.getUserName().equals(resultUser.getUserName()));
		assertEquals("사용자 권한 저장 테스트", true, resultUser.getAuths().size() == 2);
	}
	
	@Test
	@Order(2)
	public void mergeEntityTest() {
		EntityManager em = emf.createEntityManager();

		UserInfo user = UserInfo.builder()
		.userName("test")
		.password("1234")
		.build();
		
		Address ads = new Address("주소");
		user.addAddress(ads);
		
		em.getTransaction().begin();
		em.persist(ads);
		em.persist(user);
		em.getTransaction().commit();
		em.clear();
		
		System.out.println("=============================================");
		
		em.getTransaction().begin();
		UserInfo resultUser = em.find(UserInfo.class, user.getId());
		resultUser.setUserName("test123");
		Address resultAddress = resultUser.getAddresses().get(0);
		resultAddress.setAddress("주소123");
		long addressId = resultAddress.getId();
		em.merge(resultUser);
		em.getTransaction().commit();
		em.clear();
		
		Address adss = em.find(Address.class, addressId);
		
		assertEquals("사용자 주소 테스트", true, "주소123".equals(adss.getAddress()));
	}
	
	@Test
	@Order(1)
	public void deleteEntityTest() {
		EntityManager em = emf.createEntityManager();

		UserInfo user = UserInfo.builder()
		.userName("test")
		.password("1234")
		.build();
		
		OrderBasket order = new OrderBasket("주문");
		user.addOrder(order);
		em.getTransaction().begin();
		em.persist(order);
		em.persist(user);
		long orderId = order.getId();
		em.getTransaction().commit();
		em.clear();
		
		System.out.println("=============================================");
		
		em.getTransaction().begin();
		UserInfo resultUser = em.find(UserInfo.class, user.getId());
		em.remove(resultUser);
		em.getTransaction().commit();
		OrderBasket resultOrder = em.find(OrderBasket.class, orderId);
		
		assertEquals("주문 삭제 테스트", true, resultOrder == null);
	}
	
}
