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


@WebServlet("/conversation")
public class Conversation extends HttpServlet {
    public void doGet(HttpServletRequest request, HttpServletResponse response)
            throws IOException, ServletException {
        HttpSession session = request.getSession();
        int room;
        int number;
        if (session.getAttribute("room")==null) {
          room = 3;
          session.setAttribute("room",room);
        }
        if (session.getAttribute("number")==null) {
          number = 0;
          session.setAttribute("number",number);
        }
         room = (int) session.getAttribute("room");
         number = (int) session.getAttribute("number");

         room = 3;

          try(DBManager db = new DBManager()){//Iniciamos para poder acceder a las funciones del DBManager
            int idMensaje = Integer.parseInt(request.getParameter("id_mensaje"));
            //Obtenemos el id del mensaje principal
            number = idMensaje;
            Message principalMessage = db.searchMessage(idMensaje);
            //Obtenemos el mensaje priuncipal por medio de buscarlo por su id
    				List<Message> messages = db.listAnswer(idMensaje);
            //obtenemos todos los mensajes que estan respondiendo l mensaje principal

            //guardamos los dos objetos en la sesion para poder llamarlos luego en el jsp
            request.setAttribute("principalMessage", principalMessage);
            request.setAttribute("messages", messages);

            RequestDispatcher rd = request.getRequestDispatcher("/WEB-INF/jsp/conversation.jsp");
            rd.forward(request, response);
    				} catch (SQLException | NamingException e){
    					e.printStackTrace();
    					response.sendError(500);
    				}
    }
}
