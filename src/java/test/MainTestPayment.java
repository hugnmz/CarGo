/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package test;

import dao.impl.ContractsDAOImpl;
import dao.impl.PaymentsDAOImpl;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import model.Payments;

/**
 *
 * @author laptop lenovo
 */
public class MainTestPayment {
    public static void main(String[] args) {

        PaymentsDAOImpl paymentsDAO = new PaymentsDAOImpl();
        ContractsDAOImpl contractsDAO = new ContractsDAOImpl();

        // ====== 1. Thông tin hợp đồng cần test ======
        int contractId = 1; // sửa thành ID thật trong database

        // ====== 2. Thêm payment mới (PENDING) để test ======
        Payments newPayment = new Payments();
        newPayment.setContractId(contractId);
        newPayment.setAmount(new BigDecimal("500000")); // 500k
        newPayment.setMethodId(1); // ví dụ phương thức thanh toán
        newPayment.setStatus("PENDING");
        newPayment.setPaymentDate(LocalDateTime.now());

        boolean added = paymentsDAO.addPayment(newPayment);
        if (added) {
            System.out.println("✅ Thêm payment PENDING thành công, ID: " + newPayment.getPaymentId());
        } else {
            System.out.println("❌ Thêm payment thất bại!");
            return;
        }

        // ====== 3. Giả lập hành vi servlet: tìm payment đang PENDING ======
        var pendingPayment = paymentsDAO.findPendingPayment(contractId, null);
        if (pendingPayment != null) {
            System.out.println("🔍 Tìm thấy payment đang PENDING, ID: " + pendingPayment.getPaymentId());

            // ====== 4. Update payment thành COMPLETED ======
            boolean updatedPayment = paymentsDAO.updatePaymentStatus(pendingPayment.getPaymentId(), "COMPLETED");
            if (updatedPayment) {
                System.out.println("✅ Payment ID " + pendingPayment.getPaymentId() + " đã cập nhật thành COMPLETED");

                // ====== 5. Update trạng thái contract (giống servlet) ======
                boolean updatedContract = contractsDAO.updateContractStatus(contractId, "ACCEPTED");
                if (updatedContract) {
                    System.out.println("✅ Hợp đồng " + contractId + " đã cập nhật thành ACCEPTED");
                } else {
                    System.out.println("⚠️ Cập nhật hợp đồng thất bại (xem lại ContractsDAOImpl)");
                }

            } else {
                System.out.println("❌ Update payment thất bại!");
            }

        } else {
            System.out.println("⚠️ Không tìm thấy payment đang PENDING cho contractId " + contractId);
        }

        // ====== 6. Kiểm tra lại kết quả ======
        var completed = paymentsDAO.getPaymentById(newPayment.getPaymentId());
        System.out.println("🔎 Trạng thái sau khi update: " + completed.getStatus());
        }
    
}
