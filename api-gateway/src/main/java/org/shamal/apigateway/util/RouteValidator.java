package org.shamal.apigateway.util;

import org.springframework.http.server.reactive.ServerHttpRequest;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.function.Predicate;

@Component
public class RouteValidator {
    public static final List<String> openApiEndPoints = List.of(
            "/auth/register",
            "/auth/login",
            "/eureka"
    );
    public Predicate<ServerHttpRequest> isSecured =
            serverHttpRequest -> openApiEndPoints.stream().noneMatch(url ->
                    serverHttpRequest.getURI().getPath().contains(url));

}
