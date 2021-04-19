package controllers;
import bookshop.*;//Esto accede a la libreria que hemos creado nosotros en BookShop y podamos acceder a llamarlo

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




@WebServlet("/add")
public class Add extends HttpServlet {
    public void doGet(HttpServletRequest request, HttpServletResponse response)
            throws IOException, ServletException {
        // Obtiene el carro de la compra desde la sesión. Lo crea si no existe.
        HttpSession session = request.getSession();
        List<Book> cart = (List<Book>) session.getAttribute("cart");
        if (cart == null) {
            cart = new ArrayList<Book>();
            session.setAttribute("cart", cart);
        }
        try(DBManager db = new DBManager()){
  				// Obtiene el catálogo de libros desde la base de datos
          String[] lista = request.getParameterValues("book");

          if (lista != null) {//si no hay libros marcados, esto devuelve null
            for(String idstr:lista) {//Esto hace que se recorran todos los elementos de la lista
              int id =Integer.parseInt(idstr);//hacemos que el String se vuelva un Int
              Book libro =db.searchBook(id);//IMPORTANTE: hay que modificar el DBManagerpara que tenga el searchBOOK(int id)
              if (libro!= null) {//Comprobamos que la info del libro este bien metida
                cart.add(libro);//añadimos el libro a la lista que este en la sesion
              }
            }
          }
          response.sendRedirect("/cart");

  				} catch (SQLException | NamingException e){
  					e.printStackTrace();
  					response.sendError(500);
  				}
    }
}
