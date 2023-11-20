package net.rainbowcreation.loginer.guard.payload;

import java.util.HashSet;
import java.util.Set;
import javax.validation.ConstraintViolation;
import javax.validation.Validation;
import javax.validation.Validator;
import javax.validation.ValidatorFactory;
import javax.validation.constraints.AssertTrue;
import javax.validation.constraints.Email;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

public class LoginPayload implements IPayload {
  @Email
  private String email;
  
  @NotNull
  @Size(min = 5, max = 100)
  private String password;
  
  @NotNull
  private String username;
  
  @Size(min = 32, max = 36)
  private String uuid;
  
  private Set<ConstraintViolation<IPayload>> errors = new HashSet<>();
  
  private boolean emailRequired;
  
  public LoginPayload setPassword(String password) {
    this.password = password;
    return this;
  }
  
  public boolean isValid() {
    ValidatorFactory factory = Validation.buildDefaultValidatorFactory();
    Validator validator = factory.getValidator();
    this.errors = validator.validate(this, new Class[0]);
    return this.errors.isEmpty();
  }
  
  public LoginPayload setUsername(String username) {
    this.username = username;
    return this;
  }
  
  public LoginPayload setEmail(String email) {
    if (email != null)
      email = (email.length() < 3) ? null : email; 
    this.email = email;
    return this;
  }
  
  public boolean isEmailRequired() {
    return this.emailRequired;
  }
  
  public IPayload setEmailRequired(boolean emailRequired) {
    this.emailRequired = emailRequired;
    return this;
  }
  
  public String getEmail() {
    return this.email;
  }
  
  public String getUsername() {
    return this.username;
  }
  
  public String getPassword() {
    return this.password;
  }
  
  public Set<ConstraintViolation<IPayload>> getErrors() {
    return this.errors;
  }
  
  @AssertTrue
  private boolean isEmailDefined() {
    return (!this.emailRequired || getEmail() != null);
  }
  
  public String getUuid() {
    return this.uuid;
  }
  
  public LoginPayload setUuid(String uuid) {
    this.uuid = uuid;
    return this;
  }
  
  public String toString() {
    return "LoginPayload{email='" + this.email + '\'' + ", username='" + this.username + '\'' + ", uuid='" + this.uuid + '\'' + '}';
  }
}