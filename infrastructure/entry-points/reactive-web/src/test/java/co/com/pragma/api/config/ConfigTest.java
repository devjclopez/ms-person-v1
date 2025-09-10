package co.com.pragma.api.config;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

import co.com.pragma.api.Handler;
import co.com.pragma.api.RouterRest;
import co.com.pragma.model.person.Person;
import co.com.pragma.usecase.person.PersonUseCase;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.reactive.WebFluxTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Import;
import org.springframework.http.MediaType;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.web.reactive.server.WebTestClient;
import reactor.core.publisher.Mono;

@ContextConfiguration(classes = {RouterRest.class, Handler.class})
@WebFluxTest
@Import({CorsConfig.class, SecurityHeadersConfig.class})
class ConfigTest {

  @Autowired
  private WebTestClient webTestClient;

  @MockBean
  private PersonUseCase personUseCase;

  private Person validPerson;

  @BeforeEach
  void setUp() {
    validPerson = Person.builder()
        .name("Juan")
        .idNumber("12345678")
        .email("juan@mail.com")
        .build();

    when(personUseCase.savePerson(any(Person.class))).thenReturn(Mono.just(validPerson));
    when(personUseCase.getPersonById(any(String.class))).thenReturn(Mono.just(validPerson));
  }

  @Test
  void corsConfigurationShouldAllowOrigins() {
    webTestClient.get()
        .uri("/api/person/v1/12345678")
        .exchange()
        .expectStatus().isOk()
        .expectHeader().valueEquals("Content-Security-Policy",
            "default-src 'self'; frame-ancestors 'self'; form-action 'self'")
        .expectHeader().valueEquals("Strict-Transport-Security", "max-age=31536000;")
        .expectHeader().valueEquals("X-Content-Type-Options", "nosniff")
        .expectHeader().valueEquals("Server", "")
        .expectHeader().valueEquals("Cache-Control", "no-store")
        .expectHeader().valueEquals("Pragma", "no-cache")
        .expectHeader().valueEquals("Referrer-Policy", "strict-origin-when-cross-origin");

    webTestClient.post()
        .uri("/api/person/v1")
        .contentType(MediaType.APPLICATION_JSON)
        .bodyValue("{}")
        .exchange()
        .expectStatus().isOk()
        .expectHeader().valueEquals("Content-Security-Policy",
            "default-src 'self'; frame-ancestors 'self'; form-action 'self'")
        .expectHeader().valueEquals("Strict-Transport-Security", "max-age=31536000;")
        .expectHeader().valueEquals("X-Content-Type-Options", "nosniff")
        .expectHeader().valueEquals("Server", "")
        .expectHeader().valueEquals("Cache-Control", "no-store")
        .expectHeader().valueEquals("Pragma", "no-cache")
        .expectHeader().valueEquals("Referrer-Policy", "strict-origin-when-cross-origin");
  }

  @Test
  void corsShouldRejectNotAllowedOrigin() {
    webTestClient.post()
        .uri("/api/person/v1")
        .contentType(MediaType.APPLICATION_JSON)
        .bodyValue("{}")
        .header("Origin", "http://not-allowed.com")
        .exchange()
        .expectStatus().isForbidden();
  }

}