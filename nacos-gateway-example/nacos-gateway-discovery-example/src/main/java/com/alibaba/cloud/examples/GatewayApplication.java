package com.alibaba.cloud.examples;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.cloud.gateway.route.RouteLocator;
import org.springframework.cloud.gateway.route.builder.RouteLocatorBuilder;
import org.springframework.cloud.security.oauth2.gateway.TokenRelayGatewayFilterFactory;
import org.springframework.context.annotation.Bean;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.server.reactive.ServerHttpRequest;
import org.springframework.http.server.reactive.ServerHttpResponse;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.oauth2.client.OAuth2AuthorizedClient;
import org.springframework.security.oauth2.client.annotation.RegisteredOAuth2AuthorizedClient;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.cors.reactive.CorsUtils;
import org.springframework.web.filter.reactive.HiddenHttpMethodFilter;
import org.springframework.web.server.ServerWebExchange;
import org.springframework.web.server.WebFilter;
import org.springframework.web.server.WebFilterChain;

import reactor.core.publisher.Mono;

/**
 * @author lengleng
 */
@SpringBootApplication
@EnableDiscoveryClient
@Controller
public class GatewayApplication {
 
	private Log log =LogFactory.getLog(GatewayApplication.class);
	public static void main(String[] args) {
		SpringApplication.run(GatewayApplication.class, args);
	}

	
	
	
	@Bean
    public HiddenHttpMethodFilter hiddenHttpMethodFilter() {
        return new HiddenHttpMethodFilter() {
            @Override
            public Mono<Void> filter(ServerWebExchange exchange, WebFilterChain chain) {
                return chain.filter(exchange);
            }
        };
    }

	   @Bean
	    public WebFilter corsFilter() {
	        return (ServerWebExchange ctx, WebFilterChain chain) -> {
	           ServerHttpRequest request = ctx.getRequest();
	            if (!CorsUtils.isCorsRequest(request)) {
	                return chain.filter(ctx);
	            }
	 
	            HttpHeaders requestHeaders = request.getHeaders();
	            ServerHttpResponse response = ctx.getResponse();
	            HttpMethod requestMethod = requestHeaders.getAccessControlRequestMethod();
	            HttpHeaders headers = response.getHeaders();
	            headers.add(HttpHeaders.ACCESS_CONTROL_ALLOW_ORIGIN, requestHeaders.getOrigin());
	            headers.addAll(HttpHeaders.ACCESS_CONTROL_ALLOW_HEADERS, requestHeaders.getAccessControlRequestHeaders());
	            if (requestMethod != null) {
	                headers.add(HttpHeaders.ACCESS_CONTROL_ALLOW_METHODS, requestMethod.name());
	            }
	            headers.add(HttpHeaders.ACCESS_CONTROL_ALLOW_CREDENTIALS, "true");
	            headers.add(HttpHeaders.ACCESS_CONTROL_EXPOSE_HEADERS, "all");
	            headers.add(HttpHeaders.ACCESS_CONTROL_MAX_AGE, "1000000");
	            if (request.getMethod() == HttpMethod.OPTIONS) {
	                response.setStatusCode(HttpStatus.OK);
	                return Mono.empty();
	            }
	            return chain.filter(ctx);
	        };
	    }
	
	
	/*
	 * @Bean
	 * 
	 * @Order(-1) public GlobalFilter a() { return (exchange, chain) -> {
	 * log.info("first pre filter"); return
	 * chain.filter(exchange).then(Mono.fromRunnable(() -> {
	 * log.info("third post filter"); })); }; }
	 * 
	 * @Bean
	 * 
	 * @Order(0) public GlobalFilter b() { return (exchange, chain) -> {
	 * log.info("second pre filter"); return
	 * chain.filter(exchange).then(Mono.fromRunnable(() -> {
	 * log.info("second post filter"); })); }; }
	 * 
	 * @Bean
	 * 
	 * @Order(1) public GlobalFilter c() { return (exchange, chain) -> {
	 * log.info("third pre filter"); return
	 * chain.filter(exchange).then(Mono.fromRunnable(() -> {
	 * log.info("first post filter"); })); }; }
	 */
	@Autowired
	private TokenRelayGatewayFilterFactory filterFactory;

	//@Bean
	public RouteLocator customRouteLocator(RouteLocatorBuilder builder) {
		//@formatter:off
		return builder.routes()
				.route("resource", r -> r.path("/resource")
						.filters(f -> f.filter(filterFactory.apply()))
						.uri("http://localhost:9000"))
				.build();
		//@formatter:on
	}

	@GetMapping("/")
	public String index(Model model,
						@RegisteredOAuth2AuthorizedClient OAuth2AuthorizedClient authorizedClient,
						@AuthenticationPrincipal OAuth2User oauth2User) {
		model.addAttribute("userName", oauth2User.getName());
		model.addAttribute("clientName", authorizedClient.getClientRegistration().getClientName());
		model.addAttribute("userAttributes", oauth2User.getAttributes());
		return "index";
	}
	
	
}
