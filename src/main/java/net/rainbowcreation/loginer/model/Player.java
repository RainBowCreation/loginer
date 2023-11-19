package net.rainbowcreation.loginer.model;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

public class Player implements IPlayer {
  private String password;
  
  @Email
  private String email;
  
  private boolean banned;
  
  @Size(min = 36, max = 36)
  private String uuid;
  
  @NotNull
  private String username;
  
  public String getPassword() {
    return this.password;
  }
  
  public IPlayer setPassword(String password) {
    this.password = password;
    return this;
  }
  
  public String getEmail() {
    return this.email;
  }
  
  public IPlayer setEmail(String email) {
    this.email = (email == null) ? "" : email.trim();
    return this;
  }
  
  public boolean isBanned() {
    return this.banned;
  }
  
  public IPlayer setBanned(boolean ban) {
    this.banned = ban;
    return this;
  }
  
  public String getUuid() {
    return this.uuid;
  }
  
  public IPlayer setUuid(String uuid) {
    if (uuid == null) {
      this.uuid = "";
    } else {
      if (uuid.length() == 32)
        uuid = String.format("%s-%s-%s-%s-%s", new Object[] { uuid.substring(0, 8), uuid
              .substring(8, 12), uuid
              .substring(12, 16), uuid
              .substring(16, 20), uuid
              .substring(20, 32) }); 
      this.uuid = (uuid.length() == 36) ? uuid : "";
    } 
    return this;
  }
  
  public String getUsername() {
    return this.username;
  }
  
  public boolean isPremium() {
    return (getUuid().length() != 0);
  }
  
  public IPlayer setUsername(String username) {
    this.username = (username == null) ? "" : username.trim();
    return this;
  }
  
  public String toString() {
    return String.format("{%s, %s}", new Object[] { getEmail(), getUsername() });
  }
}


/* Location:              D:\jd-gui\authmod-3.2.jar!\io\chocorean\authmod\model\Player.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */