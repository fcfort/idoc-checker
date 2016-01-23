package uo.idoc.imgur;

public class AlbumCreationResponse {
  private final String id;

  // http://imgur.com/a/asdfasdfasdf
  public AlbumCreationResponse(String id, String link) {
    this.id = id;
  }
  
  public String getId() {
    return id;
  }
  
  public String getLink() {    
    return "http://imgur.com/a/" + id;
  }
}
