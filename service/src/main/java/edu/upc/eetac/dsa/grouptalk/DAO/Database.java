package edu.upc.eetac.dsa.grouptalk.DAO;

import com.zaxxer.hikari.HikariConfig;
import com.zaxxer.hikari.HikariDataSource;

import javax.sql.DataSource;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.Enumeration;
import java.util.Properties;
import java.util.PropertyResourceBundle;
import java.util.ResourceBundle;

/**
 * Created by marc on 2/03/16.
 *
 * El singleton crea una classe donde solo se hace una instancia y que esta puede ser obtenida de forma global. En este
 * caso la classe Database crea un pool de connexiones con el Hikari. De esta manera tenemos una unica connexión
 * activa que hace merge de todas las peticiones.
 */
public class Database {
    private static Database instance = null;
    private DataSource ds;

    private Database() {
        PropertyResourceBundle prb = (PropertyResourceBundle) ResourceBundle.getBundle("hikari");
        Enumeration<String> keys = prb.getKeys();
        Properties properties = new Properties();
        while(keys.hasMoreElements()){
            String key = keys.nextElement();
            properties.setProperty(key, prb.getString(key));
        }

        //HikariConfig config = new HikariConfig(Database.class.getClassLoader().getResource("hikari.properties").getFile());
        HikariConfig config = new HikariConfig(properties);
        ds = new HikariDataSource(config);
    }

    /**
     * El final static DAtabase con el retorno de la instancia es la única variación con un otra classe normal.
     */

    private final static Database getInstance() {
        if (instance == null)
            instance = new Database();
        return instance;
    }

    public final static Connection getConnection() throws SQLException {
        return getInstance().ds.getConnection();
    }
}
