/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package pl.sodexo.it.gryf.web.security;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;
import pl.sodexo.it.gryf.root.service.ApplicationParametersService;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.sql.DataSource;
import java.sql.Connection;
import java.sql.DriverManager;
import java.util.ArrayList;
import java.util.List;

//TODO: przenieść do gryf-security.xml
@Configuration
@EnableWebSecurity
public class SecurityConfig extends WebSecurityConfigurerAdapter {

    @PersistenceContext
    EntityManager em;

    @Autowired
    DataSource dataSource;
    
    @Autowired
    ApplicationParametersService aps;

    @Override
    @Bean
    protected AuthenticationManager authenticationManager() throws Exception {
        return new AuthenticationManager() {
 
            @Override
            public Authentication authenticate(Authentication a) throws AuthenticationException {
                
                UsernamePasswordAuthenticationToken upat = (UsernamePasswordAuthenticationToken) a;
                String login = (String)upat.getPrincipal().toString().toUpperCase();
                String password = (String)upat.getCredentials();
                try (Connection conn = DriverManager.getConnection(dataSource.getConnection().getMetaData().getURL(), login, password)){
                    List<String> augids = em.createNativeQuery("select aug_id from EAGLE.ADM_USER_IN_ROLES natural left join EAGLE.ADM_GROUPS_IN_ROLES where upper(aur_id) = upper(?) and aug_id is not null")
                            .setParameter(1, login) 
                           .getResultList();
                    
                    List<GrantedAuthority> authorities = new ArrayList<>();
                    for (String augid : augids) {
                        authorities.add(new SimpleGrantedAuthority(augid));
                    }
                    return new UsernamePasswordAuthenticationToken(login, upat.getCredentials(), authorities);
                    
                } catch (Exception ex) {
                    throw new BadCredentialsException("wrong credentials or a db problem (" + ex.getMessage() + ")");
                }
            }
        };
    }

    @Override
    protected void configure(HttpSecurity http) throws Exception {
        http/*.csrf().disable()*/.authorizeRequests()
                .antMatchers(aps.getResourcesUrl() + "**").permitAll()  // "/resources/**"
                .anyRequest().authenticated()
                .and()
                .formLogin().loginPage("/login").permitAll()
                .and()
                .logout().logoutRequestMatcher(new AntPathRequestMatcher("/logout")).permitAll()
                .and()
                .httpBasic();
    }

}
