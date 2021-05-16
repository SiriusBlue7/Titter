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


@WebServlet("/description")
public class Description extends HttpServlet {
    public void doGet(HttpServletRequest request, HttpServletResponse response)
            throws IOException, ServletException {
        HttpSession session = request.getSession();
        User user = (User) session.getAttribute("user");//Obtenemos el atributo user de la sesion
        //Obtenemos la nueva descripcion que se quiere poner el usuario

        try(DBManager db = new DBManager()){//Iniciamos para poder acceder a las funciones del DBManager

          if (request.getParameter("description") != null) {//si la el campo description no esta vacio, procedemos
            String description = request.getParameter("description");//obtenemos la descripcion que nos han dado

            db.changeDescription(description, user.getId());//modificamos el elemento en la base de datos

            user.setDescription(description);//modificamos la nueva descripcio'n en el usuario que tenemos
            
            response.sendRedirect("profile?id="+user.getId());
          }else{//Si el campo description esta vacio, que nos devuelva l perfil en el que estabamos sin cambiar nada
            response.sendRedirect("profile?id="+user.getId());
          }

				} catch (SQLException | NamingException e){
					e.printStackTrace();
					response.sendError(500);
				}
    }
}
