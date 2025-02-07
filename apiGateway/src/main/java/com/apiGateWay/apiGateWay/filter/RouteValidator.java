package com.apiGateWay.apiGateWay.filter;

import org.springframework.http.server.reactive.ServerHttpRequest;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.function.Predicate;

@Component
public class RouteValidator {
    public static final List<String> openApiEndPoints=List.of(
            "/api/v1/auth/register",
            "/api/v1/auth/token",
            "/api/v1/email/simple",
            "/api/v1/auth/member/new-password",
            "api/v1/auth/member/reset-password",
            "api/v1/auth/member/forgot-password",
            "api/v1/members/{firstName}/email",
            "/eureka"


    );

    public Predicate<ServerHttpRequest> isSecured= request->
            openApiEndPoints
                    .stream()
                    .noneMatch(uri->request.getURI().getPath().contains(uri));
}
