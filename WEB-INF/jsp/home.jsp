
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

     <div class="container col-xl-10 px-4 py-5">
       <form class="p-5 border rounded-3 bg-light" action="newmessage">
         <div class="mb-3">
           <label for="floatingtitle" class="form-label">¿Que esta pasando? </label>
           <textarea class="form-control" name="text" rows="4" cols="70" maxlength="280" id="floatingtitle" placeholder="compartelo..."></textarea>
         </div>
         <input class="btn btn-primary" type="submit" value="twittear">
       </form>
     </div>

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
