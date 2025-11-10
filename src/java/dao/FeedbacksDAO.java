package dao;

import model.Feedback;
import java.util.List;

public interface FeedbacksDAO {

    // Lấy tất cả phản hồi
    List<Feedback> getAllFeedbacks();

    // Lấy phản hồi theo ID
    Feedback getFeedbackById(Integer feedbackId);

    // Lấy phản hồi theo khách hàng
    List<Feedback> getFeedbacksByCustomer(Integer customerId);

    // Lấy phản hồi theo xe
    List<Feedback> getFeedbacksByVehicle(Integer vehicleId);

    // Lấy phản hồi theo khách hàng và xe
    List<Feedback> getFeedbacksByCustomerAndVehicle(Integer customerId, Integer vehicleId);

    // Thêm phản hồi mới
    boolean addFeedback(Feedback feedback);

    // Cập nhật phản hồi
    boolean updateFeedback(Feedback feedback);

    // Xóa phản hồi
    boolean deleteFeedback(Integer feedbackId);

    // Lấy phản hồi gần đây nhất
    List<Feedback> getRecentFeedbacks(int limit);

    // Lấy phản hồi theo khoảng thời gian
    List<Feedback> getFeedbacksByDateRange(java.time.LocalDateTime startDate, java.time.LocalDateTime endDate);
    // Thêm vào cuối interface
// Lấy danh sách phản hồi mới nhất có phân trang (offset, limit)

    List<Feedback> getRecentFeedbacksPaged(int offset, int limit);
}