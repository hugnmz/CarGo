/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package service.impl;

import dto.CustomerDTO;
import dto.LocationDTO;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import mapper.CustomerMapper;
import model.Customer;
import util.PasswordUtil;
import util.VerificationUtil;
import util.di.annotation.Autowired;
import util.di.annotation.Service;
import util.MessageUtil;
import util.exception.ApplicationException;
import util.exception.DataAccessException;
import util.exception.ValidationException;
import util.exception.BusinessException;
import service.CustomersService;
import dao.LocationDAO;
import dao.CustomerDAO;

/**
 *
 * @author admin
 */
@Service
public class CustomerServiceImpl implements CustomersService {

    @Autowired
    private CustomerDAO customersDAO;

    @Autowired
    private CustomerMapper customerMapper;

    @Autowired
    private LocationDAO locationsDAO;

    @Override
    public Optional<CustomerDTO> loginCustomer(String username, String password) {

        // Bước 1: Lấy thông tin khách hàng từ database theo username
        Optional<Customer> oc = customersDAO.getCustomerByUserName(username);
        if (oc.isEmpty()) {
            throw new ValidationException(MessageUtil.getError("error.login.invalid"));
        }

        Customer customer = oc.get();

        if (!customer.isIsVerified()) {
            throw new ValidationException(MessageUtil.getError("error.account.not.verified"));
        }

        boolean passwordValid = PasswordUtil.verifyPassword(password, customer.getPasswordHash(), customer.getPasswordSalt());
        if (!passwordValid) {
            throw new ValidationException(MessageUtil.getError("error.login.invalid"));
        }

        CustomerDTO customerDTO = customerMapper.toDTO(customer);
        return Optional.of(customerDTO);
    }

    @Override
    public boolean registerCustomer(CustomerDTO customerDTO, String password) {

        try {
            // Bước 1: Kiểm tra dữ liệu đầu vào
            if (customerDTO == null || password == null) {
                throw new ValidationException(MessageUtil.getError("error.validation.data.invalid"));
            }

            // Chuyển check trùng từ servlet vào đây - throw WebException nếu trùng
            if (customerDTO.getUsername() != null && customersDAO.existsUsername(customerDTO.getUsername())) {
                throw new ValidationException(MessageUtil.getError("error.username.exists"));
            }
            if (customerDTO.getEmail() != null && customersDAO.existEmail(customerDTO.getEmail())) {
                String emailError = MessageUtil.getError("error.email.exists").replace("{0}", customerDTO.getEmail());
                throw new ValidationException(emailError);
            }
            if (customerDTO.getPhone() != null && customersDAO.existPhone(customerDTO.getPhone())) {
                String phoneError = MessageUtil.getError("error.phone.exists").replace("{0}", customerDTO.getPhone());
                throw new ValidationException(phoneError);
            }

            Integer locationId = null;
            if (customerDTO.getCity() != null) {
                locationId = locationsDAO.findIdByCity(customerDTO.getCity());
            }

            // Bước 4: Mã hóa mật khẩu với salt ngẫu nhiên
            byte[] hashSalt = PasswordUtil.generateSalt(); // Tạo salt ngẫu nhiên
            byte[][] hashPassword = PasswordUtil.hashPassword(password, hashSalt); // Hash password
            byte[] passwordHash = hashPassword[0]; // Lấy hash
            byte[] passwordSalt = hashPassword[1]; // Lấy salt

            String code = VerificationUtil.generateNumbericCode();
            LocalDateTime expireAt = VerificationUtil.expiryAfterMinutes(10);

            // Bước 5: Chuyển đổi DTO sang Model
            Customer customer = customerMapper.toUsers(customerDTO);
            customer.setPasswordHash(passwordHash); // Lưu hash password (byte[])
            customer.setPasswordSalt(passwordSalt); // Lưu salt (byte[])
            customer.setLocationId(locationId);

            customer.setIsVerified(false);
            customer.setVerifyCode(code);
            customer.setVerifyCodeExpire(expireAt);

            // Bước 6: Lưu khách hàng vào database
            return customersDAO.addCustomer(customer);
        } catch (ApplicationException e) {
            throw e;
        } catch (Exception e) {
            throw new DataAccessException(MessageUtil.getError("error.dataaccess.customer.register.failed"), e);
        }
    }

    @Override
    public Optional<CustomerDTO> getCustomerByUsername(String username) {
        // Lấy thông tin khách hàng theo username
        if (username == null) {
            throw new ValidationException(MessageUtil.getError("error.validation.data.invalid"));
        }

        Optional<Customer> oc = customersDAO.getCustomerByUserName(username);
        if (!oc.isPresent()) {
            throw new ValidationException(MessageUtil.getError("error.validation.customer.not.found"));
        }

        return Optional.of(customerMapper.toDTO(oc.get()));
    }

    @Override
    public Optional<CustomerDTO> getCustomerById(Integer customerId) {
        // Lấy thông tin khách hàng theo ID
        if (customerId == null) {
            throw new ValidationException(MessageUtil.getError("error.validation.data.invalid"));
        }

        Optional<Customer> oc = customersDAO.getCustomerById(customerId);
        if (!oc.isPresent()) {
            throw new ValidationException(MessageUtil.getError("error.validation.customer.not.found"));
        }

        return Optional.of(customerMapper.toDTO(oc.get()));
    }

    @Override
    public List<CustomerDTO> getAllCustomers() {
        // Lấy danh sách tất cả khách hàng
        try {
            // Lấy danh sách khách hàng từ database
            List<Customer> customersList = customersDAO.getAllCustomers();
            List<CustomerDTO> dto = new ArrayList<>();

            // Chuyển đổi từng Model sang DTO
            for (Customer c : customersList) {
                dto.add(customerMapper.toDTO(c));
            }

            return dto; // Tra ve danh sach DTO
        } catch (ApplicationException e) {
            throw e;
        } catch (Exception e) {
            throw new DataAccessException(MessageUtil.getError("error.dataaccess.customer.list.failed"), e);
        }
    }

    @Override
    public boolean addCustomer(CustomerDTO customerDTO) {
        // Thêm khách hàng mới vào hệ thống
        if (customerDTO == null) {
            throw new ValidationException(MessageUtil.getError("error.validation.data.invalid"));
        }

        try {
            // Chuyển đổi DTO sang Model
            Customer customers = customerMapper.toUsers(customerDTO);
            // Lưu vào database
            boolean result = customersDAO.addCustomer(customers);
            if (!result) {
                throw new BusinessException(MessageUtil.getError("error.dataaccess.customer.add.failed"));
            }
            return true;
        } catch (ApplicationException e) {
            throw e;
        } catch (Exception e) {
            throw new DataAccessException(MessageUtil.getError("error.dataaccess.customer.register.failed"), e);
        }
    }

    @Override
    public boolean updateCustomer(CustomerDTO customerDTO) {
        // Cập nhật thông tin khách hàng
        if (customerDTO == null) {
            throw new ValidationException(MessageUtil.getError("error.validation.data.invalid"));
        }
        Optional<Customer> existingCustomer = customersDAO.getCustomerById(customerDTO.getCustomerId());
        if (existingCustomer.isEmpty()) {
            throw new ValidationException(MessageUtil.getError("error.validation.customer.not.found"));
        }

        Customer existing = existingCustomer.get();

        if (customerDTO.getFullName() != null) {
            existing.setFullName(customerDTO.getFullName());
        }
        if (customerDTO.getEmail() != null) {
            String newEmail = customerDTO.getEmail().trim();
            if (!newEmail.equalsIgnoreCase(existing.getEmail())) {
                if (isEmailExists(newEmail)) {
                    String emailError = MessageUtil.getError("error.email.exists").replace("{0}", newEmail);
                    throw new ValidationException(emailError);
                }
                existing.setEmail(newEmail);
            }
        }
        if (customerDTO.getPhone() != null) {
            String newPhone = customerDTO.getPhone().trim();
            if (!newPhone.equals(existing.getPhone())) {
                if (isPhoneExists(newPhone)) {
                   String phoneError = MessageUtil.getError("error.phone.exists").replace("{0}", newPhone);
                   throw new ValidationException(phoneError);
                }
                existing.setPhone(newPhone);
            }
        }
        if (customerDTO.getDateOfBirth() != null) {
            existing.setDateOfBirth(customerDTO.getDateOfBirth());
        }

        if (customerDTO.getIsVerified() != null) {
            existing.setIsVerified(customerDTO.getIsVerified());
        }

        if (customerDTO.getLocationId() != null) {
            existing.setLocationId(customerDTO.getLocationId());
        } else if (customerDTO.getCity() != null) {
            Integer locationId = locationsDAO.findIdByCity(customerDTO.getCity());
            existing.setLocationId(locationId);
        }

        boolean updated = customersDAO.updateCustomer(existing);
        if (!updated) {
            throw new DataAccessException(MessageUtil.getError("error.dataaccess.customer.update.failed"));
        }
        return true;
    }

    @Override
    public boolean deleteCustomer(Integer customerId) {
        // Xoa khach hang khoi he thong
        if (customerId == null) {
            throw new ValidationException(MessageUtil.getError("error.validation.data.invalid"));
        }

        try {
            // Xóa khách hàng trong database
            boolean result = customersDAO.deleteCustomer(customerId);
            if (!result) {
                throw new BusinessException(MessageUtil.getError("error.dataaccess.customer.delete.failed"));
            }
            return true;
        } catch (ApplicationException e) {
            throw e;
        } catch (Exception e) {
            throw new DataAccessException(MessageUtil.getError("error.dataaccess.customer.delete.failed"), e);
        }
    }

    @Override
    public boolean isUsernameExists(String username) {
        // Kiểm tra username đã tồn tại chưa
        try {
            return customersDAO.existsUsername(username);
        } catch (ApplicationException e) {
            throw e;
        } catch (Exception e) {
            throw new DataAccessException(MessageUtil.getError("error.system"), e);
        }
    }

    @Override
    public boolean isEmailExists(String email) {
        // Kiểm tra email đã tồn tại chưa
        try {
            return customersDAO.existEmail(email);
        } catch (ApplicationException e) {
            throw e;
        } catch (Exception e) {
            throw new DataAccessException(MessageUtil.getError("error.system"), e);
        }
    }

    @Override
    public boolean isPhoneExists(String phone) {
        // Kiểm tra số điện thoại đã tồn tại chưa
        try {
            return customersDAO.existPhone(phone);
        } catch (ApplicationException e) {
            throw e;
        } catch (Exception e) {
            throw new DataAccessException(MessageUtil.getError("error.system"), e);
        }
    }

    @Override
    public boolean changeCustomerPassword(Integer customerId, String oldPassword, String newPassword) {
        // Thay đổi mật khẩu khách hàng
        try {
            if (oldPassword.equals(newPassword)) {
                throw new ValidationException(MessageUtil.getError("error.password.change.failed"));
            }

            // Bước 1: Lấy thông tin khách hàng từ database
            Optional<Customer> oc = customersDAO.getCustomerById(customerId);
            if (!oc.isPresent()) {
                throw new ValidationException(MessageUtil.getError("error.validation.customer.not.found"));
            }

            Customer customer = oc.get();

            // Bước 2: Xác thực mật khẩu cũ
            if (!PasswordUtil.verifyPassword(oldPassword,
                    customer.getPasswordHash(), customer.getPasswordSalt())) {
                throw new ValidationException(MessageUtil.getError("error.password.change.failed"));
            }

            // Bước 3: Mã hóa mật khẩu mới
            byte[] newSalt = PasswordUtil.generateSalt(); // Tạo salt mới
            byte[][] newHash = PasswordUtil.hashPassword(newPassword, newSalt); // Hash mật khẩu mới
            byte[] newPasswordHash = newHash[0]; // Lấy hash
            byte[] newPasswordSalt = newHash[1]; // Lấy salt

            // Bước 4: Cập nhật mật khẩu mới trong database
            boolean result = customersDAO.changePassword(customer.getCustomerId(),
                    newPasswordHash, newPasswordSalt);
            if (!result) {
                throw new BusinessException(MessageUtil.getError("error.password.change.failed"));
            }
            return true;
        } catch (ApplicationException e) {
            throw e;
        } catch (Exception e) {
            throw new DataAccessException(MessageUtil.getError("error.password.change.failed"), e);
        }
    }

    @Override
    public boolean resetCustomerPassword(String username, String newPassword) {
        // Dat lai mat khau cho khach hang (quen mat khau)
        try {
            // Bước 1: Lấy thông tin khách hàng từ database theo username
            Optional<Customer> oc = customersDAO.getCustomerByUserName(username);
            if (!oc.isPresent()) {
                throw new ValidationException(MessageUtil.getError("error.validation.customer.not.found"));
            }

            Customer customer = oc.get();

            // Bước 2: Mã hóa mật khẩu mới
            byte[] newSalt = PasswordUtil.generateSalt(); // Tạo salt mới
            byte[][] newHash = PasswordUtil.hashPassword(newPassword, newSalt); // Hash mật khẩu mới
            byte[] newPasswordHash = newHash[0]; // Lấy hash
            byte[] newPasswordSalt = newHash[1]; // Lấy salt

            // Bước 3: Cập nhật mật khẩu mới trong database
            boolean result = customersDAO.changePassword(customer.getCustomerId(),
                    newPasswordHash, newPasswordSalt);
            if (!result) {
                throw new BusinessException(MessageUtil.getError("error.password.change.failed"));
            }
            return true;

        } catch (ApplicationException e) {
            throw e;
        } catch (Exception e) {
            throw new DataAccessException(MessageUtil.getError("error.password.change.failed"), e);
        }
    }

    @Override
    public boolean verifyAccount(String username, String code) {
        try {
            Optional<Customer> oc = customersDAO.getCustomerByUserName(username);
            if (oc.isEmpty()) {
                throw new ValidationException(MessageUtil.getError("error.validation.customer.not.found"));
            }

            Customer c = oc.get();

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
                if (!updateResult) {
                    throw new BusinessException(MessageUtil.getError("error.verification.code.failed"));
                }
                return true;
            }

            throw new ValidationException(MessageUtil.getError("error.verification.code.wrong"));
        } catch (ApplicationException e) {
            throw e;
        } catch (Exception e) {
            throw new DataAccessException(MessageUtil.getError("error.verification.code.failed"), e);
        }
    }

    @Override
    public Optional<String> generateAndStoreVerificationCode(String username) {
        Optional<Customer> oc = customersDAO.getCustomerByUserName(username);
        if (oc.isEmpty() || oc.get().isIsVerified()) {
            return Optional.empty();
        }

        Customer c = oc.get();
        String code = VerificationUtil.generateNumbericCode();

        c.setVerifyCode(code);
        c.setVerifyCodeExpire(LocalDateTime.now().plusMinutes(10));

        customersDAO.updateCustomer(c);
        return Optional.of(code);
    }

    @Override
    public Optional<CustomerDTO> getCustomerByUsernameAndEmail(String username, String email) {
        try {
            Optional<Customer> oc = customersDAO.getCustomerByUsernameAndEmail(username, email);
            if (!oc.isPresent()) {
                return Optional.empty();
            }
            return Optional.of(customerMapper.toDTO(oc.get()));
        } catch (ApplicationException e) {
            throw e;
        } catch (Exception e) {
            throw new DataAccessException(MessageUtil.getError("error.system"), e);
        }
    }

    @Override
    public Optional<String> generateAndStoreResetCode(String username) {
        try {
            Optional<Customer> oc = customersDAO.getCustomerByUserName(username);
            if (!oc.isPresent()) {
                return Optional.empty();
            }

            Customer customer = oc.get();
            String code = VerificationUtil.generateNumbericCode(); // 6 digits
            LocalDateTime expireAt = LocalDateTime.now().plusMinutes(15); // 15 minutes

            customer.setVerifyCode(code);
            customer.setVerifyCodeExpire(expireAt);

            if (customersDAO.updateCustomer(customer)) {
                return Optional.of(code);
            }
            throw new BusinessException(MessageUtil.getError("error.forgot.password.code.generation.failed"));
        } catch (ApplicationException e) {
            throw e;
        } catch (Exception e) {
            throw new DataAccessException(MessageUtil.getError("error.forgot.password.code.generation.failed"), e);
        }
    }

    @Override
    public boolean resetPasswordWithCode(String username, String code, String newPassword) {
        try {
            Optional<Customer> oc = customersDAO.getCustomerByUserName(username);
            if (!oc.isPresent()) {
                throw new ValidationException(MessageUtil.getError("error.validation.customer.not.found"));
            }

            Customer customer = oc.get();

            // Verify code
            if (customer.getVerifyCode() == null
                    || !customer.getVerifyCode().equals(code)
                    || customer.getVerifyCodeExpire() == null
                    || customer.getVerifyCodeExpire().isBefore(LocalDateTime.now())) {
                throw new ValidationException(MessageUtil.getError("error.reset.password.code.expired"));
            }

            // Hash new password
            byte[] newSalt = PasswordUtil.generateSalt();
            byte[][] newHash = PasswordUtil.hashPassword(newPassword, newSalt);
            byte[] newPasswordHash = newHash[0];
            byte[] newPasswordSalt = newHash[1];

            // Update password
            boolean success = customersDAO.changePassword(customer.getCustomerId(),
                    newPasswordHash, newPasswordSalt);

            if (success) {
                // Clear reset code
                customer.setVerifyCode(null);
                customer.setVerifyCodeExpire(null);
                customersDAO.updateCustomer(customer);
                return true;
            }

            throw new BusinessException(MessageUtil.getError("error.password.change.failed"));
        } catch (ApplicationException e) {
            throw e;
        } catch (Exception e) {
            throw new DataAccessException(MessageUtil.getError("error.reset.password.system.error"), e);
        }
    }

    @Override
    public List<LocationDTO> getAllLocation() {
        try {
            // Lấy toàn bộ danh sách Location từ Database
            var locations = locationsDAO.getAllLocations();

            // Chuyển sang DTO
            return locations.stream()
                    .map(loc -> {
                        LocationDTO dto = new LocationDTO();
                        dto.setLocationId(loc.getLocationId());
                        dto.setCity(loc.getCity());
                        dto.setAddress(loc.getAddress());
                        return dto;
                    })
                    .toList();

        } catch (ApplicationException e) {
            throw e;
        } catch (Exception e) {
            throw new DataAccessException(MessageUtil.getError("error.system"), e);
        }

    }

    @Override
    public int countCustomer() {
        return customersDAO.countCustomer();
    }
}
