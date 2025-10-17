/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Interface.java to edit this template
 */
package service;

import dto.*;
import java.util.*;

/**
 *
 * @author DELL
 */
public interface UserService {

    Optional<UserDTO> loginUser(String username, String password);

    List<UserDTO> getAllUser();
    
    List<LocationDTO> getAllLocation();
    
    UserDTO getUserById(Integer userId);

    void addUser(UserDTO user, String password);

    void updateUser(UserDTO user);

    void deleteUser(Integer userId);
}
