package co.com.pragma.model.person.gateways;

import co.com.pragma.model.person.Person;
import reactor.core.publisher.Mono;

public interface PersonRepository {

  Mono<Person> savePerson(Person person);

  Mono<Person> getPersonById(String idNumber);
}
