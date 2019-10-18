package com.alibaba.cloud.examples;


import java.security.KeyPair;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.oauth2.config.annotation.configurers.ClientDetailsServiceConfigurer;
import org.springframework.security.oauth2.config.annotation.web.configuration.AuthorizationServerConfigurerAdapter;
import org.springframework.security.oauth2.config.annotation.web.configuration.AuthorizationServerEndpointsConfiguration;
import org.springframework.security.oauth2.config.annotation.web.configurers.AuthorizationServerEndpointsConfigurer;
import org.springframework.security.oauth2.config.annotation.web.configurers.AuthorizationServerSecurityConfigurer;
import org.springframework.security.oauth2.provider.token.TokenStore;
import org.springframework.security.oauth2.provider.token.store.JwtAccessTokenConverter;
import org.springframework.security.oauth2.provider.token.store.JwtTokenStore;

@Configuration
@Import(AuthorizationServerEndpointsConfiguration.class)
public class CustomAuthorizationServerConfigurer extends AuthorizationServerConfigurerAdapter {

	@Autowired
	private UserDetailsService userDetailsService;
	AuthenticationManager authenticationManager;
	KeyPair keyPair;

	public CustomAuthorizationServerConfigurer(AuthenticationConfiguration authenticationConfiguration,
			KeyPair keyPair) throws Exception {

		this.authenticationManager = authenticationConfiguration.getAuthenticationManager();
		this.keyPair = keyPair;
	}

	@Override
	public void configure(AuthorizationServerSecurityConfigurer security) throws Exception {
		security.tokenKeyAccess("permitAll()")
        // 开启/oauth/check_token验证端口认证权限访问
        .checkTokenAccess("isAuthenticated()")
        // 开启表单认证
        .allowFormAuthenticationForClients();
		
	}

	@Override
	public void configure(ClientDetailsServiceConfigurer clients) throws Exception {
		clients.inMemory().withClient("gateway-client").authorizedGrantTypes(/* "password", */"authorization_code")
				.secret(new BCryptPasswordEncoder().encode("secret")/*"{noop}secret"  "secret" */).scopes("all").redirectUris("http://192.168.43.94:18085/login/oauth2/code/gateway-client","http://localhost:18085/login/oauth2/code/gateway-client","http://localhost:18085/login/oauth2/code/login-client").autoApprove(true);
	}
	
	@Override
	public void configure(AuthorizationServerEndpointsConfigurer endpoints) throws Exception {
		endpoints.userDetailsService(userDetailsService).authenticationManager(this.authenticationManager)
				.accessTokenConverter(accessTokenConverter()).tokenStore(tokenStore());
	}

	
	
	@Bean
	public TokenStore tokenStore() {
		return new JwtTokenStore(accessTokenConverter());
	}

	@Bean
	public JwtAccessTokenConverter accessTokenConverter() {
		JwtAccessTokenConverter converter = new JwtAccessTokenConverter();
		converter.setKeyPair(this.keyPair);
		return converter;
	}
	
	
}
