package bookshop;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.sql.PreparedStatement;
import java.util.ArrayList;
import java.util.List;

import javax.sql.DataSource;
import javax.naming.NamingException;
import javax.naming.InitialContext;
import javax.naming.Context;


public class DBManager implements AutoCloseable {

    private Connection connection;

    public DBManager() throws SQLException, NamingException{
        connect();
    }

   	private void connect() throws SQLException, NamingException {
    	Context initCtx = new InitialContext();
    	Context envCtx = (Context) initCtx.lookup("java:comp/env");
    	DataSource ds = (DataSource) envCtx.lookup("jdbc/BookShop");
    	connection = ds.getConnection();
	}

    /**
     * Close the connection to the database if it is still open.
     *
     */
    public void close() throws SQLException {
        if (connection != null) {
            connection.close();
        }
        connection = null;
    }

    /**
     * Return the number of units in stock of the given book.
     *
     * @param book The book object.
     * @return The number of units in stock, or 0 if the book does not
     *         exist in the database.
     * @throws SQLException If somthing fails with the DB.
     */
    public int getStock(Book book) throws SQLException {
		Book libro= null;
        return getStock(book.getId());
    }

    /**
     * Return the number of units in stock of the given book.
     *
     * @param bookId The book identifier in the database.
     * @return The number of units in stock, or 0 if the book does not
     *         exist in the database.
     */
    public int getStock(int bookId) throws SQLException {

		String query = " SELECT *" + " FROM Inventario WHERE libro='"+ bookId + "'";
		try ( Statement stmt = connection . createStatement () ) {
				  ResultSet rs = stmt . executeQuery ( query );
				  while ( rs . next () ) {
				    int id = rs.getInt("libro");
				    int numero = rs.getInt("numero");
				    //En esta método no definimos el sibn ya que se mete por parametro
					System.out.println("el libro con id : " + bookId + " tiene disponibles "+ numero +" unidades");
				  }
				}
        return 0;
    }


    /**
     * Search book by ISBN.
     *
     * @param isbn The ISBN of the book.
     * @return The Book object, or null if not found.
     * @throws SQLException If somthing fails with the DB.
     */
    public Book searchBook(String isbn) throws SQLException {

        Book libro= null;
        String query = " SELECT *" + " FROM Libros WHERE isbn='" + isbn + "'";
        try ( Statement stmt = connection . createStatement () ) {
          ResultSet rs = stmt . executeQuery ( query );
          while ( rs . next () ) {
            int id = rs.getInt("id");
            String titulo = rs.getString("titulo");
            //En esta método no definimos el sibn ya que se mete por parametro
            int anyo = rs.getInt("anyo");
            float precio = rs.getFloat("precio");

            //Una vez guardados los datos del libro, lo definimos y lo rellenamos
            libro = new Book();
            libro.setId(id);
            libro.setTitle(titulo);
            libro.setIsbn(isbn);
            libro.setYear(anyo);
          }
        }
        return libro;
    }
    /**
     * Sell a book.
     *
     * @param book The book.
     * @param units Number of units that are being sold.
     * @return True if the operation succeeds, or false otherwise
     *         (e.g. when the stock of the book is not big enough).
     * @throws SQLException If somthing fails with the DB.
     */
    public boolean sellBook(Book book, int units) throws SQLException {
        return sellBook(book.getId(), units);
    }

    /**
     * Sell a book.
     *
     * @param book The book's identifier.
     * @param units Number of units that are being sold.
     * @return True if the operation succeeds, or false otherwise
     *         (e.g. when the stock of the book is not big enough).
     * @throws SQLException If something fails with the DB.
     */
    public boolean sellBook(int book, int units) throws SQLException {
        //Lo que tenemos que hacer es
        int unidades;
        String query = " SELECT numero" + " FROM Inventario WHERE libro='" + book + "' FOR UPDATE";
        try ( Statement stmt1 = connection . createStatement () ;
              Statement stmt2 = connection . createStatement ()) {
          ResultSet rs1 = stmt1 . executeQuery ( query );
           unidades = rs1.getInt("numero");

          if (unidades <= 0 || unidades < units) {
            return false;
          }
          unidades = unidades - units;
          String query2 = "UPDATE Inventario" + " SET numero='"+ unidades +"' WHERE libro='" + book + "'";

            ResultSet rs2 = stmt2 . executeQuery ( query2 );
              return true;
            }
    }
  	public User searchUser(int id) throws SQLException{
        String query = "SELECT * FROM Usuarios WHERE id=?";
        User user;
        try(PreparedStatement pst = connection.PreparedStatement(query)){
          pst.setInt(1, id);
          ResultSet rs = pst.executeQuery(query);
          user = new User();
          user.setId(id);
          user.setShort_name(rs.getString("short_name"));
          user.setLong_name(rs.getString("long_name"));
          user.setMail(rs.getString("mail"));
          user.setPassword(rs.getString("password"));
        }
      return user;
  	}

    /**
     * Return a list with all the books in the database.
     *
     * @return List with all the books.
     * @throws SQLException If something fails with the DB.
     */
    public List<Book> listBooks() throws SQLException {
        String query = " SELECT *" + " FROM Libros ";
		ArrayList<Book> listaLibro = new ArrayList<Book>();
      try ( Statement stmt = connection.createStatement () ) {
        ResultSet rs = stmt . executeQuery ( query );
        while ( rs . next () ) {
		//Creamos el libro dentro del While para que no haya conflictos al introducirlo en el ArrayList
		  Book libro = new Book();
          libro.setId(rs.getInt("id"));
          libro.setTitle(rs.getString("titulo"));
          libro.setIsbn(rs.getString("isbn"));
          libro.setYear(rs.getInt("anyo"));
          float precio = rs.getFloat("precio");
			listaLibro.add(libro);

        }
      }
        return listaLibro;
    }
}
