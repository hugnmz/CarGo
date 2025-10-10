package test;

import dto.*;
import mapper.*;
import model.*;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class MapperSmokeTest {

	public static void main(String[] args) {
		List<String> results = new ArrayList<>();
		try { results.add(testCarMapper()); } catch (Throwable t) { results.add("CarMapper: FAIL - " + t.getMessage()); }
		try { results.add(testUserMapper()); } catch (Throwable t) { results.add("UserMapper: FAIL - " + t.getMessage()); }
		try { results.add(testPaymentMethodMapper()); } catch (Throwable t) { results.add("PaymentMethodMapper: FAIL - " + t.getMessage()); }
		try { results.add(testVehicleMapper()); } catch (Throwable t) { results.add("VehicleMapper: FAIL - " + t.getMessage()); }
		try { results.add(testLocationMapper()); } catch (Throwable t) { results.add("LocationMapper: FAIL - " + t.getMessage()); }
		try { results.add(testFeedbackMapper()); } catch (Throwable t) { results.add("FeedbackMapper: FAIL - " + t.getMessage()); }
		try { results.add(testIncidentMapper()); } catch (Throwable t) { results.add("IncidentMapper: FAIL - " + t.getMessage()); }
		try { results.add(testCarPriceMapper()); } catch (Throwable t) { results.add("CarPriceMapper: FAIL - " + t.getMessage()); }
		try { results.add(testRoleMapper()); } catch (Throwable t) { results.add("RoleMapper: FAIL - " + t.getMessage()); }

		System.out.println("==== Mapper Smoke Test Results ====");
		for (String r : results) {
			System.out.println(r);
		}
	}

	private static String testCarMapper() {
		// Arrange model
		Cars car = new Cars();
		car.setCarId(1);
		car.setName("Toyota Vios");
		car.setYear(2022);
		car.setDescription("Sedan");
		car.setImage("vios.jpg");

		Categories cat = new Categories();
		cat.setCategoryName("Sedan");
		car.setCategory(cat);

		Fuels fuel = new Fuels();
		fuel.setFuelType("Xang");
		car.setFuel(fuel);

		Seatings seat = new Seatings();
		seat.setSeatingType(5);
		car.setSeating(seat);

		Locations loc = new Locations();
		loc.setCity("Ha Noi");
		loc.setAddress("123 Pho Hue");
		car.setLocation(loc);

		CarPrices cp = new CarPrices();
		cp.setDailyPrice(new BigDecimal("500000"));
		cp.setDepositAmount(new BigDecimal("1000000"));
		cp.setEndDate(null);
		List<CarPrices> priceList = new ArrayList<>();
		priceList.add(cp);
		car.setCarPrices(priceList);

		Vehicles v1 = new Vehicles();
		v1.setPlateNumber("30A-123.45");
		v1.setIsActive(true);
		Vehicles v2 = new Vehicles();
		v2.setPlateNumber("30A-678.90");
		v2.setIsActive(false);
		car.setVehicles(Arrays.asList(v1, v2));

		CarMapper mapper = new CarMapper();
		// Act DTO
		CarDTO dto = mapper.toDTO(car);
		// Assert DTO
		assertNotNull(dto, "CarDTO null");
		assertEquals(1, dto.getCarId(), "carId");
		assertEquals("Toyota Vios", dto.getName(), "name");
		assertEquals(Integer.valueOf(2022), dto.getYear(), "year");
		assertEquals("Sedan", dto.getDescription(), "description");
		assertEquals("vios.jpg", dto.getImage(), "image");
		assertEquals("Sedan", dto.getCategoryName(), "categoryName");
		assertEquals("Xang", dto.getFuelType(), "fuelType");
		assertEquals(Integer.valueOf(5), dto.getSeatingType(), "seatingType");
		assertEquals("Ha Noi", dto.getCity(), "city");
		assertEquals("123 Pho Hue", dto.getAddress(), "address");
		assertEquals(new BigDecimal("500000"), dto.getCurrentPrice(), "currentPrice");
		assertEquals(new BigDecimal("1000000"), dto.getDepositAmount(), "depositAmount");
		assertEquals(1, dto.getAvailableVehicles() != null ? dto.getAvailableVehicles().size() : 0, "availableVehicles size active only");

		// Act Model
		Cars car2 = mapper.toModel(dto);
		// Assert Model basic fields
		assertNotNull(car2, "Cars null from DTO");
		assertEquals(dto.getCarId(), car2.getCarId(), "carId back");
		assertEquals(dto.getName(), car2.getName(), "name back");
		assertEquals(dto.getYear(), car2.getYear(), "year back");
		assertEquals(dto.getDescription(), car2.getDescription(), "description back");
		assertEquals(dto.getImage(), car2.getImage(), "image back");

		return "CarMapper: OK";
	}

	private static String testUserMapper() {
		Users user = new Users();
		user.setUserId(10);
		user.setUsername("huyhn");
		user.setFullName("Huy Nguyen");
		user.setPhone("0900000000");
		user.setEmail("huy@example.com");
		user.setDateOfBirth(LocalDate.of(2000, 1, 1));
		user.setCreateAt(LocalDateTime.of(2024, 1, 1, 0, 0));

		Locations loc = new Locations();
		loc.setCity("Da Nang");
		loc.setAddress("Tran Phu");
		user.setLocation(loc);

		Roles r1 = new Roles(); r1.setRoleName("USER");
		Roles r2 = new Roles(); r2.setRoleName("ADMIN");
		user.setRoles(Arrays.asList(r1, r2));

		UserMapper mapper = new UserMapper();
		UserDTO dto = mapper.toDTO(user);
		assertNotNull(dto, "UserDTO null");
		assertEquals("Da Nang", dto.getCity(), "user city");
		assertEquals(2, dto.getRoles() != null ? dto.getRoles().size() : 0, "role names size");

		Users user2 = mapper.toModel(dto);
		assertNotNull(user2, "Users back null");
		assertEquals(dto.getUsername(), user2.getUsername(), "username back");
		return "UserMapper: OK";
	}

	private static String testPaymentMethodMapper() {
		PaymentMethods pm = new PaymentMethods();
		pm.setMethodId(3);
		pm.setMethodName("Bank");
		Payments p1 = new Payments(); p1.setStatus("completed"); p1.setAmount(new BigDecimal("100"));
		Payments p2 = new Payments(); p2.setStatus("pending"); p2.setAmount(new BigDecimal("50"));
		pm.setPayments(Arrays.asList(p1, p2));

		PaymentMethodMapper mapper = new PaymentMethodMapper();
		PaymentMethodDTO dto = mapper.toDTO(pm);
		assertNotNull(dto, "PaymentMethodDTO null");
		assertEquals(2, dto.getPaymentCount(), "payment count");
		assertEquals(new BigDecimal("100.0"), dto.getTotalAmount(), "total amount of completed");

		PaymentMethods pm2 = mapper.toModel(dto);
		assertNotNull(pm2, "PaymentMethods back null");
		return "PaymentMethodMapper: OK";
	}

	private static String testVehicleMapper() {
		Vehicles v = new Vehicles();
		v.setVehicleId(100);
		v.setCarId(1);
		v.setPlateNumber("43A-123.45");
		v.setIsActive(true);
		v.setLocationId(5);

		Cars car = new Cars();
		car.setName("Mazda 3");
		car.setYear(2021);
		v.setCar(car);

		Locations loc = new Locations();
		loc.setCity("Da Nang");
		loc.setAddress("Hai Chau");
		v.setLocation(loc);

		VehicleMapper mapper = new VehicleMapper();
		VehicleDTO dto = mapper.toDTO(v);
		assertNotNull(dto, "VehicleDTO null");
		assertEquals("Mazda 3", dto.getCarName(), "vehicle.car.name");
		assertEquals("Da Nang", dto.getCity(), "vehicle.location.city");

		Vehicles v2 = mapper.toModel(dto);
		assertNotNull(v2, "Vehicles back null");
		assertEquals(dto.getPlateNumber(), v2.getPlateNumber(), "plate back");
		return "VehicleMapper: OK";
	}

	private static String testLocationMapper() {
		Locations l = new Locations();
		l.setLocationId(9);
		l.setCity("Hai Phong");
		l.setAddress("Le Loi");
		LocationMapper mapper = new LocationMapper();
		LocationDTO dto = mapper.toDTO(l);
		assertNotNull(dto, "LocationDTO null");
		Locations l2 = mapper.toModel(dto);
		assertNotNull(l2, "Locations back null");
		assertEquals(dto.getCity(), l2.getCity(), "city back");
		return "LocationMapper: OK";
	}

	private static String testFeedbackMapper() {
		Feedbacks f = new Feedbacks();
		f.setFeedbackId(7);
		f.setCustomerId(2);
		f.setVehicleId(100);
		f.setComment("Tot");
		f.setCreateAt(LocalDateTime.of(2024, 2, 2, 0, 0));
		FeedbackMapper mapper = new FeedbackMapper();
		FeedbackDTO dto = mapper.toDTO(f);
		assertNotNull(dto, "FeedbackDTO null");
		Feedbacks f2 = mapper.toModel(dto);
		assertNotNull(f2, "Feedbacks back null");
		assertEquals(dto.getComment(), f2.getComment(), "comment back");
		return "FeedbackMapper: OK";
	}

	private static String testIncidentMapper() {
		Incidents i = new Incidents();
		i.setIncidentId(11);
		i.setContractDetailId(22);
		i.setIncidentTypeId(3);
		i.setDescription("Va cham nhe");
		i.setIncidentDate(LocalDateTime.of(2024, 3, 10, 0, 0));
		IncidentMapper mapper = new IncidentMapper();
		IncidentDTO dto = mapper.toDTO(i);
		assertNotNull(dto, "IncidentDTO null");
		Incidents i2 = mapper.toModel(dto);
		assertNotNull(i2, "Incidents back null");
		assertEquals(dto.getDescription(), i2.getDescription(), "incident desc back");
		return "IncidentMapper: OK";
	}

	private static String testCarPriceMapper() {
		CarPrices cp = new CarPrices();
		cp.setPriceId(5);
		cp.setCarId(1);
		cp.setDailyPrice(new BigDecimal("700000"));
		cp.setDepositAmount(new BigDecimal("1500000"));
		cp.setStartDate(LocalDate.of(2024,1,1));
		cp.setEndDate(null);
		CarPriceMapper mapper = new CarPriceMapper();
		CarPriceDTO dto = mapper.toDTO(cp);
		assertNotNull(dto, "CarPriceDTO null");
		CarPrices cp2 = mapper.toModel(dto);
		assertNotNull(cp2, "CarPrices back null");
		assertEquals(dto.getDailyPrice(), cp2.getDailyPrice(), "daily price back");
		return "CarPriceMapper: OK";
	}

	private static String testRoleMapper() {
		Roles r = new Roles();
		r.setRoleId(2);
		r.setRoleName("ADMIN");
		RoleMapper mapper = new RoleMapper();
		RoleDTO dto = mapper.toDTO(r);
		assertNotNull(dto, "RoleDTO null");
		Roles r2 = mapper.toModel(dto);
		assertNotNull(r2, "Roles back null");
		assertEquals(dto.getRoleName(), r2.getRoleName(), "role name back");
		return "RoleMapper: OK";
	}

	private static void assertEquals(Object expected, Object actual, String field) {
		if (expected == null && actual == null) return;
		if (expected != null && expected.equals(actual)) return;
		throw new AssertionError("Mismatch for " + field + ": expected=" + expected + ", actual=" + actual);
	}

	private static void assertNotNull(Object obj, String field) {
		if (obj == null) throw new AssertionError(field + " is null");
	}
}


 