package co.com.pragma.config;

import static org.junit.jupiter.api.Assertions.assertNotNull;

import co.com.pragma.model.person.gateways.PersonRepository;
import co.com.pragma.usecase.person.PersonUseCase;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;

public class UseCasesConfigTest {

  @Mock
  private PersonRepository personRepository;

  @Test
  void savePersonUseCaseBeanIsCreatedTest() {

    UseCasesConfig config = new UseCasesConfig();
    PersonUseCase useCase = config.savePerson(personRepository);
    assertNotNull(useCase);
  }
}