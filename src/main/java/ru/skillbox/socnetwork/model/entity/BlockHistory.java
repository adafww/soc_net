package ru.skillbox.socnetwork.model.entity;

import lombok.Data;
import ru.skillbox.socnetwork.model.entity.enums.TypeAction;

@Data
public class BlockHistory {
    private Integer id;
    private Long time;
    private Integer personId;
    private Integer postId;
    private Integer commentId;
    private TypeAction action;
}
