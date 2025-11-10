
package service.impl;

import dao.FeedbacksDAO;
import dto.FeedbackDTO;
import mapper.FeedbackMapper;
import model.Feedback;
import util.di.annotation.Autowired;
import util.di.annotation.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class FeedbackServiceImpl implements service.FeedbackService {

    @Autowired
    private FeedbacksDAO feedbacksDAO;

    @Autowired
    private FeedbackMapper feedbackMapper;

    @Override
    public List<FeedbackDTO> listRecent(int offset, int limit) {
        // Giới hạn limit
        int safeLimit = Math.max(1, Math.min(limit, 20));
        int safeOffset = Math.max(0, offset);
        List<Feedback> list = feedbacksDAO.getRecentFeedbacksPaged(safeOffset, safeLimit);
        return list.stream().map(feedbackMapper::toDTO).collect(Collectors.toList());
    }

    @Override
    public FeedbackDTO create(Integer customerId, String comment, Integer vehicleId) {
        if (customerId == null) {
            throw new IllegalArgumentException("Not logged in");
        }
        if (comment == null || comment.trim().isEmpty()) {
            throw new IllegalArgumentException("Comment is required");
        }
        if (comment.length() > 255) {
            throw new IllegalArgumentException("Comment too long");
        }

        Feedback f = new Feedback();
        f.setCustomerId(customerId);
        // Cho phép feedback chung (không gắn xe)
        f.setVehicleId(vehicleId); // có thể null
        f.setComment(comment.trim());
        f.setCreateAt(LocalDateTime.now());

        boolean ok = feedbacksDAO.addFeedback(f);
        if (!ok) {
            throw new RuntimeException("Failed to save feedback");
        }
        return feedbackMapper.toDTO(f);
    }
}
