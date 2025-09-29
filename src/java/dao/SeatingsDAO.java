/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Interface.java to edit this template
 */
package dao;

import java.util.List;
import model.Seatings;

/**
 *
 * @author admin
 */
public interface SeatingsDAO {

    List<Seatings> getAllSeatings();

    Seatings getSeatingById(Integer seatingId);
    boolean addSeating(Seatings seating);

    boolean updateSeating(Seatings seating);

    boolean deleteSeating(Integer seatingId);
}
