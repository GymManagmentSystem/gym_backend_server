package com.apiGateWay.apiGateWay.filter;

import com.apiGateWay.apiGateWay.utill.JwtUtill;
import org.apache.http.HttpHeaders;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.gateway.filter.GatewayFilter;
import org.springframework.cloud.gateway.filter.factory.AbstractGatewayFilterFactory;
import org.springframework.stereotype.Component;


@Component
public class AuthenticationFilter extends AbstractGatewayFilterFactory<AuthenticationFilter.Config> {

    @Autowired
    private RouteValidator routeValidator;

    @Autowired
    private JwtUtill jwtUtill;

    public AuthenticationFilter() {
        super(Config.class);
    }

    @Override
    public GatewayFilter apply(Config config) {
        return ((exchange,chain)->{
            if(routeValidator.isSecured.test(exchange.getRequest())){
                //header contains token or not
                try{
                if(!exchange.getRequest().getHeaders().containsKey(HttpHeaders.AUTHORIZATION)){
                    throw new RuntimeException("Authorization header not present");
                }

                String authHeader=exchange.getRequest().getHeaders().get(HttpHeaders.AUTHORIZATION).get(0);
                if(authHeader!=null && authHeader.startsWith("Bearer ")){
                    authHeader=authHeader.substring(7);
                }
                    String finalToken=authHeader;
                    jwtUtill.vlidateToken(finalToken);
                }catch(Exception e){
                    System.out.println(e.getMessage());
                    throw new RuntimeException("something went wrong");
                }
            }
            return chain.filter(exchange);

        });
    }

    public static class Config {

    }
}
