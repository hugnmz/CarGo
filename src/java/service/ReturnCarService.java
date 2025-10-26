/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package service;

import java.util.Collection;
import model.RequestReturnCar;

/**
 *
 * @author Admin
 */
public interface ReturnCarService {

    public Collection<RequestReturnCar> all();

    public RequestReturnCar get(int contractId);

    public void putIfAbsentOrKeep(int contractId, RequestReturnCar req);

    public void remove(int contractId);

    public boolean contains(int contractId);
}
