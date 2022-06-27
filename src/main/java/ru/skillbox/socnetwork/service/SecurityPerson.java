package ru.skillbox.socnetwork.service;

import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import ru.skillbox.socnetwork.security.SecurityUser;

public class SecurityPerson {

    public Integer getPersonId() {
        return getSecurityUser().getId();
    }

    public SecurityUser getSecurityUser() {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        return (SecurityUser) auth.getPrincipal();
    }

    public String getEmail() {
        return getSecurityUser().getUsername();
    }
}
