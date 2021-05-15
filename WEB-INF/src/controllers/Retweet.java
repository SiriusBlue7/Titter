package controllers;
import twitter.*;//Esto accede a la libreria que hemos creado nosotros en BookShop y podamos acceder a llamarlo

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Date;
import java.sql.SQLException;
import javax.naming.NamingException;
import javax.servlet.RequestDispatcher;//para evitar que el RequestDispatcher no de error

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

@WebServlet("/retweet")
public class Retweet extends HttpServlet {
  public void doGet(HttpServletRequest request, HttpServletResponse response)
          throws IOException, ServletException {

      HttpSession session = request.getSession();//Con esto lo que hacemos es entrar en la sesion
      User user = (User) session.getAttribute("user");//Cogemos el atributo user dentro de la sesion
      //cogemos los elementos del texto que hemos escrito y el id del mensaje para crear el objeto y enviarlo en la funcion
      int id_mensaje = Integer.parseInt(request.getParameter("id_mensaje"));
      //cogemos la hora para crear el mensaje del usuario
      java.util.Date utilDate = new java.util.Date();
      //Definimos el mensaje a subir
      Message mensaje = new Message();
      //Rellenamos el mensaje
      mensaje.setUserId(user.getId());
      mensaje.setDate(utilDate);

      try(DBManager db = new DBManager()){
        //llamamos a la funcion para que guarde el mensaje en la base de datos
        db.retweet(mensaje,id_mensaje);
        if (request.getParameter("id")!=null) {//Comprobamos si el parametro "id" es distinto de null
          //Cogemos el parametro para poder usarlo
          int id = Integer.parseInt(request.getParameter("id"));
          //nos reenvia profile en el que estabamos
          response.sendRedirect("profile?id=");
        }else{//sino, nos reenvia al home
          response.sendRedirect("home");//como estamos en el home, que nos vuelvba a llevar ahi
        }
      }catch (SQLException | NamingException e){
				e.printStackTrace();
				response.sendError(500);
			}
      //Aqui si ya existe el usuario, tendriamos que mostrar un mensaje por pantalla, o redirigir a otra página de error
      //session.sendRedirect("/error");
    }
  }
