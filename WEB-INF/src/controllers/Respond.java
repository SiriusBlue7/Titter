package controllers;
import twitter.*;

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

@WebServlet("/respond")
public class Respond extends HttpServlet {
  public void doGet(HttpServletRequest request, HttpServletResponse response)
          throws IOException, ServletException {

      HttpSession session = request.getSession();//Con esto lo que hacemos es entrar en la sesion
      User user = (User) session.getAttribute("user");//Cogemos el atributo user dentro de la sesion
      //cogemos los elementos del texto que hemos escrito y el id del mensaje para crear el objeto y enviarlo en la funcion
      String texto = request.getParameter("text");
      int id_mensaje = Integer.parseInt(request.getParameter("id_mensaje"));
      //cogemos la hora para crear el mensaje del usuario
      java.util.Date utilDate = new java.util.Date();
      Message mensaje = new Message();
      //creamos mensaje del usuario
      mensaje.setUserId(user.getId());
      mensaje.setText(texto);
      mensaje.setDate(utilDate);

      try(DBManager db = new DBManager()){
        //llamamos a la funcion para que guarde el mensaje en la base de datos
        db.respond(mensaje,id_mensaje);

        if (request.getParameter("id")!=null) {
         int id = Integer.parseInt(request.getParameter("id"));
         response.sendRedirect("profile?id=");//en caso de estar viendo un perfil, volvemos a el
       }else{
         response.sendRedirect("home");//como estamos en el home, que nos vuelvba a llevar ahi
       }
      }catch (SQLException | NamingException e){
				e.printStackTrace();
				response.sendError(500);
			}
    }
  }
