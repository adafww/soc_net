package ru.skillbox.socnetwork.exception;

public enum ExceptionText {

    POST_INCORRECT_CANT_FIND_ID("Incorrect post data, can't find this id "),
    POST_ID_NOT_FOUND_TO_DELETE("No post id found to delete"),
    POST_INCORRECT_AUTHOR_TO_DELETE("You cannot delete this post, you are not the author. Author ID is "),
    POST_INCORRECT_AUTHOR_TO_EDIT("You cannot edit this post, you are not the author. Author ID is "),
    COMMENT_INCORRECT_CANT_FIND_ID("Incorrect comment data, can't find this id "),
    COMMENT_INCORRECT_AUTHOR_TO_DELETE("You cannot delete this comment, you are not the author. Author ID is "),
    COMMENT_INCORRECT_AUTHOR_TO_EDIT("You cannot edit this comment, you are not the author. Author ID is "),
    LIKE_WRONG_TYPE("Bad like type. Required 'Post' or 'Comment' types"),
    TAG_MAX_LENGTH(" symbol's is MAX tag length, current length is "),
    INCORRECT_CAPTCHA("Incorrect captcha, please try again"),
    INCORRECT_PASSWORD("Passwords do not match"),
    INCORRECT_EMAIL("Incorrect email, please try again"),
    WRONG_RECOVERY_LINK("Wrong recovery link"),
    INVALID_RECOVERY_TOKEN("Invalid recovery token"),
    NOT_REGISTERED("User not registered"),
    DEFAULT_ERROR_DESCRIPTION ("This request was denied. " +
            "Please register or use your existing account to get the updated token value and try again."),
    CANT_SEND_A_REQUEST_TO_YOURSELF("You can't send a request to yourself."),
    USERS_ARE_ALREADY_FRIENDS("It is not possible to apply as a friend, because these users are already friends."),
    UNABLE_TO_ADD_BLOCKED_USER("The request is not possible because the specified user is blocked."),
    DUPLICATE_FRIEND_REQUEST("It is not possible to submit a request to add as a friend, " +
            "as it has already been submitted earlier."),
    USERS_ARE_NOT_FRIENDS("Deletion is not possible. No friendly relationship found between the specified user."),
    UNABLE_TO_APPLY_OPERATION_TO_SELF("Cannot apply this operation to itself."),
    UNABLE_TO_DELETE_NON_EXISTENT_FRIEND_REQUEST("Deletion failed. The specified friend request was not found."),
    UNSUCCESSFUL_USER_SEARCH("Request denied. Failed to get user with specified id in database.");

    private final String message;

    ExceptionText(String s) {
        this.message = s;
    }

    public String getMessage() {
        return this.message;
    }
}
