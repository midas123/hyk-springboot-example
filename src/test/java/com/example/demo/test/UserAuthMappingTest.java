package com.example.demo.test;

import static org.junit.Assert.assertEquals;

import java.util.HashSet;
import java.util.Set;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;

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

import com.example.demo.entity.AuthorityName;
import com.example.demo.entity.User;
import com.example.demo.entity.UserAuth;
import com.example.demo.entity.UserAuthMap;
import com.example.demo.entity.UserInfo;
import com.example.demo.service.UserService;

@RunWith(SpringRunner.class) 
@SpringBootTest
@TestMethodOrder(OrderAnnotation.class)
public class UserAuthMappingTest {
	@Autowired
	UserService userService; 
	
	@Autowired
	private EntityManagerFactory emf;
	
	@Test
	@Transactional
	@Rollback(false)
	@Order(0)
	public void userAndAuthEntityTest() {
		EntityManager em = emf.createEntityManager();

		User user = User.builder()
		.userName("test")
		.password("1234")
		.build();

		UserAuth auth = new UserAuth(AuthorityName.ROLE_USER);
		UserAuth auth2 = new UserAuth(AuthorityName.ROLE_ADMIN);
		
		Set<UserAuthMap> list = new HashSet<>();
		list.add(new UserAuthMap(user, auth, false));
		list.add(new UserAuthMap(user, auth2, false));
		
		user.setUserAuthMaps(list);
		
		em.getTransaction().begin();
		em.persist(auth);
		em.persist(auth2);
		em.persist(user);
		for(UserAuthMap a : list) {
			em.persist(a);
		}
		
		long userId = user.getId();
		em.getTransaction().commit();
		
		User resultUser = em.find(User.class, userId);
		resultUser.getUserAuthMaps().forEach(x-> {
			UserAuthMap am = x;
			System.out.println("사용자 권한 맵핑: " +am.toString());
			System.out.println("사용자 권한: " +am.getAuth());
			});
		assertEquals("사용자 권한 맵핑 테스트", true, user.getUserAuthMaps().size()>0);
	}
		
}
