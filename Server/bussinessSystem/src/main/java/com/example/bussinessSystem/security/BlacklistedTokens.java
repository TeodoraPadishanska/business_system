package com.example.bussinessSystem.security;

import org.springframework.stereotype.Component;

import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;

@Component
public class BlacklistedTokens {

    private final Set<String> blacklistedTokens = ConcurrentHashMap.newKeySet();

    public void invalidateToken(String token){
        blacklistedTokens.add(token);
    }

    public boolean isBlacklisted(String token){
        return blacklistedTokens.contains(token);
    }


}
