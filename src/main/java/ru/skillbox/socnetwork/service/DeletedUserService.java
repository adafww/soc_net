package ru.skillbox.socnetwork.service;

import com.dropbox.core.DbxException;
import lombok.RequiredArgsConstructor;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;
import ru.skillbox.socnetwork.model.entity.DeletedUser;
import ru.skillbox.socnetwork.model.entity.Person;
import ru.skillbox.socnetwork.model.entity.PostFile;
import ru.skillbox.socnetwork.repository.*;
import ru.skillbox.socnetwork.service.storage.StorageService;


import java.util.List;

@Component
@RequiredArgsConstructor
public class DeletedUserService {

  private final DeletedUsersRepository deletedUsersRepository;
  private final PersonRepository personRepository;
  private final CommentLikeRepository commentLikeRepository;
  private final MessageRepository messageRepository;
  private final NotificationRepository notificationRepository;
  private final FriendshipRepository friendshipRepository;
  private final PostLikeRepository postLikeRepository;
  private final PostFileRepository postFileRepository;
  private final PostCommentRepository postCommentRepository;
  private final Post2TagRepository post2TagRepository;
  private final PostRepository postRepository;
  private final StorageService storageService;
  private final MailService mailService;

  @Scheduled(fixedRateString = "PT10M")
  public void checkExpiredUsers() throws DbxException {
    List<DeletedUser> expiredUsers = deletedUsersRepository.getAllExpired();
    if(!expiredUsers.isEmpty()){
      for(DeletedUser user : expiredUsers){
        deletePersonData(user.getPersonId());
        storageService.deleteFile(storageService.getRelativePath(user.getPhoto()));
        deletedUsersRepository.delete(user.getId());
      }
    }
  }

  public void add(Person person){
    deletedUsersRepository.addDeletedUser(person);
    personRepository.setDeleted(person.getId(), true);
  }

  @Transactional
  public void deletePersonData(Integer personId) throws DbxException {
    commentLikeRepository.deleteAllPersonCommentLikes(personId);
    commentLikeRepository.deleteAllCommentLikesFromPersonComments(personId);

    messageRepository.deleteAllPersonMessages(personId);

    notificationRepository.deleteAllPersonNotifications(personId);

    friendshipRepository.deleteAllPersonFriendships(personId);

    postLikeRepository.deleteAllPersonPostLikes(personId);
    postLikeRepository.deleteAllPostLikesFromPersonPosts(personId);

    List<PostFile> postFiles = postFileRepository.getAllPersonFiles(personId);
    for(PostFile file : postFiles){
      storageService.deleteFile(storageService.getRelativePath(file.getPath()));
    }
    postFileRepository.deleteAllPersonFiles(personId);

    postCommentRepository.deleteAllPersonComments(personId);
    postCommentRepository.deleteAllPersonPostsComments(personId);

    post2TagRepository.deleteAllPersonTags(personId);

    postRepository.deleteAllPersonPosts(personId);

    String email = personRepository.getById(personId).getEmail();
    personRepository.delete(personId);
    mailService.send(email, Constants.MAIL_DELETE_SUBJECT, Constants.MAIL_DELETE_TEXT);
  }

  public DeletedUser getDeletedUser(Integer personId){
    return deletedUsersRepository.getDeletedUser(personId);
  }

  public void delete(Integer personId){
    deletedUsersRepository.delete(personId);
  }
}
