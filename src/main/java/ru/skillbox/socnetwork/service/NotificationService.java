package ru.skillbox.socnetwork.service;

import lombok.AllArgsConstructor;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import ru.skillbox.socnetwork.model.entity.Notification;
import ru.skillbox.socnetwork.model.entity.enums.TypeNotificationCode;
import ru.skillbox.socnetwork.model.rsdto.NotificationDto;
import ru.skillbox.socnetwork.model.rsdto.NotificationDtoToView;
import ru.skillbox.socnetwork.model.rsdto.PersonDto;
import ru.skillbox.socnetwork.repository.NotificationSettingsRepository;
import ru.skillbox.socnetwork.security.SecurityUser;

import java.time.Instant;
import java.time.LocalDate;
import java.time.ZoneId;
import java.util.ArrayList;
import java.util.List;

@Service
@AllArgsConstructor
@EnableScheduling
public class NotificationService {

    //private final NotificationRepository notificationRepository;
    private final NotificationAddService notificationAddService;
    private final PersonService personService;
    private final FriendsService friendsService;
    private final NotificationSettingsRepository notificationSettingsRepository;

    private final SecurityPerson securityPerson = new SecurityPerson();

    public void addNotificationForFriends(NotificationDto notificationDto) {

        List<PersonDto> friends = friendsService.getUserFriends();
        for (PersonDto friend : friends) {
            addNotificationForOnePerson(notificationDto, friend.getId());
        }
    }

    public void addNotificationForOnePerson(NotificationDto notificationDto,
                                            Integer destinationId) {

        boolean settingsEnable = notificationSettingsRepository.checkSettingsForNotification
                (notificationDto.getNotificationType(), destinationId);
        if (settingsEnable) {
            notificationDto.setDistUserId(destinationId);
            notificationAddService.addNotificationForOnePerson(notificationDto);
        }

    }

    public List<NotificationDtoToView> getAllNotifications() {

        personService.addOnlinePerson(securityPerson.getPersonId());

        List<Notification> notifications = notificationAddService.getAllNotifications(securityPerson.getPersonId());
        return notificationsToDto(notifications);
    }

    private List<NotificationDtoToView> notificationsToDto(List<Notification> notifications) {
        List<NotificationDto> notificationsDto = new ArrayList<>();
        for (Notification notification : notifications) {
            notificationsDto.add(new NotificationDto(notification));
        }

        return notificationDtoToNotificationViews(notificationsDto);
    }

    private List<NotificationDtoToView> notificationDtoToNotificationViews(List<NotificationDto> notificationsDto) {

        List<NotificationDtoToView> notificationDtoToView = new ArrayList<>();
        for (NotificationDto notification : notificationsDto) {
            notificationDtoToView.add(new NotificationDtoToView(notification,
                    personService.getById(notification.getPersonId()),
                    notification.getTitle()
            ));
        }
        return notificationDtoToView;
    }

    public void readAllNotifications(int id, boolean all) {
        notificationAddService.readAllNotifications(id, all);
    }

    public Integer getPersonId() {
        return securityPerson.getPersonId();
    }

    //@Scheduled(fixedRate = 10000)
    public void checkIfBirthdayOfFriends() {
        System.out.println("Running sheduling method...");

        List<PersonDto> friends = friendsService.getUserFriends();
        for (PersonDto friend : friends) {
            int friendId = friend.getId();
            if (checkIfBirthDayToday(friendId)) {
                //createNotificationAboutBirthdayFriend(friendId);
            }
        }
    }

    private boolean checkIfBirthDayToday(Integer personId) {

        long birthDayDate = personService.getPersonBirthDay(personId).getBirthDate();
        LocalDate birthDayDay = Instant.ofEpochMilli(birthDayDate).atZone(ZoneId.systemDefault())
                .toLocalDate();
        int dayOfBirthday = birthDayDay.getDayOfMonth();
        String monthOfBirthday = birthDayDay.getMonth().toString();


        LocalDate today = Instant.ofEpochMilli(System.currentTimeMillis()).atZone(ZoneId.systemDefault())
                .toLocalDate();
        int dayOfYear = today.getDayOfMonth();
        String month = today.getMonth().toString();

        return (dayOfBirthday == dayOfYear) && (month.equals(monthOfBirthday));
    }

    private void createNotificationAboutBirthdayFriend(Integer personId) {
        NotificationDto notificationDto = new NotificationDto(
                TypeNotificationCode.FRIEND_BIRTHDAY, System.currentTimeMillis(), personId,
                "У вашего друга сегодня день рождения!");
        addNotificationForOnePerson(notificationDto, securityPerson.getPersonId());
    }
}
