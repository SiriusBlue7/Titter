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

@WebServlet("/search")
public class Search extends HttpServlet {
    public void doGet(HttpServletRequest request, HttpServletResponse response)
            throws IOException, ServletException {
          HttpSession session = request.getSession();
          String new_user = request.getParameter("search");
          try(DBManager db = new DBManager()){
            User usuario = db.searchUser(new_user);

            if (usuario!=null) {
              int id = usuario.getId();
              response.sendRedirect("profile?id="+ id);
            }
    				} catch (SQLException | NamingException e){
    					e.printStackTrace();
    					response.sendError(500);
    				}

    }
}
