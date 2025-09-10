package co.com.pragma.config;

import co.com.pragma.model.person.gateways.PersonRepository;
import co.com.pragma.usecase.person.PersonUseCase;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.FilterType;
import org.springframework.context.annotation.Primary;

@Configuration
@ComponentScan(basePackages = "co.com.pragma.usecase",
    includeFilters = {
        @ComponentScan.Filter(type = FilterType.REGEX, pattern = "^.+UseCase$")
    },
    useDefaultFilters = false)
public class UseCasesConfig {

  @Bean
  @Primary
  public PersonUseCase savePerson(PersonRepository personRepository) {
    return new PersonUseCase(personRepository);
  }
}
