package org.example.service;

import lombok.RequiredArgsConstructor;
import org.example.repository.DevTavaresUserRepository;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
@RequiredArgsConstructor
public class DevTavaresUserDetailsService implements UserDetailsService {

    private final DevTavaresUserRepository devTavaresUserRepository;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        return Optional.ofNullable(devTavaresUserRepository.findByUsername(username))
                .orElseThrow(() -> new UsernameNotFoundException("Username not found"));
    }
}
