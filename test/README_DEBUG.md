# Contract Debug Tests

Các test này được tạo để debug vấn đề không thấy thông tin hợp đồng và redirect về home khi xem chi tiết.

## Các Test Files

### 1. SimpleContractTest.java

- **Mục đích**: Test ContractService và các method chính
- **Kiểm tra**:
  - `getContractsByCustomer()` - Lấy danh sách contracts của customer
  - `getContractById()` - Lấy chi tiết contract
  - `getContractDetails()` - Lấy contract details
- **Chạy**: `java -cp "src\java;lib\*;build\classes" test.SimpleContractTest`

### 2. SimpleDatabaseTest.java

- **Mục đích**: Kiểm tra dữ liệu trong database
- **Kiểm tra**:
  - Có customers trong database không
  - Có contracts trong database không
  - Customer có contracts không
  - Contract có details không
- **Chạy**: `java -cp "src\java;lib\*;build\classes" test.SimpleDatabaseTest`

### 3. ContractMapperTest.java

- **Mục đích**: Test mapper và DI injection
- **Kiểm tra**:
  - DI injection cho các DAO và Mapper
  - Mapper functionality với dữ liệu mẫu
  - Complete flow từ DAO -> Mapper -> DTO
- **Chạy**: `java -cp "src\java;lib\*;build\classes" test.ContractMapperTest`

### 4. ContractDITest.java

- **Mục đích**: Test DI injection trong ContractService
- **Kiểm tra**:
  - ContractService được inject đúng
  - Các dependencies được inject
  - DI container hoạt động đúng
- **Chạy**: `java -cp "src\java;lib\*;build\classes" test.ContractDITest`

### 5. ContractMappingResultTest.java

- **Mục đích**: Test mapping result từ database
- **Kiểm tra**:
  - Mapping từ database với dữ liệu thực
  - Mapping với edge cases (null, empty, special values)
  - Kiểm tra tất cả fields được map đúng
- **Chạy**: `java -cp "src\java;lib\*;build\classes" test.ContractMappingResultTest`

### 3. ServletSimulationTest.java

- **Mục đích**: Mô phỏng servlet request để test logic
- **Kiểm tra**:
  - ListMyContractsServlet hoạt động như thế nào
  - ViewContractServlet với các trường hợp khác nhau
  - Khi nào servlet redirect về home
- **Chạy**: `java -cp "src\java;lib\*;build\classes" test.ServletSimulationTest`

## Cách Chạy Tất Cả Tests

### Windows:

```bash
run_debug_tests.bat
```

### Manual:

```bash
# Test 1: Contract Service
java -cp "src\java;lib\*;build\classes" test.SimpleContractTest

# Test 2: Database
java -cp "src\java;lib\*;build\classes" test.SimpleDatabaseTest
```

**Lưu ý**: DI Container tự động scan và register các beans, không cần gọi `DIContainer.init()`

## Các Vấn Đề Có Thể Gặp

### 1. Không thấy contracts trong my-contracts

**Nguyên nhân có thể**:

- Database không có contracts
- Customer không có contracts
- Lỗi trong `getContractsByCustomer()`
- Session không có customerId

**Cách debug**:

- Chạy `DatabaseContractTest` để kiểm tra dữ liệu
- Chạy `ContractDebugTest` để kiểm tra service

### 2. Redirect về home khi xem chi tiết

**Nguyên nhân có thể**:

- Không có `contractId` parameter
- `contractId` không hợp lệ (không phải số)
- Contract không tồn tại
- Customer không phải owner của contract

**Cách debug**:

- Chạy `ServletSimulationTest` để xem servlet logic
- Kiểm tra URL có đúng format không: `/view-contract?contractId=123`

### 3. Lỗi trong ContractServiceImpl

**Đã sửa**: Lỗi syntax ở dòng 51 trong `ContractServiceImpl.java`

## Kết Quả Mong Đợi

### ContractDebugTest:

```
✓ ContractService initialized successfully
✓ Tìm thấy contracts: [danh sách contracts]
✓ Contract found: [chi tiết contract]
✓ Contract details found: [danh sách details]
```

### DatabaseContractTest:

```
✓ DAOs initialized successfully
✓ Tổng số customers: [số lượng]
✓ Tổng số contracts: [số lượng]
✓ Customer có contracts: [danh sách]
```

### ServletSimulationTest:

```
✓ SERVLET FORWARD - Hoạt động bình thường
✓ FORWARD TO JSP - Hoạt động bình thường
```

## Troubleshooting

1. **Lỗi ClassNotFoundException**: Kiểm tra classpath có đúng không
2. **Lỗi database connection**: Kiểm tra `db.properties` và database
3. **Lỗi DI Container**: Kiểm tra `DIContainer.init()`
4. **Không có dữ liệu**: Chạy script insert data trước

## Next Steps

Sau khi chạy tests:

1. Xem kết quả để xác định vấn đề
2. Sửa lỗi được phát hiện
3. Test lại để confirm fix
4. Deploy và test trên browser
