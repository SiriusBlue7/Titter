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

@WebServlet("/newmessage")
public class Newmessage extends HttpServlet {
  public void doGet(HttpServletRequest request, HttpServletResponse response)
          throws IOException, ServletException {
      // Obtiene el carro de la compra desde la sesión. Lo crea si no existe.
      HttpSession session = request.getSession();//Con esto lo que hacemos es entrar en la sesion
      User user = (User) session.getAttribute("user");//Cogemos el atributo user dentro de la sesion
      String texto = request.getParameter("text");
      java.util.Date utilDate = new java.util.Date();
      Message mensaje = new Message();
       mensaje.setUserId(user.getId());
       mensaje.setText(texto);
       mensaje.setDate(utilDate);

          try(DBManager db = new DBManager()){
            // Obtiene el catálogo de libros desde la base de datos
            //recogemos los valores que tiene el servidor del formulario
            db.addMessage(mensaje);
            //Buscamos en la base de datos el usuario
                //una vez comprobado que todo esta bien, redirigimos a la pagina principal del usuario
              //  response.sendRedirect("/home");
              //como no estamos cambianndo de página, creo que no hace falta q
              response.sendRedirect("home");
          }catch (SQLException | NamingException e){
  					e.printStackTrace();
  					response.sendError(500);
  				}
      //Aqui si ya existe el usuario, tendriamos que mostrar un mensaje por pantalla, o redirigir a otra página de error
      //session.sendRedirect("/error");
    }
  }
