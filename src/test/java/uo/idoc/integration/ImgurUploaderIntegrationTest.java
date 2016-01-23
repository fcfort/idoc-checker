package uo.idoc.integration;

import static org.junit.Assert.*;

import java.io.IOException;

import org.apache.http.ParseException;

import com.google.common.collect.Lists;

import uo.idoc.TestUtils;
import uo.idoc.imgur.AlbumCreationResponse;
import uo.idoc.imgur.ImageCreationResponse;
import uo.idoc.imgur.ImgurUploader;

/** Imgur Integration Tests */
public class ImgurUploaderIntegrationTest {

  private static final String CLIENT_ID = "PUT CLIENT ID HERE";

  public void testImageCreate() throws ParseException, IOException {
    ImageCreationResponse resp = new ImgurUploader(CLIENT_ID)
        .uploadImage(TestUtils.readRourceAsImage("One_black_Pixel.png"));
    assertNotNull(resp.getLink());
    System.out.println(resp.getLink());
  }

  public void testAlbumCreate() throws ParseException, IOException {
    ImageCreationResponse imgageCreate = new ImgurUploader(CLIENT_ID)
        .uploadImage(TestUtils.readRourceAsImage("One_black_Pixel.png"));
    AlbumCreationResponse albumCreate = new ImgurUploader(CLIENT_ID)
        .createAlbum(Lists.newArrayList(imgageCreate.getId()));
    assertNotNull(albumCreate.getLink());
    System.out.println(albumCreate.getLink());
  }

}
