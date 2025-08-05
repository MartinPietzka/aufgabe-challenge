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
    private final ThreadLocal<Connection> connectionHolder = new ThreadLocal<>();

    public RepositoryJdbcImpl(DataSource dataSource) {
        this.dataSource = dataSource;
    }


    @Override
    public Optional<Bankkonto> findBankkontoByKontonummer(long kontonummer) {

        if (kontonummer <= 0) throw new IllegalArgumentException("kontonummer muss positiv sein");

        String sql = """
                SELECT *
                FROM bankkonto b
                JOIN kunde k ON k.kundennummer = b.kundennummer
                JOIN geldkarte g ON g.kontonummer = b.kontonummer
                WHERE b.kontonummer = ?
                """;


        try (Connection con = dataSource.getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {

            ps.setLong(1, kontonummer);

            try (ResultSet rs = ps.executeQuery()) {
                if (!rs.next()) return Optional.empty();

                long kartennummer = rs.getLong("kartennummer");
                String kartenPin = rs.getString("karte_pin");
                Geldkarte geldkarte = new GeldkarteImpl(kartennummer, kontonummer, kartenPin);

                long kundennummer = rs.getLong("kundennummer");
                String vorname = rs.getString("vorname");
                String nachname = rs.getString("nachname");
                Kunde kunde = new KundeImpl(kundennummer, vorname, nachname, geldkarte);

                String kontoPin = rs.getString("konto_pin");
                BigDecimal kontostand = rs.getBigDecimal("kontostand");
                Bankkonto bankkonto = new BankkontoImpl(kontonummer, kunde, kontoPin, kontostand);

                return Optional.of(bankkonto);
            }

        } catch (SQLException e) {
            throw new RuntimeException("DB-Fehler beim Lesen des Bankkontos " + kontonummer, e);
        }
    }

    @Override
    public Optional<Geldautomat> findGeldautomatByNummer(long nummer) {
        return Optional.empty();
    }

    @Override
    public Optional<Geldautomat> findGeldautomatByLocation(Location location) {
        requireNonNull(location, "location darf nicht null sein");

        String sql = """
                SELECT *
                FROM geldautomat
                WHERE latitude = ? AND longitude = ?
                """;

        try (Connection con = dataSource.getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {

            ps.setDouble(1, location.latitude());
            ps.setDouble(2, location.longitude());

            try (ResultSet rs = ps.executeQuery()) {
                if (!rs.next()) return Optional.empty();

                long nummer = rs.getLong("automatennummer");
                BigDecimal bargeld = rs.getBigDecimal("bargeld");
                Geldautomat automat = new GeldautomatImpl(nummer, location, bargeld);
                return Optional.of(automat);
            }

        } catch (SQLException e) {
            throw new RuntimeException("DB-Fehler beim Lesen des Geldautomaten an Location " + location, e);
        }
    }


    @Override
    public <T> void save(T obj) {

    }

    @Override
    public void beginTransaction() {
        try {
            Connection con = dataSource.getConnection();
            con.setAutoCommit(false);
            connectionHolder.set(con);
        } catch (SQLException e) {
            throw new RuntimeException("Transaktion konnte nicht gestartet werden", e);
        }
    }


    @Override
    public void commitTransaction() {
        try (Connection con = connectionHolder.get()) {
            if (con != null) con.commit();
        } catch (SQLException e) {
            throw new RuntimeException("Commit konnte nicht durchgeführt werden", e);
        } finally {
            // der try with resources block schließt nur die con
            // con muss aus dem holder entfernt werden
            connectionHolder.remove();

        }
    }


    @Override
    public void rollbackTransaction() {
        try (Connection con = connectionHolder.get()) {
            if (con != null) con.rollback();
        } catch (SQLException e) {
            throw new RuntimeException("Rollback konnte nicht durchgeführt werden", e);
        } finally {
            connectionHolder.remove();
        }
    }

    @Override
    public Geldautomat createGeldautomat(Location location, BigDecimal verfuegbaresBargeld) {

        Connection con = connectionHolder.get();
        if (con == null) {
            throw new IllegalStateException("Keine Connection vorhanden.");
        }

        String insertGeldautomat = "INSERT INTO geldautomat (latitude, longitude, bargeld) VALUES (?, ?, ?)";
        try (PreparedStatement ps = con.prepareStatement(insertGeldautomat, Statement.RETURN_GENERATED_KEYS)) {
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

        Connection con = connectionHolder.get();
        if (con == null) {
            throw new IllegalStateException("Keine Connection vorhanden.");
        }


        try {
            // Kunde
            String insertKunde = "INSERT INTO kunde (vorname, nachname) VALUES (?, ?)";

            long kundennummer;
            try (PreparedStatement ps = con.prepareStatement(insertKunde, Statement.RETURN_GENERATED_KEYS)) {
                ps.setString(1, vorname);
                ps.setString(2, nachname);
                ps.executeUpdate();
                try (ResultSet rs = ps.getGeneratedKeys()) {
                    if (!rs.next()) throw new SQLException("kundennummer wurde nicht generiert");
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
