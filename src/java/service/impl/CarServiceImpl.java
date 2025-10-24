package service.impl;

import dao.CarsDAO;
import dto.CarDTO;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import mapper.CarMapper;
import model.Cars;
import service.CarService;
import util.di.annotation.Autowired;
import util.di.annotation.Service;

// lop trien khai service cho car
// muc dich: implement business logic cho car operations, chuyen doi entity sang dto, su dung carsdao de truy cap database
@Service
public class CarServiceImpl implements CarService {

    @Autowired
    private CarsDAO carsDAO;
    
    @Autowired
    private CarMapper carMapper;
    
    @Override
    public List<CarDTO> getAllCars() {
        // lay danh sach tat ca xe
        List<Cars> cars = carsDAO.getAllCars();
        List<CarDTO> carDTOs = new ArrayList<>();
        
        // chuyen doi entity sang dto
        for (Cars car : cars) {
            CarDTO dto = carMapper.toDTO(car);
            carDTOs.add(dto);
        }
        
        return carDTOs;
    }

    @Override
    public Optional<CarDTO> getCarById(Integer carId) {
        // lay xe theo id
        Optional<Cars> car = carsDAO.getCarById(carId);
        if (car.isPresent()) {
            CarDTO dto = carMapper.toDTO(car.get());
            return Optional.of(dto);
        }
        return Optional.empty();
    }
}
