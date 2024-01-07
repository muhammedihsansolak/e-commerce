package com.cydeo.aspect;

import org.springframework.security.core.context.SecurityContextHolder;

public class GetUsername {
    protected static String getUsername(){
        return SecurityContextHolder.getContext().getAuthentication().getName();
    }
}
