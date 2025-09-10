package co.com.pragma.model.person;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

import co.com.pragma.model.person.exceptions.InvalidPersonException;
import org.junit.jupiter.api.Test;

class PersonValidatorTest {

  @Test
  void validate_validPerson_noException() {
    Person person = new Person();
    person.setIdNumber("123");
    person.setName("Juan");
    person.setEmail("juan@mail.com");
    assertDoesNotThrow(() -> PersonValidator.validate(person));
  }

  @Test
  void validate_nullPerson_throwsException() {
    InvalidPersonException ex = assertThrows(
        InvalidPersonException.class,
        () -> PersonValidator.validate(null)
    );
    assertEquals("Persona no puede ser nulo", ex.getMessage());
  }

  @Test
  void validate_missingIdNumber_throwsException() {
    Person person = new Person();
    person.setName("Juan");
    person.setEmail("juan@mail.com");
    InvalidPersonException ex = assertThrows(
        InvalidPersonException.class,
        () -> PersonValidator.validate(person)
    );
    assertEquals("El IdNumber es requerido", ex.getMessage());
  }

  @Test
  void validate_blankIdNumber_throwsException() {
    Person person = new Person();
    person.setIdNumber("   ");
    person.setName("Juan");
    person.setEmail("juan@mail.com");
    InvalidPersonException ex = assertThrows(
        InvalidPersonException.class,
        () -> PersonValidator.validate(person)
    );
    assertEquals("El IdNumber es requerido", ex.getMessage());
  }

  @Test
  void validate_missingName_throwsException() {
    Person person = new Person();
    person.setIdNumber("123");
    person.setEmail("juan@mail.com");
    InvalidPersonException ex = assertThrows(
        InvalidPersonException.class,
        () -> PersonValidator.validate(person)
    );
    assertEquals("El nombre es requerido", ex.getMessage());
  }

  @Test
  void validate_blankName_throwsException() {
    Person person = new Person();
    person.setIdNumber("123");
    person.setName("   ");
    person.setEmail("juan@mail.com");
    InvalidPersonException ex = assertThrows(
        InvalidPersonException.class,
        () -> PersonValidator.validate(person)
    );
    assertEquals("El nombre es requerido", ex.getMessage());
  }

  @Test
  void validate_missingEmail_throwsException() {
    Person person = new Person();
    person.setIdNumber("123");
    person.setName("Juan");
    InvalidPersonException ex = assertThrows(
        InvalidPersonException.class,
        () -> PersonValidator.validate(person)
    );
    assertEquals("El email es requerido", ex.getMessage());
  }

  @Test
  void validate_blankEmail_throwsException() {
    Person person = new Person();
    person.setIdNumber("123");
    person.setName("Juan");
    person.setEmail("   ");
    InvalidPersonException ex = assertThrows(
        InvalidPersonException.class,
        () -> PersonValidator.validate(person)
    );
    assertEquals("El email es requerido", ex.getMessage());
  }

  @Test
  void validate_invalidEmail_throwsException() {
    Person person = new Person();
    person.setIdNumber("123");
    person.setName("Juan");
    person.setEmail("juanmail.com");
    InvalidPersonException ex = assertThrows(
        InvalidPersonException.class,
        () -> PersonValidator.validate(person)
    );
    assertEquals("El formato del email no es válido", ex.getMessage());
  }
}
