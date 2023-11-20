package net.rainbowcreation.loginer.guard.datasource;

import net.rainbowcreation.loginer.Main;
import net.rainbowcreation.loginer.exception.PlayerAlreadyExistException;
import net.rainbowcreation.loginer.exception.RegistrationException;
import net.rainbowcreation.loginer.model.IPlayer;
import net.rainbowcreation.loginer.model.Player;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;

import net.rainbowcreation.loginer.utils.Reference;
import org.apache.logging.log4j.Logger;

public class FileDataSourceStrategy implements IDataSourceStrategy {
  private final File authFile;
  
  private final List<IPlayer> players;
  
  private long lastModification;
  
  private static final Logger LOGGER = Main.LOGGER;
  
  private static final String SEPARATOR = ",";
  
  public FileDataSourceStrategy() {
    this.authFile = Paths.get(System.getProperty("java.io.tmpdir"), new String[] {Reference.NAME}).toFile();
    this.players = new ArrayList<>();
    readFile();
  }
  
  public FileDataSourceStrategy(File authFile) {
    this.authFile = authFile;
    this.players = new ArrayList<>();
    readFile();
  }
  
  private void readFile() {
    this.players.clear();
    try {
      boolean created = this.authFile.createNewFile();
      LOGGER.info((created ? "Create " : "Use ") + this.authFile.getAbsolutePath());
      try (BufferedReader bf = new BufferedReader(new FileReader(this.authFile))) {
        String line;
        while ((line = bf.readLine()) != null && line.trim().length() > 0) {
          if (!line.startsWith("#")) {
            String[] parts = line.trim().split(",");
            Player player = new Player();
            player.setEmail(parts[0].trim());
            player.setUsername(parts[1].trim());
            player.setPassword(parts[2].trim());
            player.setBanned(Boolean.parseBoolean(parts[3].trim()));
            this.players.add(player);
          } 
        } 
        this.lastModification = this.authFile.lastModified();
      } 
    } catch (IOException e) {
      LOGGER.catching(e);
    } 
  }
  
  private void reloadFile() {
    if (this.lastModification != this.authFile.lastModified())
      readFile(); 
  }
  
  public IPlayer find(String email, String username) {
    reloadFile();
    if (email != null)
      return this.players
        .stream()
        .filter(tmp -> tmp.getEmail().equals(email))
        .findFirst()
        .orElse(null); 
    if (username != null)
      return this.players
        .stream()
        .filter(tmp -> tmp.getUsername().equals(username))
        .findFirst()
        .orElse(null); 
    return null;
  }
  
  public boolean add(IPlayer player) throws RegistrationException {
    reloadFile();
    if (!exist(player)) {
      this.players.add(player);
      saveFile();
      return true;
    } 
    throw new PlayerAlreadyExistException();
  }
  
  public boolean exist(IPlayer player) {
    reloadFile();
    IPlayer p = this.players.stream().filter(tmp -> ((player.getEmail().equals(tmp.getEmail()) && !tmp.getEmail().equals("")) || (player.getUsername().equals(tmp.getUsername()) && !tmp.getUsername().equals("")))).findFirst().orElse(null);
    return (p != null);
  }
  
  private void saveFile() {
    try (BufferedWriter bw = new BufferedWriter(new FileWriter(this.authFile))) {
      bw.write(String.join(",", new CharSequence[] { "# email", " username", " hashed password", " is banned ?" }));
      bw.newLine();
      for (IPlayer entry : this.players) {
        bw.write(String.join(",", new CharSequence[] { entry.getEmail(), entry.getUsername(), entry.getPassword(), Boolean.toString(entry.isBanned()) }));
        bw.newLine();
      } 
      this.lastModification = Files.getLastModifiedTime(this.authFile.toPath(), new java.nio.file.LinkOption[0]).toMillis();
    } catch (IOException e) {
      LOGGER.catching(e);
    } 
  }
}