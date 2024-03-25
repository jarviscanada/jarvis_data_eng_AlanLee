package ca.jrvs.apps.jdbc.stockquote.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;


public class QuoteDao implements CrudDao<Quote, String> {
    final Logger logger = LoggerFactory.getLogger(CrudDao.class);
    private Connection c;

    public QuoteDao(Connection c) {
        this.c = c;
    }

    @Override
    public Quote save(Quote entity) throws IllegalArgumentException {
        if (entity == null) throw new IllegalArgumentException("argument entity is null");

        String sqlStatement;

        boolean exists = this.findById(entity.getTicker()).isPresent();

        if (exists) {
            sqlStatement = "UPDATE quote SET open = ?, high = ?, low = ?, price = ?, volume = ?, latest_trading_day = ?, previous_close = ?, change = ?, change_percent = ?, timestamp = ? WHERE symbol = ?";
        } else {
            sqlStatement = "INSERT INTO quote (symbol, open, high, low, price, volume, latest_trading_day, previous_close, change, change_percent, timestamp) VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)";
        }

        try (PreparedStatement ps = c.prepareStatement(sqlStatement)) {
            if (exists) {
                ps.setDouble(1, entity.getOpen());
                ps.setDouble(2, entity.getHigh());
                ps.setDouble(3, entity.getLow());
                ps.setDouble(4, entity.getPrice());
                ps.setInt(5, entity.getVolume());
                ps.setDate(6, entity.getLatestTradingDay());
                ps.setDouble(7, entity.getPreviousClose());
                ps.setDouble(8, entity.getChange());
                ps.setString(9, entity.getChangePercent());
                ps.setTimestamp(10, entity.getTimestamp());
                ps.setString(11, entity.getTicker());
            } else {
                ps.setString(1, entity.getTicker());
                ps.setDouble(2, entity.getOpen());
                ps.setDouble(3, entity.getHigh());
                ps.setDouble(4, entity.getLow());
                ps.setDouble(5, entity.getPrice());
                ps.setInt(6, entity.getVolume());
                ps.setDate(7, entity.getLatestTradingDay());
                ps.setDouble(8, entity.getPreviousClose());
                ps.setDouble(9, entity.getChange());
                ps.setString(10, entity.getChangePercent());
                ps.setTimestamp(11, entity.getTimestamp());
            }

            ps.executeUpdate();

            return entity;
        } catch (SQLException e) {
            logger.error("Failed to save quote", e);
            throw new RuntimeException("Failed to save quote", e);
        }
    }

    @Override
    public Optional<Quote> findById(String id) throws IllegalArgumentException {
        if (id == null) {
            throw new IllegalArgumentException("id is null");
        }

        String selectSql = "SELECT * FROM quote WHERE symbol = ?";
        try (PreparedStatement ps = c.prepareStatement(selectSql)) {
            ps.setString(1, id);
            ResultSet rs = ps.executeQuery();
            if (rs.next()) {
                return Optional.of(mapResultSetToQuote(rs));
            }
            return Optional.empty();
        } catch (SQLException e) {
            logger.error("\"Failed to find quote by ticker: \" + ticker, e");
            throw new RuntimeException("Failed to find quote by ticker: " + id, e);
        }
    }

    @Override
    public Iterable<Quote> findAll() {
        List<Quote> quotes = new ArrayList<>();
        String selectAllSql = "SELECT * FROM quote";
        try (PreparedStatement ps = c.prepareStatement(selectAllSql)) {
            ResultSet rs = ps.executeQuery();
            while (rs.next()) {
                Quote quote = mapResultSetToQuote(rs);
                quotes.add(quote);
            }
        } catch (SQLException e) {
            logger.error("Failed to find all quotes", e);
            throw new RuntimeException("Failed to find all quotes", e);
        }
        return quotes;
    }

    @Override
    public void deleteById(String id) throws IllegalArgumentException {
        if (id == null) {
            throw new IllegalArgumentException("id is null");
        }

        String deleteSql = "DELETE FROM quote WHERE symbol = ?";
        try (PreparedStatement ps = c.prepareStatement(deleteSql)) {
            ps.setString(1, id);
            ps.executeUpdate();
        } catch (SQLException e) {
            logger.error("Deleted quote with ticker: " + id, e);
            throw new RuntimeException("Failed to delete quote by ticker: " + id, e);
        }
    }

    @Override
    public void deleteAll() {
        String deleteAllSql = "DELETE FROM quote";
        try (PreparedStatement ps = c.prepareStatement(deleteAllSql)) {
            ps.executeUpdate();
        } catch (SQLException e) {
            logger.error("Failed to delete all quotes", e);
            throw new RuntimeException("Failed to delete all quotes", e);
        }
    }

    private Quote mapResultSetToQuote(ResultSet rs) throws SQLException {
        Quote quote = new Quote();
        quote.setTicker(rs.getString("symbol"));
        quote.setOpen(rs.getDouble("open"));
        quote.setHigh(rs.getDouble("high"));
        quote.setLow(rs.getDouble("low"));
        quote.setPrice(rs.getDouble("price"));
        quote.setVolume(rs.getInt("volume"));
        quote.setLatestTradingDay(rs.getDate("latest_trading_day"));
        quote.setPreviousClose(rs.getDouble("previous_close"));
        quote.setChange(rs.getDouble("change"));
        quote.setChangePercent(rs.getString("change_percent"));
        quote.setTimestamp(rs.getTimestamp("timestamp"));
        return quote;
    }

}