package ca.jrvs.apps.jdbc.stockquote.dao;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
public class PositionDao implements CrudDao<Position, String> {

    final Logger logger = LoggerFactory.getLogger(CrudDao.class);
    private Connection c;

    public PositionDao(Connection c) {
        this.c = c;
    }

    @Override
    public Position save(Position entity) throws IllegalArgumentException {
        if (entity == null) {
            throw new IllegalArgumentException("Position entity must not be null");
        }

        String sqlStatement;

        boolean exists = this.findById(entity.getTicker()).isPresent();

        if (exists) {
            sqlStatement = "UPDATE position SET number_of_shares = ?, value_paid = ? WHERE symbol = ?";
        } else {
            sqlStatement = "INSERT INTO position (symbol, number_of_shares, value_paid) VALUES (?, ?, ?) ";
        }

        try (PreparedStatement ps = c.prepareStatement(sqlStatement)) {
            if (exists) {
                ps.setInt(1, entity.getNumOfShares());
                ps.setDouble(2, entity.getValuePaid());
                ps.setString(3, entity.getTicker());
            } else {
                ps.setString(1, entity.getTicker());
                ps.setInt(2, entity.getNumOfShares());
                ps.setDouble(3, entity.getValuePaid());
            }

            ps.executeUpdate();
        } catch (SQLException e) {
            logger.error("Failed to save Position: " + entity, e);
            throw new RuntimeException("Failed to save Position", e);
        }

        return entity;
    }

    @Override
    public Optional<Position> findById(String id) throws IllegalArgumentException {
        if (id == null) {
            throw new IllegalArgumentException("id is null");
        }

        String selectSql = "SELECT * FROM position WHERE symbol = ?";
        try (PreparedStatement ps = c.prepareStatement(selectSql)) {
            ps.setString(1, id);
            ResultSet rs = ps.executeQuery();
            if (rs.next()) {
                return Optional.of(mapResultSetToPosition(rs));
            }
            return Optional.empty();
        } catch (SQLException e) {
            logger.error("\"Failed to find quote by ticker: \" + ticker, e");
            throw new RuntimeException("Failed to find quote by ticker: " + id, e);
        }
    }

    @Override
    public Iterable<Position> findAll() {
        List<Position> positions = new ArrayList<>();
        String selectAllSql = "SELECT * FROM position";
        try (PreparedStatement ps = c.prepareStatement(selectAllSql)) {
            ResultSet rs = ps.executeQuery();
            while (rs.next()) {
                Position position = mapResultSetToPosition(rs);
                positions.add(position);
            }
        } catch (SQLException e) {
            logger.error("Failed to find all positions", e);
            throw new RuntimeException("Failed to find all positions", e);
        }
        return positions;
    }

    @Override
    public void deleteById(String id) throws IllegalArgumentException {
        if (id == null) {
            throw new IllegalArgumentException("id is null");
        }

        String deleteSql = "DELETE FROM position WHERE symbol = ?";
        try (PreparedStatement ps = c.prepareStatement(deleteSql)) {
            ps.setString(1, id);
            ps.executeUpdate();
        } catch (SQLException e) {
            logger.error("Deleted position with ticker: " + id, e);
            throw new RuntimeException("Failed to delete position by ticker: " + id, e);
        }
    }

    @Override
    public void deleteAll() {
        String deleteAllSql = "DELETE FROM position";
        try (PreparedStatement ps = c.prepareStatement(deleteAllSql)) {
            ps.executeUpdate();
        } catch (SQLException e) {
            logger.error("Failed to delete all positions", e);
            throw new RuntimeException("Failed to delete all positions", e);
        }
    }

    private Position mapResultSetToPosition(ResultSet rs) throws SQLException {
        Position position = new Position();
        position.setTicker(rs.getString("symbol"));
        position.setNumOfShares(rs.getInt("number_of_shares"));
        position.setValuePaid(rs.getDouble("value_paid"));
        return position;
    }

}