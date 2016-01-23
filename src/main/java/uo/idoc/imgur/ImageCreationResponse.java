package uo.idoc.imgur;

public class ImageCreationResponse {
  private final String id;
  private final String link;

  public ImageCreationResponse(String id, String link) {
    this.id = id;
    this.link = link;
  }

  public String getLink() {
    return link;
  }

  public String getId() {
    return id;
  }
}
