package co.com.pragma.api;

import co.com.pragma.api.model.ErrorResponse;
import co.com.pragma.model.person.Person;
import co.com.pragma.model.person.exceptions.InvalidPersonException;
import co.com.pragma.usecase.person.PersonUseCase;
import java.time.LocalDateTime;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.server.ServerRequest;
import org.springframework.web.reactive.function.server.ServerResponse;
import reactor.core.publisher.Mono;

@Component
@RequiredArgsConstructor
public class Handler {

  private static final Logger log = LoggerFactory.getLogger(Handler.class);
  private final PersonUseCase personUseCase;

  public Mono<ServerResponse> listenSavePerson(ServerRequest request) {
    log.info("Inicio del proceso de registro de persona");
    return request.bodyToMono(Person.class)
        .doOnNext(person -> log.debug("Datos recibidos: {}", person))
        .flatMap(personUseCase::savePerson)
        .doOnSuccess(person -> log.info("Persona registrada exitosamente: {}", person.getName()))
        .flatMap(person -> ServerResponse.ok()
            .bodyValue(person))
        .doOnError(e -> log.error("Error durante el registro de persona: {}", e.getMessage(), e))
        .onErrorResume(InvalidPersonException.class, this::handleInvalidPersonException)
        .onErrorResume(Exception.class, this::handleGenericException);
  }

  public Mono<ServerResponse> listenGetPersonByIdNumber(ServerRequest serverRequest) {
    log.info("Inicio del proceso de consulta de persona por numero de identificacion");
    return Mono.fromCallable(() -> serverRequest.pathVariable("id"))
        .doOnNext(id -> log.debug("numero de identificacion recibido: {}", id))
        .map(String::trim)
        .filter(item -> !item.isEmpty())
        .flatMap(personUseCase::getPersonById)
        .doOnSuccess(person -> log.info("Persona consultada exitosamente: {}", person.getName()))
        .flatMap(person -> ServerResponse.ok()
            .contentType(MediaType.APPLICATION_JSON)
            .bodyValue(person))
        .doOnError(e -> log.error("Error durante la consulta de persona: {}", e.getMessage(), e))
        .onErrorResume(InvalidPersonException.class, this::handleInvalidPersonException)
        .onErrorResume(Exception.class, this::handleGenericException);
  }

  private Mono<ServerResponse> handleInvalidPersonException(InvalidPersonException ex) {
    return buildErrorResponse(
        ex.getMessage(),
        "Validation Error",
        HttpStatus.BAD_REQUEST
    );
  }

  private Mono<ServerResponse> handleGenericException(Exception ex) {
    return buildErrorResponse(
        "Error interno del servidor",
        ex.getMessage(),
        HttpStatus.INTERNAL_SERVER_ERROR
    );
  }

  private Mono<ServerResponse> buildErrorResponse(String message, String error, HttpStatus status) {
    return ServerResponse
        .status(status)
        .contentType(MediaType.APPLICATION_JSON)
        .bodyValue(ErrorResponse.builder()
            .message(message)
            .error(error)
            .status(status.value())
            .path("/api/person/v1")
            .timestamp(LocalDateTime.now())
            .build()
        );
  }
}
