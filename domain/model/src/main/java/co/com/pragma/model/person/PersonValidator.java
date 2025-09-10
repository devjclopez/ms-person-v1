package co.com.pragma.model.person;

import co.com.pragma.model.person.exceptions.InvalidPersonException;
import java.util.regex.Pattern;

public class PersonValidator {

  private static final Pattern EMAIL_PATTERN = Pattern.compile(
      "^[a-zA-Z0-9._%+-]+@[a-zA-Z0-9.-]+\\.[a-zA-Z]{2,}$"
  );

  public static void validate(Person person) {
    validateNotNull(person);
    validateRequiredFields(person);
    validateEmail(person.getEmail());
  }

  private static void validateNotNull(Person person) {
    if (person == null) {
      throw new InvalidPersonException("Persona no puede ser nulo");
    }
  }

  private static void validateRequiredFields(Person person) {
    if (isNullOrBlank(person.getIdNumber())) {
      throw new InvalidPersonException("El IdNumber es requerido");
    }

    if (isNullOrBlank(person.getName())) {
      throw new InvalidPersonException("El nombre es requerido");
    }

    if (isNullOrBlank(person.getEmail())) {
      throw new InvalidPersonException("El email es requerido");
    }
  }

  private static void validateEmail(String email) {
    if (!EMAIL_PATTERN.matcher(email).matches()) {
      throw new InvalidPersonException("El formato del email no es válido");
    }
  }

  private static boolean isNullOrBlank(String value) {
    return value == null || value.trim().isEmpty();
  }
}
