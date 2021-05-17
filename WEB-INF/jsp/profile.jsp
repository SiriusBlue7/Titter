
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
         <a class="navbar-brand" href="home">Atras</a>
       </div>
        <form class="d-flex" action="search" >
          <input class="form-control me-2" type="search" placeholder="Search" aria-label="Search" name="search">
          <button class="btn btn-primary" type="submit">Search</button>
        </form>
       <form action = logout>
         <input class="btn btn-primary" type="submit" value="cerrar sesion">
       </form>
      </nav>

     <section class="py-5 text-center container">
       <div class="row py-lg-5">
         <div class="col-lg-6 col-md-8 mx-auto">
           <% User user = (User) request.getAttribute("profile_user"); %>
           <h1 class="fw-light">Perfil de <%= user.getLong_name() %></h1>
           <h3 class="fw-light">@<%= user.getShort_name() %></h3>
           <p class="lead text-muted">biografia</p>
           <%if(user.getDescription()!=null){%>
           <p class="lead text-muted"><%= user.getDescription() %></p>
           <% } %>
           <% boolean prof = (boolean)request.getAttribute("profile"); %>
           <% if( prof == false) { %>
           <form action = "description">
             <p>
               <div class="mb-3">
                 <label for="floatingtitle" class="form-label">¿Que quieres compartir con el mundo? </label>
                 <textarea class="form-control" name="text" rows="4" cols="70" maxlength="280" id="floatingtitle" placeholder="compartelo..."></textarea>
               </div>
               <input class="btn btn-primary" type="submit" value="Editar">
             </p>
           </form>
           <% } %>
           <% if(prof == true) { %>
             <% if((boolean)request.getAttribute("botones")==true){%>
               <% if((boolean)request.getAttribute("followed") == false){ %>
               <form action = follow>
                 <p>
                   <input type="hidden" name="userId" value="<%= user.getId()%>" >
                   <input class="btn btn-primary" type="submit" value="Seguir">
                 </p>
               </form>

               <% } %>
               <% if((boolean) request.getAttribute("followed") == true){ %>
               <form action = unfollow? >
                 <p>
                   <input type="hidden" name="userId" value="<%= user.getId()%>" >
                   <input class="btn btn-primary" type="submit" value="Dejar de seguir">
                 </p>
               </form>

               <% } %>
             <% } %>
           <% } %>
         </div>
       </div>
     </section>

     <% User us = (User) session.getAttribute("user"); %>
     <% if(us != null){ %>
     <div class="container col-xl-10 px-4 py-5">
       <form class="p-5 border rounded-3 bg-light" action="newmessage">
         <div class="mb-3">
           <label for="floatingtitle" class="form-label">¿Que esta pasando? </label>
           <textarea class="form-control" name="text" rows="4" cols="70" maxlength="280" id="floatingtitle" placeholder="compartelo..."></textarea>
         </div>
         <input class="btn btn-primary" type="submit" value="twittear">
       </form>
     </div>
     <% } %>

     <div class="my-3 p-3 bg-body rounded shadow-sm">
       <h6 class="border-bottom pb-2 mb-0">Ultimos mensajes</h6>
       <% List<Message> lista = (List<Message>) request.getAttribute("messagesUser"); %>
       <% for (Message mensaje: lista) { %>

       <div class="d-flex text-muted pt-3">
         <svg class="bd-placeholder-img flex-shrink-0 me-2 rounded" width="32" height="32" xmlns="http://www.w3.org/2000/svg" role="img" aria-label="Placeholder: 32x32" preserveAspectRatio="xMidYMid slice" focusable="false"><title>Placeholder</title><rect width="100%" height="100%" fill="#007bff"/><text x="50%" y="50%" fill="#007bff" dy=".3em">32x32</text></svg>
         <p class="pb-3 mb-0 small lh-sm border-bottom" action="profile" method="post">
           <strong class="d-block text-gray-dark"><a href="profile?id=<%= mensaje.getUserId() %>" method=><%=mensaje.getShortName()%></a>@<%=mensaje.getLongName()%></strong>
           <small calss="text-muted"><%=mensaje.getDate()%></small>
           <%= mensaje.getText()%>
         </p>
       </div>

       <% if(us != null){ %>
         <div class="mb-3">
          <form action="respond">
             <textarea class="form-control" name="text" rows="2" cols="35" maxlength="280" id="floatingtitle" placeholder="responde a este mensaje"></textarea>
             <% if(mensaje.getRespuesta()>0){ %>
              <input type="hidden" name="id_mensaje" value="<%= mensaje.getRespuesta()%>" >
             <%}else{%>
              <input type="hidden" name="id_mensaje" value="<%= mensaje.getId()%>" >
             <%}%>
             <input type="submit" class="btn-sm btn-outline-primary" name="respuesta" id="<%= mensaje.getId()%>" value="responder" >
           </form>
         </div>
         <div class="btn-group btn-group" role="group" aria-label="retweet">
           <form action="retweet">
             <input type="hidden" name="id_mensaje" value="<%= mensaje.getId()%>" >
             <button type="submit" class="btn-sm btn-outline-primary">retweet</button>
           </form>
         </div>
       <% } %>

       <div class="btn-group btn-group" role="group" aria-label="retweet">
         <% if(mensaje.getRespuesta() > 0){ %>
           <form action="conversation">
             <input type="hidden" name="id_mensaje" value="<%= mensaje.getRespuesta()%>" >
             <button type="submit" class="btn-sm btn-outline-primary">Conversacion</button>
           </form>
         <% }else{ %>
           <form action="conversation">
             <input type="hidden" name="id_mensaje" value="<%= mensaje.getId()%>" >
             <button type="submit" class="btn-sm btn-outline-primary">Conversacion</button>
           </form>
         <% } %>
       </div>
       <% } %>
     </div>

   </body>
</html>
