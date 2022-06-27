package ru.skillbox.socnetwork.service.storage;

import lombok.AllArgsConstructor;
import org.redisson.Redisson;
import org.redisson.api.RMap;
import org.redisson.api.RedissonClient;
import org.redisson.config.Config;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import ru.skillbox.socnetwork.model.entity.Person;
import ru.skillbox.socnetwork.repository.PersonRepository;
import ru.skillbox.socnetwork.service.Constants;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

@Component
@AllArgsConstructor
public class StorageCache {

  private final PersonRepository personRepository;
  private static RMap<String, String> cache;

  private static String redissonServer = "redis://127.0.0.1:6379";

  @Scheduled(initialDelayString = "PT5M", fixedDelay=Long.MAX_VALUE)
  private void initCache(){
    RedissonClient redissonClient = connect();
    cache = redissonClient.getMap("image-cache");
    cache.clear();
    cache.put(Constants.PHOTO_DEFAULT_NAME, Constants.PHOTO_DEFAULT_LINK);
    cache.put(Constants.PHOTO_DELETED_NAME, Constants.PHOTO_DELETED_LINK);
    cache.putAll(getPhotos());
  }

  public void addLink(String fileName, String link){
    cache.put(fileName, link);
    }

  public String getLink(String fileName){
    return cache.get(fileName);
  }

  public Boolean isLinkExists(String fileName){
    return cache.containsKey(fileName);
  }

  public void deleteLink(String fileName){
    cache.remove(fileName);
  }

  private static RedissonClient connect(){
    Config config = new Config();
    config.useSingleServer().setAddress(redissonServer);
    return Redisson.create(config);
  }

  private Map<String, String> getPhotos(){
    Map<String, String> photos = new HashMap<>();
    List<Person> personList = personRepository.getAll();
    for(Person person : personList){
      photos.put(getRelativePath(person.getPhoto()), person.getPhoto());
    }
    return photos;
  }

  private String getRelativePath(String path) {
    Pattern pattern = Pattern.compile(".*(/\\w*\\.[A-z]*)\\?raw=1");
    Matcher matcher = pattern.matcher(path);
    return (matcher.find()) ? matcher.group(1) : "";
  }
}
