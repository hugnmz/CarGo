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
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import mapper.*;
import model.CarPrices;
import model.Cars;
import service.CarService;
import util.di.annotation.Autowired;
import util.di.annotation.Service;


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
    private LocationsDAO locationsDAO;

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
        List<Cars> cars = carsDAO.getAllCars();
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
            Cars cars = carMapper.toModel(carDTO);
            return carsDAO.addCar(cars);
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    @Override
    public boolean addPriceForCar(int carId, double price, double deposit) {
        try {
            model.CarPrices carPrice = new model.CarPrices();
            carPrice.setCarId(carId);
            carPrice.setDailyPrice(BigDecimal.valueOf(price));
            carPrice.setDepositAmount(BigDecimal.valueOf(deposit));

            carPrice.setStartDate(java.time.LocalDate.now());
            carPrice.setCreateAt(java.time.LocalDateTime.now());

            return carPricesDAO.addCarPrice(carPrice);
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    @Override
    public int addCarAndGetId(CarDTO carDTO) {
        Cars car = carMapper.toModel(carDTO);
        return carsDAO.addCarAndReturnId(car);
    }

    @Override
    public boolean updateCar(CarDTO carDTO) {
        if (carDTO == null) {
            return false;
        }
        try {
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
                    newPrice.setStartDate(java.time.LocalDate.now());
                    newPrice.setCreateAt(java.time.LocalDateTime.now());

                    carPricesDAO.addCarPrice(newPrice);
                }
            } else {
                // Nếu chưa có giá nào, tạo luôn
                model.CarPrices newPrice = new model.CarPrices();
                newPrice.setCarId(carDTO.getCarId());
                newPrice.setDailyPrice(newDailyPrice);
                newPrice.setDepositAmount(newDeposit);
                newPrice.setStartDate(java.time.LocalDate.now());
                newPrice.setCreateAt(java.time.LocalDateTime.now());

                carPricesDAO.addCarPrice(newPrice);
            }

            return true;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    /*
    @Override
    public boolean updateCar(CarDTO carDTO) {
        if (carDTO == null) {
            return false;
        }
        try {
            Cars cars = carMapper.toModel(carDTO);
            return carsDAO.updateCar(cars);
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }
     */
    @Override
    public boolean deleteCar(Integer carId) {
        if (carId == null) {
            return false;
        }

        try {
            // Xóa toàn bộ giá xe
            boolean deletedPrices = carPricesDAO.deleteCarPricesByCarId(carId);
            if (!deletedPrices) {
                System.out.println("⚠ Không có giá nào để xóa hoặc lỗi khi xóa giá xe.");
            }

            // Xóa các vehical liên quan
            boolean deletedVehicle = vehiclesDAO.deleteVehiclesByCarId(carId);
            if (!deletedVehicle) {
                System.out.println("⚠ Không có vehicle nào để xóa hoặc lỗi khi xóa vehicle.");
            }

            // Xóa hết các xe
            boolean deletedCar = carsDAO.deleteCar(carId);

            return deletedCar;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
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
            e.printStackTrace();
            return List.of();
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
            e.printStackTrace();
            // Trả về danh sách rỗng nếu lỗi
            return List.of();
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
            e.printStackTrace();
            // Trả về danh sách rỗng nếu lỗi
            return List.of();
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
            e.printStackTrace();
            // Trả về danh sách rỗng nếu lỗi
            return List.of();
        }
    }

    public List<VehicleDTO> getVehicalByCarId(int carId) {
        try {
            var vehicals = vehiclesDAO.getVehiclesByCar(carId);
            return vehicals.stream().map(vehicleMapper::toDTO).toList();
        } catch (Exception e) {
            e.printStackTrace();
            // Trả về danh sách rỗng nếu lỗi
            return List.of();
        }
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
