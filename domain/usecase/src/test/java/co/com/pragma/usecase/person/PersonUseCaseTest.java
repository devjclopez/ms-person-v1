package co.com.pragma.usecase.person;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import co.com.pragma.model.person.Person;
import co.com.pragma.model.person.exceptions.InvalidPersonException;
import co.com.pragma.model.person.gateways.PersonRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import reactor.core.publisher.Mono;
import reactor.test.StepVerifier;

class PersonUseCaseTest {

  private PersonRepository repository;
  private PersonUseCase useCase;

  @BeforeEach
  void setUp() {
    repository = mock(PersonRepository.class);
    useCase = new PersonUseCase(repository);
  }

  @Test
  void savePerson_validPerson_success() {
    Person person = new Person();
    person.setIdNumber("123");
    person.setName("Juan");
    person.setEmail("juan@mail.com");

    when(repository.savePerson(any(Person.class))).thenReturn(Mono.just(person));

    StepVerifier.create(useCase.savePerson(person))
        .expectNext(person)
        .verifyComplete();

    verify(repository, times(1)).savePerson(person);
  }

  @Test
  void savePerson_nullPerson_throwsException() {
    StepVerifier.create(useCase.savePerson(null))
        .expectErrorSatisfies(e -> {
          assert e instanceof InvalidPersonException;
          assert e.getMessage().equals("El usuario no puede ser nulo");
        })
        .verify();

    verify(repository, never()).savePerson(any());
  }

  @Test
  void savePerson_invalidPerson_throwsException() {
    Person person = new Person(); // Sin datos requeridos

    StepVerifier.create(useCase.savePerson(person))
        .expectErrorSatisfies(e -> {
          assert e instanceof InvalidPersonException;
        })
        .verify();

    verify(repository, never()).savePerson(any());
  }

  @Test
  void getPersonById_success() {
    Person person = new Person();
    person.setIdNumber("123");
    when(repository.getPersonById("123")).thenReturn(Mono.just(person));

    StepVerifier.create(useCase.getPersonById("123"))
        .expectNext(person)
        .verifyComplete();

    verify(repository, times(1)).getPersonById("123");
  }

  @Test
  void getPersonById_notFound() {
    when(repository.getPersonById("999")).thenReturn(Mono.empty());

    StepVerifier.create(useCase.getPersonById("999"))
        .verifyComplete();

    verify(repository, times(1)).getPersonById("999");
  }
}
