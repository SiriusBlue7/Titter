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


//href="perfil?id=<%= tweet.getUser().getId() %>"
//para coger esto seria rquest.getParameter("id")

@WebServlet("/profile")
public class Profile extends HttpServlet {
    public void doPost(HttpServletRequest request, HttpServletResponse response)
            throws IOException, ServletException {
        // Obtiene el carro de la compra desde la sesión. Lo crea si no existe.
        HttpSession session = request.getSession();
        User user = (User) session.getAttribute("user");

          try(DBManager db = new DBManager()){
            User profile_user = db.searchUser(request.getParameter("id"));
            List<Message> messagesUser = db.listUserMessage(profile_user.getId());

            request.setAttribute("profile_user",profile_user);
            request.setAttribute("messagesUser", messagesUser);
            
            RequestDispatcher rd = request.getRequestDispatcher("/WEB-INF/jsp/profile.jsp");
            rd.forward(request, response);
    			} catch (SQLException | NamingException e){
    					e.printStackTrace();
    					response.sendError(500);
    			}

    }
}
