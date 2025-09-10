package co.com.pragma.r2dbc;

import co.com.pragma.model.person.Person;
import co.com.pragma.model.person.exceptions.InvalidPersonException;
import co.com.pragma.model.person.gateways.PersonRepository;
import co.com.pragma.r2dbc.entity.PersonEntity;
import co.com.pragma.r2dbc.helper.ReactiveAdapterOperations;
import org.reactivecommons.utils.ObjectMapper;
import org.springframework.stereotype.Repository;
import reactor.core.publisher.Mono;

@Repository
public class PersonR2DBCRepositoryAdapter extends ReactiveAdapterOperations<
    Person,
    PersonEntity,
    Long,
    PersonR2DBCRepository
    > implements PersonRepository {

  public PersonR2DBCRepositoryAdapter(PersonR2DBCRepository repository, ObjectMapper mapper) {
    super(repository, mapper, d -> mapper.map(d, Person.class/* change for domain model */));
  }

  @Override
  public Mono<Person> savePerson(Person person) {
    return repository.existsByIdNumber(person.getIdNumber())
        .flatMap(exists -> exists
            ? Mono.error(
            new InvalidPersonException("El numero de identificacion ya se encuentra registrado"))
            : super.save(person));
  }

  @Override
  public Mono<Person> getPersonById(String idNumber) {
    return repository.findByIdNumber(idNumber)
        .switchIfEmpty(
            Mono.error(new InvalidPersonException("La persona con el numero de identificacion "
                + idNumber + " no se encuentra registrada")))
        .flatMap(personEntity -> Mono.just(mapper.map(personEntity, Person.class)));
  }
}
