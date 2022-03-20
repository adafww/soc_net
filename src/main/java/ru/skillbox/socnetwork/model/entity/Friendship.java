package ru.skillbox.socnetwork.model.entity;

import lombok.Data;

@Data
public class Friendship {
  int id;
  int statusId;
  int srcPersonId;
  int dstPersonId;
}