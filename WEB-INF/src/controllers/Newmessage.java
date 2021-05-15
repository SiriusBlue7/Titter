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
      String texto = request.getParameter("text");//Cogemos el parametro del texto escrito
      int id = Integer.parseInt(request.getParameter("id"));

      java.util.Date utilDate = new java.util.Date();
      Message mensaje = new Message();//Creamos el mensaje
      //rellenamos los datos del mensaje
       mensaje.setUserId(user.getId());
       mensaje.setText(texto);
       mensaje.setDate(utilDate);

          try(DBManager db = new DBManager()){
            //Lllamamos a la funcion que añade el mensaje
            db.addMessage(mensaje);
            //Una vez enviado el mensaje , nos devuelve al home
            if (id>0){//Si el id estamos en un profile ynos reenvia al perfil en el que estabamos
              response.sendRedirect("profile?=" + id);
            }else{//si el id es menor o igual que cero, es que estamos en el home, por lo que nos reenvia al home
              response.sendRedirect("home");
            }
          }catch (SQLException | NamingException e){
  					e.printStackTrace();
  					response.sendError(500);
  				}
      //Aqui si ya existe el usuario, tendriamos que mostrar un mensaje por pantalla, o redirigir a otra página de error
      //session.sendRedirect("/error");
    }
  }
