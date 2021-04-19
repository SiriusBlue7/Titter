package twitter;

public class Message{
  private int id;
  private int userId;
  private String text;
  private boolean respuesta;
  private boolean retweet;
  private String shortName;
  private String longName;

  public int getId(){
    return id;
  }

  public int getUserId(){
    return userId;
  }

  public String getText(){
    return text;
  }

  public String getShortName(){
    return shortName;
  }

  public String getLongName(){
    return longName;
  }

  public void setId(int id){
    this.id = id;
  }

  public void setUserId(int id){
    this.userId = id;
  }

  public void setText(String text){
    this.text = text;
  }

  public void setShortName(String name){
    this.shortName = name;
  }


  public void setLongName(String name){
    this.longName = name;
  }
}
