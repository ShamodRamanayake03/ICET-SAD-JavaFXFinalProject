package controller.user;

import java.util.List;

import entity.UserEntity;

public interface UserService {
    boolean saveUser(UserEntity user);
    boolean updateUser(UserEntity user);
    boolean deleteUser(String id);
    UserEntity getUserById(String id);
    List<UserEntity> getAllUsers();
}
