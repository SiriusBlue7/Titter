<%@ page language='java' contentType='text/html;charset=utf-8'%>
<%@ page import='bookshop.Book' %>
<%@ page import='java.util.List' %>

<!DOCTYPE html>
<html>
    <head>
        <title>Catalog</title>
    </head>
    <body>
        <h1>Buy books</h1>
        <form action="add">
<% List<Book> books = (List<Book>) request.getAttribute("books"); %>
<% for (Book book: books) { %>
            <div>
                <label>
                    <input type="checkbox" name="book" value="<%= book.getId() %>">
                    <%= book.getIsbn() %> <%= book.getTitle() %><%= book.getTitle() %>
                </label>
            </div>
<% } %>
            <input type="submit" value="Add to your cart">
        </form>
    </body>
</html>
