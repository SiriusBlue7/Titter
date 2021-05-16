package controllers;
import twitter.*;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.sql.SQLException;
import javax.naming.NamingException;
import javax.servlet.RequestDispatcher;//para evitar que el RequestDispatcher no de error

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

@WebServlet("/login")
public class Login extends HttpServlet {
  public void doPost(HttpServletRequest request, HttpServletResponse response)
          throws IOException, ServletException {

      HttpSession session = request.getSession();//Con esto lo que hacemos es entrar en la sesion
      User user = (User) session.getAttribute("user");//Cogemos el atributo user dentro de la sesion
      if (user == null) {//comprobamos si en la sesion ya hay algun usuario almacenado

          try(DBManager db = new DBManager()){
            // recogemos el valor del nombre de usuario y la contraseña
            String new_user = request.getParameter("user");
            String password = request.getParameter("password");
            //Buscamos en la base de datos el usuario
            user = db.authenticate(new_user,password);//Nos devuelve un usuario que tenga el mismo name y password
            if(user!=null) {//comprobamos si el usuario, existe, de no ser asi, se nos devuelve un user null
              int room = 1;//Este parametro indica que que habitacion estamos
              int number= 0;//Este indica , que si estamos en un Profile o Conversation, cual es el id de esta
              session.setAttribute("room",room);
              session.setAttribute("number",number);
              session.setAttribute("user",user);
              //una vez comprobado que todo esta bien, redirigimos a la pagina principal del usuario
              response.sendRedirect("home");
            }else{//de no ser asi, devuelve un parametro error
              response.sendRedirect("iniciosesion.html?error=1");
            }

          }catch (SQLException | NamingException e){
  					e.printStackTrace();
  					response.sendError(500);
  				}
      }else{//si ya hay un usuario almacenado nos redirige directamente a la pagina home del usuario
        response.sendRedirect("home");
      }
    }
  }
