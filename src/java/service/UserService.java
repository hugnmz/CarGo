/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Interface.java to edit this template
 */
package service;

import dto.*;
import java.util.Optional;

/**
 *
 * @author DELL
 */
public interface UserService {
    Optional<UserDTO> loginUser(String username, String password);
}
