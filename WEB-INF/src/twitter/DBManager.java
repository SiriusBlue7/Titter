package twitter;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.sql.PreparedStatement;
import java.util.ArrayList;
import java.util.List;
import java.util.Date;
import java.sql.Timestamp;

import javax.sql.DataSource;
import javax.naming.NamingException;
import javax.naming.InitialContext;
import javax.naming.Context;


public class DBManager implements AutoCloseable {

    private Connection connection;

    public DBManager() throws SQLException, NamingException{
        connect();
    }

   	private void connect() throws SQLException, NamingException {
    	Context initCtx = new InitialContext();
    	Context envCtx = (Context) initCtx.lookup("java:comp/env");
    	DataSource ds = (DataSource) envCtx.lookup("jdbc/twitter");
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

    public User searchUser(String short_name) throws SQLException {
      // crea la busqueda
      String query = " SELECT * FROM Usuarios WHERE short_name=?";
      // creamos el usuario para rellenarlo
      User usuario = null;
      try ( PreparedStatement ps = connection.prepareStatement(query)) {
        ps.setString(1, short_name);
        ResultSet rs = ps.executeQuery();
        // si el resultado existe lo pasamos al usuario creado
        if(rs.next()){

          usuario = new User();
          usuario.setId(rs.getInt("id"));
          usuario.setShort_name(rs.getString("short_name"));
          usuario.setLong_name(rs.getString("long_name"));
          usuario.setMail(rs.getString("mail"));
          usuario.setPassword(rs.getString("password"));
        }
      }
        return usuario;
    }

    //
    public User authenticate(String short_name, String password) throws SQLException {
      // crea la busqueda
      String query = " SELECT * FROM Usuarios WHERE short_name=? AND password=?";
      // creamos el usuario para rellenarlo
      User usuario = null;
      try ( PreparedStatement ps = connection.prepareStatement(query)) {
        ps.setString(1, short_name);
        ps.setString(2,password);
        ResultSet rs = ps.executeQuery();
        // si el resultado existe lo pasamos al usuario creado
        if(rs.next()){
          usuario = new User();
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
      try ( PreparedStatement ps = connection.prepareStatement(query_short);
            PreparedStatement pn = connection.prepareStatement(query_name);
            PreparedStatement pm = connection.prepareStatement(query_mail)) {
        ps.setString(1, shortName);
        pn.setString(1, name);
        pm.setString(1, email);

        ResultSet rs = ps.executeQuery();
        ResultSet rn = pn.executeQuery();
        ResultSet rm = pm.executeQuery();

        if(rs.next()) return 1;
        if(rn.next()) return 2;
        if(rm.next()) return 3;
        return 0;
      }
    }

    /*
     * añade un usuario a la base de datos
     *
     */
    public void addUser(User usuario) throws SQLException{

      String query = "INSERT INTO Usuarios (short_name, long_name, mail, password) VALUES (?, ?,?,?)";

      try ( PreparedStatement ps = connection.prepareStatement(query)){
        ps.setString(1, usuario.getShort_name());
        ps.setString(2, usuario.getLong_name());
        ps.setString(3, usuario.getMail());
        ps.setString(4, usuario.getPassword());

        int rowcount = ps.executeUpdate();
      }
    }

    public User searchUser(int id) throws SQLException{
        String query = "SELECT * FROM Usuarios WHERE id=?";
        User user = null;
        try(PreparedStatement pst = connection.prepareStatement(query)){

          pst.setInt(1, id);

          ResultSet rs = pst.executeQuery();

          if (rs.next()) {
            user = new User();
            user.setId(id);
            user.setShort_name(rs.getString("short_name"));
            user.setLong_name(rs.getString("long_name"));
            user.setMail(rs.getString("mail"));
            user.setPassword(rs.getString("password"));
          }
        }
      return user;
  	}

    /*
     * devuelve un ArrayList con los mensajes relacionados con el id
     * del usuario
     */
     //Necesaria revision y terminar
    public List<Message> listMessages(int id) throws SQLException{
      String query = "SELECT Mensajes.id AS idmensaje , Mensajes.text AS mensajes , Mensajes.userId AS iduser ,  Mensajes.respuesta AS respuesta , Mensajes.retweet AS retweet , Mensajes.fecha AS fecha FROM Usuarios INNER JOIN Seguidos ON Usuarios.id = Seguidos.seguido INNER JOIN Mensajes ON Usuarios.id = Mensajes.userId WHERE Seguidos.user = ? ORDER BY Mensajes.fecha DESC";

		  ArrayList<Message> buzon = new ArrayList<Message>();

      try(PreparedStatement stmt = connection.prepareStatement(query)) {

        stmt.setInt(1, id);

        ResultSet rs = stmt.executeQuery();
        while(rs.next()) {

		      Message mensaje = new Message();

          mensaje.setId(rs.getInt("idmensaje"));
          mensaje.setText(rs.getString("mensajes"));
          mensaje.setDate(rs.getTimestamp("fecha"));
          mensaje.setRetweet(rs.getInt("retweet"));
          mensaje.setRespuesta(rs.getInt("respuesta"));
          int user_id = rs.getInt("iduser");
          User usuario = searchUser(user_id);
          mensaje.setShortName(usuario.getShort_name());
          mensaje.setLongName(usuario.getLong_name());

			    buzon.add(mensaje);
        }
      }
      return buzon;
    }

    public List<Message> listUserMessage(int id) throws SQLException{
      String query="SELECT * FROM Mensajes WHERE userId=? ORDER BY fecha DESC";
      ArrayList<Message> buzon = new ArrayList<Message>();

      try(PreparedStatement stmt = connection.prepareStatement(query)) {

        stmt.setInt(1, id);

        ResultSet rs = stmt.executeQuery();
        while(rs.next()) {

		      Message mensaje = new Message();
          int retweet = rs.getInt("retweet");
          if (retweet>=0) {
            //si el mensaje tiene el valor de retweet igual a null, entonces es un mensaje normal
            mensaje.setRetweet(retweet);
            mensaje.setId(rs.getInt("idmensaje"));
            mensaje.setText(rs.getString("mensajes"));
            mensaje.setDate(rs.getTimestamp("fecha"));
            mensaje.setRespuesta(rs.getInt("respuesta"));
            int user_id = rs.getInt("iduser");
            User usuario = searchUser(user_id);
            mensaje.setShortName(usuario.getShort_name());
            mensaje.setLongName(usuario.getLong_name());


          }else{//en el caso de que el campo sea distitnto de null, tenemos que buscar el mensaje original
                //y rellenar los campos con los datos de ese mensaje
                mensaje = searchMessage(retweet);
                //Igualamos el mensaje que tenemos con el Message que nos devuelve el searchMessage()
          }
          buzon.add(mensaje);
        }
      }
      return buzon;
    }

    public Message searchMessage(int id) throws SQLException{
      String query = " SELECT * FROM Mensajes WHERE id=? ORDER BY Mensajes.fecha DESC";
      // creamos el usuario para rellenarlo
      Message mensaje = null;
      try ( PreparedStatement ps = connection.prepareStatement(query)) {
        ps.setInt(1, id);
        ResultSet rs = ps.executeQuery();
        // si el resultado existe lo pasamos al usuario creado
        if(rs.next()){
          mensaje = new Message();

          mensaje.setId(id);
          mensaje.setUserId(rs.getInt("userId"));
          mensaje.setText(rs.getString("texto"));
          mensaje.setDate(rs.getTimestamp("fecha"));
          mensaje.setRetweet(rs.getInt("retweet"));
          mensaje.setRespuesta(rs.getInt("respuesta"));
          //para evitar tener que hacer consultas más dificiles, buscamos el usuario por su id
          //a partir del objeto que nos devuelve, rellenamoslos campos que nos faltan
          User usuario = searchUser(mensaje.getUserId());
          mensaje.setShortName(usuario.getShort_name());
          mensaje.setLongName(usuario.getLong_name());
        }
      }
      return mensaje;
    }

    public List<Message> listAnswer(int id) throws SQLException{
      String query = " SELECT * FROM Mensajes WHERE respuesta=?";

      ArrayList<Message> buzon = new ArrayList<Message>();

      try(PreparedStatement stmt = connection.prepareStatement(query)) {

        stmt.setInt(1, id);

        ResultSet rs = stmt.executeQuery();
        while(rs.next()) {

		      Message mensaje = new Message();

          mensaje.setId(id);
          mensaje.setText(rs.getString("texto"));
          mensaje.setDate(rs.getTimestamp("fecha"));
          mensaje.setRetweet(rs.getInt("retweet"));
          mensaje.setRespuesta(rs.getInt("respuesta"));
          mensaje.setUserId(rs.getInt("userId"));
          User usuario = searchUser(mensaje.getUserId());
          mensaje.setShortName(usuario.getShort_name());
          mensaje.setLongName(usuario.getLong_name());

			    buzon.add(mensaje);
        }
      }
      return buzon;

    }

    public void addMessage(Message mensaje) throws SQLException{
      String query = "INSERT INTO Mensajes (userId  , text , fecha) VALUES (? , ? , ?)";

      try ( PreparedStatement ps = connection.prepareStatement(query)){
        ps.setInt(1, mensaje.getUserId());
        ps.setString(2, mensaje.getText());
        //Cogemos la hora en el servidor
        Date date = new Date();
        Timestamp ts=new Timestamp(date.getTime());//Cambiamos el tipo de la hora para poder añadirlo con Timestamp
        ps.setTimestamp(3,ts);

        ps.executeUpdate();
      }
    }

    public void respond(Message mensaje, int id) throws SQLException{
      //Introducmos en la funcioin el mensaje que queremos crear y el id del mensaje al que estamos respondiendo
      String query = "INSERT INTO Mensajes (userId ,respuesta , text , fecha) VALUES (? , ? , ? , ?)";
      try ( PreparedStatement ps = connection.prepareStatement(query)){
        ps.setInt(1, mensaje.getUserId());
        ps.setString(2, mensaje.getText());
        ps.setInt(3,id);
        //Cogemos la hora en el servidor
        Date date = new Date();
        Timestamp ts=new Timestamp(date.getTime());//Cambiamos el tipo de la hora para poder añadirlo con Timestamp
        ps.setTimestamp(4,ts);

        ps.executeUpdate();
      }
    }

    public void retweet(Message mensaje, int id) throws SQLException{
      //Introducmos en la funcioin el mensaje que queremos crear y el id del mensaje al que estamos respondiendo
      String query = "INSERT INTO Mensajes (userId , retweet , fecha) VALUES (? , ? , ?)";
      try ( PreparedStatement ps = connection.prepareStatement(query)){
        ps.setInt(1, mensaje.getUserId());
        //cogemos el id del mensaje al que estamos retweeteando
        ps.setInt(2,id);
        //Cogemos la hora en el servidor
        Date date = new Date();
        Timestamp ts=new Timestamp(date.getTime());//Cambiamos el tipo de la hora para poder añadirlo con Timestamp
        ps.setTimestamp(3,ts);

        ps.executeUpdate();
      }
    }

    public void follow(int id1, int id2) throws SQLException{
      String query = "INSERT INTO Seguidos VALUES (?,?)";
      try ( PreparedStatement ps = connection.prepareStatement(query)){
        ps.setInt(1, id1);
        ps.setInt(2, id2);
        ps.executeUpdate();
      }
    }

    public void unfollow(int id1, int id2) throws SQLException{
      String query = "DELETE FROM Seguidos WHERE id=?  AND seguido=?";
      try ( PreparedStatement ps = connection.prepareStatement(query)){
        ps.setInt(1, id1);
        ps.setInt(2, id2);
        ps.executeUpdate();
      }
    }

}
