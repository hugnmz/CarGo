package model;

import java.util.List;
import util.di.annotation.Column;


public class Seatings {
    
    @Column()
    private Integer seatingId;       // ID số chỗ ngồi
    @Column()
    private Integer seatingType;     // Số chỗ ngồi
    private List<Cars> cars;         // Danh sách xe có số chỗ ngồi này
    
    // Constructors
    public Seatings() {}
    
    public Seatings(Integer seatingType) {
        this.seatingType = seatingType;
    }
    
    // Getters and Setters
    public Integer getSeatingId() { return seatingId; }
    public void setSeatingId(Integer seatingId) { this.seatingId = seatingId; }
    
    public Integer getSeatingType() { return seatingType; }
    public void setSeatingType(Integer seatingType) { this.seatingType = seatingType; }
    
    public List<Cars> getCars() { return cars; }
    public void setCars(List<Cars> cars) { this.cars = cars; }
}
