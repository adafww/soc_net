package ru.skillbox.socnetwork.service;

import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import ru.skillbox.socnetwork.model.entity.Notification;
import ru.skillbox.socnetwork.model.rsdto.NotificationDto;
import ru.skillbox.socnetwork.repository.NotificationRepository;

import java.util.List;

@Service
@AllArgsConstructor
public class NotificationAddService {
    private final NotificationRepository notificationRepository;

    private final SecurityPerson securityPerson = new SecurityPerson();


    public void addNotificationForOnePerson(NotificationDto notificationDto) {
        notificationRepository.addNotification(notificationDto);
    }

    public List<Notification> getAllNotifications(Integer currentUserId) {
        return notificationRepository.getAllNotificationsForPerson(currentUserId);
    }

    public void readAllNotifications(int id, boolean all) {

        if (all) {
            notificationRepository.readAllNotificationByUser(securityPerson.getPersonId());
        } else {
            notificationRepository.readUsersNotificationById(id);
        }
    }
}