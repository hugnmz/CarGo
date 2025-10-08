package model;

import java.util.List;
import util.di.annotation.Column;

public class Fuels {

    @Column()
    private Integer fuelId;          // ID loại nhiên liệu
    @Column()
    private String fuelType;         // Loại nhiên liệu
    private List<Cars> cars;         // Danh sách xe sử dụng loại nhiên liệu này

    // Constructors
    public Fuels() {
    }

    public Fuels(String fuelType) {
        this.fuelType = fuelType;
    }

    // Getters and Setters
    public Integer getFuelId() {
        return fuelId;
    }

    public void setFuelId(Integer fuelId) {
        this.fuelId = fuelId;
    }

    public String getFuelType() {
        return fuelType;
    }

    public void setFuelType(String fuelType) {
        this.fuelType = fuelType;
    }

    public List<Cars> getCars() {
        return cars;
    }

    public void setCars(List<Cars> cars) {
        this.cars = cars;
    }
}
