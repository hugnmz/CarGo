/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package dto;

import java.util.List;
import model.Cars;
import util.di.annotation.Column;

/**
 *
 * @author DELL
 */
public class SeatingDTO {
    
    private Integer seatingId;       // ID số chỗ ngồi
    private Integer seatingType;     // Số chỗ ngồi
    private List<Cars> cars;

    public SeatingDTO() {
    }

    public Integer getSeatingId() {
        return seatingId;
    }

    public void setSeatingId(Integer seatingId) {
        this.seatingId = seatingId;
    }

    public Integer getSeatingType() {
        return seatingType;
    }

    public void setSeatingType(Integer seatingType) {
        this.seatingType = seatingType;
    }

    public List<Cars> getCars() {
        return cars;
    }

    public void setCars(List<Cars> cars) {
        this.cars = cars;
    }
    
    
}
