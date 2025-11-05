package service;

import dto.FeedbackDTO;
import java.util.List;

public interface FeedbackService {

    List<FeedbackDTO> listRecent(int offset, int limit);

    FeedbackDTO create(Integer customerId, String comment, Integer vehicleId);
}