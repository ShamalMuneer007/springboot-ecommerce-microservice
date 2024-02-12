package org.shamal.apigateway.config;

import org.shamal.apigateway.filters.AuthorizationFilter;
import org.shamal.apigateway.filters.LoggingFilter;
import org.springframework.cloud.gateway.filter.GlobalFilter;
import org.springframework.cloud.gateway.route.RouteLocator;
import org.springframework.cloud.gateway.route.builder.RouteLocatorBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class ApiGatewayConfiguration {
    @Bean
    public RouteLocator myRoutes(RouteLocatorBuilder builder,AuthorizationFilter authorizationFilter) {
        return builder.routes()
                .route("user-identity-route",p ->
                        p.path("/user-identity/**")
                                .uri("lb://user-identity"))
                .route("product-service-route",p -> p
                        .path("/inventory/**")
                        .filters(gatewayFilterSpec -> gatewayFilterSpec
                                .filter(authorizationFilter.apply(new AuthorizationFilter.Config())))
                        .uri("lb://inventory"))
                .route("cart-service-route", p -> p
                        .path("/cart/**")
                        .filters(gatewayFilterSpec -> gatewayFilterSpec
                                .filter(authorizationFilter.apply(new AuthorizationFilter.Config())))
                        .uri("lb://cart"))
                .build();
    }
    @Bean
    public GlobalFilter globalFilter(){
        return new LoggingFilter();
    }
}
