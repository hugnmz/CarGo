package dao.impl;

import dao.SeatingsDAO;
import java.util.List;
import model.Seatings;
import util.JdbcTemplateUtil;
import util.di.annotation.Repository;

@Repository
public class SeatingsDAOImpl implements SeatingsDAO {

    @Override
    public List<Seatings> getAllSeatings() {
        String sql = "SELECT * FROM dbo.Seatings";
        return JdbcTemplateUtil.query(sql, Seatings.class);
    }

    @Override
    public Seatings getSeatingById(Integer seatingId) {
        String sql = "SELECT * FROM dbo.Seatings WHERE seatingId = ?";
        return JdbcTemplateUtil.queryOne(sql, Seatings.class, seatingId);
    }

    @Override
    public boolean addSeating(Seatings seating) {
        String sql = "INSERT INTO dbo.Seatings(seatingType) VALUES (?)";
        int result = JdbcTemplateUtil.insertAndReturnKey(sql, seating.getSeatingType());
        if (result > 0) {
            seating.setSeatingId(result);
            return true;
        }
        return false;
    }

    @Override
    public boolean updateSeating(Seatings seating) {
        String sql = "UPDATE dbo.Seatings SET seatingType = ? WHERE seatingId = ?";
        int result = JdbcTemplateUtil.update(sql, seating.getSeatingType(), seating.getSeatingId());
        return result > 0;
    }

    @Override
    public boolean deleteSeating(Integer seatingId) {
        String sql = "DELETE FROM dbo.Seatings WHERE seatingId = ?";
        int result = JdbcTemplateUtil.update(sql, seatingId);
        return result > 0;
    }
}
