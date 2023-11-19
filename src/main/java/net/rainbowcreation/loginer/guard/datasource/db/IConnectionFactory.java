package net.rainbowcreation.loginer.guard.datasource.db;

import java.sql.Connection;
import java.sql.SQLException;

public interface IConnectionFactory {
  Connection getConnection() throws SQLException;
  
  String getURL();
}


/* Location:              D:\jd-gui\authmod-3.2.jar!\io\chocorean\authmod\guard\datasource\db\IConnectionFactory.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */