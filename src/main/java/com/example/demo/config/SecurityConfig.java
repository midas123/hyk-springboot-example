package com.example.demo.config;


import javax.sql.DataSource;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.web.servlet.ServletListenerRegistrationBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.authentication.rememberme.JdbcTokenRepositoryImpl;
import org.springframework.security.web.authentication.rememberme.PersistentTokenBasedRememberMeServices;
import org.springframework.security.web.authentication.rememberme.PersistentTokenRepository;
import org.springframework.security.web.authentication.rememberme.RememberMeAuthenticationFilter;

import com.example.demo.filter.CustomFilter;
import com.example.demo.security.CustomDaoAuthenticationProvider;
import com.example.demo.security.SessionListener;
import com.example.demo.security.UserSecurityService;

@Configuration
//@EnableWebSecurity
@EnableWebSecurity(debug = true)
@EnableGlobalMethodSecurity(prePostEnabled=true)
public class SecurityConfig extends WebSecurityConfigurerAdapter {


	@Autowired
	@Qualifier("userSecurityService")
	private UserSecurityService userDetailsService;
	
	@Autowired
	private DataSource reMemberMeDataSource;
	
	@Bean
	public ServletListenerRegistrationBean<SessionListener> sessionListenerWithMetrics() {
	   ServletListenerRegistrationBean<SessionListener> listenerRegBean =
	     new ServletListenerRegistrationBean<>();
	   listenerRegBean.setListener(new SessionListener());
	   return listenerRegBean;
	}
	
	@Bean
	public PasswordEncoder encoder() {
		return new BCryptPasswordEncoder();
	}

//	@Bean
//	public CustomAuthenticationProvider authProvider() {
//		return new CustomAuthenticationProvider();
//	}
//
//	@Autowired
//	protected void configureGlobal(AuthenticationManagerBuilder auth) throws Exception {
//		auth.authenticationProvider(authProvider());
//	}
	
	@Bean
	public DaoAuthenticationProvider customDaoAuthenticationProvider() {
		DaoAuthenticationProvider provider = new CustomDaoAuthenticationProvider();
		provider.setPasswordEncoder(encoder());
		provider.setUserDetailsService(userDetailsService);
		return provider;
	}
	
	@Bean
	public PersistentTokenRepository jdbcTokenRepository() {
		JdbcTokenRepositoryImpl jtri = new JdbcTokenRepositoryImpl();
		jtri.setCreateTableOnStartup(false);
		jtri.setDataSource(reMemberMeDataSource);
		return jtri;
	}
	 
	@Override
	protected void configure(AuthenticationManagerBuilder auth) throws Exception {
		auth.userDetailsService(userDetailsService);
		auth.authenticationProvider(customDaoAuthenticationProvider());
	}
	
	@Override
	protected void configure(final HttpSecurity http) throws Exception {
		http.csrf().disable()
				.authorizeRequests()
				.antMatchers("/admin/**").hasRole("ADMIN")
				.antMatchers("/anonymous*").anonymous().antMatchers("/user/**").permitAll()
				.antMatchers("/login*").permitAll().anyRequest()
				.authenticated().and().formLogin().loginPage("/login").loginProcessingUrl("/loginprocess")
				.defaultSuccessUrl("/main", true)
				.failureUrl("/login?error=true")
				//.and().exceptionHandling().accessDeniedPage("/error/403")
				.and()
			    .rememberMe()
                .key("rem-me-key")
                .rememberMeCookieName("remember-me-cookie")
                .rememberMeParameter("remember-me")  
                .tokenRepository(jdbcTokenRepository())
                .tokenValiditySeconds(1*24*60*60)
				.and()
				//.addFilterBefore(new CustomFilter(), RememberMeAuthenticationFilter.class)
				//.authenticationProvider(customDaoAuthenticationProvider()) //여러 개의 커스텀 authenticationProvider 추가시 
				// .failureHandler(authenticationFailureHandler())
				.logout().logoutUrl("/logout").deleteCookies("JSESSIONID");
		http
		.sessionManagement()
		//.sessionCreationPolicy(SessionCreationPolicy.IF_REQUIRED)
		.sessionFixation().changeSessionId();
	}

}
