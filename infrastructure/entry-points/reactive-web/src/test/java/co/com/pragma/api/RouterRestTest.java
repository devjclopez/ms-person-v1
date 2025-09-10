package co.com.pragma.api;

import co.com.pragma.model.person.Person;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.MediaType;
import org.springframework.test.web.reactive.server.WebTestClient;
import org.springframework.web.reactive.function.server.RouterFunction;
import org.springframework.web.reactive.function.server.ServerResponse;

@ExtendWith(MockitoExtension.class)
class RouterRestTest {

  @Mock
  private Handler handler;

  private Person person;

  private WebTestClient testClient;

  @BeforeEach
  void setUp() {
    person = new Person();
    person.setIdNumber("123");
    person.setName("Juan");
    person.setEmail("juan@mail.com");

    RouterFunction<ServerResponse> routerFunction = new RouterRest().routerFunction(handler);

    testClient = WebTestClient
        .bindToRouterFunction(routerFunction)
        .build();
  }

  @Test
  void route_postPerson_callsHandler() {
    Mockito.when(handler.listenSavePerson(Mockito.any()))
        .thenReturn(ServerResponse.ok().bodyValue(person));

    testClient.post()
        .uri("/api/person/v1")
        .contentType(MediaType.APPLICATION_JSON)
        .bodyValue(person)
        .exchange()
        .expectStatus().isOk();
    Mockito.verify(handler).listenSavePerson(Mockito.any());
  }

  @Test
  void route_getPersonById_callsHandler() {
    Mockito.when(handler.listenGetPersonByIdNumber(Mockito.any()))
        .thenReturn(ServerResponse.ok().bodyValue(person));

    testClient.get()
        .uri("/api/person/v1/123")
        .accept(MediaType.APPLICATION_JSON)
        .exchange()
        .expectStatus().isOk();
    Mockito.verify(handler).listenGetPersonByIdNumber(Mockito.any());
  }
}