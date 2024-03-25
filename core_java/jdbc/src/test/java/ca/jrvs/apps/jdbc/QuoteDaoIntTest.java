package ca.jrvs.apps.jdbc;

import ca.jrvs.apps.jdbc.stockquote.dao.Quote;
import ca.jrvs.apps.jdbc.stockquote.dao.QuoteDao;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;

import java.sql.*;
import java.util.Optional;

import static org.junit.Assert.assertTrue;
import static org.mockito.Mockito.*;

@RunWith(MockitoJUnitRunner.class)
public class QuoteDaoIntTest {

    @Mock
    private Connection connection;

    @Mock
    private PreparedStatement preparedStatement;

    @Mock
    private ResultSet resultSet;

    @InjectMocks
    private QuoteDao quoteDao;

    @Before // Use @Before for setup method in JUnit 4
    public void setup() throws SQLException {
        when(connection.prepareStatement(anyString())).thenReturn(preparedStatement);
        when(preparedStatement.executeQuery()).thenReturn(resultSet);
        when(resultSet.next()).thenReturn(true);

        when(resultSet.getString("symbol")).thenReturn("MSFT");
        when(resultSet.getDouble("open")).thenReturn(332.3800);
        when(resultSet.getDouble("high")).thenReturn(333.8300);
        when(resultSet.getDouble("low")).thenReturn(326.3600);
        when(resultSet.getDouble("price")).thenReturn(327.7300);
        when(resultSet.getInt("volume")).thenReturn(21085695);

        Date sqlDate = Date.valueOf("2023-10-13");

        when(resultSet.getDate("latest_trading_day")).thenReturn(sqlDate);
        when(resultSet.getDouble("previous_close")).thenReturn(331.1600);
        when(resultSet.getDouble("change")).thenReturn(-3.4300);
        when(resultSet.getString("change_percent")).thenReturn("-1.0358%");
        // Adjust for any missing setup.
    }

    @Test
    public void testFindById() throws SQLException {
        Optional<Quote> result = quoteDao.findById("MSFT");
        assertTrue(result.isPresent());
    }
}
