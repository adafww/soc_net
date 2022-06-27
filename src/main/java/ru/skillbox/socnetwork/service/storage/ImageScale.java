package ru.skillbox.socnetwork.service.storage;

import org.imgscalr.Scalr;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class ImageScale {

  private ImageScale() {}

  public static File resize(InputStream stream, String fileName) throws IOException {
    BufferedImage image = ImageIO.read(stream);
    int size = Math.min(image.getHeight(), image.getWidth());

    int leftOffset = Math.max((image.getWidth() - image.getHeight()) / 2, 0);
    int topOffset = Math.max((image.getHeight() - image.getWidth()) / 2, 0);

    BufferedImage croppedImage = Scalr.crop(image, leftOffset, topOffset, size, size);
    BufferedImage scaledImage = Scalr.resize(croppedImage, 205);

    File newFile = new File("/tmp/" + fileName);
    ImageIO.write(scaledImage, getExtension(fileName), newFile);

    return newFile;
  }

  private static String getExtension(String name){
    Pattern pattern = Pattern.compile(".*(\\.[A-z]*)$");
    Matcher matcher = pattern.matcher(name);
    return (matcher.find()) ? matcher.group(1).substring(1) : "";
  }
}
