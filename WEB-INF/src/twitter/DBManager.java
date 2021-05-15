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
    /*
    *
    *Buscamos el usuario por medio de su short_name
    *
    */
    public User searchUser(String short_name) throws SQLException {
      // crea la busqueda
      String query = "SELECT * FROM Usuarios WHERE short_name=?";
      // creamos el usuario para rellenarlo
      User usuario = null;
      //creamos el usuario a null, para que no haya que hacer cambios
      try ( PreparedStatement ps = connection.prepareStatement(query)) {

        ps.setString(1, short_name);//rellenamos el statement con el String de forma segura

        ResultSet rs = ps.executeQuery();
        //ejecutamos la consulta en la BD
        if(rs.next()){
          //SI la consulta fue existosa, nos devuelve una línea
          //Recorremos las columnas de la consulta almacenando los datos en el User para devolverlo
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
    *
    *Buscamos el usuario por de su id
    *
    */
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

    //Usamos esta funciona para comprobar si lois datos introducidos en el login coinciden con los de algun usuario almacenado
    public User authenticate(String short_name, String password) throws SQLException {
      // crea la busqueda
      String query = " SELECT * FROM Usuarios WHERE short_name=? AND password=?";//Vemos si hay un usuario con el shortname y la contraseña
      // creamos el usuario para rellenarlo
      User usuario = null;
      try ( PreparedStatement ps = connection.prepareStatement(query)) {
        //rellenamos la consulta con los datos, de forma segura con el Prepared Statement
        ps.setString(1, short_name);
        ps.setString(2,password);
        //Ejecutamos la consulta
        ResultSet rs = ps.executeQuery();
        // si el resultado existe lo pasamos al usuario creado
        if(rs.next()){
          //Si la consulta tiene una línea, es que ha sido exitosa, por lo que rellenamos el user, para almacenarlo en la sesion
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
      //Preparamos las distintas consultas para comprobar si los campos a rellenar estan libres
      String query_short = " SELECT short_name FROM Usuarios WHERE short_name=?";
      String query_name = " SELECT long_name FROM Usuarios WHERE long_name=?";
      String query_mail = " SELECT mail FROM Usuarios WHERE mail=?";

      try ( PreparedStatement ps = connection.prepareStatement(query_short);
            PreparedStatement pn = connection.prepareStatement(query_name);
            PreparedStatement pm = connection.prepareStatement(query_mail)) {
        //rellenamos los statement con los datos introducidos por el usuario
        ps.setString(1, shortName);
        pn.setString(1, name);
        pm.setString(1, email);

        ResultSet rs = ps.executeQuery();
        ResultSet rn = pn.executeQuery();
        ResultSet rm = pm.executeQuery();
        //Comprobamos los resultados de las consultas, si ninguna nos devuelve una línea, significa que la informacion esta libre
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
      //Creamos la onsulta, con todos los datos a rellenar en la BD
      //No indicamos el id, ya que al ser autoincremental , se rellena solo de esta forma y asi evitamos problemas
      String query = "INSERT INTO Usuarios (short_name, long_name, mail, password) VALUES (?, ?,?,?)";

      try ( PreparedStatement ps = connection.prepareStatement(query)){
        //Rellenamos todos los campos de la consulta de forma segura
        ps.setString(1, usuario.getShort_name());
        ps.setString(2, usuario.getLong_name());
        ps.setString(3, usuario.getMail());
        ps.setString(4, usuario.getPassword());

       ps.executeUpdate();//Ejecutamos el update para subir los archivos
      }
    }


    /*
     * devuelve un ArrayList con los mensajes relacionados con el id
     * del usuario
     */
    public List<Message> listMessages(int id) throws SQLException{
      String query = "SELECT Mensajes.id AS idmensaje , Mensajes.text AS mensajes , Mensajes.userId AS iduser ,  Mensajes.respuesta AS respuesta , Mensajes.retweet AS retweet , Mensajes.fecha AS fecha FROM Usuarios INNER JOIN Seguidos ON Usuarios.id = Seguidos.seguido INNER JOIN Mensajes ON Usuarios.id = Mensajes.userId WHERE Seguidos.user = ? ORDER BY Mensajes.fecha DESC LIMIT 50";

		  ArrayList<Message> buzon = new ArrayList<Message>();

      try(PreparedStatement stmt = connection.prepareStatement(query)) {

        stmt.setInt(1, id);

        ResultSet rs = stmt.executeQuery();
        while(rs.next()) {

		      Message mensaje = new Message();
          int retweet = rs.getInt("retweet");
          if (retweet<=0) {
            //si el mensaje tiene el valor de retweet igual a null, entonces es un mensaje normal
            mensaje.setRetweet(retweet);
            mensaje.setId(rs.getInt("idmensaje"));
            mensaje.setText(rs.getString("mensajes"));
            mensaje.setDate(rs.getTimestamp("fecha"));
            mensaje.setRespuesta(rs.getInt("respuesta"));
            int user_id = rs.getInt("iduser");
            User usuario = searchUser(user_id);
            mensaje.setUserId(user_id);
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

    /*
    *
    *En esta funcion lo que hacemos es buscar todos los mensajes que ha escrito el usuario para imprimirlos en su perfil
    *Es parecido al listMessages pero solo buscando los mensajes  con el idUser del usuario
    *Devuelve una lista de como mucho 50 mensajes
    */
    public List<Message> listUserMessage(int id) throws SQLException{
      String query="SELECT * FROM Mensajes WHERE userId=? ORDER BY fecha DESC LIMIT 50";
      ArrayList<Message> buzon = new ArrayList<Message>();//Creamos el ArrayList en el que vamos a ir añadiendo los mensajes
      try(PreparedStatement stmt = connection.prepareStatement(query)) {

        stmt.setInt(1, id);

        ResultSet rs = stmt.executeQuery();
        while(rs.next()) {

		      Message mensaje = new Message();
          int retweet = rs.getInt("retweet");
          if (retweet<=0) {//Si el campo retweet es = que 0 , es como si estuviese a null, por lo que no habria que hacer nada raro
            //si el mensaje tiene el valor de retweet igual a null, entonces es un mensaje normal
            mensaje.setRetweet(retweet);
            mensaje.setId(rs.getInt("id"));
            mensaje.setText(rs.getString("text"));
            mensaje.setDate(rs.getTimestamp("fecha"));
            mensaje.setRespuesta(rs.getInt("respuesta"));
            int user_id = rs.getInt("userId");
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

    /*
    *
    *Busca un mensaje por su id
    *
    */
    public Message searchMessage(int id) throws SQLException{
      //preparamos la consulta de la búsqueda
      String query = " SELECT * FROM Mensajes WHERE id=?";//buscamos un mensaje cuyo id sea el deseado
      // creamos el usuario para rellenarlo
      Message mensaje = null;
      try ( PreparedStatement ps = connection.prepareStatement(query)) {
        //rellenamos el campo con el dato introducido
        ps.setInt(1, id);
        //ejecutamos la consulta
        ResultSet rs = ps.executeQuery();
        // si el resultado existe lo pasamos al usuario creado
        if(rs.next()){
          mensaje = new Message();

          mensaje.setId(id);
          mensaje.setUserId(rs.getInt("userId"));
          mensaje.setText(rs.getString("text"));
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

    /*
    *
    *Busca todos los mensajes que responden a un mensaje con X id
    *Se hace una consulta que devuelve todos los mensajes que tienen en la colummna respuesta ese id
    *
    */
    public List<Message> listAnswer(int id) throws SQLException{
      String query = " SELECT * FROM Mensajes WHERE respuesta=?";

      ArrayList<Message> buzon = new ArrayList<Message>();

      try(PreparedStatement stmt = connection.prepareStatement(query)) {

        stmt.setInt(1, id);

        ResultSet rs = stmt.executeQuery();
        while(rs.next()) {

		      Message mensaje = new Message();

          mensaje.setId(rs.getInt("id"));
          mensaje.setText(rs.getString("text"));
          mensaje.setDate(rs.getTimestamp("fecha"));
          mensaje.setRetweet(rs.getInt("retweet"));
          mensaje.setRespuesta(rs.getInt("respuesta"));
          mensaje.setUserId(rs.getInt("userId"));
          User usuario = searchUser(mensaje.getUserId());
          mensaje.setShortName(usuario.getShort_name());
          mensaje.setLongName(usuario.getLong_name());

			    buzon.add(mensaje);//añadimos el mensaje a la List
        }
      }
      return buzon;//Devolvemos el buzon
    }

    /*
    *
    *Creamos el mensaje con los datos que se nos pasan y los añadimos a la BD
    *
    */
    public void addMessage(Message mensaje) throws SQLException{
      //Creamos la query, en la que indicamos
      String query = "INSERT INTO Mensajes (userId  , text , fecha) VALUES (? , ? , ?)";

      try ( PreparedStatement ps = connection.prepareStatement(query)){
        //Rellenamos los valores del PreparedStatement de forma segura
        ps.setInt(1, mensaje.getUserId());
        ps.setString(2, mensaje.getText());
        //Cogemos la hora en el servidor
        Date date = new Date();
        Timestamp ts=new Timestamp(date.getTime());//Cambiamos el tipo de la hora para poder añadirlo con Timestamp
        ps.setTimestamp(3,ts);

        ps.executeUpdate();//Ejecutamos el update
      }
    }

    /*
    *
    *Inserta un mensaje nuevo marcado como respuesta a otro mensaje, cuyo id se indica
    *como parametro.
    */
    public void respond(Message mensaje, int id) throws SQLException{
      //Introducmos en la funcioin el mensaje que queremos crear y el id del mensaje al que estamos respondiendo
      String query = "INSERT INTO Mensajes (userId ,respuesta , text , fecha) VALUES (? , ? , ? , ?)";
      try ( PreparedStatement ps = connection.prepareStatement(query)){
        //Rellenamos los datos del prepareStatement de forma segura
        ps.setInt(1, mensaje.getUserId());
        ps.setInt(2,id);
        ps.setString(3, mensaje.getText());
        //Cogemos la hora en el servidor
        Date date = new Date();
        Timestamp ts=new Timestamp(date.getTime());//Cambiamos el tipo de la hora para poder añadirlo con Timestamp
        ps.setTimestamp(4,ts);

        ps.executeUpdate();//Ejecutamos el update
      }
    }

    /*
    *
    *Inserta un mensaje vacio que marca en el apartado retweet el id del mensaje que
    *se desea retransmitir.
    *
    */
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

        ps.executeUpdate();//Ejecutamos el update
      }
    }

    /*
    *El usuario con id1 le da a aseguir al usuario id2 y actualizamos la BD para que se añada el elemento
    */
    public void follow(int id1, int id2) throws SQLException{
      //Creamos el query con el que insertamos el nuevo elemento en la tabla de seguidos
      String query = "INSERT INTO Seguidos VALUES (?,?)";
      try ( PreparedStatement ps = connection.prepareStatement(query)){
        //rellenamos con los ids, del usuario que esta en sesion y el usuario al que va a seguir
        ps.setInt(1, id1);
        ps.setInt(2, id2);
        //Ejecutamos el update
        ps.executeUpdate();
      }
    }

    /*
    *El usuario con id1 deja de seguir al usuario con id2 y actualiza el campo en la tabla de seguidos
    */
    public void unfollow(int id1, int id2) throws SQLException{
      //Creamos el query con el que buscamos la línea con los ids de los interesados y las borramos
      String query = "DELETE FROM Seguidos WHERE user=? AND seguido=?";
      try ( PreparedStatement ps = connection.prepareStatement(query)){
        //rellenamos con los ids que se nos pasan
        ps.setInt(1, id1);
        ps.setInt(2, id2);
        //ejecutamos el update
        ps.executeUpdate();
      }
    }
    /*
    *Comprobamos si el usuario con id1 esta siguiendo o no al usuario con id2
    *Si le sigue, devolvemos un true
    *En caso contrario, devolvemos un false
    */
    public boolean followed(int id1, int id2) throws SQLException{
      boolean result = false;
      String query = "SELECT * FROM Seguidos WHERE user=? AND seguido=?";
      try ( PreparedStatement ps = connection.prepareStatement(query)){
        ps.setInt(1, id1);
        ps.setInt(2, id2);
        ResultSet rs = ps.executeQuery();
        while(rs.next()) {
          result = true;
        }
      }
      return result;
    }
}
