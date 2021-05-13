package controllers;
import twitter.*;//Esto accede a la libreria que hemos creado nosotros en BookShop y podamos acceder a llamarlo

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

@WebServlet("/unfollow")
public class Unfollow extends HttpServlet {
  public void doGet(HttpServletRequest request, HttpServletResponse response)
          throws IOException, ServletException {
      HttpSession session = request.getSession();//Con esto lo que hacemos es entrar en la sesion
      User user = (User) session.getAttribute("user");//Cogemos el atributo user dentro de la sesion
      int profileId = Integer.parseInt(request.getParameter("userId"));
      if (user != null) {//comprobamos si en la sesion ya hay algun usuario almacenado

          try(DBManager db = new DBManager()){
            // Obtiene el catálogo de libros desde la base de datos
            //recogemos los valores que tiene el servidor del formulario


                db.unfollow(user.getId(), profileId);//comprobamos si el usuario tiene la misma contraseña que la que han metido ellos
            //habria que poner un mensaje para el usuario para decirle que no existe ese usuario
            //System.out.println("No existe ese usuario");
            response.sendRedirect("profile?id="+profileId);
          }catch (SQLException | NamingException e){
  					e.printStackTrace();
  					response.sendError(500);
  				}
      }
      //Aqui si ya existe el usuario, tendriamos que mostrar un mensaje por pantalla, o redirigir a otra página de error
      //session.sendRedirect("/error");
    }
  }
