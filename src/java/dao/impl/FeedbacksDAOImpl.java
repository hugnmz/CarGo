package dao.impl;

import dao.FeedbacksDAO;
import model.Feedbacks;
import util.JdbcTemplateUtil;
import util.di.annotation.Repository;

import java.util.List;

@Repository
public class FeedbacksDAOImpl implements FeedbacksDAO {

    @Override
    public List<Feedbacks> getAllFeedbacks() {
        String sql = "SELECT * FROM dbo.Feedbacks ORDER BY createAt DESC";
        return JdbcTemplateUtil.query(sql, Feedbacks.class);
    }

    @Override
    public Feedbacks getFeedbackById(Integer feedbackId) {
        String sql = "SELECT * FROM dbo.Feedbacks WHERE feedbackId = ?";
        return JdbcTemplateUtil.queryOne(sql, Feedbacks.class, feedbackId);
    }

    @Override
    public List<Feedbacks> getFeedbacksByCustomer(Integer customerId) {
        String sql = "SELECT * FROM dbo.Feedbacks WHERE customerId = ? ORDER BY createAt DESC";
        return JdbcTemplateUtil.query(sql, Feedbacks.class, customerId);
    }

    @Override
    public List<Feedbacks> getFeedbacksByVehicle(Integer vehicleId) {
        String sql = "SELECT * FROM dbo.Feedbacks WHERE vehicleId = ? ORDER BY createAt DESC";
        return JdbcTemplateUtil.query(sql, Feedbacks.class, vehicleId);
    }

    @Override
    public List<Feedbacks> getFeedbacksByCustomerAndVehicle(Integer customerId, Integer vehicleId) {
        String sql = "SELECT * FROM dbo.Feedbacks WHERE customerId = ? AND vehicleId = ? ORDER BY createAt DESC";
        return JdbcTemplateUtil.query(sql, Feedbacks.class, customerId, vehicleId);
    }

@Override
public boolean addFeedback(Feedbacks feedback) {
    int newId;
    if (feedback.getVehicleId() == null) {
        String sql = "INSERT INTO dbo.Feedbacks (customerId, comment, createAt) VALUES (?,?,?)";
        newId = JdbcTemplateUtil.insertAndReturnKey(
                sql,
                feedback.getCustomerId(),
                feedback.getComment(),
                feedback.getCreateAt()
        );
    } else {
        String sql = "INSERT INTO dbo.Feedbacks (customerId, vehicleId, comment, createAt) VALUES (?,?,?,?)";
        newId = JdbcTemplateUtil.insertAndReturnKey(
                sql,
                feedback.getCustomerId(),
                feedback.getVehicleId(),
                feedback.getComment(),
                feedback.getCreateAt()
        );
    }
    if (newId > 0) {
        feedback.setFeedbackId(newId);
        return true;
    }
    return false;
}

    @Override
    public boolean updateFeedback(Feedbacks feedback) {
        String sql = "UPDATE dbo.Feedbacks SET comment = ? WHERE feedbackId = ?";
        int affected = JdbcTemplateUtil.update(sql, feedback.getComment(), feedback.getFeedbackId());
        return affected > 0;
    }

    @Override
    public boolean deleteFeedback(Integer feedbackId) {
        String sql = "DELETE FROM dbo.Feedbacks WHERE feedbackId = ?";
        int affected = JdbcTemplateUtil.update(sql, feedbackId);
        return affected > 0;
    }

    @Override
    public List<Feedbacks> getRecentFeedbacks(int limit) {
        String sql = "SELECT * FROM dbo.Feedbacks ORDER BY createAt DESC OFFSET 0 ROWS FETCH NEXT ? ROWS ONLY";
        return JdbcTemplateUtil.query(sql, Feedbacks.class, limit);
    }

    @Override
    public List<Feedbacks> getFeedbacksByDateRange(java.time.LocalDateTime startDate, java.time.LocalDateTime endDate) {
        String sql = "SELECT * FROM dbo.Feedbacks WHERE createAt BETWEEN ? AND ? ORDER BY createAt DESC";
        return JdbcTemplateUtil.query(sql, Feedbacks.class, startDate, endDate);
    }

    @Override
    public List<Feedbacks> getRecentFeedbacksPaged(int offset, int limit) {
        String sql = "SELECT * FROM dbo.Feedbacks ORDER BY createAt DESC OFFSET ? ROWS FETCH NEXT ? ROWS ONLY";
        return JdbcTemplateUtil.query(sql, Feedbacks.class, offset, limit);
    }
}