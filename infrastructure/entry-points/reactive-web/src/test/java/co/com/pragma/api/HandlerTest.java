package co.com.pragma.api;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import co.com.pragma.model.person.Person;
import co.com.pragma.model.person.exceptions.InvalidPersonException;
import co.com.pragma.usecase.person.PersonUseCase;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.http.HttpStatus;
import org.springframework.web.reactive.function.server.ServerRequest;
import org.springframework.web.reactive.function.server.ServerResponse;
import reactor.core.publisher.Mono;
import reactor.test.StepVerifier;

class HandlerTest {

  private PersonUseCase personUseCase;
  private Handler handler;

  @BeforeEach
  void setUp() {
    personUseCase = mock(PersonUseCase.class);
    handler = new Handler(personUseCase);
  }

  @Test
  void listenSavePerson_success() {
    Person person = new Person();
    person.setName("Juan");
    ServerRequest request = mock(ServerRequest.class);
    when(request.bodyToMono(Person.class)).thenReturn(Mono.just(person));
    when(personUseCase.savePerson(person)).thenReturn(Mono.just(person));

    Mono<ServerResponse> response = handler.listenSavePerson(request);

    StepVerifier.create(response)
        .assertNext(serverResponse -> assertEquals(HttpStatus.OK, serverResponse.statusCode()))
        .verifyComplete();
  }

  @Test
  void listenSavePerson_invalidPersonException() {
    Person person = new Person();
    ServerRequest request = mock(ServerRequest.class);
    when(request.bodyToMono(Person.class)).thenReturn(Mono.just(person));
    when(personUseCase.savePerson(person)).thenReturn(
        Mono.error(new InvalidPersonException("Datos inválidos")));

    Mono<ServerResponse> response = handler.listenSavePerson(request);

    StepVerifier.create(response)
        .assertNext(
            serverResponse -> assertEquals(HttpStatus.BAD_REQUEST, serverResponse.statusCode()))
        .verifyComplete();
  }

  @Test
  void listenSavePerson_genericException() {
    Person person = new Person();
    ServerRequest request = mock(ServerRequest.class);
    when(request.bodyToMono(Person.class)).thenReturn(Mono.just(person));
    when(personUseCase.savePerson(person)).thenReturn(
        Mono.error(new RuntimeException("Error inesperado")));

    Mono<ServerResponse> response = handler.listenSavePerson(request);

    StepVerifier.create(response)
        .assertNext(serverResponse -> assertEquals(HttpStatus.INTERNAL_SERVER_ERROR,
            serverResponse.statusCode()))
        .verifyComplete();
  }

  @Test
  void listenGetPersonByIdNumber_success() {
    String id = "123";
    Person person = new Person();
    person.setName("Ana");
    ServerRequest request = mock(ServerRequest.class);
    when(request.pathVariable("id")).thenReturn(id);
    when(personUseCase.getPersonById(id)).thenReturn(Mono.just(person));

    Mono<ServerResponse> response = handler.listenGetPersonByIdNumber(request);

    StepVerifier.create(response)
        .assertNext(serverResponse -> assertEquals(HttpStatus.OK, serverResponse.statusCode()))
        .verifyComplete();
  }

  @Test
  void listenGetPersonByIdNumber_invalidPersonException() {
    String id = "123";
    ServerRequest request = mock(ServerRequest.class);
    when(request.pathVariable("id")).thenReturn(id);
    when(personUseCase.getPersonById(id)).thenReturn(
        Mono.error(new InvalidPersonException("No existe")));

    Mono<ServerResponse> response = handler.listenGetPersonByIdNumber(request);

    StepVerifier.create(response)
        .assertNext(
            serverResponse -> assertEquals(HttpStatus.BAD_REQUEST, serverResponse.statusCode()))
        .verifyComplete();
  }

  @Test
  void listenGetPersonByIdNumber_genericException() {
    String id = "123";
    ServerRequest request = mock(ServerRequest.class);
    when(request.pathVariable("id")).thenReturn(id);
    when(personUseCase.getPersonById(id)).thenReturn(Mono.error(new RuntimeException("Error")));

    Mono<ServerResponse> response = handler.listenGetPersonByIdNumber(request);

    StepVerifier.create(response)
        .assertNext(serverResponse -> assertEquals(HttpStatus.INTERNAL_SERVER_ERROR,
            serverResponse.statusCode()))
        .verifyComplete();
  }
}
