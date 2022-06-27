package ru.skillbox.socnetwork.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.dao.DataAccessException;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import ru.skillbox.socnetwork.exception.ExceptionText;
import ru.skillbox.socnetwork.exception.InvalidRequestException;
import ru.skillbox.socnetwork.logging.DebugLogs;
import ru.skillbox.socnetwork.model.entity.Friendship;
import ru.skillbox.socnetwork.model.entity.Person;
import ru.skillbox.socnetwork.model.entity.enums.TypeCode;
import ru.skillbox.socnetwork.model.entity.enums.TypeNotificationCode;
import ru.skillbox.socnetwork.model.entity.enums.TypeReadStatus;
import ru.skillbox.socnetwork.model.rqdto.UserIdsDto;
import ru.skillbox.socnetwork.model.rsdto.FriendshipPersonDto;
import ru.skillbox.socnetwork.model.rsdto.NotificationDto;
import ru.skillbox.socnetwork.model.rsdto.PersonDto;
import ru.skillbox.socnetwork.repository.FriendshipRepository;
import ru.skillbox.socnetwork.repository.PersonRepository;
import ru.skillbox.socnetwork.security.SecurityUser;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Slf4j
@Service
@RequiredArgsConstructor

public class FriendsService {

    private final PersonRepository personRepository;
    private final FriendshipRepository friendshipRepository;
    private final NotificationAddService notificationAddService;

    private SecurityUser getAuthorizedUser() {
        return (SecurityUser) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
    }

    public List<PersonDto> getListRecommendedFriends() {
        String email = getAuthorizedUser().getUsername();
        return personRepository.getListRecommendedFriends(email).stream()
                .map(PersonDto::new)
                .collect(Collectors.toList());
    }

    public List<PersonDto> getUserFriends() {
        String email = getAuthorizedUser().getUsername();
        return personRepository.getUserFriends(email).stream()
                .map(PersonDto::new)
                .collect(Collectors.toList());
    }

    public void deleteFriendById(Integer friendId) throws InvalidRequestException {
        String email = getAuthorizedUser().getUsername();
        Integer authorizedUserId = personRepository.getByEmail(email).getId();
        int countFrom = friendshipRepository.removeFriendlyStatusByPersonIdsAndCode(authorizedUserId, friendId,
                TypeCode.FRIEND.toString());
        int countTo = friendshipRepository.removeFriendlyStatusByPersonIdsAndCode(friendId, authorizedUserId,
                TypeCode.FRIEND.toString());
        if (countFrom == 0 && countTo == 0) {
            throw new InvalidRequestException(ExceptionText.USERS_ARE_NOT_FRIENDS.getMessage());
        }
    }

    public void addFriendById(Integer focusPersonId) throws InvalidRequestException {
        String email = getAuthorizedUser().getUsername();
        Integer authorizedUserId = personRepository.getByEmail(email).getId();
        if (authorizedUserId.equals(focusPersonId)) {
            throw new InvalidRequestException(ExceptionText.CANT_SEND_A_REQUEST_TO_YOURSELF.getMessage());
        }

        try {
            personRepository.getById(focusPersonId);
            Optional<Friendship> friendshipFromInitiator = friendshipRepository
                    .getFriendlyStatusByPersonIds(authorizedUserId, focusPersonId);
            Optional<Friendship> friendshipFromFocusPerson = friendshipRepository
                    .getFriendlyStatusByPersonIds(focusPersonId, authorizedUserId);

            if (friendshipFromInitiator.isPresent() || friendshipFromFocusPerson.isPresent()) {
                Friendship friendshipInitiator = friendshipFromInitiator.orElse(Friendship.getWithIncorrectId());
                Friendship friendshipFocusPerson = friendshipFromFocusPerson.orElse(Friendship.getWithIncorrectId());

                checkExistingFriendships(friendshipInitiator, friendshipFocusPerson, focusPersonId, authorizedUserId);
            } else {
                Friendship friendship = friendshipRepository.createFriendlyStatusByPersonIdsAndCode(authorizedUserId,
                        focusPersonId, TypeCode.REQUEST.toString());

                sendFriendRequestNotification(authorizedUserId, friendship, focusPersonId);
            }
        } catch (DataAccessException ex) {
            throw new InvalidRequestException(ExceptionText.UNSUCCESSFUL_USER_SEARCH.getMessage());
        }
    }

    private void checkExistingFriendships(Friendship friendshipInitiator, Friendship friendshipFocusPerson,
                                          Integer focusPersonId, Integer authorizedUserId)
            throws InvalidRequestException {
        if ((friendshipInitiator.getId() != -1 && friendshipInitiator.getCode() == TypeCode.FRIEND) ||
                (friendshipFocusPerson.getId() != -1 && friendshipFocusPerson.getCode() == TypeCode.FRIEND)) {
            throw new InvalidRequestException(ExceptionText.USERS_ARE_ALREADY_FRIENDS.getMessage());

        } else if ((friendshipInitiator.getId() != -1 && friendshipInitiator.getCode() == TypeCode.BLOCKED) ||
                (friendshipFocusPerson.getId() != -1 && friendshipFocusPerson.getCode() == TypeCode.BLOCKED)) {
            throw new InvalidRequestException(ExceptionText.UNABLE_TO_ADD_BLOCKED_USER.getMessage());

        } else if (friendshipInitiator.getId() != -1 && friendshipInitiator.getCode() == TypeCode.REQUEST) {
            throw new InvalidRequestException(ExceptionText.DUPLICATE_FRIEND_REQUEST.getMessage());

        } else if (friendshipFocusPerson.getId() != -1 && friendshipFocusPerson.getCode() == TypeCode.REQUEST) {
            friendshipRepository.updateFriendlyStatusByPersonIdsAndCode(focusPersonId, authorizedUserId,
                    TypeCode.FRIEND.toString());
        }
    }

    private void sendFriendRequestNotification(Integer authorizedUserId, Friendship friendship, Integer focusPersonId) {
        Person focusPerson = personRepository.getById(focusPersonId);
        NotificationDto notificationDto = new NotificationDto(TypeNotificationCode.FRIEND_REQUEST,
                System.currentTimeMillis(), authorizedUserId, friendship.getId(), focusPersonId,
                TypeReadStatus.SENT, focusPerson.getFirstName().concat(" ").concat(focusPerson.getLastName()));

        notificationAddService.addNotificationForOnePerson(notificationDto);
    }

    public List<PersonDto> getListIncomingFriendRequests() {
        String email = getAuthorizedUser().getUsername();
        return personRepository.getListIncomingFriendRequests(email).stream()
                .map(PersonDto::new)
                .collect(Collectors.toList());
    }

    public List<FriendshipPersonDto> getInformationAboutFriendships(UserIdsDto userIdsDto) {
        String email = getAuthorizedUser().getUsername();
        List<Integer> userIds = userIdsDto.getUserIds();
        return friendshipRepository.getInformationAboutFriendships(email, userIds);
    }

    public void deleteFriendRequestByPersonId(Integer srcPersonId) throws InvalidRequestException {
        String email = getAuthorizedUser().getUsername();
        Integer authorizedUserId = personRepository.getByEmail(email).getId();
        if (srcPersonId.equals(authorizedUserId)) {
            throw new InvalidRequestException(ExceptionText.UNABLE_TO_APPLY_OPERATION_TO_SELF.getMessage());
        }

        int count = friendshipRepository.removeFriendlyStatusByPersonIdsAndCode(srcPersonId, authorizedUserId,
                TypeCode.REQUEST.toString());
        if (count == 0) {
            throw new InvalidRequestException(ExceptionText.UNABLE_TO_DELETE_NON_EXISTENT_FRIEND_REQUEST.getMessage());
        }
    }
}
