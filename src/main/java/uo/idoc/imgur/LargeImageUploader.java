package uo.idoc.imgur;

import java.awt.image.BufferedImage;
import java.io.IOException;
import java.util.List;

import org.apache.http.ParseException;

import com.google.common.collect.Lists;

public class LargeImageUploader {

  private final ImgurUploader imgurUploader;
  
  public LargeImageUploader(ImgurUploader imgurUploader) {
    this.imgurUploader = imgurUploader;
  }

  /**  
   * @param im
   * @return An album URL
   * @throws ParseException
   * @throws IOException
   */
  public String uploadImage(BufferedImage im) throws ParseException, IOException {
    
    List<String> imageIds = Lists.newArrayList();
    for(BufferedImage subImage : divideByFour(im)) {
      ImageCreationResponse resp = imgurUploader.uploadImage(subImage);
      System.out.println("Uploading image, got link " + resp.getLink());
      imageIds.add(imgurUploader.uploadImage(subImage).getId());
    }
    
    // Return album URL
    String link = imgurUploader.createAlbum(imageIds).getLink();
    System.out.println("Creating album, got link " + link);
    return link;
  }

  private static List<BufferedImage> divideByFour(BufferedImage im) {
    int width = im.getWidth();
    int height = im.getHeight();           
    return Lists.newArrayList(
        im.getSubimage(0, 0, width/2, height/2),
        im.getSubimage(width/2, 0, width/2, height/2),
        im.getSubimage(0, height/2, width/2, height/2),
        im.getSubimage(width/2, height/2, width/2, height/2)
    );
  }

}
