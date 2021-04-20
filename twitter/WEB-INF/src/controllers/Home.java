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
public class Catalog extends HttpServlet {
    public void doGet(HttpServletRequest request, HttpServletResponse response)
            throws IOException, ServletException {
        // Obtiene el carro de la compra desde la sesión. Lo crea si no existe.
        HttpSession session = request.getSession();
        User user = (List<Book>) session.getAttribute("user");
        if (cart == null) {
            cart = new ArrayList<Book>();
            session.setAttribute("cart", cart);
        }
        try(DBManager db = new DBManager()){
  				// Obtiene el catálogo de libros desde la base de datos
  				List<Message> messages = db.listMessages(user.getId());
  				System.out.println("Catálogo: se han leído " + books.size() + " libros.");
          request.setAttribute("messages", mesages);
          RequestDispatcher rd = request.getRequestDispatcher("/WEB-INF/jsp/catalog.jsp");
          rd.forward(request, response);
  				} catch (SQLException | NamingException e){
  					e.printStackTrace();
  					response.sendError(500);
  				}
    }
}
