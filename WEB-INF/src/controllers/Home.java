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


@WebServlet("/home")
public class Home extends HttpServlet {
    public void doGet(HttpServletRequest request, HttpServletResponse response)
            throws IOException, ServletException {
        HttpSession session = request.getSession();
        User user = (User) session.getAttribute("user");//Obtenemos el atributo user de la sesion
        int room;
        int number;
        if (session.getAttribute("room")==null) {
          room = 1;
          session.setAttribute("room",room);
        }
        if (session.getAttribute("number")==null) {
          number = 0;
          session.setAttribute("number",number);
        }
         room = (int) session.getAttribute("room");
         number = (int) session.getAttribute("number");

         room = 1;

        if (user != null) {//Comprobamos si el atributo ya ha sido inicializado
          //si lo esta, procedemos aprerpara los datos para el jsp
          try(DBManager db = new DBManager()){
    				List<Message> messages = db.listMessages(user.getId());
            request.setAttribute("messages", messages);

            RequestDispatcher rd = request.getRequestDispatcher("/WEB-INF/jsp/home.jsp");
            rd.forward(request, response);
    				} catch (SQLException | NamingException e){
    					e.printStackTrace();
    					response.sendError(500);
    				}
        }else{//Sino,se nos reenvia al inicio de sesion
          response.sendRedirect("iniciosesion.html");
        }

    }
}
