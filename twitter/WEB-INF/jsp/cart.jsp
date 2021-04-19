<%@ page language='java' contentType='text/html;charset=utf-8'%>
<%@ page import='bookshop.Book' %>
<%@ page import='java.util.List' %>

<!DOCTYPE html>
<html>
    <head>
        <title>Cart</title>
    </head>
    <body>
        <h1>Shopping cart</h1>
<% List<Book> carts = (List<Book>)session.getAttribute("cart"); %>
<table>
<% for (Book book: carts) { %>
            <tr>
                  <td><%= book.getIsbn() %></td> <td><%= book.getTitle() %></td>
            </tr>
<% } %>
</table>
            <input type="submit" value="Checkout">

    </body>
</html>
