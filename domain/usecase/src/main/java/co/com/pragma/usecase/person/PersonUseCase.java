package co.com.pragma.usecase.person;

import co.com.pragma.model.person.Person;
import co.com.pragma.model.person.PersonValidator;
import co.com.pragma.model.person.exceptions.InvalidPersonException;
import co.com.pragma.model.person.gateways.PersonRepository;
import lombok.RequiredArgsConstructor;
import reactor.core.publisher.Mono;

@RequiredArgsConstructor
public class PersonUseCase {

  private final PersonRepository repository;

  public Mono<Person> savePerson(Person person) {
    return Mono.justOrEmpty(person)
        .switchIfEmpty(Mono.error(new InvalidPersonException("El usuario no puede ser nulo")))
        .doOnNext(PersonValidator::validate)
        .flatMap(repository::savePerson);
  }

  public Mono<Person> getPersonById(String idNumber) {
    return repository.getPersonById(idNumber);
  }
}
