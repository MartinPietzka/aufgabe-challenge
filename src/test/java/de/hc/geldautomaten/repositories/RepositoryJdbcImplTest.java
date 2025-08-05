package de.hc.geldautomaten.repositories;

import com.zaxxer.hikari.HikariConfig;
import com.zaxxer.hikari.HikariDataSource;
import de.hc.geldautomaten.Bank; // Um das DB-Schema zu erstellen
import de.hc.geldautomaten.entities.Geldautomat;
import de.hc.geldautomaten.records.Location;
import org.junit.jupiter.api.*;

import javax.sql.DataSource;
import java.math.BigDecimal;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

import static org.assertj.core.api.Assertions.*;

class RepositoryJdbcImplTest {

    private static DataSource dataSource;
    private RepositoryJdbcImpl repository;

    @BeforeAll
    static void setUpDataBase() {
        HikariConfig config = new HikariConfig();
        config.setJdbcUrl("jdbc:h2:mem:testdb;DB_CLOSE_DELAY=-1");
        config.setUsername("sa");
        config.setPassword("");
        dataSource = new HikariDataSource(config);

        Bank.erstelleDatenbank(dataSource);

    }

    @BeforeEach
    void setUp() throws SQLException {
        try (Connection con = dataSource.getConnection();
             Statement s = con.createStatement()) {
            s.execute("DROP ALL OBJECTS");
            Bank.erstelleDatenbank(dataSource);
        }

        repository = new RepositoryJdbcImpl(dataSource);
    }

    @AfterAll
    static void closeDataSource() {
        ((HikariDataSource) dataSource).close();
    }

    @Test
    void createGeldautomat_wennAufgerufen_sollteAutomatInDerDatenbankErstellen() throws Exception {
        Location location = new Location(50.123, 7.456);
        BigDecimal startBargeld = new BigDecimal("25000.00");

        repository.beginTransaction();
        Geldautomat dummyAutomat = repository.createGeldautomat(location, startBargeld);
        repository.commitTransaction();

        assertThat(dummyAutomat).isNotNull();
        assertThat(dummyAutomat.getNummer()).isPositive();
        assertThat(dummyAutomat.getLocation()).isEqualTo(location);
        assertThat(dummyAutomat.ermittleVerfuegbaresBargeld()).isEqualTo(startBargeld);

        try (Connection con = dataSource.getConnection();
             Statement s = con.createStatement();
             ResultSet rs = s.executeQuery("SELECT COUNT(*) FROM geldautomat WHERE automatennummer = " + dummyAutomat.getNummer())) {

            assertThat(rs.next()).isTrue();
            assertThat(rs.getInt(1)).isEqualTo(1);
        }
    }

    @Test
    void createGeldautomat_wennKeineTransaktionAktiv_sollteExceptionWerfen() {
        Location testLocation = new Location(50.123, 7.456);
        BigDecimal startBargeld = new BigDecimal("25000.00");

        assertThatThrownBy(() -> {
            repository.createGeldautomat(testLocation, startBargeld);
        })
                .isInstanceOf(IllegalStateException.class)
                .hasMessageContaining("Keine Connection vorhanden");
    }
}