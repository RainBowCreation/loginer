package net.rainbowcreation.loginer.guard.payload;

import java.util.Set;
import javax.validation.ConstraintViolation;

public interface IPayload {
  IPayload setPassword(String paramString);
  
  boolean isValid();
  
  IPayload setUsername(String paramString);
  
  IPayload setUuid(String paramString);
  
  String getUuid();
  
  IPayload setEmail(String paramString);
  
  boolean isEmailRequired();
  
  IPayload setEmailRequired(boolean paramBoolean);
  
  String getEmail();
  
  String getUsername();
  
  String getPassword();
  
  Set<ConstraintViolation<IPayload>> getErrors();
}


/* Location:              D:\jd-gui\authmod-3.2.jar!\io\chocorean\authmod\guard\payload\IPayload.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */