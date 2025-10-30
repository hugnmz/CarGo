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
public interface UseService {

    Optional<UseDTO> loginUser(String username, String password);

    List<UseDTO> getAllUser();
    
    List<LocationDTO> getAllLocation();
    
    UseDTO getUserById(Integer userId);

    void addUser(UseDTO user, String password);

    void updateUser(UseDTO user);

    void deleteUser(Integer userId);
    
    public boolean changeUserPassword(Integer userId, String oldPassword, String newPassword);
}
