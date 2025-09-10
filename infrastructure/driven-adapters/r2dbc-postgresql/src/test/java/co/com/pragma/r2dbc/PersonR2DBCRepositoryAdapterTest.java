package co.com.pragma.r2dbc;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import co.com.pragma.model.person.Person;
import co.com.pragma.model.person.exceptions.InvalidPersonException;
import co.com.pragma.r2dbc.entity.PersonEntity;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.reactivecommons.utils.ObjectMapper;
import reactor.core.publisher.Mono;
import reactor.test.StepVerifier;

class PersonR2DBCRepositoryAdapterTest {

  @Mock
  PersonR2DBCRepository repository;

  @Mock
  ObjectMapper mapper;

  PersonR2DBCRepositoryAdapter adapter;

  @BeforeEach
  void setUp() {
    MockitoAnnotations.openMocks(this);
    adapter = new PersonR2DBCRepositoryAdapter(repository, mapper);
  }

  @Test
  void savePerson_whenNotExists_shouldSave() {
    Person person = new Person();
    person.setIdNumber("123");
    PersonEntity entity = new PersonEntity();
    when(repository.existsByIdNumber("123")).thenReturn(Mono.just(false));
    when(repository.save(any(PersonEntity.class))).thenReturn(Mono.just(entity));
    when(mapper.map(person, PersonEntity.class)).thenReturn(entity);
    when(mapper.map(entity, Person.class)).thenReturn(person);

    StepVerifier.create(adapter.savePerson(person))
        .expectNext(person)
        .verifyComplete();

    verify(repository).save(any(PersonEntity.class));
  }

  @Test
  void savePerson_whenExists_shouldThrow() {
    Person person = new Person();
    person.setIdNumber("123");
    when(repository.existsByIdNumber("123")).thenReturn(Mono.just(true));

    StepVerifier.create(adapter.savePerson(person))
        .expectErrorSatisfies(e -> {
          assert e instanceof InvalidPersonException;
          assert e.getMessage().contains("ya se encuentra registrado");
        })
        .verify();

    verify(repository, never()).save(any());
  }

  @Test
  void getPersonById_whenExists_shouldReturnPerson() {
    String idNumber = "123";
    PersonEntity entity = new PersonEntity();
    Person person = new Person();
    when(repository.findByIdNumber(idNumber)).thenReturn(Mono.just(entity));
    when(mapper.map(entity, Person.class)).thenReturn(person);

    StepVerifier.create(adapter.getPersonById(idNumber))
        .expectNext(person)
        .verifyComplete();
  }

  @Test
  void getPersonById_whenNotExists_shouldThrow() {
    String idNumber = "999";
    when(repository.findByIdNumber(idNumber)).thenReturn(Mono.empty());

    StepVerifier.create(adapter.getPersonById(idNumber))
        .expectErrorSatisfies(e -> {
          assert e instanceof InvalidPersonException;
          assert e.getMessage().contains("no se encuentra registrada");
        })
        .verify();
  }
}