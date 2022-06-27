package ru.skillbox.socnetwork.service;

public class Constants {

    private Constants() {}

    public static final Long FIFTY_SECONDS_IN_MILLIS = 50_000L;
    public static final int MAX_TAG_LENGTH = 15;
    public static final String POST = "Post";
    public static final String COMMENT = "Comment";
    public static final String MAIL_UPDATE_EMAIL_SUBJECT = "Your email has been changed";
    public static final String MAIL_UPDATE_EMAIL_TEXT = "Your email changed successfully!";
    public static final String MAIL_RECOVER_EMAIL_SUBJECT = "Your SocNetwork Email change link";
    public static final String MAIL_RECOVER_EMAIL_LINK = "/shift-email?token=";
    public static final String MAIL_RECOVER_PASSWORD_SUBJECT = "SocNetwork Password recovery";
    public static final String MAIL_RECOVER_PASSWORD_LINK = "/change-password?token=";
    public static final String MAIL_UPDATE_PASSWORD_SUBJECT = "Your password has been changed";
    public static final String MAIL_UPDATE_PASSWORD_TEXT = "Your password changed successfully! You can now log in with new password!";
    public static final String MAIL_MARK_DELETE_SUBJECT = "Your account will be deleted in 3 days!";
    public static final String MAIL_MARK_DELETE_TEXT = "You requested to delete your account, it will be completely deleted in 3 days!";
    public static final String MAIL_RESTORE_SUBJECT = "Your account restored!";
    public static final String MAIL_RESTORE_TEXT = "Your account was completely restored!";
    public static final String MAIL_DELETE_SUBJECT = "Your account deleted!";
    public static final String MAIL_DELETE_TEXT = "Your account and all your data was completely deleted!";
    public static final String PHOTO_DEFAULT_NAME = "/default.jpg";
    public static final String PHOTO_DEFAULT_LINK = "https://www.dropbox.com/s/ekczqxzi1jw8b0y/default.jpg?raw=1";
    public static final String PHOTO_DELETED_NAME = "/deleted.jpg";
    public static final String PHOTO_DELETED_LINK = "https://www.dropbox.com/s/3l8tr9rii4sq30y/deleted.jpg?raw=1";

    public static final String SENT = "SENT";

    public static final String STRING = "string";
    public static final String TOKEN = "token";
    public static final String NOT_REGISTERED = " not registered";
    public static final String USER_WITH_EMAIL = "User with email ";

    public static final Long MILLISECONDS_IN_YEAR = 31718612432L;

}
