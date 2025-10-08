package model;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import util.di.annotation.Column;
import util.di.annotation.Nested;


public class CarPrices {
    
    @Column()
    private Integer priceId;          // ID giá
    @Column()
    private Integer carId;            // ID xe
    @Column()
    private BigDecimal dailyPrice;    // Giá theo ngày
    @Column()
    private BigDecimal depositAmount; // So tien coc
    @Column()
    private LocalDate startDate;      // Ngày bắt đầu áp dụng
    @Column()
    private LocalDate endDate;        // Ngày kết thúc áp dụng (NULL = hiện hành)
    @Column()
    private LocalDateTime createAt;   // Ngày tạo
    
    // Doi tuong lien quan
    @Nested
    private Cars car;                // Model xe
    
    // Constructors
    public CarPrices() {}
    
    public CarPrices(Integer carId, BigDecimal dailyPrice, LocalDate startDate) {
        this.carId = carId;
        this.dailyPrice = dailyPrice;
        this.startDate = startDate;
        this.createAt = LocalDateTime.now();
    }
    
    // Getters and Setters
    public Integer getPriceId() { return priceId; }
    public void setPriceId(Integer priceId) { this.priceId = priceId; }
    
    public Integer getCarId() { return carId; }
    public void setCarId(Integer carId) { this.carId = carId; }
    
    public BigDecimal getDailyPrice() { return dailyPrice; }
    public void setDailyPrice(BigDecimal dailyPrice) { this.dailyPrice = dailyPrice; }
    
    public BigDecimal getDepositAmount() { return depositAmount; }
    public void setDepositAmount(BigDecimal depositAmount) { this.depositAmount = depositAmount; }
    
    public LocalDate getStartDate() { return startDate; }
    public void setStartDate(LocalDate startDate) { this.startDate = startDate; }
    
    public LocalDate getEndDate() { return endDate; }
    public void setEndDate(LocalDate endDate) { this.endDate = endDate; }
    
    public LocalDateTime getCreateAt() { return createAt; }
    public void setCreateAt(LocalDateTime createAt) { this.createAt = createAt; }
    
    public Cars getCar() { return car; }
    public void setCar(Cars car) { this.car = car; }
}
