package de.hc.geldautomaten.repositories;

import de.hc.geldautomaten.entities.*;
import de.hc.geldautomaten.records.Location;

import javax.sql.DataSource;
import java.math.BigDecimal;
import java.sql.*;
import java.util.Optional;

import static java.util.Objects.requireNonNull;

public class RepositoryJdbcImpl implements Repository {

    private final DataSource dataSource;

    public RepositoryJdbcImpl(DataSource dataSource) {
        this.dataSource = dataSource;
    }

    @Override
    public Optional<Bankkonto> findBankkontoByKontonummer(long kontonummer) {
        return Optional.empty();
    }

    @Override
    public Optional<Geldautomat> findGeldautomatByNummer(long nummer) {
        return Optional.empty();
    }

    @Override
    public Optional<Geldautomat> findGeldautomatByLocation(Location location) {
        return Optional.empty();
    }

    @Override
    public <T> void save(T obj) {

    }

    @Override
    public void beginTransaction() {

    }

    @Override
    public void commitTransaction() {

    }

    @Override
    public void rollbackTransaction() {

    }

    @Override
    public Geldautomat createGeldautomat(Location location, BigDecimal verfuegbaresBargeld) {

        String sql = "INSERT INTO geldautomat (latitude, longitude, bargeld) VALUES (?, ?, ?)";
        try (Connection con = dataSource.getConnection();
             PreparedStatement ps = con.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
            ps.setDouble(1, location.latitude());
            ps.setDouble(2, location.longitude());
            ps.setBigDecimal(3, verfuegbaresBargeld);
            ps.execute();

            try (ResultSet rs = ps.getGeneratedKeys()) {
                if (!rs.next())
                    throw new SQLException("automatennummer wurde nicht generiert");
                long automatennummer = rs.getLong(1);
                return new GeldautomatImpl(automatennummer, location, verfuegbaresBargeld);
            }
        } catch (SQLException e) {
            throw new RuntimeException("DB-Fehler beim Anlegen eines Geldautomaten.", e);
        }
    }

    @Override
    public Bankkonto createBankkonto(String vorname, String nachname, String pin) { // Bankkonto hat Kunde hat Geldkarte

        requireNonNull(vorname, "vorname darf nicht null sein");
        requireNonNull(nachname, "nachname darf nicht null sein");
        requireNonNull(pin, "kontopin darf nicht null sein");


        try (Connection con = dataSource.getConnection()) {

            // Kunde
            String insertKunde = "INSERT INTO kunde (vorname, nachname) VALUES (?, ?)";

            long kundennummer;
            try (PreparedStatement ps = con.prepareStatement(insertKunde, Statement.RETURN_GENERATED_KEYS)) {
                ps.setString(1, vorname);
                ps.setString(2, nachname);
                ps.executeUpdate();
                try (ResultSet rs = ps.getGeneratedKeys()) {
                    if (!rs.next()) throw new SQLException("Kundennummer wurde nicht generiert");
                    kundennummer = rs.getLong(1);
                }
            }

            // Bankkonto
            String insertKonto = "INSERT INTO bankkonto (kundennummer, konto_pin, kontostand) VALUES (?, ?, ?)";

            BigDecimal startKontostand = BigDecimal.ZERO;
            long kontonummer;
            try (PreparedStatement ps = con.prepareStatement(insertKonto, Statement.RETURN_GENERATED_KEYS)) {
                ps.setLong(1, kundennummer);
                ps.setString(2, pin);
                ps.setBigDecimal(3, startKontostand);
                ps.executeUpdate();
                try (ResultSet rs = ps.getGeneratedKeys()) {
                    if (!rs.next()) throw new SQLException("kontonummer wurde nicht generiert");
                    kontonummer = rs.getLong(1);
                }
            }

            // Geldkarte mit zufälligem Pin die später vom Kunde geändert wird
            String insertGeldkarte = "INSERT INTO geldkarte (kontonummer, karte_pin) VALUES (?, ?)";

            long kartennummer;
            String zufaelligerKartenPin = "4711";
            try (PreparedStatement ps = con.prepareStatement(insertGeldkarte, Statement.RETURN_GENERATED_KEYS)) {
                ps.setLong(1, kontonummer);
                ps.setString(2, zufaelligerKartenPin);
                ps.executeUpdate();
                try (ResultSet rs = ps.getGeneratedKeys()) {
                    if (!rs.next()) throw new SQLException("kartennummer wurde nicht generiert");
                    kartennummer = rs.getLong(1);
                }
            }


            Geldkarte geldkarte = new GeldkarteImpl(kartennummer, kontonummer, zufaelligerKartenPin);
            Kunde kunde = new KundeImpl(kundennummer, vorname, nachname, geldkarte);
            return new BankkontoImpl(kontonummer, kunde, pin, startKontostand);

        } catch (SQLException e) {
            throw new RuntimeException("DB-Fehler beim Anlegen eines Bankkontos", e);
        }
    }


}
