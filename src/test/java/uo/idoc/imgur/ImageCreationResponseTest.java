package uo.idoc.imgur;

import static org.junit.Assert.*;

import org.junit.Test;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

public class ImageCreationResponseTest {

  @Test
  public void testImageCreationDeserialization() {
    String json = "{\"data\":{\"id\":\"a3rfasdf\",\"title\":null,\"description\":null,\"datetime\":1453524538,"
        + "\"type\":\"image\\/png\",\"animated\":false,\"width\":1,\"height\":1,\"size\":69,\"views\":0,\""
        + "bandwidth\":0,\"vote\":null,\"favorite\":false,\"nsfw\":null,\"section\":null,\"account_url\":null"
        + ",\"account_id\":0,\"comment_preview\":null,\"deletehash\":\"f3rasdfa\",\"name\":\"\",\"link\""
        + ":\"http:\\/\\/i.imgur.com\\/a3rfasdf.png\"},\"success\":true,\"status\":200}";
    System.out.println(json);
    Gson g = new GsonBuilder()
        .registerTypeAdapter(ImageCreationResponse.class, new ImageDataDeserializer<ImageCreationResponse>()).create();
    ImageCreationResponse r = g.fromJson(json, ImageCreationResponse.class);

    assertEquals("a3rfasdf", r.getId());
    assertEquals("http://i.imgur.com/a3rfasdf.png", r.getLink());
  }

}
