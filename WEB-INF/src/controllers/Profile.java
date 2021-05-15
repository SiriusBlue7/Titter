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

@WebServlet("/profile")
public class Profile extends HttpServlet {
    public void doGet(HttpServletRequest request, HttpServletResponse response)
            throws IOException, ServletException {

        HttpSession session = request.getSession();

        try(DBManager db = new DBManager()){//Creamos el DBManager para usar las funciones

          int id_usuario = Integer.parseInt(request.getParameter("id"));//Cogemos el parametro del perfil para saber el perfil que buscar

          User profile_user = db.searchUser(id_usuario);
          if (profile_user!=null) {//Si el usuario que buscamos existe, preparamos para crear la página
            List<Message> messagesUser = db.listUserMessage(id_usuario);
            boolean prof=true;//el prof identifiica si este es el perfil de nuestro usuario o no, y nos deja editar y nos muestra los botones
            boolean botones = false;//Botones indica si tiene que mostrarnos los posibles botones(Editar, follow, unfollow)
            boolean followed = false;//Indica si el usuario que esta registrado sigue al usuario de la página de perfil
            if(user!=null){
              botones = true;
              if(profile_user.getId() == user.getId()){//Comprobamos si el perfil es el del usuario registrado
                prof = false;
              }else{
                followed = db.followed(user.getId(), profile_user.getId());//Comprueba si el usuario registrado sigue a la persona del perfil
              }
            }

            //Subimos todos los atributos que son importantes para que el jsp se pueda formar
            request.setAttribute("botones", botones);
            request.setAttribute("followed", followed);
            request.setAttribute("profile", prof);
            request.setAttribute("profile_user",profile_user);
            request.setAttribute("messagesUser", messagesUser);

            RequestDispatcher rd = request.getRequestDispatcher("/WEB-INF/jsp/profile.jsp");
            rd.forward(request, response);
          }else{//Si el usuario que buscamos no existe, nos redirige al Homes
            response.sendRedirect("Home");
          }

  			} catch (SQLException | NamingException e){
  					e.printStackTrace();
  					response.sendError(500);
  			}

    }
}
