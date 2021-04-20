package twitter;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;
import javax.naming.NamingException;
import javax.naming.InitialContext;
import javax.naming.Context;
import javax.sql.DataSource;


public class DBManager implements AutoCloseable {

    private Connection connection;

    public DBManager() throws SQLException, NamingException {
        connect();
    }

    private void connect() throws SQLException, NamingException {
        Context initCtx = new InitialContext();
        Context envCtx = (Context) initCtx.lookup("java:comp/env");
        DataSource ds = (DataSource) envCtx.lookup("jdbc/BookShop");
        connection = ds.getConnection();
    }

    /**
     * Close the connection to the database if it is still open.
     *
     */
    public void close() throws SQLException {
        if (connection != null) {
            connection.close();
        }
        connection = null;
    }

    /*
     * Busca y devuelve un usuario buscnadolo por su nombre corto
     *
     */
    public User searchUser(String short_name) throws SQLException {
      // crea la busqueda
      String query = " SELECT * FROM Usuarios WHERE short_name=?";
      // creamos el usuario para rellenarlo
      User usuario = new User();
      try ( PreparedStatement ps = con.prepareStatement(query)) {
        ps.setString(1, short_name);
        ResultSet rs = ps.executeQuery();
        // si el resultado existe lo pasamos al usuario creado
        if(rs != null){
          usuario.setId(rs.getInt("id"));
          usuario.setShort_name(rs.getString("short_name"));
          usuario.setLong_name(rs.getString("long_name"));
          usuario.setMail(rs.getString("mail"));
          usuario.setPassword(rs.getString("password"));
        }
      }
        return usuario;
    }

    /*
     * Busca y devuelve un usuario buscnadolo por su nombre corto
     *
     */
    public User searchUserId(int iduser) throws SQLException {
      // crea la busqueda
      String query = " SELECT * FROM Usuarios WHERE id=?";
      // creamos el usuario para rellenarlo
      User usuario = new User();
      try ( PreparedStatement ps = con.prepareStatement(query)) {
        ps.setString(1, iduser);
        ResultSet rs = ps.executeQuery();
        // si el resultado existe lo pasamos al usuario creado
        if(rs != null){
          usuario.setId(rs.getInt("id"));
          usuario.setShort_name(rs.getString("short_name"));
          usuario.setLong_name(rs.getString("long_name"));
          usuario.setMail(rs.getString("mail"));
          usuario.setPassword(rs.getString("password"));
        }
      }
        return usuario;
    }


    /*
     * esta funcion comprueba si el nombre, el nombre corto y el email
     * estan libres en la base de datos
     * return:
     * 1: el nombre corto ya esta registrado
     * 2: el nombre ya esta registrado
     * 3: el mail ya esta registrado
     * 0: ninguno de los parametros esta registrado
     */
    public int isDataFree(String name, String shortName, String email) throws SQLException{
      String query_short = " SELECT short_name FROM Usuarios WHERE short_name=?";
      String query_name = " SELECT long_name FROM Usuarios WHERE long_name=?";
      String query_mail = " SELECT mail FROM Usuarios WHERE mail=?";
      try ( PreparedStatement ps = con.prepareStatement(query_short);
            PreparedStatement pn = con.prepareStatement(query_name);
            PreparedStatement pm = con.prepareStatement(query_mail)) {
        ps.setString(1, short_name);
        pn.setString(1, name);
        pm.setString(1, mail);

        ResultSet rs = ps.executeQuery();
        ResultSet rn = pn.executeQuery();
        ResultSet rm = pm.executeQuery();

        if(rs != null) return 1;
        if(rn != null) return 2;
        if(rm != null) return 3;
        return 0;
      }
    }

    /*
     * añade un usuario a la base de datos
     *
     */
    public void addUser(User usuario) throws SQLException{

      String query = "INSERT INTO Usuarios (short_name, long_name, mail, password) VALUES ('?', '?', '?', '?')";

      try ( PreparedStatement ps = con.prepareStatement(query)){
        ps.setString(1, usuario.getShort_name());
        ps.setString(2, usuario.getLong_name());
        ps.setString(3, usuario.getMail());
        ps.setString(4, usuario.getPassword());

        ResultSet rs = ps.executeQuery();
      }
    }

    /*
     * devuelve un ArrayList con los mensajes relacionados con el id
     * del usuario
     */
     //Necesaria revision
    public List<Message> listMessages(int id) throws SQLException{
      String query = "SELECT Mensajes.text AS mensajes , Mensajes.userId AS id , Mensajes.respuesta AS respuesta , Mensajes.retweet AS retweet , Mensajes. FROM Usuarios INNER JOIN Seguidos ON id=Seguidos.user INNER JOIN Mensajes ON Seguidos.seguido=Mensajes.userId DESC ";

		  ArrayList<Message> buzon = new ArrayList<Message>();

      try(Statement stmt = connection.createStatement()) {
        ResultSet rs = stmt.executeQuery(query);
        while(rs.next()) {
		    //Creamos el libro dentro del While para que no haya conflictos al introducirlo en el ArrayList
		      Message mensaje = new Message();
          mensaje.setId(null);
          mensaje.setUserId(null);
          mensaje.setText(rs.getString("mensajes"));
          mensaje.setShortName(rs.getString("nick"));
          mensaje.setLongName(rs.getString("name"));

			    buzon.add(libro);

        }
      }
      return buzon;
    }

}
