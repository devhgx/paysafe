package com.tai.paysafe.service;

import com.tai.paysafe.dto.response.RegistrationRequest;
import com.tai.paysafe.entities.User;
import com.tai.paysafe.entities.UserBank;
import com.tai.paysafe.errors.exception.ErrorException;

import java.util.List;

public interface UserService {
      User create(RegistrationRequest request, String role);
      void deleteUserBank (Long userId) throws ErrorException;

      User findById(Long id);
      List<User> getAllUser();
      List<UserBank> getUserBanks(Long userId);
}
