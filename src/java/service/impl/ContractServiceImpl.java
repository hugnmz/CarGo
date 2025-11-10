package service.impl;

import dao.CarsDAO;
import dao.ContractsDAO;
import dao.ContractDetailsDAO;
import dao.OrdersDAO;
import dao.VehiclesDAO;
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
import model.ContractDetail;
import model.Contract;
import model.Customer;
import service.ContractService;
import util.di.annotation.Autowired;
import util.di.annotation.Service;
import util.MessageUtil;
import util.exception.ApplicationException;
import util.exception.DataAccessException;
import util.exception.BusinessException;
import dao.UserDAO;
import dao.CustomerDAO;

@Service
public class ContractServiceImpl implements ContractService {

    @Autowired
    private ContractsDAO contractsDAO;

    @Autowired
    private ContractDetailsDAO contractDetailsDAO;

    @Autowired
    private CustomerDAO customersDAO;

    @Autowired
    private OrdersDAO ordersDAO;

    @Autowired
    private ContractMapper contractMapper;

    @Autowired
    private ContractDetailMapper contractDetailMapper;

    @Autowired
    private OrderMapper orderMapper;

    @Autowired
    private VehiclesDAO vehiclesDAO;

    @Autowired
    private CarsDAO carsDAO;

    @Autowired
    private UserDAO usersDAO;

    @Override
    public List<ContractDTO> getContractsByCustomer(Integer customerId) {
        List<ContractDTO> contractDTOs = new ArrayList<>();

        // Lấy danh sách contracts từ DAO
        List<Contract> contracts = contractsDAO.getContractByCustomer(customerId);

        for (Contract contract : contracts) {
            ContractDTO dto = contractMapper.toDTO(contract);
            contractDTOs.add(dto);
        }

        return contractDTOs;
    }

    @Override
    public Optional<ContractDTO> getContractById(Integer contractId) {
        Optional<Contract> contract = contractsDAO.getContractById(contractId);
        if (contract.isPresent()) {
            ContractDTO dto = contractMapper.toDTO(contract.get());

            Optional<Customer> customer = customersDAO.getCustomerById(dto.getCustomerId());
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

        List<ContractDetail> details = contractDetailsDAO.getContractDetailsByContractId(contractId);

        for (ContractDetail detail : details) {
            Optional<model.Vehicle> vehicle = vehiclesDAO.getVehicleById(detail.getVehicleId());
            vehicle.ifPresent(c -> detail.setVehicle(c));
            Optional<model.Cars> car = carsDAO.getCarById(detail.getVehicle().getCarId());
            car.ifPresent(c -> detail.getVehicle().setCar(c));
            ContractDetailDTO dto = contractDetailMapper.toDTO(detail);
            detailDTOs.add(dto);
        }

        return detailDTOs;
    }

    @Override
    public boolean updateContractStatus(Integer contractId, String status) {
        try {
            boolean result = contractsDAO.updateContractStatus(contractId, status);
            if (!result) {
                throw new BusinessException(MessageUtil.getError("error.dataaccess.contract.update.failed"));
            }
            return true;
        } catch (ApplicationException e) {
            throw e;
        } catch (Exception e) {
            throw new DataAccessException(MessageUtil.getError("error.dataaccess.contract.update.failed"), e);
        }
    }

    @Override
    public boolean calculateTotalAmount(Integer contractId) {
        throw new UnsupportedOperationException("Not supported yet."); // Generated from nbfs://nbhost/SystemFileSystem/Templates/Classes/Code/GeneratedMethodBody
    }

    @Override
    public boolean deleteContract(Integer contractId) {
        try {
            contractDetailsDAO.deleteContractDetailByContractId(contractId);
            boolean deleteContract = contractsDAO.deleteContract(contractId);
            if (!deleteContract) {
                throw new BusinessException(MessageUtil.getError("error.dataaccess.contract.delete.failed"));
            }
            return true;
        } catch (ApplicationException e) {
            throw e;
        } catch (Exception e) {
            throw new DataAccessException(MessageUtil.getError("error.dataaccess.contract.delete.failed"), e);
        }
    }

    @Override
    public List<ContractDTO> getContractsByStaff(Integer staffId) {
        List<ContractDTO> contractDTOs = new ArrayList<>();
        try {
            List<Contract> contracts = contractsDAO.getContractByStaff(staffId);
            for (Contract contract : contracts) {
                ContractDTO dto = contractMapper.toDTO(contract);
                Optional<Customer> customer = customersDAO.getCustomerById(dto.getCustomerId());
                customer.ifPresent(c -> dto.setCustomerName(c.getFullName()));
                contractDTOs.add(dto);
            }
        } catch (ApplicationException e) {
            throw e;
        } catch (Exception e) {
            throw new DataAccessException(MessageUtil.getError("error.dataaccess.contract.list.failed"), e);
        }
        return contractDTOs;
    }

    @Override
    public List<ContractDTO> getAllContracts() {
        List<ContractDTO> contractDTOs = new ArrayList<>();

        try {
            List<model.Contract> contracts = contractsDAO.getAllContracts();
            for (Contract contract : contracts) {
                ContractDTO dto = contractMapper.toDTO(contract);
                Optional<model.Customer> customer = customersDAO.getCustomerById(dto.getCustomerId());
                customer.ifPresent(c -> dto.setCustomerName(c.getFullName()));
                customer.ifPresent(c -> dto.setCustomerPhone(c.getPhone()));
                Optional<model.User> staff = usersDAO.getUserById(dto.getStaffId());
                staff.ifPresent(s -> dto.setStaffName(s.getFullName()));
                contractDTOs.add(dto);
            }
        } catch (ApplicationException e) {
            throw e;
        } catch (Exception e) {
            throw new DataAccessException(MessageUtil.getError("error.dataaccess.contract.list.failed"), e);
        }
        return contractDTOs;
    }

    @Override
    public boolean createContract(ContractDTO contractDTO) {
        try {
            model.Contract contract = contractMapper.toModel(contractDTO);
            if (contract.getCreateAt() == null) {
                contract.setCreateAt(LocalDateTime.now());
            }
            if (contract.getStatus() != null) {
                contract.setStatus(contract.getStatus().toUpperCase());
            }

            Integer staffId = contractsDAO.findLeastLoadedStaffId();
            contract.setStaffId(staffId);
            boolean result = contractsDAO.addContract(contract);
            if (!result) {
                throw new BusinessException(MessageUtil.getError("error.dataaccess.contract.create.failed"));
            }
            return true;
        } catch (ApplicationException e) {
            throw e;
        } catch (Exception e) {
            throw new DataAccessException(MessageUtil.getError("error.dataaccess.contract.create.failed"), e);
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

            // 3. Chọn staff một lần cho toàn bộ phiên tạo hợp đồng
            // Staff được chọn là staff có số lượng hợp đồng PENDING ít nhất
            // Nếu có nhiều staff cùng số lượng, random chọn một
            Integer assignedStaffId = contractsDAO.findLeastLoadedStaffId();
            if (assignedStaffId == null) {
                throw new BusinessException(MessageUtil.getError("error.dataaccess.staff.not.found"));
            }

            // 4. Nhóm orders theo (startDate, endDate)
            Map<String, List<OrderDTO>> groups = groupOrdersByDateRange(selectedOrders);

            // 5. Tạo hợp đồng cho từng nhóm - dùng cùng 1 staff đã chọn
            for (Map.Entry<String, List<OrderDTO>> entry : groups.entrySet()) {
                List<OrderDTO> orders = entry.getValue();
                if (orders.isEmpty()) {
                    continue;
                }

                ContractDTO contractDTO = createContractFromOrders(customerId, customerName, orders, assignedStaffId);
                if (contractDTO != null) {
                    createdContracts.add(contractDTO);
                }
            }

        } catch (ApplicationException e) {
            throw e;
        } catch (Exception e) {
            throw new DataAccessException(MessageUtil.getError("error.dataaccess.contract.create.failed"), e);
        }

        return createdContracts;
    }

    private String getCustomerName(Integer customerId) {
        try {
            Optional<model.Customer> customer = customersDAO.getCustomerById(customerId);
            if (customer.isPresent() && customer.get().getFullName() != null) {
                return customer.get().getFullName();
            }
        } catch (ApplicationException e) {
            throw e;
        } catch (Exception e) {
            throw new DataAccessException(MessageUtil.getError("error.system"), e);
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
        } catch (ApplicationException e) {
            throw e;
        } catch (Exception e) {
            throw new DataAccessException(MessageUtil.getError("error.system"), e);
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

    private ContractDTO createContractFromOrders(Integer customerId, String customerName, List<OrderDTO> orders, Integer staffId) {
        try {
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
            model.Contract contract = new model.Contract();
            contract.setCustomerId(customerId);
            contract.setStartDate(start);
            contract.setEndDate(end);
            contract.setStatus("PENDING");
            contract.setTotalAmount(total);
            contract.setDepositAmount(deposit);
            contract.setCreateAt(LocalDateTime.now());

            // Sử dụng staffId đã được chọn từ đầu phiên
            contract.setStaffId(staffId);

            // Lưu contract
            boolean contractSaved = contractsDAO.addContract(contract);
            if (!contractSaved) {
                return null;
            }

            // Lấy contract ID vừa tạo
            List<Contract> customerContracts = contractsDAO.getContractByCustomer(customerId);
            model.Contract savedContract = null;
            for (model.Contract c : customerContracts) {
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
                ContractDetail detail = new ContractDetail();
                detail.setContractId(contractId);
                detail.setVehicleId(order.getVehicleId());
                detail.setPrice(order.getPrice());
                detail.setRentStartDate(order.getRentStartDate());
                detail.setRentEndDate(order.getRentEndDate());
                detail.setNote(null);

                // Lưu contract detail (cần implement method này trong DAO)
                contractDetailsDAO.addContractDetail(detail);

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

        } catch (ApplicationException e) {
            throw e;
        } catch (Exception e) {
            throw new DataAccessException(MessageUtil.getError("error.dataaccess.contract.create.failed"), e);
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
        return new BigDecimal("5000");
    }

    @Override
    public int countContract() {
        int totalContract = contractsDAO.countContract();
        return totalContract;
    }

    @Override
    public void updateContractTotalAmount(Integer contractId, BigDecimal totalAmount) {
        try {
            boolean result = contractsDAO.updateContractTotalAmount(contractId, totalAmount);
            if (!result) {
                throw new BusinessException(MessageUtil.getError("error.dataaccess.contract.update.failed"));
            }
        } catch (ApplicationException e) {
            throw e;
        } catch (Exception e) {
            throw new DataAccessException(MessageUtil.getError("error.dataaccess.contract.update.failed"), e);
        }
    }

    @Override
    public void updateStaffId(Integer staffId, Integer contractId) {
        try {
            boolean result = contractsDAO.updateStaffId(staffId, contractId);
            if (!result) {
                throw new BusinessException(MessageUtil.getError("error.dataaccess.contract.update.failed"));
            }
        } catch (ApplicationException e) {
            throw e;
        } catch (Exception e) {
            throw new DataAccessException(MessageUtil.getError("error.dataaccess.contract.update.failed"), e);
        }
    }

    public void updateNote(String note, Integer contractId) {
        try {
            boolean result = contractsDAO.updateNote(note, contractId);
            if (!result) {
                throw new BusinessException(MessageUtil.getError("error.dataaccess.contract.update.failed"));
            }
        } catch (ApplicationException e) {
            throw e;
        } catch (Exception e) {
            throw new DataAccessException(MessageUtil.getError("error.dataaccess.contract.update.failed"), e);
        }
    }

    @Override
    public boolean updateContractStatus(Integer contractId, String status, String reason) {
        try {
            boolean result = contractsDAO.updateContractStatus(contractId, status, reason);
            if (!result) {
                throw new BusinessException(MessageUtil.getError("error.dataaccess.contract.update.failed"));
            }
            return true;
        } catch (ApplicationException e) {
            throw e;
        } catch (Exception e) {
            throw new DataAccessException(MessageUtil.getError("error.dataaccess.contract.update.failed"), e);
        }
    }

}
