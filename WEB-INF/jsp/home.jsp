
<%@ page language='java' contentType='text/html;charset=utf-8'%>
<%@ page import='twitter.*' %>
<%@ page import='java.util.List' %>

<!DOCTYPE html>
<html>
  <head>
    <meta charset="utf-8">
    <meta name="viewport" content="width=device-width, initial-scale=1">

    <!-- Bootstrap CSS -->
    <link href="https://cdn.jsdelivr.net/npm/bootstrap@5.0.0-beta3/dist/css/bootstrap.min.css" rel="stylesheet" integrity="sha384-eOJMYsd53ii+scO/bJGFsiCZc+5NDVN2yr8+0RDqr0Ql0h+rP48ckxlpbzKgwra6" crossorigin="anonymous">

    <title>Home</title>
   </head>
   <body class="bg-light">
     <script src="https://cdn.jsdelivr.net/npm/bootstrap@5.0.0-beta3/dist/js/bootstrap.bundle.min.js" integrity="sha384-JEW9xMcG8R+pH31jmWH6WWP0WintQrMb4s7ZOdauHnUtxwoG2vI5DkLtS3qm9Ekf" crossorigin="anonymous"></script>

     <nav class="navbar navbar-expand-lg fixed-top navbar-dark bg-dark" aria-label="Main navigation">
       <div class="container-fluid">
         <a class="navbar-brand">¡Bienvenido de nuevo <% =request.getAttribute("user"); %>!</a>
       </div>
       <form action = logout>
         <input class="btn btn-primary" type="submit" value="cerrar sesion">
       </form>
     </nav>

     <div class="d-flex align-items-center p-3 my-3 rounded shadow-sm">
       <div class="lh-1">
         <h1 class="h6 mb-0 lh-1">¿Que esta pasando?</h1>
       </div>
       <form action="newmessage">
         <div>
           <textarea name="text" rows="5" cols="80" maxlength="280" placeholder="texto aqui..."></textarea>
         </div>
         <input class="btn btn-primary" type="submit" value="twittear">
       </form>
     </div>

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
