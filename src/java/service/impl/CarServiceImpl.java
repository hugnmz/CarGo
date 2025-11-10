package service.impl;

import dao.*;
import dao.CategoriesDAO;
import dto.CarDTO;
import dto.CategoryDTO;
import dto.FuelDTO;
import dto.LocationDTO;
import dto.SeatingDTO;
import dto.VehicleDTO;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import mapper.*;
import model.CarPrices;
import model.Cars;
import service.CarService;
import util.MessageUtil;
import util.di.annotation.Autowired;
import util.di.annotation.Service;
import util.exception.ValidationException;
import util.exception.DataAccessException;

@Service
public class CarServiceImpl implements CarService {

    @Autowired
    private CarsDAO carsDAO;

    @Autowired
    private CategoriesDAO categoriesDAO;

    @Autowired
    private FuelsDAO fuelsDAO;

    @Autowired
    private SeatingsDAO seatingsDAO;

    @Autowired
    private CarPricesDAO carPricesDAO;

    @Autowired
    private VehiclesDAO vehiclesDAO;

    @Autowired
    private LocationDAO locationsDAO;

    @Autowired
    private CarMapper carMapper;

    @Autowired
    private CategoryMapper categoryMapper;

    @Autowired
    private FuelMapper fuelMapper;

    @Autowired
    private SeatingMapper seatingMapper;

    @Autowired
    private VehicleMapper vehicleMapper;

    @Override
    public List<CarDTO> getAllCars() {
        // lay danh sach tat ca xe
        List<Cars> cars = carsDAO.getAllCar();
        List<CarDTO> carDTOs = new ArrayList<>();

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

    @Override
    public boolean addCar(CarDTO carDTO) {
        if (carDTO == null) {
            return false;
        }
        try {

            Optional<Cars> existing = carsDAO.findByNameAndYear(carDTO.getName(), carDTO.getYear());
            if (existing.isPresent()) {
                throw new ValidationException(
                        String.format("Xe %s (%d) đã tồn tại trong hệ thống.",
                                carDTO.getName(), carDTO.getYear())
                );
            }

            Cars cars = carMapper.toModel(carDTO);
            return carsDAO.addCar(cars);
        } catch (Exception e) {
            throw new RuntimeException("error.system", e);
        }
    }

    @Override
    public boolean addPriceForCar(int carId, double price, double deposit) {
        try {
            model.CarPrices carPrice = new model.CarPrices();
            carPrice.setCarId(carId);
            carPrice.setDailyPrice(BigDecimal.valueOf(price));
            carPrice.setDepositAmount(BigDecimal.valueOf(deposit));

            carPrice.setStartDate(LocalDate.now());
            carPrice.setCreateAt(LocalDateTime.now());

            return carPricesDAO.addCarPrice(carPrice);
        } catch (Exception e) {
            throw new RuntimeException("error.system", e);
        }
    }

    @Override
    public int addCarAndGetId(CarDTO carDTO) {
        Optional<Cars> existing = carsDAO.findByNameAndYear(carDTO.getName(), carDTO.getYear());
        if (existing.isPresent()) {
            throw new ValidationException(
                    String.format("Xe %s (%d) đã tồn tại trong hệ thống.",
                            carDTO.getName(), carDTO.getYear())
            );
        }

        Cars car = carMapper.toModel(carDTO);
        return carsDAO.addCarAndReturnId(car);
    }

    @Override
    public boolean updateCar(CarDTO carDTO) {
        if (carDTO == null) {
            return false;
        }
        try {
            Optional<Cars> existing = carsDAO.findByNameAndYear(carDTO.getName(), carDTO.getYear());
            if (existing.isPresent() && !existing.get().getCarId().equals(carDTO.getCarId())) {
                throw new ValidationException(
                        String.format("Xe %s (%d) đã tồn tại trong hệ thống.", carDTO.getName(), carDTO.getYear())
                );
            }

            Cars cars = carMapper.toModel(carDTO);
            boolean updatedCar = carsDAO.updateCar(cars);
            if (!updatedCar) {
                return false;
            }

            // Update giá xe nếu có thay đổi
            Optional<CarPrices> currentPriceOpt = carPricesDAO.getCurrentPriceByCar(carDTO.getCarId());
            BigDecimal newDailyPrice = BigDecimal.valueOf(carDTO.getDailyPrice());
            BigDecimal newDeposit = BigDecimal.valueOf(carDTO.getDepositAmount());

            if (currentPriceOpt.isPresent()) {
                CarPrices currentPrice = currentPriceOpt.get();
                if (currentPrice.getDailyPrice().compareTo(newDailyPrice) != 0
                        || currentPrice.getDepositAmount().compareTo(newDeposit) != 0) {
                    // Kết thúc giá hiện tại
                    carPricesDAO.endCurrentPrice(carDTO.getCarId());

                    // Thêm giá mới
                    model.CarPrices newPrice = new model.CarPrices();
                    newPrice.setCarId(carDTO.getCarId());
                    newPrice.setDailyPrice(newDailyPrice);
                    newPrice.setDepositAmount(newDeposit);
                    newPrice.setStartDate(LocalDate.now());
                    newPrice.setCreateAt(LocalDateTime.now());

                    carPricesDAO.addCarPrice(newPrice);
                }
            } else {
                CarPrices newPrice = new CarPrices();
                newPrice.setCarId(carDTO.getCarId());
                newPrice.setDailyPrice(newDailyPrice);
                newPrice.setDepositAmount(newDeposit);
                newPrice.setStartDate(LocalDate.now());
                newPrice.setCreateAt(LocalDateTime.now());

                carPricesDAO.addCarPrice(newPrice);
            }

            return true;
        } catch (ValidationException ve) {
            throw ve;
        } catch (Exception e) {
            throw new RuntimeException("error.system", e);
        }
    }

    @Override
    public boolean deleteCar(Integer carId) {
        if (carId == null) {
            return false;
        }

        try {
            // Xóa toàn bộ giá xe
            boolean deletedPrices = carPricesDAO.deleteCarPricesByCarId(carId);
            if (!deletedPrices) {
            }

            // Xóa các vehical liên quan
            boolean deletedVehicle = vehiclesDAO.deleteVehiclesByCarId(carId);
            if (!deletedVehicle) {
            }

            // Xóa hết các xe
            boolean deletedCar = carsDAO.deleteCar(carId);

            return deletedCar;
        } catch (Exception e) {
            throw new DataAccessException(MessageUtil.getError("error.dataaccess.car.delete.failed"), e);
        }
    }

    @Override
    public List<CategoryDTO> getAllCategories() {
        try {
            var categories = categoriesDAO.getAllCategories();
            return categories.stream()
                    .map(categoryMapper::toDTO)
                    .toList();
        } catch (Exception e) {
            throw new RuntimeException("error.system", e);
        }
    }

    @Override
    public List<FuelDTO> getAllFuels() {
        try {
            //Lấy toàn bộ nhiên liệu
            var fuels = fuelsDAO.getAllFuels();
            //Mapper sang DTO
            return fuels.stream()
                    .map(fuelMapper::toDTO)
                    .toList();
        } catch (Exception e) {
            throw new RuntimeException("error.system", e);
        }
    }

    @Override
    public List<SeatingDTO> getAllSeatings() {
        try {
            //Lấy toàn bộ các ghế
            var seatings = seatingsDAO.getAllSeatings();
            //Mapper sang DTO
            return seatings.stream()
                    .map(seatingMapper::toDTO)
                    .toList();
        } catch (Exception e) {
            throw new RuntimeException("error.system", e);
        }
    }

    @Override
    public List<LocationDTO> getAllLocation() {
        try {
            // Lấy toàn bộ danh sách location từ DAO
            var locations = locationsDAO.getAllLocations();

            // Mapper sang DTO
            return locations.stream()
                    .map(loc -> {
                        LocationDTO dto = new LocationDTO();
                        dto.setLocationId(loc.getLocationId());
                        dto.setCity(loc.getCity());
                        dto.setAddress(loc.getAddress());
                        return dto;
                    })
                    .toList();

        } catch (Exception e) {
            throw new RuntimeException("error.system", e);
        }
    }

    public List<VehicleDTO> getVehicalByCarId(int carId) {
        try {
            var vehicals = vehiclesDAO.getVehiclesByCar(carId);
            return vehicals.stream().map(vehicleMapper::toDTO).toList();
        } catch (Exception e) {
            throw new RuntimeException("error.system", e);
        }
    }

    @Override
    public List<CarDTO> searchCars(Integer locationId, String name, Integer categoryId, Double price) {
        // lay danh sach tat ca xe
        List<Cars> cars = carsDAO.searchCars(locationId, name, categoryId, price);
        List<CarDTO> carDTOs = new ArrayList<>();

        for (Cars car : cars) {
            CarDTO dto = carMapper.toDTO(car);

            // Set locationCity from Vehicle if available
            if (car.getVehicles() != null && !car.getVehicles().isEmpty()) {
                var firstVehicle = car.getVehicles().get(0);
                if (firstVehicle.getLocation() != null && firstVehicle.getLocation().getCity() != null) {
                    dto.setLocationCity(firstVehicle.getLocation().getCity());
                }
            }

            carDTOs.add(dto);
        }

        return carDTOs;
    }

    @Override
    public List<CarPrices> getPricesByCarId(int carId) {
        try {
            return carPricesDAO.getPricesByCar(carId);
        } catch (Exception e) {
            e.printStackTrace();
            return List.of();
        }
    }
}