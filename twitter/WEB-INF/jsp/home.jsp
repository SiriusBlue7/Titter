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
        <form action="newmessage">
            <div>
                <label>
                    <textarea name="text" rows="5" cols="80" maxlength="280" placeholder="¿Que estas pensando?"></textarea>
                </label>
            </div>
            <input type="submit" value="twittear">
        </form>

    </body>
</html>
