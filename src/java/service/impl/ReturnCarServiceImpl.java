/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package service.impl;

import java.util.Collection;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;
import model.RequestReturnCar;
import service.ReturnCarService;
import util.di.annotation.Service;

/**
 *
 * @author Admin
 */
@Service
public class ReturnCarServiceImpl implements ReturnCarService {

    private final ConcurrentMap<Integer, RequestReturnCar> queue = new ConcurrentHashMap<>();

    @Override
    public Collection<RequestReturnCar> all() {
        return queue.values();
    }

    @Override
    public RequestReturnCar get(int contractId) {
        return queue.get(contractId);
    }

    @Override
    public void putIfAbsentOrKeep(int contractId, RequestReturnCar req) {
        queue.merge(contractId, req, (oldV, newV) -> oldV); // giữ nếu đã có
    }

    @Override
    public void remove(int contractId) {
        queue.remove(contractId);
    }

    @Override
    public boolean contains(int contractId) {
        return queue.containsKey(contractId);
    }
}