package ru.skillbox.socnetwork.service.storage;

import com.dropbox.core.DbxException;
import com.dropbox.core.DbxRequestConfig;
import com.dropbox.core.v2.DbxClientV2;
import com.dropbox.core.v2.files.FileMetadata;
import com.dropbox.core.v2.files.WriteMode;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.apache.commons.text.RandomStringGenerator;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import ru.skillbox.socnetwork.logging.DebugLogs;
import ru.skillbox.socnetwork.model.entity.Person;
import ru.skillbox.socnetwork.model.rsdto.filedto.FileUploadDTO;
import ru.skillbox.socnetwork.repository.PersonRepository;
import ru.skillbox.socnetwork.security.SecurityUser;
import ru.skillbox.socnetwork.service.Constants;
import ru.skillbox.socnetwork.service.LocalFileService;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

@Service
@RequiredArgsConstructor

public class StorageService {

  @Getter
  @Value("${skillbox.app.logRootPath}")
  private String localRootPath;
  private static String token = "";
  private static final DbxRequestConfig config = DbxRequestConfig.newBuilder("socNet").build();
  private static DbxClientV2 client = new DbxClientV2(config, token);
  private final PersonRepository personRepository;
  private final LocalFileService localFileService;
  private final StorageCache cache;

  public String getDefaultProfileImage(){
    return cache.getLink(Constants.PHOTO_DEFAULT_NAME);
  }

  public String getDeletedProfileImage() {
    return cache.getLink(Constants.PHOTO_DELETED_NAME);
  }

  @Scheduled(cron = "${skillbox.app.cronUploadLogFiles}")
  public void uploadLogFiles() throws IOException, DbxException {
    List<File> logFiles = localFileService.getAllFilesInADirectory(getLocalRootPath());

    for (File file : logFiles) {
      try (InputStream in = new FileInputStream(file)) {
        client.files().uploadBuilder(file.getPath()).withMode(WriteMode.OVERWRITE).uploadAndFinish(in);
      }
    }

    localFileService.deleteLocalFilesInADirectory(getLocalRootPath());
  }

  @Scheduled(cron = "${skillbox.app.cronDeleteLogFolderInRemoteStorage}")
  public void deleteLogFolderInRemoteStorage() throws DbxException {
    deleteFile(getLocalRootPath());
  }

  public FileUploadDTO uploadFile(MultipartFile file) throws IOException, DbxException {
    if(file == null) return new FileUploadDTO();

    Person person = null;
    FileMetadata fileMetadata = null;

      //Generate new random file name
      String fileName = "/".concat(generateName(file.getOriginalFilename()));

      File resizedImage = ImageScale.resize(file.getInputStream(), fileName);
      InputStream stream = new FileInputStream(resizedImage);
      fileMetadata = client.files().uploadBuilder(fileName).withMode(WriteMode.ADD).uploadAndFinish(stream);
      stream.close();

      Files.delete(resizedImage.toPath());

      //Share image if not shared yet
      if (client.sharing().listSharedLinksBuilder().withPath(fileName).start().getLinks().isEmpty()) {
        client.sharing().createSharedLinkWithSettings(fileMetadata.getPathDisplay());
      }

      //Get current user
      Authentication auth = SecurityContextHolder.getContext().getAuthentication();
      SecurityUser securityUser = (SecurityUser) auth.getPrincipal();
      person = personRepository.getByEmail(securityUser.getUsername());

      deleteFile(getRelativePath(person.getPhoto()));

      cache.addLink(fileName, getAbsolutePath(fileName));
      cache.deleteLink(getRelativePath(person.getPhoto()));
      person.setPhoto(getAbsolutePath(fileName));
      personRepository.updatePhotoByEmail(person);

    return new FileUploadDTO(person, fileMetadata);
  }

  public void deleteFile(String path) throws DbxException {
    if (!path.equals(Constants.PHOTO_DEFAULT_NAME) && !path.equals(Constants.PHOTO_DELETED_NAME)) {
      client.files().deleteV2(path);
    }
  }

  public static void updateToken(String newToken) {
    token = newToken;
    client = new DbxClientV2(config, token);
  }

  public String getRelativePath(String path) {
    Pattern pattern = Pattern.compile(".*(/\\w*\\.[A-z]*)\\?raw=1");
    Matcher matcher = pattern.matcher(path);
    return (matcher.find()) ? matcher.group(1) : "";
  }

  private String getAbsolutePath(String path) throws DbxException {
    if(cache.isLinkExists(path)){
      return cache.getLink(path);
    }
    return client.sharing().listSharedLinksBuilder().withPath(path).start()
            .getLinks().get(0).getUrl().replace("dl=0", "raw=1");
  }

  private String generateName(String name) {
    RandomStringGenerator generator = new RandomStringGenerator.Builder().withinRange(65, 90).build();
    Pattern pattern = Pattern.compile(".{1,10}(\\.[A-z]{3,5})$");
    Matcher matcher = pattern.matcher(name);
    String format = (matcher.find()) ? matcher.group(1) : "";
    return generator.generate(10) + format;
  }
}