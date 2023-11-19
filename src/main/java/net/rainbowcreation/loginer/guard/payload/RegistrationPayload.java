package net.rainbowcreation.loginer.guard.payload;

import java.util.HashSet;
import java.util.Set;
import javax.validation.ConstraintViolation;
import javax.validation.Validation;
import javax.validation.Validator;
import javax.validation.ValidatorFactory;
import javax.validation.constraints.AssertTrue;
import javax.validation.constraints.NotNull;

public class RegistrationPayload implements IPayload {
  @NotNull
  private final LoginPayload payload = new LoginPayload();
  
  @NotNull
  private String passwordConfirmation;
  
  private Set<ConstraintViolation<IPayload>> errors = new HashSet<>();
  
  public RegistrationPayload(IPayload payload, String passwordConfirmation) {
    this();
    this.payload.setEmail(payload.getEmail());
    this.payload.setUsername(payload.getUsername());
    this.payload.setUuid(payload.getUuid());
    this.payload.setPassword(payload.getPassword());
    this.passwordConfirmation = passwordConfirmation;
  }
  
  public RegistrationPayload setPassword(String password) {
    this.payload.setPassword(password);
    return this;
  }
  
  public boolean isValid() {
    ValidatorFactory factory = Validation.buildDefaultValidatorFactory();
    Validator validator = factory.getValidator();
    this.errors = validator.validate(this.payload, new Class[0]);
    this.errors.addAll(validator.validate(this, new Class[0]));
    return this.errors.isEmpty();
  }
  
  public RegistrationPayload setUsername(String username) {
    this.payload.setUsername(username);
    return this;
  }
  
  public RegistrationPayload setUuid(String uuid) {
    this.payload.setUuid(uuid);
    return this;
  }
  
  public String getUuid() {
    return this.payload.getUuid();
  }
  
  public RegistrationPayload setEmail(String email) {
    this.payload.setEmail(email);
    return this;
  }
  
  public boolean isEmailRequired() {
    return this.payload.isEmailRequired();
  }
  
  public RegistrationPayload setEmailRequired(boolean emailRequired) {
    this.payload.setEmailRequired(emailRequired);
    return this;
  }
  
  @AssertTrue
  private boolean isEmailValid() {
    return (!isEmailRequired() || this.payload.getEmail() != null);
  }
  
  @AssertTrue
  private boolean isEmailDefined() {
    return (!isEmailRequired() || this.payload.getEmail() != null);
  }
  
  @AssertTrue
  private boolean isPasswordConfirmationMatches() {
    return this.passwordConfirmation.equals(this.payload.getPassword());
  }
  
  public String getEmail() {
    return this.payload.getEmail();
  }
  
  public String getUsername() {
    return this.payload.getUsername();
  }
  
  public String getPassword() {
    return this.payload.getPassword();
  }
  
  public Set<ConstraintViolation<IPayload>> getErrors() {
    return this.errors;
  }
  
  public RegistrationPayload setPasswordConfirmation(String passwordConfirmation) {
    this.passwordConfirmation = passwordConfirmation;
    return this;
  }
  
  public String toString() {
    return "RegistrationPayload{email='" + this.payload
      .getEmail() + '\'' + ", username='" + this.payload
      .getUsername() + '\'' + ", uuid='" + this.payload
      .getUuid() + '\'' + '}';
  }
  
  public RegistrationPayload() {}
}


/* Location:              D:\jd-gui\authmod-3.2.jar!\io\chocorean\authmod\guard\payload\RegistrationPayload.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */