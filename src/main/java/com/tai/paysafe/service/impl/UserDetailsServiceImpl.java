package com.tai.paysafe.service.impl;

import com.tai.paysafe.dto.UserDetailsImpl;
import com.tai.paysafe.entities.User;
import com.tai.paysafe.repository.UserRepository;
import jakarta.transaction.Transactional;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@Slf4j
@Service
public class UserDetailsServiceImpl implements UserDetailsService {

    @Autowired
    public UserRepository userRepository;

    @Override
    @Transactional
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        User user = this.userRepository.findByUsername(username)
                .orElseThrow(() -> new UsernameNotFoundException("User Not Found with username : " + username));

        return UserDetailsImpl.build(user);
    }

    @Transactional
    public UserDetails loadUserByUserId(Long userId) throws UsernameNotFoundException {
        User user = this.userRepository.findById(userId)
                .orElseThrow(() -> {
                    log.error("User Not Found with userid : " + userId);
                    new UsernameNotFoundException("User Not Found with userid : " + userId);
                    return null;
                });

        return UserDetailsImpl.build(user);
    }
}