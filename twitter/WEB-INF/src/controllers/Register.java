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


@WebServlet("/register")
public class Register extends HttpServlet {
    public void doPost(HttpServletRequest request, HttpServletResponse response)
            throws IOException, ServletException {
        // Obtiene el carro de la compra desde la sesión. Lo crea si no existe.
        HttpSession session = request.getSession();

      				// Obtiene el catálogo de libros desde la base de datos
              String short_name = request.getParameter("short_name");
              String long_name = request.getParameter("long_name");
              String mail = request.getParameter("mail");
              String password = request.getParameter("password1");
              String password2 = request.getParameter("password2");
              try(DBManager db = new DBManager()){
        				int resultado = db.isDataFree(short_name, long_name,mail);

          				if (resultado == 0) {
                    //creamos el usuario que vamos a guardar en la
                    User new_user= new User();
                    new_user.setShort_name(short_name);
                    new_user.setLong_name(long_name);
                    new_user.setMail(mail);
                    new_user.setPassword(password);

                    db.addUser(new_user);//Guardamos el nuevo usuario en la BD

                    session.setAttribute("user", new_user);//Automaticamente iniciamos sesion en la aplicacion
                    //una vez comprobado que todo esta bien, redirigimos a la pagina principal del usuario
                    response.sendRedirect("home");
                  }
      			   } catch (SQLException | NamingException e){
      					e.printStackTrace();
      					response.sendError(500);
      			}
    }
}
