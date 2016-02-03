package uo.idoc.imgur;

import java.awt.image.BufferedImage;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.List;

import javax.imageio.ImageIO;

import org.apache.http.Header;
import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.ParseException;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.mime.MultipartEntityBuilder;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.message.BasicHeader;
import org.apache.http.util.EntityUtils;

import com.google.common.base.Joiner;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

public class ImgurUploader {

  private static final String IMAGE_ENDPOINT = "https://api.imgur.com/3/image";
  private static final String ALBUM_ENDPOINT = "https://api.imgur.com/3/album";
  private final Header authorization;
  private final Gson gson;

  public ImgurUploader(String clientId) {
    authorization = new BasicHeader("Authorization", "Client-ID " + clientId);
    gson = new GsonBuilder()
        .registerTypeAdapter(ImageCreationResponse.class, new ImageDataDeserializer<ImageCreationResponse>())
        .registerTypeAdapter(AlbumCreationResponse.class, new ImageDataDeserializer<AlbumCreationResponse>())
        .create();
  }
  
  /**
   * ids[]  optional  The image ids that you want to be included in the album.
title optional  The title of the album
description optional  The description of the album
privacy optional  Sets the privacy level of the album. Values are : public | hidden | secret. Defaults to user's privacy settings for logged in users.
layout  optional  Sets the layout to display the album. Values are : blog | grid | horizontal | vertical
cover optional  The ID of an image that you want to be the cover of the album
   * @param imageIds
   * @return
   * @throws IOException 
   * @throws ClientProtocolException 
   */
  public AlbumCreationResponse createAlbum(List<String> imageIds, String albumTitle)
      throws ClientProtocolException, IOException {
    CloseableHttpClient httpClient = HttpClients.createDefault();

    HttpPost httpPost = new HttpPost(ALBUM_ENDPOINT);
    httpPost.addHeader(authorization);

    HttpEntity entity = MultipartEntityBuilder.create()
        .addTextBody("ids", Joiner.on(",").join(imageIds))
        .addTextBody("title", albumTitle)
        .addTextBody("layout", "grid")
        .build();
    httpPost.setEntity(entity);

    final HttpResponse response = httpClient.execute(httpPost);

    String responseJson = EntityUtils.toString(response.getEntity());
    httpClient.close();
    System.out.println(responseJson);

    return gson.fromJson(responseJson, AlbumCreationResponse.class);
  }
  
  /**
   * image  required  A binary file, base64 data, or a URL for an image. (up to 10MB)
album optional  The id of the album you want to add the image to. For anonymous albums, {album} should be the deletehash that is returned at creation.
type  optional  The type of the file that's being sent; file, base64 or URL
name  optional  The name of the file, this is automatically detected if uploading a file with a POST and multipart / form-data
title optional  The title of the image.
description optional  The description of the image.
   */
  public ImageCreationResponse uploadImage(BufferedImage im) throws ParseException, IOException {
    CloseableHttpClient httpClient = HttpClients.createDefault();
    
    HttpPost httpPost = new HttpPost(IMAGE_ENDPOINT);
    httpPost.addHeader(authorization);

    ByteArrayOutputStream os = new ByteArrayOutputStream();
    ImageIO.write(im, "png", os);
    InputStream is = new ByteArrayInputStream(os.toByteArray());

    HttpEntity entity = MultipartEntityBuilder.create().addBinaryBody("image", is).build();
    httpPost.setEntity(entity);

    final HttpResponse response = httpClient.execute(httpPost);
           
    String responseJson = EntityUtils.toString(response.getEntity());
   
    httpClient.close();
    
    return gson.fromJson(responseJson, ImageCreationResponse.class);        
  }   
}
