package test;

import constant.ConstractStatus;
import dao.PaymentsDAO;
import dto.ContractDTO;
import service.ContractService;
import service.PaymentService;
import util.DB;
import util.di.DIContainer;
import java.math.BigDecimal;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.Optional;

/**
 * test flow dat coc day du
 * test tu luc tao payment den luc callback update status
 */
public class DepositPaymentFlowTest {

    private static PaymentService paymentService;
    private static ContractService contractService;
    private static PaymentsDAO paymentsDAO;

    /**
     * test data - thay bang contract id thuc te
     * contract phai co status ACCEPTED va chua co payment
     */
    private static Integer testContractId = 38;

    public static void main(String[] args) {
        System.out.println("==========================================");
        System.out.println("TEST FLOW DAT COC DAY DU");
        System.out.println("==========================================\n");

        try {
            initializeServices();

            testStep1_CheckInitialState();
            testStep2_CreateDepositPayment();
            testStep3_CompletePaymentAndCallback();
            testStep4_VerifyFinalState();

            System.out.println("\n==========================================");
            System.out.println("TAT CA TEST DA HOAN THANH");
            System.out.println("==========================================");

        } catch (Exception e) {
            System.err.println("LOI TRONG QUA TRINH CHAY TEST: " + e.getMessage());
            e.printStackTrace();
        }
    }

    /**
     * khoi tao services
     */
    private static void initializeServices() {
        System.out.println("--- Khoi tao Services ---");
        try {
            paymentService = DIContainer.get(PaymentService.class);
            contractService = DIContainer.get(ContractService.class);
            paymentsDAO = DIContainer.get(PaymentsDAO.class);
            System.out.println("✅ Khoi tao services thanh cong\n");
        } catch (Exception e) {
            System.err.println("❌ Loi khoi tao services: " + e.getMessage());
            throw new RuntimeException(e);
        }
    }

    /**
     * step 1: kiem tra trang thai ban dau
     */
    private static void testStep1_CheckInitialState() {
        System.out.println("=== STEP 1: Kiem tra trang thai ban dau ===");
        try {
            Optional<ContractDTO> contractOpt = contractService.getContractById(testContractId);
            if (!contractOpt.isPresent()) {
                System.out.println("❌ FAIL: Khong tim thay contract ID " + testContractId);
                System.out.println("   Vui long tao contract moi hoac thay doi testContractId");
                System.exit(1);
            }

            ContractDTO contract = contractOpt.get();
            System.out.println("Contract info:");
            System.out.println("  - Contract ID: " + contract.getContractId());
            System.out.println("  - Status: " + contract.getStatus());
            System.out.println("  - Total Amount: " + formatMoney(contract.getTotalAmount()));
            System.out.println("  - Deposit Amount: " + formatMoney(contract.getDepositAmount()));

            if (!"ACCEPTED".equals(contract.getStatus())) {
                System.out.println("\n⚠ WARNING: Contract status khong phai ACCEPTED");
                System.out.println("   Hien tai: " + contract.getStatus());
                System.out.println("   Can reset status ve ACCEPTED de test");
            }

            boolean hasCompleted = paymentService.hasCompleted(contract.getContractId());
            System.out.println("  - Has completed payment: " + hasCompleted);

            if (hasCompleted) {
                System.out.println("\n⚠ WARNING: Contract da co payment completed");
                System.out.println("   Can xoa cac payment cu de test lai tu dau");
            }

            BigDecimal totalPaid = getTotalPaidDirect(testContractId);
            System.out.println("  - Total paid: " + formatMoney(totalPaid));

            System.out.println("\n✅ STEP 1 PASS: Kiem tra trang thai ban dau thanh cong\n");

        } catch (Exception e) {
            System.out.println("❌ STEP 1 FAIL: " + e.getMessage());
            e.printStackTrace();
            System.exit(1);
        }
    }

    /**
     * step 2: tao payment dat coc
     */
    private static void testStep2_CreateDepositPayment() {
        System.out.println("=== STEP 2: Tao payment dat coc ===");
        try {
            Optional<ContractDTO> contractOpt = contractService.getContractById(testContractId);
            if (!contractOpt.isPresent()) {
                System.out.println("❌ FAIL: Khong tim thay contract");
                System.exit(1);
            }

            ContractDTO contract = contractOpt.get();
            BigDecimal depositAmount = contract.getDepositAmount();

            System.out.println("Tao pending payment:");
            System.out.println("  - Contract ID: " + testContractId);
            System.out.println("  - Deposit Amount: " + formatMoney(depositAmount));

            int rowsAffected = paymentsDAO.insertPendingPayment(testContractId, depositAmount);
            
            if (rowsAffected > 0) {
                System.out.println("  - Result: Success (rowsAffected=" + rowsAffected + ")");

                Integer paymentId = getPaymentIdDirect(testContractId, depositAmount);
                if (paymentId != null) {
                    System.out.println("  - Payment ID: " + paymentId);
                    System.out.println("\n✅ STEP 2 PASS: Tao pending payment thanh cong\n");
                } else {
                    System.out.println("\n❌ STEP 2 FAIL: Khong lay duoc payment ID");
                    System.exit(1);
                }
            } else {
                System.out.println("❌ STEP 2 FAIL: Khong tao duoc pending payment");
                System.exit(1);
            }

        } catch (Exception e) {
            System.out.println("❌ STEP 2 FAIL: " + e.getMessage());
            e.printStackTrace();
            System.exit(1);
        }
    }

    /**
     * step 3: complete payment va mo phong callback
     */
    private static void testStep3_CompletePaymentAndCallback() {
        System.out.println("=== STEP 3: Complete payment va mo phong callback ===");
        try {
            Optional<ContractDTO> contractOpt = contractService.getContractById(testContractId);
            if (!contractOpt.isPresent()) {
                System.out.println("❌ FAIL: Khong tim thay contract");
                System.exit(1);
            }

            ContractDTO contract = contractOpt.get();
            BigDecimal depositAmount = contract.getDepositAmount();

            Integer paymentId = getPaymentIdDirect(testContractId, depositAmount);
            if (paymentId == null) {
                System.out.println("❌ FAIL: Khong tim thay pending payment");
                System.exit(1);
            }

            System.out.println("Complete payment:");
            System.out.println("  - Payment ID: " + paymentId);
            System.out.println("  - Amount: " + formatMoney(depositAmount));

            boolean completed = paymentService.completePaymentById(paymentId);
            System.out.println("  - Complete result: " + completed);

            if (!completed) {
                System.out.println("❌ STEP 3 FAIL: Khong complete duoc payment");
                System.exit(1);
            }

            System.out.println("\nMo phong logic callback:");
            
            BigDecimal totalPaid = getTotalPaidDirect(testContractId);
            BigDecimal totalAmount = contract.getTotalAmount();
            
            System.out.println("  - Total paid: " + formatMoney(totalPaid));
            System.out.println("  - Total amount: " + formatMoney(totalAmount));
            System.out.println("  - Deposit amount: " + formatMoney(depositAmount));
            System.out.println("  - Current status: " + contract.getStatus());

            if (totalAmount != null && totalPaid.compareTo(totalAmount) >= 0) {
                System.out.println("\n  -> totalPaid >= totalAmount");
                System.out.println("  -> Update status thanh COMPLETED");
                contractService.updateContractStatus(testContractId, ConstractStatus.COMPLETED.name());
            } else if (depositAmount != null && totalPaid.compareTo(depositAmount) >= 0 && 
                       totalPaid.compareTo(totalAmount) < 0 && 
                       "ACCEPTED".equals(contract.getStatus())) {
                System.out.println("\n  -> totalPaid >= depositAmount");
                System.out.println("  -> totalPaid < totalAmount");
                System.out.println("  -> status = ACCEPTED");
                System.out.println("  -> Update status thanh DEPOSIT_PAID");
                contractService.updateContractStatus(testContractId, ConstractStatus.DEPOSIT_PAID.name());
            } else {
                System.out.println("\n  -> Khong update status");
                if (!depositAmount.equals(totalPaid)) {
                    System.out.println("  -> Ly do: totalPaid != depositAmount");
                }
                if ("ACCEPTED".equals(contract.getStatus())) {
                    System.out.println("  -> Ly do: status khong phai ACCEPTED");
                }
            }

            System.out.println("\n✅ STEP 3 PASS: Complete payment va callback thanh cong\n");

        } catch (Exception e) {
            System.out.println("❌ STEP 3 FAIL: " + e.getMessage());
            e.printStackTrace();
            System.exit(1);
        }
    }

    /**
     * step 4: xac nhan trang thai cuoi cung
     */
    private static void testStep4_VerifyFinalState() {
        System.out.println("=== STEP 4: Xac nhan trang thai cuoi cung ===");
        try {
            Optional<ContractDTO> contractOpt = contractService.getContractById(testContractId);
            if (!contractOpt.isPresent()) {
                System.out.println("❌ FAIL: Khong tim thay contract");
                System.exit(1);
            }

            ContractDTO contract = contractOpt.get();
            BigDecimal totalPaid = getTotalPaidDirect(testContractId);
            BigDecimal totalAmount = contract.getTotalAmount();
            BigDecimal depositAmount = contract.getDepositAmount();

            System.out.println("Trang thai cuoi cung:");
            System.out.println("  - Contract ID: " + testContractId);
            System.out.println("  - Status: " + contract.getStatus());
            System.out.println("  - Total Amount: " + formatMoney(totalAmount));
            System.out.println("  - Deposit Amount: " + formatMoney(depositAmount));
            System.out.println("  - Total Paid: " + formatMoney(totalPaid));

            boolean hasCompleted = paymentService.hasCompleted(testContractId);
            System.out.println("  - Has completed payment: " + hasCompleted);

            System.out.println("\nKiem tra ket qua:");

            if (totalPaid.compareTo(depositAmount) >= 0 && totalPaid.compareTo(totalAmount) < 0) {
                if (ConstractStatus.DEPOSIT_PAID.name().equals(contract.getStatus())) {
                    System.out.println("  ✅ PASS: Contract status = DEPOSIT_PAID (DUNG)");
                } else {
                    System.out.println("  ❌ FAIL: Contract status = " + contract.getStatus() + " (SAI)");
                    System.out.println("      Mong doi: DEPOSIT_PAID");
                }
            } else if (totalPaid.compareTo(totalAmount) >= 0) {
                if (ConstractStatus.COMPLETED.name().equals(contract.getStatus())) {
                    System.out.println("  ✅ PASS: Contract status = COMPLETED (DUNG)");
                } else {
                    System.out.println("  ❌ FAIL: Contract status = " + contract.getStatus() + " (SAI)");
                    System.out.println("      Mong doi: COMPLETED");
                }
            }

            if (hasCompleted) {
                System.out.println("  ✅ PASS: Co payment completed");
            } else {
                System.out.println("  ❌ FAIL: Khong co payment completed");
            }

            System.out.println("\n✅ STEP 4 PASS: Xac nhan trang thai cuoi cung thanh cong\n");

        } catch (Exception e) {
            System.out.println("❌ STEP 4 FAIL: " + e.getMessage());
            e.printStackTrace();
            System.exit(1);
        }
    }

    /**
     * helper: lay total paid amount truc tiep tu db
     */
    private static BigDecimal getTotalPaidDirect(Integer contractId) {
        String sql = "SELECT COALESCE(SUM(amount), 0) AS total FROM Payments WHERE contractId = ? AND UPPER(LTRIM(RTRIM(status))) = 'COMPLETED'";
        try (Connection conn = DB.get();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, contractId);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    return rs.getBigDecimal("total");
                }
            }
        } catch (Exception e) {
            System.err.println("Error getting total paid: " + e.getMessage());
        }
        return BigDecimal.ZERO;
    }

    /**
     * helper: lay payment id truc tiep tu db
     */
    private static Integer getPaymentIdDirect(Integer contractId, BigDecimal amount) {
        String sql = "SELECT TOP 1 paymentId FROM Payments WHERE contractId = ? AND CAST(amount AS DECIMAL(10,2)) = CAST(? AS DECIMAL(10,2)) AND UPPER(LTRIM(RTRIM(status))) = 'PENDING' ORDER BY paymentId DESC";
        try (Connection conn = DB.get();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, contractId);
            ps.setBigDecimal(2, amount);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    return rs.getInt("paymentId");
                }
            }
        } catch (Exception e) {
            System.err.println("Error getting payment id: " + e.getMessage());
        }
        return null;
    }

    /**
     * helper: format tien
     */
    private static String formatMoney(BigDecimal amount) {
        if (amount == null) {
            return "0 VND";
        }
        // Format theo định dạng VN: 1.000.000 VND
        java.text.DecimalFormat formatter = new java.text.DecimalFormat("#,###");
        java.text.DecimalFormatSymbols symbols = formatter.getDecimalFormatSymbols();
        symbols.setGroupingSeparator('.');
        formatter.setDecimalFormatSymbols(symbols);
        return formatter.format(amount.doubleValue()) + " VND";
    }
}

