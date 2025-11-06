package service.impl;

import dao.FeedbacksDAO;
import dto.FeedbackDTO;
import mapper.FeedbackMapper;
import model.Feedbacks;
import util.di.annotation.Autowired;
import util.di.annotation.Service;
import util.exception.ApplicationException;
import util.exception.DataAccessException;
import util.exception.ValidationException;
import util.exception.BusinessException;
import util.MessageUtil;

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
        try {
            List<Feedbacks> list = feedbacksDAO.getRecentFeedbacksPaged(safeOffset, safeLimit);
            return list.stream().map(feedbackMapper::toDTO).collect(Collectors.toList());
        } catch (ApplicationException e) {
            throw e;
        } catch (Exception e) {
            throw new DataAccessException(MessageUtil.getError("error.dataaccess.feedback.list.failed"), e);
        }
    }

    @Override
    public FeedbackDTO create(Integer customerId, String comment, Integer vehicleId) {
        if (customerId == null) {
            throw new ValidationException(MessageUtil.getError("error.validation.not.logged.in"));
        }
        if (comment == null || comment.trim().isEmpty()) {
            throw new ValidationException(MessageUtil.getError("error.validation.comment.required"));
        }
        if (comment.length() > 255) {
            throw new ValidationException(MessageUtil.getError("error.validation.comment.too.long"));
        }

        try {
            Feedbacks f = new Feedbacks();
            f.setCustomerId(customerId);
            // Cho phép feedback chung (không gắn xe)
            f.setVehicleId(vehicleId); // có thể null
            f.setComment(comment.trim());
            f.setCreateAt(LocalDateTime.now());

            boolean ok = feedbacksDAO.addFeedback(f);
            if (!ok) {
                throw new BusinessException(MessageUtil.getError("error.business.feedback.save.failed"));
            }
            return feedbackMapper.toDTO(f);
        } catch (ApplicationException e) {
            throw e;
        } catch (Exception e) {
            throw new DataAccessException(MessageUtil.getError("error.dataaccess.feedback.create.failed"), e);
        }
    }
}