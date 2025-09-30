/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package service.impl;

import dao.CustomersDAO;
import dto.CustomerDTO;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import mapper.CustomerMapper;
import model.Customers;
import util.PasswordUtil;
import service.CustomerService;

/**
 *
 * @author admin
 */
public class CustomerServiceImpl implements CustomerService {

    // Mapper để chuyển đổi giữa DTO và Model
    private CustomerMapper customerMapper;
    // DAO để truy cập cơ sở dữ liệu
    private CustomersDAO customersDAO;
    
    // Constructor để khởi tạo dependencies (tránh NullPointerException)
    public CustomerServiceImpl(CustomersDAO customersDAO, CustomerMapper customerMapper) {
        this.customersDAO = customersDAO;
        this.customerMapper = customerMapper;
    }

    @Override
    public Optional<CustomerDTO> loginCustomer(String username, String password) {
        // Bước 1: Lấy thông tin khách hàng từ database theo username
        try {
            Optional<Customers> oc = customersDAO.getCustomerByUserName(username);
            if (oc.isEmpty()) {
                return Optional.empty(); // Username không tồn tại trong hệ thống
            }

            Customers customer = oc.get();
            
            // Bước 2: Xác thực mật khẩu (so sánh password nhập vào với hash trong DB)
            if (!PasswordUtil.verifyPassword(password, customer.getPasswordHash(), customer.getPasswordSalt())) {
                return Optional.empty(); // Mật khẩu không đúng
            }

            // Bước 3: Chuyển đổi từ Model sang DTO để trả về cho Controller
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

            // Bước 2: Kiểm tra username đã tồn tại chưa
            if (isUsernameExists(customerDTO.getUsername())) {
                return false; // Username đã được sử dụng
            }

            // Bước 3: Kiểm tra email đã tồn tại chưa
            if (isEmailExists(customerDTO.getEmail())) {
                return false; // Email đã được sử dụng
            }

            // Bước 4: Mã hóa mật khẩu với salt ngẫu nhiên
            byte[] hashSalt = PasswordUtil.generateSalt(); // Tạo salt ngẫu nhiên
            String[] hashPassword = PasswordUtil.hashPassword(password, hashSalt); // Hash password
            String passwordHash = hashPassword[0]; // Lấy hash
            String passwordSalt = hashPassword[1]; // Lấy salt

            // Bước 5: Chuyển đổi DTO sang Model
            Customers customer = customerMapper.toUsers(customerDTO);
            customer.setPasswordHash(passwordHash.getBytes()); // Lưu hash password
            customer.setPasswordSalt(passwordSalt.getBytes()); // Lưu salt

            // Bước 6: Lưu khách hàng vào database
            return customersDAO.addCustomer(customer);
        } catch (Exception e) {
            e.printStackTrace();
            return false; // Có lỗi xảy ra trong quá trình đăng ký
        }
    }

    @Override
    public Optional<CustomerDTO> getCustomerByUsername(String username) {
        // Lấy thông tin khách hàng theo username
        try {
            if (username == null) {
                return null; // Username không hợp lệ
            }

            // Tìm khách hàng trong database
            Optional<Customers> oc = customersDAO.getCustomerByUserName(username);
            if (!oc.isPresent()) {
                return Optional.empty(); // Không tìm thấy khách hàng
            }
            
            // Chuyển đổi từ Model sang DTO và trả về
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

            return dto; // Trả về danh sách DTO
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
        // Xóa khách hàng khỏi hệ thống
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
            String[] newHash = PasswordUtil.hashPassword(newPassword, newSalt); // Hash mật khẩu mới
            String newPasswordHash = newHash[0]; // Lấy hash
            String newPasswordSalt = newHash[1]; // Lấy salt

            // Bước 4: Cập nhật mật khẩu mới trong database
            return customersDAO.changePassword(customer.getCustomerId(),
                    newPasswordHash.getBytes(), newPasswordSalt.getBytes());
        } catch (Exception e) {
            e.printStackTrace();
            return false; // Có lỗi xảy ra
        }
    }

    @Override
    public boolean resetCustomerPassword(String username, String newPassword) {
        // Đặt lại mật khẩu cho khách hàng (quên mật khẩu)
        try {
            // Bước 1: Lấy thông tin khách hàng từ database theo username
            Optional<Customers> oc = customersDAO.getCustomerByUserName(username);
            if (!oc.isPresent()) {
                return false; // Không tìm thấy khách hàng
            }

            Customers customer = oc.get();
            
            // Bước 2: Mã hóa mật khẩu mới
            byte[] newSalt = PasswordUtil.generateSalt(); // Tạo salt mới
            String[] newHash = PasswordUtil.hashPassword(newPassword, newSalt); // Hash mật khẩu mới
            String newPasswordHash = newHash[0]; // Lấy hash
            String newPasswordSalt = newHash[1]; // Lấy salt

            // Bước 3: Cập nhật mật khẩu mới trong database
            return customersDAO.changePassword(customer.getCustomerId(),
                    newPasswordHash.getBytes(), newPasswordSalt.getBytes());

        } catch (Exception e) {
            e.printStackTrace();
            return false; // Có lỗi xảy ra
        }
    }

}
