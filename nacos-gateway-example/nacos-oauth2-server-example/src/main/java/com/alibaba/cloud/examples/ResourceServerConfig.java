package com.alibaba.cloud.examples;

import org.springframework.boot.actuate.autoconfigure.security.servlet.EndpointRequest;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.oauth2.config.annotation.web.configuration.EnableResourceServer;
import org.springframework.security.oauth2.config.annotation.web.configuration.ResourceServerConfigurerAdapter;

/**
 * Created by wangyunfei on 2017/6/12.
 */
@Configuration
@EnableResourceServer
public class ResourceServerConfig extends ResourceServerConfigurerAdapter{

	@Override
	public void configure(HttpSecurity http) throws Exception {
		http.sessionManagement().sessionCreationPolicy(SessionCreationPolicy.IF_REQUIRED)
				.and()/* .requestMatchers().mvcMatchers("/.well-known/jwks.json").and() */
        .authorizeRequests()
        .antMatchers("/login/**","/oauth/**","/").permitAll()
        .requestMatchers(EndpointRequest.toAnyEndpoint()).permitAll()
        // 监控端点内部放行
        .anyRequest().authenticated()
        .and()
				.formLogin()/* .loginPage("/login") */.permitAll()
		.and().cors().and().httpBasic().disable().csrf().disable();
	}
	
}
