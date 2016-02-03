package uo.idoc.integration;

import static org.junit.Assert.*;

import java.io.IOException;

import org.apache.http.ParseException;

import uo.idoc.TestUtils;
import uo.idoc.imgur.ImgurUploader;
import uo.idoc.imgur.LargeImageUploader;

public class LargeImageUploaderIntegrationTest {
  private static final String CLIENT_ID = TestUtils.readImgurClientId();

  public void testAlbumCreation() throws ParseException, IOException {
    LargeImageUploader u = new LargeImageUploader(new ImgurUploader(CLIENT_ID));
    String link = u.uploadImage(TestUtils.readRourceAsImage("diff.png"), "title");
    assertNotNull(link);
    System.out.println(link);
  }

}
