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

@WebServlet("/follow")
public class Follow extends HttpServlet {
  public void doGet(HttpServletRequest request, HttpServletResponse response)
          throws IOException, ServletException {
      HttpSession session = request.getSession();//Con esto lo que hacemos es entrar en la sesion
      User user = (User) session.getAttribute("user");//Cogemos el atributo user dentro de la sesion
      int profileId = Integer.parseInt(request.getParameter("userId"));
      //obtenemos el parametro que se nos pasa con el id del usuario al que queremos seguir
      try(DBManager db = new DBManager()){//Creamos la clase DBManager para poder acceder a sus funciones
        //llamamos a la funcion que se encarga de añadir en la BD la línea que indica que se siguen
        db.follow(user.getId(), profileId);
        //Una vez terminado, nos redirige a la página de perfil en la que estabamos
        response.sendRedirect("profile?id="+profileId);
      }catch (SQLException | NamingException e){
				e.printStackTrace();
				response.sendError(500);
			}
    }
  }
