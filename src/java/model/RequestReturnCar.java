/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package model;

import dto.ContractDTO;
import java.time.Duration;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

/**
 *
 * @author Admin
 */
public class RequestReturnCar {

    private ContractDTO contract;
    private LocalDateTime timeRequest;

    public RequestReturnCar() {
    }

    public RequestReturnCar(ContractDTO contract, LocalDateTime timeRequest) {
        this.contract = contract;
        this.timeRequest = timeRequest;
    }

    public ContractDTO getContract() {
        return contract;
    }

    public void setContract(ContractDTO contract) {
        this.contract = contract;
    }

    public LocalDateTime getTimeRequest() {
        return timeRequest;
    }

    public void setTimeRequest(LocalDateTime timeRequest) {
        this.timeRequest = timeRequest;
    }

    public String timeRequestToString() {
        DateTimeFormatter fmt = DateTimeFormatter.ofPattern("HH:mm dd/MM/yyyy");
        return fmt.format(timeRequest);
    }

    public boolean isLate() {
        return timeRequest != null && contract.getEndDate() != null && timeRequest.isAfter(contract.getEndDate());
    }

    public String lateTime() {
        if (!isLate()) {
            return "0 phút";
        }

        Duration d = Duration.between(contract.getEndDate(), timeRequest);
        long days = d.toDays();
        long hours = d.toHoursPart();   // Java 9+
        long minutes = d.toMinutesPart();

        StringBuilder sb = new StringBuilder();
        if (days > 0) {
            sb.append(days).append(" ngày ");
        }
        if (hours > 0) {
            sb.append(hours).append(" giờ ");
        }
        if (minutes > 0 || sb.length() == 0) {
            sb.append(minutes).append(" phút");
        }

        return sb.toString().trim();
    }
}