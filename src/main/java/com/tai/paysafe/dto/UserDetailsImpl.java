package com.tai.paysafe.dto;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Objects;
import com.tai.paysafe.entities.User;
import lombok.Getter;
import lombok.Setter;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import com.fasterxml.jackson.annotation.JsonIgnore;

public class UserDetailsImpl implements UserDetails {

    private static final long serialVersionUID = 1L;
    @Getter
    @Setter
    private Long id;
    @Getter
    @Setter
    private String username;
    @JsonIgnore
    @Getter
    @Setter
    private String password;
    @Getter
    @Setter
    private String phoneNumber;
    @Getter
    @Setter
    public Collection<? extends GrantedAuthority> authorities;

    public UserDetailsImpl(
            Long id,
            String username,
            String phoneNumber,
            String password,
            Collection<? extends GrantedAuthority> authorities) {
        this.id = id;
        this.username = username;
        this.phoneNumber = phoneNumber;
        this.password = password;
        this.authorities = authorities;
    }

    public static UserDetailsImpl build(User user) {
        List<GrantedAuthority> authorities = new ArrayList<>();
        authorities.add(new SimpleGrantedAuthority(user.getRole()));
//                user.getRoles().stream()
//                .map(role -> new SimpleGrantedAuthority(user.getRole()))
//                .collect(Collectors.toList());

        return new UserDetailsImpl(
                user.getId(),
                user.getUsername(),
                user.getPhoneNumber(),
                user.getPassword(),
                authorities);
    }

    @Override
    public boolean isAccountNonExpired() {
        return true;
    }

    @Override
    public boolean isAccountNonLocked() {
        return true;
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return true;
    }

    @Override
    public boolean isEnabled() {
        return true;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o)
            return true;
        if (o == null || getClass() != o.getClass())
            return false;
        UserDetailsImpl user = (UserDetailsImpl) o;
        return Objects.equals(id, user.id);
    }
}