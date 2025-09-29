package dao;

import model.Feedbacks;
import java.util.List;

public interface FeedbacksDAO {
    
    // Lấy tất cả phản hồi
    List<Feedbacks> getAllFeedbacks();
    
    // Lấy phản hồi theo ID
    Feedbacks getFeedbackById(Integer feedbackId);
    
    // Lấy phản hồi theo khách hàng
    List<Feedbacks> getFeedbacksByCustomer(Integer customerId);
    
    // Lấy phản hồi theo xe
    List<Feedbacks> getFeedbacksByVehicle(Integer vehicleId);
    
    // Lấy phản hồi theo khách hàng và xe
    List<Feedbacks> getFeedbacksByCustomerAndVehicle(Integer customerId, Integer vehicleId);
    
    // Thêm phản hồi mới
    boolean addFeedback(Feedbacks feedback);
    
    // Cập nhật phản hồi
    boolean updateFeedback(Feedbacks feedback);
    
    // Xóa phản hồi
    boolean deleteFeedback(Integer feedbackId);
    
    // Lấy phản hồi gần đây nhất
    List<Feedbacks> getRecentFeedbacks(int limit);
    
    // Lấy phản hồi theo khoảng thời gian
    List<Feedbacks> getFeedbacksByDateRange(java.time.LocalDateTime startDate, java.time.LocalDateTime endDate);
}
