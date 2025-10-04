/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package service.impl;

import dao.CustomersDAO;
import dao.LocationsDAO;
import dto.CustomerDTO;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import mapper.CustomerMapper;
import model.Customers;
import util.PasswordUtil;
import service.CustomerService;
import util.VerificationUtil;
import util.di.annotation.Autowired;
import util.di.annotation.Service;

/**
 *
 * @author admin
 */
@Service
public class CustomerServiceImpl implements CustomerService {

    @Autowired
    private CustomersDAO customersDAO;

    @Autowired
    private CustomerMapper customerMapper;

    @Autowired
    private LocationsDAO locationsDAO;

    @Override
    public Optional<CustomerDTO> loginCustomer(String username, String password) {

        // Bước 1: Lấy thông tin khách hàng từ database theo username
        try {
            Optional<Customers> oc = customersDAO.getCustomerByUserName(username);
            if (oc.isEmpty()) {
                return Optional.empty(); // Username không tồn tại trong hệ thống
            }

            Customers customer = oc.get();

            if (!customer.isIsVerified()) {
                return Optional.empty();
            }
            
            // Bước 2: Xác thực mật khẩu (so sánh password nhập vào với hash trong DB)
            boolean passwordValid = PasswordUtil.verifyPassword(password, customer.getPasswordHash(), customer.getPasswordSalt());
            if (!passwordValid) {
                return Optional.empty(); // Mật khẩu không đúng
            }

            // Buoc 3: Chuyen doi tu Model sang DTO de tra ve cho Controller
            CustomerDTO customerDTO = customerMapper.toDTO(customer);

            return Optional.of(customerDTO);
        } catch (Exception e) {
            e.printStackTrace();
            return Optional.empty(); // Có lỗi xảy ra trong quá trình xử lý
        }
    }

    @Override
    public boolean registerCustomer(CustomerDTO customerDTO, String password) {

        try {
            // Bước 1: Kiểm tra dữ liệu đầu vào
            if (customerDTO == null || password == null) {
                return false; // Dữ liệu không hợp lệ
            }

            // resolve locationId
            Integer locationId = null;
            if (customerDTO.getCity() != null) {
                locationId = locationsDAO.getOrCreateIdByCity(customerDTO.getCity());
            }

            // Bước 4: Mã hóa mật khẩu với salt ngẫu nhiên
            byte[] hashSalt = PasswordUtil.generateSalt(); // Tạo salt ngẫu nhiên
            byte[][] hashPassword = PasswordUtil.hashPassword(password, hashSalt); // Hash password
            byte[] passwordHash = hashPassword[0]; // Lấy hash
            byte[] passwordSalt = hashPassword[1]; // Lấy salt

            String code = VerificationUtil.generateNumbericCode();
            LocalDateTime expireAt = VerificationUtil.expiryAfterMinutes(10);

            // Bước 5: Chuyển đổi DTO sang Model
            Customers customer = customerMapper.toUsers(customerDTO);
            customer.setPasswordHash(passwordHash); // Lưu hash password (byte[])
            customer.setPasswordSalt(passwordSalt); // Lưu salt (byte[])
            customer.setLocationId(locationId);

            customer.setIsVerified(false);
            customer.setVerifyCode(code);
            customer.setVerifyCodeExpire(expireAt);

            // Bước 6: Lưu khách hàng vào database
            return customersDAO.addCustomer(customer);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public Optional<CustomerDTO> getCustomerByUsername(String username) {
        // Lấy thông tin khách hàng theo username
        try {
            if (username == null) {
                return Optional.empty(); // Username không hợp lệ
            }

            // Tìm khách hàng trong database
            Optional<Customers> oc = customersDAO.getCustomerByUserName(username);
            if (!oc.isPresent()) {
                return Optional.empty(); // Không tìm thấy khách hàng
            }

            // Chuyen doi tu Model sang DTO va tra ve
            return Optional.of(customerMapper.toDTO(oc.get()));
        } catch (Exception e) {
            e.printStackTrace();
            return Optional.empty(); // Có lỗi xảy ra
        }
    }

    @Override
    public List<CustomerDTO> getAllCustomers() {
        // Lấy danh sách tất cả khách hàng
        try {
            // Lấy danh sách khách hàng từ database
            List<Customers> customersList = customersDAO.getAllCustomers();
            List<CustomerDTO> dto = new ArrayList<>();

            // Chuyển đổi từng Model sang DTO
            for (Customers c : customersList) {
                dto.add(customerMapper.toDTO(c));
            }

            return dto; // Tra ve danh sach DTO
        } catch (Exception e) {
            e.printStackTrace();
            return null; // Có lỗi xảy ra
        }
    }

    @Override
    public boolean addCustomer(CustomerDTO customerDTO) {
        // Thêm khách hàng mới vào hệ thống
        if (customerDTO == null) {
            return false; // Dữ liệu không hợp lệ
        }

        try {
            // Chuyển đổi DTO sang Model
            Customers customers = customerMapper.toUsers(customerDTO);
            // Lưu vào database
            return customersDAO.addCustomer(customers);
        } catch (Exception e) {
            e.printStackTrace();
            return false; // Có lỗi xảy ra
        }
    }

    @Override
    public boolean updateCustomer(CustomerDTO customerDTO) {
        // Cập nhật thông tin khách hàng
        if (customerDTO == null) {
            return false; // Dữ liệu không hợp lệ
        }
        try {
            // Chuyển đổi DTO sang Model
            Customers customer = customerMapper.toUsers(customerDTO);
            // Cập nhật trong database
            return customersDAO.updateCustomer(customer);
        } catch (Exception e) {
            e.printStackTrace();
            return false; // Có lỗi xảy ra
        }
    }

    @Override
    public boolean deleteCustomer(Integer customerId) {
        // Xoa khach hang khoi he thong
        if (customerId == null) {
            return false; // ID không hợp lệ
        }

        try {
            // Xóa khách hàng trong database
            return customersDAO.deleteCustomer(customerId);
        } catch (Exception e) {
            e.printStackTrace();
            return false; // Có lỗi xảy ra
        }
    }

    @Override
    public boolean isUsernameExists(String username) {
        // Kiểm tra username đã tồn tại chưa
        try {
            return customersDAO.existsUsername(username);
        } catch (Exception e) {
            e.printStackTrace();
            return false; // Có lỗi xảy ra
        }
    }

    @Override
    public boolean isEmailExists(String email) {
        // Kiểm tra email đã tồn tại chưa
        try {
            return customersDAO.existEmail(email);
        } catch (Exception e) {
            e.printStackTrace();
            return false; // Có lỗi xảy ra
        }
    }

    @Override
    public boolean isPhoneExists(String phone) {
        // Kiểm tra số điện thoại đã tồn tại chưa
        try {
            return customersDAO.existPhone(phone);
        } catch (Exception e) {
            e.printStackTrace();
            return false; // Có lỗi xảy ra
        }
    }

    @Override
    public boolean changeCustomerPassword(Integer customerId, String oldPassword, String newPassword) {
        // Thay đổi mật khẩu khách hàng
        try {
            // Bước 1: Lấy thông tin khách hàng từ database
            Optional<Customers> oc = customersDAO.getCustomerById(customerId);
            if (!oc.isPresent()) {
                return false; // Không tìm thấy khách hàng
            }

            Customers customer = oc.get();

            // Bước 2: Xác thực mật khẩu cũ
            if (!PasswordUtil.verifyPassword(oldPassword,
                    customer.getPasswordHash(), customer.getPasswordSalt())) {
                return false; // Mật khẩu cũ không đúng
            }

            // Bước 3: Mã hóa mật khẩu mới
            byte[] newSalt = PasswordUtil.generateSalt(); // Tạo salt mới
            byte[][] newHash = PasswordUtil.hashPassword(newPassword, newSalt); // Hash mật khẩu mới
            byte[] newPasswordHash = newHash[0]; // Lấy hash
            byte[] newPasswordSalt = newHash[1]; // Lấy salt

            // Bước 4: Cập nhật mật khẩu mới trong database
            return customersDAO.changePassword(customer.getCustomerId(),
                    newPasswordHash, newPasswordSalt);
        } catch (Exception e) {
            e.printStackTrace();
            return false; // Có lỗi xảy ra
        }
    }

    @Override
    public boolean resetCustomerPassword(String username, String newPassword) {
        // Dat lai mat khau cho khach hang (quen mat khau)
        try {
            // Bước 1: Lấy thông tin khách hàng từ database theo username
            Optional<Customers> oc = customersDAO.getCustomerByUserName(username);
            if (!oc.isPresent()) {
                return false; // Không tìm thấy khách hàng
            }

            Customers customer = oc.get();

            // Bước 2: Mã hóa mật khẩu mới
            byte[] newSalt = PasswordUtil.generateSalt(); // Tạo salt mới
            byte[][] newHash = PasswordUtil.hashPassword(newPassword, newSalt); // Hash mật khẩu mới
            byte[] newPasswordHash = newHash[0]; // Lấy hash
            byte[] newPasswordSalt = newHash[1]; // Lấy salt

            // Bước 3: Cập nhật mật khẩu mới trong database
            return customersDAO.changePassword(customer.getCustomerId(),
                    newPasswordHash, newPasswordSalt);

        } catch (Exception e) {
            e.printStackTrace();
            return false; // Có lỗi xảy ra
        }
    }

    @Override
    public boolean verifyAccount(String username, String code) {
        try {
            Optional<Customers> oc = customersDAO.getCustomerByUserName(username);
            if (oc.isEmpty()) {
                System.out.println("[DEBUG] User not found: " + username);
                return false;
            }

            Customers c = oc.get();
            
            boolean check = !c.isIsVerified() 
                    && c.getVerifyCode() != null
                    && c.getVerifyCode().equals(code)
                    && c.getVerifyCodeExpire() != null
                    && c.getVerifyCodeExpire().isAfter(LocalDateTime.now());

            if (check) {
                c.setIsVerified(true);
                c.setVerifyCode(null);
                c.setVerifyCodeExpire(null);
                boolean updateResult = customersDAO.updateCustomer(c);
                return updateResult;
            }

            return false;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    @Override
    public Optional<String> generateAndStoreVerificationCode(String username) {
        Optional<Customers> oc = customersDAO.getCustomerByUserName(username);
        if (oc.isEmpty() || oc.get().isIsVerified()) {
            return Optional.empty();
        }

        Customers c = oc.get();
        String code = VerificationUtil.generateNumbericCode();

        c.setVerifyCode(code);
        c.setVerifyCodeExpire(LocalDateTime.now().plusMinutes(10));

        customersDAO.updateCustomer(c);
        return Optional.of(code);
    }

}
