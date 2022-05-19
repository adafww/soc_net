package ru.skillbox.socnetwork.service;

import lombok.RequiredArgsConstructor;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.dao.DuplicateKeyException;
import org.springframework.stereotype.Service;
import ru.skillbox.socnetwork.exception.InvalidRequestException;
import ru.skillbox.socnetwork.logging.DebugLogs;
import ru.skillbox.socnetwork.model.entity.Tag;
import ru.skillbox.socnetwork.model.rqdto.NewPostDto;
import ru.skillbox.socnetwork.model.rsdto.postdto.PostDto;
import ru.skillbox.socnetwork.repository.Post2TagRepository;
import ru.skillbox.socnetwork.repository.TagRepository;

import java.util.HashSet;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@DebugLogs
public class TagService {

    private static final int MAX_TAG_LENGTH = 15;
    private final TagRepository tagRepository;
    private final Post2TagRepository post2TagRepository;


    public List<String> getPostTags(int postId) {
        return tagRepository
                .getPostTags(postId)
                .stream()
                .map(Tag::getTag)
                .collect(Collectors.toList());
    }

    public void addTag(String tag) throws InvalidRequestException {
        if (tag.length() > MAX_TAG_LENGTH) {
            System.out.println("too long tag, max length is "
                    + MAX_TAG_LENGTH + ", current = " + tag.length());
            throw new InvalidRequestException("too long tag, max length is "
                    + MAX_TAG_LENGTH + ", current = " + tag.length());
        } else {
        tagRepository.addTag(tag);
        }
    }

    public void addTagsFromNewPost(int postId, NewPostDto newPostDto) throws InvalidRequestException {
        List<Tag> tagList = tagRepository.getAllTags();
        List<String> postTags = newPostDto.getTags();
        for (String tag : postTags) {
            if (getTagId(tagList, tag) == -1) {
                this.addTag(tag);
            }
        }
        List<Tag> newTagsList = tagRepository.getAllTags();
        postTags.forEach(tag -> post2TagRepository.addTag2Post(postId, getTagId(newTagsList, tag)));
    }

    public void editOldTags(int postId, NewPostDto newPostDto) throws InvalidRequestException {
        List<Tag> allTags = tagRepository.getAllTags();
        List<Tag> oldTags = tagRepository.getPostTags(postId);
        HashSet<String> postTagsSet = new HashSet<>(newPostDto.getTags());

        for (String tag : postTagsSet) {
            if (getTagId(allTags, tag) == -1) {
                this.addTag(tag);
            }
        }
        List<Tag> newTagsList = tagRepository.getAllTags();
        postTagsSet
                .stream()
                .filter(tag -> getTagId(oldTags, tag) == -1)
                .forEach(tag -> post2TagRepository.addTag2Post(postId, getTagId(newTagsList, tag)));
    }

    private int getTagId(List<Tag> tags, String tag) {
        return tags.stream().filter(t -> t.getTag().equals(tag)).findFirst().orElse(new Tag()).getId();
    }

    public void deletePostTags(int postId) {
        post2TagRepository.deletePostTags(postId);
    }
}
