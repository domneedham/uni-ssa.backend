package com.example.ssa.security;

import com.example.ssa.entity.user.UserRole;
import com.example.ssa.filter.CustomAuthenticationFilter;
import com.example.ssa.filter.CustomAuthorisationFilter;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

@Configuration
@EnableWebSecurity
public class SecurityConfig extends WebSecurityConfigurerAdapter {
    private final String staffRole = UserRole.STAFF.name();
    private final String managerRole = UserRole.MANAGER.name();

    private final UserDetailsService userDetailsService;
    private final BCryptPasswordEncoder bCryptPasswordEncoder;

    public SecurityConfig(UserDetailsService userDetailsService, BCryptPasswordEncoder bCryptPasswordEncoder) {
        this.userDetailsService = userDetailsService;
        this.bCryptPasswordEncoder = bCryptPasswordEncoder;
    }

    @Override
    protected void configure(AuthenticationManagerBuilder auth) throws Exception {
        auth.userDetailsService(userDetailsService).passwordEncoder(bCryptPasswordEncoder);
    }

    @Override
    protected void configure(HttpSecurity http) throws Exception {
        CustomAuthenticationFilter customAuthenticationFilter = new CustomAuthenticationFilter(authenticationManagerBean());
        customAuthenticationFilter.setFilterProcessesUrl("/api/auth/login");

        http.csrf().disable();
        http.sessionManagement().sessionCreationPolicy(SessionCreationPolicy.STATELESS);

        // AuthController
        http.authorizeRequests().antMatchers("/api/auth/**").permitAll();

        // AppUserController
        http.authorizeRequests().antMatchers("/api/user/**").hasAnyAuthority(staffRole, managerRole);
        // ManagerController - only allow staff to get specific manager with id
        http.authorizeRequests().antMatchers(HttpMethod.GET, "/api/manager/{id}").hasAnyAuthority(staffRole, managerRole);
        http.authorizeRequests().antMatchers("/api/manager/**").hasAuthority(managerRole);
        // StaffController
        http.authorizeRequests().antMatchers("/api/staff/create").hasAuthority(managerRole);
        http.authorizeRequests().antMatchers("/api/staff/**").hasAnyAuthority(staffRole, managerRole);

        // CategoryController
        http.authorizeRequests().antMatchers(HttpMethod.GET, "/api/category/**").hasAnyAuthority(staffRole, managerRole);
        http.authorizeRequests().antMatchers("/api/category/**").hasAuthority(managerRole);

        // ManagerStaffSkillController
        http.authorizeRequests().antMatchers("/api/skill/manager/**").hasAuthority(managerRole);
        // StaffSkill Controller
        http.authorizeRequests().antMatchers("/api/skill/staff/**").hasAnyAuthority(staffRole, managerRole);
        // SkillController
        http.authorizeRequests().antMatchers(HttpMethod.GET, "/api/skill/**").hasAnyAuthority(staffRole, managerRole);
        http.authorizeRequests().antMatchers("/api/skill/**").hasAnyAuthority(managerRole);

        // deny all other requests as they do not match any route configured
        http.authorizeRequests().anyRequest().denyAll();

        // add the authentication filter to all requests
        http.addFilter(customAuthenticationFilter);
        // add the authorisation filter before all requests
        http.addFilterBefore(new CustomAuthorisationFilter(), UsernamePasswordAuthenticationFilter.class);
    }

    @Bean
    @Override
    public AuthenticationManager authenticationManagerBean() throws Exception {
        return super.authenticationManagerBean();
    }
}
