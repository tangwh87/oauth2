package com.alibaba.cloud.examples;

import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
@Service("userDetailService")
public class UMUserDetailsService implements UserDetailsService {

	@Override
	public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
		
	       
	    
	        boolean credentialsNonExpired = true;
	        boolean accountNonExpired = true;
	        OpenUserDetails userDetails = new OpenUserDetails();
	        userDetails.setUsername("user1");
		userDetails.setPassword( new BCryptPasswordEncoder().encode("123456")/* "{noop}123456" */);
	      
	        userDetails.setAccountNonLocked(true);
	        userDetails.setAccountNonExpired(accountNonExpired);
	        userDetails.setCredentialsNonExpired(credentialsNonExpired);
	        userDetails.setEnabled(true);
	        userDetails.setClientId("gateway-client");
	        return userDetails;
	    
	}

}
