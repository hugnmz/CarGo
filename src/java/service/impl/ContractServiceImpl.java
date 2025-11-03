package service.impl;

import dao.CarsDAO;
import dao.CarsDAO;
import dao.ContractsDAO;
import dao.ContractDetailsDAO;
import dao.CustomersDAO;
import dao.OrdersDAO;
import dao.UsersDAO;
import dao.VehiclesDAO;
import dao.impl.CarsDAOImpl;
import dao.impl.ContractDetailsDAOImpl;
import dto.ContractDTO;
import dto.ContractDetailDTO;
import dto.OrderDTO;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import mapper.ContractMapper;
import mapper.ContractDetailMapper;
import mapper.OrderMapper;
import model.Cars;
import model.ContractDetails;
import model.Contracts;
import service.ContractService;
import util.di.annotation.Autowired;
import util.di.annotation.Service;

@Service
public class ContractServiceImpl implements ContractService {

    @Autowired
    private ContractsDAO contractsDAO;


    @Autowired
    private ContractDetailsDAO contractDetailsDAO;


    @Autowired
    private CustomersDAO customersDAO;


    @Autowired
    private OrdersDAO ordersDAO;

    @Autowired
    private ContractMapper contractMapper;


    @Autowired
    private ContractDetailMapper contractDetailMapper;


    @Autowired
    private OrderMapper orderMapper;

    @Autowired
    private UsersDAO usersDAO;

    @Autowired
    private CarsDAO carsDAO;

    @Autowired
    private VehiclesDAO vehiclesDAO;

    @Override
    public List<ContractDTO> getContractsByCustomer(Integer customerId) {
        List<ContractDTO> contractDTOs = new ArrayList<>();


        // Lấy danh sách contracts từ DAO
        List<Contracts> contracts = contractsDAO.getContractByCustomer(customerId);

        for (Contracts contract : contracts) {
            ContractDTO dto = contractMapper.toDTO(contract);
            contractDTOs.add(dto);
        }


        return contractDTOs;
    }

    @Override
    public Optional<ContractDTO> getContractById(Integer contractId) {
        Optional<Contracts> contract = contractsDAO.getContractById(contractId);
        if (contract.isPresent()) {
            ContractDTO dto = contractMapper.toDTO(contract.get());

            // Lấy tên khách hàng
            Optional<model.Customers> customer = customersDAO.getCustomerById(dto.getCustomerId());
            if (customer.isPresent() && customer.get().getFullName() != null) {
                dto.setCustomerName(customer.get().getFullName());
            }
            //lấy số điện thoại
            if (customer.isPresent() && customer.get().getPhone() != null) {
                dto.setCustomerPhone(customer.get().getPhone());
            }

            return Optional.of(dto);
        }
        return Optional.empty();
    }

    @Override
    public List<ContractDetailDTO> getContractDetails(Integer contractId) {
        List<ContractDetailDTO> detailDTOs = new ArrayList<>();

        List<ContractDetails> details = contractDetailsDAO.getContractDetailsByContractId(contractId);

        for (ContractDetails detail : details) {
            Optional<model.Vehicles> vehicle = vehiclesDAO.getVehicleById(detail.getVehicleId());
            vehicle.ifPresent(c -> detail.setVehicle(c));
            Optional<model.Cars> car = carsDAO.getCarById(detail.getVehicle().getCarId());
            car.ifPresent(c -> detail.getVehicle().setCar(c));
            ContractDetailDTO dto = contractDetailMapper.toDTO(detail);
            detailDTOs.add(dto);
        }


        return detailDTOs;
    }

    @Override
    public boolean updateContractStatus(Integer contractId, String status, String reason) {
        try {
            return contractsDAO.updateContractStatus(contractId, status, reason);
        } catch (Exception e) {
            throw new RuntimeException("error.system", e);
        }
    }

    @Override

    public boolean calculateTotalAmount(Integer contractId) {
        throw new UnsupportedOperationException("Not supported yet."); // Generated from nbfs://nbhost/SystemFileSystem/Templates/Classes/Code/GeneratedMethodBody
    }

    @Override
    public boolean deleteContract(Integer contractId) {
        try {
            boolean deleteContractDetail = contractDetailsDAO.deleteContractDetailByContractId(contractId);
            boolean deleteContract = contractsDAO.deleteContract(contractId);
            return  deleteContract;
        } catch (Exception e) {
            throw new RuntimeException("error.system", e);
        }

    }

    @Override
    public List<ContractDTO> getContractsByStaff(Integer staffId) {
        List<ContractDTO> contractDTOs = new ArrayList<>();
        try {
            List<Contracts> contracts = contractsDAO.getContractByStaff(staffId);
            for (Contracts contract : contracts) {
                ContractDTO dto = contractMapper.toDTO(contract);
                Optional<Customers> customer = customersDAO.getCustomerById(dto.getCustomerId());
                customer.ifPresent(c -> dto.setCustomerName(c.getFullName()));
                contractDTOs.add(dto);
            }
        } catch (Exception e) {
            throw new RuntimeException("error.system", e);
        }
        return contractDTOs;
    }

    @Override
    public List<ContractDTO> getAllContracts() {
        List<ContractDTO> contractDTOs = new ArrayList<>();

        try {
            List<model.Contracts> contracts = contractsDAO.getAllContracts(); // ✅ lấy tất cả
            for (Contracts contract : contracts) {
                ContractDTO dto = contractMapper.toDTO(contract);
                Optional<model.Customers> customer = customersDAO.getCustomerById(dto.getCustomerId());
                customer.ifPresent(c -> dto.setCustomerName(c.getFullName()));
                customer.ifPresent(c -> dto.setCustomerPhone(c.getPhone()));
                Optional<model.Users> staff = usersDAO.getUserById(dto.getStaffId());
                staff.ifPresent(s -> dto.setStaffName(s.getFullName()));
                contractDTOs.add(dto);
            }
        } catch (Exception e) {
            throw new RuntimeException("error.system", e);
        }
        return contractDTOs;
    }

    @Override
    public boolean createContract(ContractDTO contractDTO) {
        try {
            model.Contracts contract = contractMapper.toModel(contractDTO);
            if (contract.getCreateAt() == null) {
                contract.setCreateAt(LocalDateTime.now());
            }
            if (contract.getStatus() != null) {
                contract.setStatus(contract.getStatus().toUpperCase());
            }
            return contractsDAO.addContract(contract);
        } catch (Exception e) {
            throw new RuntimeException("error.system", e);
        }
    }


    @Override
    public List<ContractDTO> createContractsFromCart(Integer customerId, Integer[] selectedOrderIds) {
        List<ContractDTO> createdContracts = new ArrayList<>();


        try {
            // 1. Lấy tên khách hàng
            String customerName = getCustomerName(customerId);


            // 2. Lấy danh sách orders
            List<OrderDTO> selectedOrders = getSelectedOrders(customerId, selectedOrderIds);


            if (selectedOrders.isEmpty()) {
                return createdContracts;
            }


            // 3. Nhóm orders theo (startDate, endDate)
            Map<String, List<OrderDTO>> groups = groupOrdersByDateRange(selectedOrders);


            // 4. Tạo hợp đồng cho từng nhóm
            for (Map.Entry<String, List<OrderDTO>> entry : groups.entrySet()) {
                List<OrderDTO> orders = entry.getValue();
                if (orders.isEmpty()) {
                    continue;
                }

                if (orders.isEmpty()) {
                    continue;
                }

                ContractDTO contractDTO = createContractFromOrders(customerId, customerName, orders);
                if (contractDTO != null) {
                    createdContracts.add(contractDTO);
                }
            }


        } catch (Exception e) {
            throw new RuntimeException("error.system", e);
        }


        return createdContracts;
    }


    private String getCustomerName(Integer customerId) {
        try {
            Optional<model.Customers> customer = customersDAO.getCustomerById(customerId);
            if (customer.isPresent() && customer.get().getFullName() != null) {
                return customer.get().getFullName();
            }
        } catch (Exception e) {
            throw new RuntimeException("error.system", e);
        }
        return "Khách hàng";
    }


    private List<OrderDTO> getSelectedOrders(Integer customerId, Integer[] selectedOrderIds) {
        List<OrderDTO> selectedOrders = new ArrayList<>();


        try {
            if (selectedOrderIds != null && selectedOrderIds.length > 0) {
                // Lấy orders được chọn
                for (Integer id : selectedOrderIds) {
                    Optional<model.Orders> orderOpt = ordersDAO.getOrderById(id);
                    if (orderOpt.isPresent()) {
                        OrderDTO dto = orderMapper.toDTO(orderOpt.get());
                        selectedOrders.add(dto);
                    }
                }
            } else {
                // Lấy tất cả orders trong giỏ
                List<model.Orders> allOrders = ordersDAO.getOrdersByCustomer(customerId);
                for (model.Orders order : allOrders) {
                    OrderDTO dto = orderMapper.toDTO(order);
                    selectedOrders.add(dto);
                }
            }
        } catch (Exception e) {
            throw new RuntimeException("error.system", e);
        }


        return selectedOrders;
    }


    private Map<String, List<OrderDTO>> groupOrdersByDateRange(List<OrderDTO> orders) {
        Map<String, List<OrderDTO>> groups = new HashMap<>();
        for (OrderDTO order : orders) {
            String key = order.getRentStartDate().toString() + "|" + order.getRentEndDate().toString();
            groups.computeIfAbsent(key, k -> new ArrayList<>()).add(order);
        }
        return groups;
    }


    private ContractDTO createContractFromOrders(Integer customerId, String customerName, List<OrderDTO> orders) {
        try {
            if (orders.isEmpty()) {
                return null;
            }

            if (orders.isEmpty()) {
                return null;
            }

            LocalDateTime start = orders.get(0).getRentStartDate();
            LocalDateTime end = orders.get(0).getRentEndDate();


            // Tính tổng tiền thuê
            BigDecimal total = calculateTotalAmount(orders);


            // Tính tiền đặt cọc
            BigDecimal deposit = calculateDepositAmount(total);


            // Tạo Contract entity
            model.Contracts contract = new model.Contracts();
            contract.setCustomerId(customerId);
            contract.setStartDate(start);
            contract.setEndDate(end);
            contract.setStatus("PENDING");
            contract.setTotalAmount(total);
            contract.setDepositAmount(deposit);
            contract.setCreateAt(LocalDateTime.now());


            // Lưu contract
            boolean contractSaved = contractsDAO.addContract(contract);
            if (!contractSaved) {
                return null;
            }


            // Lấy contract ID vừa tạo - KHÔNG DÙNG STREAM
            List<Contracts> customerContracts = contractsDAO.getContractByCustomer(customerId);
            model.Contracts savedContract = null;
            for (model.Contracts c : customerContracts) {
                if (c.getStartDate().equals(start) && c.getEndDate().equals(end)) {
                    savedContract = c;
                    break;
                }
            }


            if (savedContract == null) {
                return null;
            }


            Integer contractId = savedContract.getContractId();


            // Tạo contract details và xóa orders khỏi giỏ
            List<ContractDetailDTO> contractDetails = new ArrayList<>();
            for (OrderDTO order : orders) {
                // Tạo contract detail
                model.ContractDetails detail = new model.ContractDetails();
                detail.setContractId(contractId);
                detail.setVehicleId(order.getVehicleId());
                detail.setPrice(order.getPrice());
                detail.setRentStartDate(order.getRentStartDate());
                detail.setRentEndDate(order.getRentEndDate());
                detail.setNote(null);


                // Lưu contract detail (cần implement method này trong DAO)
                boolean ok = contractDetailsDAO.addContractDetail(detail);

                boolean ok = contractDetailsDAO.addContractDetail(detail);

                // Xóa order khỏi giỏ
                ordersDAO.deleteOrder(order.getCartDetailId());


                // Convert to DTO
                ContractDetailDTO detailDTO = contractDetailMapper.toDTO(detail);
                contractDetails.add(detailDTO);
            }


            // Tạo ContractDTO
            ContractDTO contractDTO = contractMapper.toDTO(savedContract);
            contractDTO.setCustomerName(customerName);
            contractDTO.setContractDetails(contractDetails);


            return contractDTO;


        } catch (Exception e) {
            throw new RuntimeException("error.system", e);
        }
    }


    private BigDecimal calculateTotalAmount(List<OrderDTO> orders) {
        BigDecimal total = BigDecimal.ZERO;
        for (OrderDTO order : orders) {
            if (order.getPrice() != null) {
                total = total.add(order.getPrice());
            }
        }
        return total;
    }


    private BigDecimal calculateDepositAmount(BigDecimal total) {
        return new BigDecimal("30000000");
    }



    @Override
    public int countContract() {
        int totalContract = contractsDAO.countContract();
        return totalContract;
    }
        

    @Override
    public void updateContractTotalAmount(Integer contractId, BigDecimal totalAmount) {
        boolean result = contractsDAO.updateContractTotalAmount(contractId, totalAmount);
        if (!result) {
            throw new RuntimeException("Không thể cập nhật tổng tiền hợp đồng mã " + contractId);
        }
    }

    @Override
    public void updateStaffId(Integer staffId, Integer contractId) {
        boolean result = contractsDAO.updateStaffId(staffId, contractId);
        if (!result) {
            throw new RuntimeException("Không thể cập nhật mã nhân viên hợp đồng mã " + contractId);
        }
    }

    public void updateNote(String note, Integer contractId) {
        boolean result = contractsDAO.updateNote(note, contractId);
        if (!result) {
            throw new RuntimeException("Không thể cập nhật ghi chú hợp đồng mã " + contractId);
        }
    }
;

}
