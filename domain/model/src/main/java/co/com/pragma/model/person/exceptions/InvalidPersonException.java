package co.com.pragma.model.person.exceptions;

public class InvalidPersonException extends IllegalArgumentException {

  public InvalidPersonException(String message) {
    super(message);
  }
}
