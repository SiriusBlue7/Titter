package twitter;

public class User{
  private int id;
  private String short_name;
  private String long_name;
  private String mail;
  private String password;
  private String description;

  public int getId(){
    return id;
  }

  public String getShort_name(){
    return short_name;
  }

  public String getLong_name(){
    return long_name;
  }

  public String getMail(){
    return mail;
  }

  public String getPassword(){
    return password;
  }

  public String getDescription(){
    return description;
  }

  public void setId(int id){
    this.id = id;
  }

  public void setShort_name(String short_name){
    this.short_name = short_name;
  }

  public void setLong_name(String long_name ){
    this.long_name = long_name;
  }

  public void setMail(String mail){
    this.mail = mail;
  }

  public void setPassword(String password){
    this.password = password;
  }

  public void setDescription(String description){
    this.description = description;
  }
}
