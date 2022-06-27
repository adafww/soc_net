package ru.skillbox.socnetwork.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import ru.skillbox.socnetwork.model.entity.NotificationSettings;
import ru.skillbox.socnetwork.model.entity.enums.TypeNotificationCode;
import ru.skillbox.socnetwork.model.rsdto.NotificationSettingsDto;
import ru.skillbox.socnetwork.repository.NotificationSettingsRepository;

import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
public class NotificationSettingsService {
    private final NotificationSettingsRepository notificationSettingsRepository;

    private final SecurityPerson securityPerson = new SecurityPerson();

    public void changeSettingsToNotification(String notificationTypeString, String enableString) {

        TypeNotificationCode notificationType = TypeNotificationCode.valueOf(notificationTypeString);
        Boolean enable = Boolean.valueOf(enableString);
        notificationSettingsRepository.changeNotificationSettings(
                notificationType, enable, securityPerson.getPersonId());
    }

    public List<NotificationSettingsDto> getSettingsForUser() {

        List<NotificationSettings> notificationSettings =
                notificationSettingsRepository.getAllNotificationsSettingsForUser(securityPerson.getPersonId());
        return notificationSettingsToDto(notificationSettings);

    }

    private List<NotificationSettingsDto> notificationSettingsToDto(List<NotificationSettings> notificationSettings) {

        List<NotificationSettingsDto> notificationSettingsDto = new ArrayList<>();
        for (NotificationSettings settings : notificationSettings) {
            notificationSettingsDto.add(new NotificationSettingsDto(settings));
        }
        return notificationSettingsDto;
    }
}
