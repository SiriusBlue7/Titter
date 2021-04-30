
<%@ page language='java' contentType='text/html;charset=utf-8'%>
<%@ page import='twitter.*' %>
<%@ page import='java.util.List' %>

<!DOCTYPE html>
<html>
   <head>
       <title>Home</title>
   </head>
   <body>
       <h1>Inicio</h1>
       <form action = logout>
         <div>
           <input type="submit" value = "cerrar sesion">
        </div>
      </form>
       <form action="newmessage">
           <div>
               <label>
                   <textarea name="text" rows="5" cols="80" maxlength="280" placeholder="¿Que estas pensando?"></textarea>
               </label>
           </div>
           <input type="submit" value="twittear">
       </form>
       <% List<Message> lista = (List<Message>) request.getAttribute("messages"); %>
       <% for (Message mensaje: lista) { %>
            <div>
              <label>
                <p>
                <a href="profile?id=<%= mensaje.getUserId() %>"> <%=mensaje.getShortName()%></a>@<%=mensaje.getLongName()%> - <%=mensaje.getDate()%>
                </p>
                <p>
                  <%= mensaje.getText()%>
                </p>
                <form action=retweet>
                  <imput type ="hidden" name = "mensaje" value = <%= mensaje.getId()%> >
                  <input type="button" value="retweet">
                </form>
              </label>
            </div>
        <% } %>
   </body>
</html>
