/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Interface.java to edit this template
 */
package dao;

import java.util.List;
import java.util.Optional;
import model.Fuels;

/**
 *
 * @author admin
 */
public interface FuelsDAO {
    List<Fuels> getAllFuels();
    Optional<Fuels> getFuelsById(Integer fuelId);
    boolean addFuels(Fuels fuel);
    boolean updateFuels(Fuels fuel);
    boolean deleteFuels(Integer fuelId);
}
