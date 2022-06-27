package ru.skillbox.socnetwork.service.storage;

import com.dropbox.core.DbxException;
import com.dropbox.core.DbxRequestConfig;
import com.dropbox.core.oauth.DbxCredential;
import com.dropbox.core.oauth.DbxRefreshResult;
import lombok.Getter;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

@Component
public class StorageTokenUpdate {

  private static DbxCredential credential;

  @Getter
  @Value("${skillbox.app.mail.db.appId}")
  private String appId;

  @Getter
  @Value("${skillbox.app.mail.db.appSecret}")
  private String appSecret;

  @Getter
  @Value("${skillbox.app.mail.db.refreshToken}")
  private String refreshToken;

  public StorageTokenUpdate() {
  }

  @Scheduled(fixedRateString = "PT03H", initialDelayString = "PT3M")
  public void refreshToken() throws DbxException {

    credential = new DbxCredential("", System.currentTimeMillis(), refreshToken, appId, appSecret);

    DbxRequestConfig config = new DbxRequestConfig(appId);
    DbxRefreshResult result = credential.refresh(config);

    credential = new DbxCredential(result.getAccessToken(), result.getExpiresAt(), refreshToken,
            appId, appSecret);

    StorageService.updateToken(result.getAccessToken());
  }
}
