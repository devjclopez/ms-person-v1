package co.com.pragma.api;

import static org.springframework.web.reactive.function.server.RequestPredicates.GET;
import static org.springframework.web.reactive.function.server.RequestPredicates.POST;
import static org.springframework.web.reactive.function.server.RouterFunctions.route;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.reactive.function.server.RouterFunction;
import org.springframework.web.reactive.function.server.ServerResponse;

@Configuration
public class RouterRest {

  @Bean
  public RouterFunction<ServerResponse> routerFunction(Handler handler) {
    return route(GET("/api/person/v1/{id}"), handler::listenGetPersonByIdNumber)
        .andRoute(POST("/api/person/v1"), handler::listenSavePerson);
  }
}
