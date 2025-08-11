package de.hc.geldautomaten;

import com.zaxxer.hikari.HikariConfig;
import com.zaxxer.hikari.HikariDataSource;
import de.hc.geldautomaten.entities.Bankkonto;
import de.hc.geldautomaten.entities.Geldautomat;
import de.hc.geldautomaten.entities.Geldkarte;
import de.hc.geldautomaten.entities.Kunde;
import de.hc.geldautomaten.records.GeldautomatSession;
import de.hc.geldautomaten.records.Kontoauszug;
import de.hc.geldautomaten.records.Location;
import de.hc.geldautomaten.records.OnlineBankingSession;
import de.hc.geldautomaten.repositories.Repository;
import de.hc.geldautomaten.repositories.RepositoryJdbcImpl;
import de.hc.geldautomaten.services.GeldautomatenService;
import de.hc.geldautomaten.services.KontoService;
import de.hc.geldautomaten.services.OnlineBankingService;

import javax.sql.DataSource;
import java.math.BigDecimal;
import java.sql.Connection;
import java.sql.SQLException;
import java.sql.Statement;
import java.time.LocalDate;

public class Bank {

    public static void main(String[] args) {
        /*
         * README
         *
         * Kommentiere eine Aufgabe nach der Anderen ein und versuche die Anwendung "zum Fliegen" zu bringen.
         * Eine entsprechende Run-Configuration für IntelliJ IDEA liegt dem Projekt unter ".run/Bank.run.xml" bei.
         *
         * Solltest du eine andere Entwicklungsumgebung nutzen wollen, ist es notwendig,
         * dass du der VM die Option '-ea' mitgibst, damit die Asserts,
         * welche die Anwendung vornimmt, auch anschlagen.
         *
         * Der folgende Code sollte in seiner Struktur nur im absoluten Notfall verändert werden - davor und danach darf ergänzt werden.
         */

        HikariConfig config = new HikariConfig();
        config.setJdbcUrl("jdbc:h2:mem:test;DB_CLOSE_DELAY=-1");
        config.setUsername("sa");
        config.setPassword("");
        DataSource dataSource = new HikariDataSource(config);

        erstelleDatenbank(dataSource);

        /*
         * Aufgabe I: Erstellen von Geldautomaten
         */


        Repository repository = new RepositoryJdbcImpl(dataSource);
        GeldautomatenService geldautomatenService = GeldautomatenService.create(repository);
        Location location = new Location(50.681580, 7.150610);
        erstelleGeldautomaten(geldautomatenService, location);

        /*
         * Aufgabe II: Konten eröffnen
         */

        KontoService kontoService = KontoService.create(repository);
        Bankkonto bankkonto = eroeffneBankkonto(kontoService);
        System.out.println(bankkonto);

        /*
         * Aufgabe III: Geldautomaten anhand der Location finden
         */

        Geldautomat geldautomat = findeGeldautomat(geldautomatenService, location);

        /*
         * Aufgabe IV: Am Geldautomaten "Anmelden"
         */

        GeldautomatSession geldautomatSession = geldautomatenService.login(geldautomat, bankkonto.getKunde().getGeldkarte(), "4711");
        System.out.println(geldautomatSession);

        /*
         * Aufgabe V: Bargeld mittels Geldautomat auf das Bankkonto laden
         */

        bankkontoMittelsGeldautomatAufladen(geldautomatenService, geldautomat, geldautomatSession); // Trans Nr.1

        /*
         * Aufgabe VI: Bargeld mittels Geldautomat vom Bankkonto abheben
         */

        bargeldMittelsGeldautomatAbheben(geldautomatenService, geldautomat, geldautomatSession);  // Trans Nr.2

        /*
         * Aufgabe VII: Kontoauszug drucken
         */

        LocalDate today = LocalDate.now();
        kontoauszugDrucken(kontoService, geldautomatSession, today.minusDays(1), today, today);

        /*
         * Aufgabe VIII: Im OnlineBanking "Anmelden"
         */

        OnlineBankingService onlineBankingService = OnlineBankingService.create(repository);
        OnlineBankingSession onlineBankingSession = onlineBankingService.login(bankkonto.getKontonummer(), bankkonto.getKunde().getKundennummer(), "0123");

        /*
         * Aufgabe IX: Geld mittels OnlineBanking an jemanden überweisen
         */

//        Bankkonto bankkontoEmpfaenger = geldMittelsOnlineBankingUeberweisen(kontoService, onlineBankingService, onlineBankingSession);

        /*
         * Aufgabe VII.II: Erneuter Kontoauszug
         */

//        OnlineBankingSession onlineBankingSessionEmpfaenger = onlineBankingService.login(bankkontoEmpfaenger.getKontonummer(), bankkontoEmpfaenger.getKunde().getKundennummer(), "4567");
//        kontoauszugDruckenNachOnlineBanking(kontoService, geldautomatSession, onlineBankingSessionEmpfaenger, today);
    }


    private static void kontoauszugDruckenNachOnlineBanking(KontoService kontoService, Session sessionKunde, Session sessionEmpfaenger, LocalDate today) {

        Kontoauszug zweiterKontoauszug = kontoService.erstelleKontoauszug(sessionKunde, today.minusDays(1), today);
        assert zweiterKontoauszug.startkontostand().doubleValue() == 0;
        assert zweiterKontoauszug.transaktionen().size() == 3;
        assert zweiterKontoauszug.endkontostand().doubleValue() == 9534.5;

        Kontoauszug kontoauszugEmpfaenger = kontoService.erstelleKontoauszug(sessionEmpfaenger, today.minusDays(1), today);
        assert kontoauszugEmpfaenger.startkontostand().doubleValue() == 0;
        assert kontoauszugEmpfaenger.transaktionen().size() == 1;
        assert kontoauszugEmpfaenger.endkontostand().doubleValue() == 120.5;
    }

    private static Bankkonto geldMittelsOnlineBankingUeberweisen(KontoService kontoService, OnlineBankingService onlineBankingService, OnlineBankingSession onlineBankingSession) {
        long kontonummerEmpfaenger = kontoService.eroeffneKonto("Bruce", "Banner", "4567");
        Bankkonto bankkontoEmpfaenger = kontoService.findByKontonummer(kontonummerEmpfaenger).orElseThrow();
        onlineBankingService.ueberweisen(onlineBankingSession, kontonummerEmpfaenger, BigDecimal.valueOf(120.5));
        assert onlineBankingService.ermittleKontostand(onlineBankingSession).doubleValue() == 9534.5;

        OnlineBankingSession onlineBankingSessionEmpfaenger = onlineBankingService.login(kontonummerEmpfaenger, bankkontoEmpfaenger.getKunde().getKundennummer(), "4567");
        assert onlineBankingService.ermittleKontostand(onlineBankingSessionEmpfaenger).doubleValue() == 120.5;
        return bankkontoEmpfaenger;
    }

    private static void kontoauszugDrucken(KontoService kontoService, Session session, LocalDate start, LocalDate ende, LocalDate erwartetesDatumDerTransaktionen) {
        Kontoauszug kontoauszug = kontoService.erstelleKontoauszug(session, start, ende);
        assert kontoauszug.startkontostand().doubleValue() == 0;
        assert kontoauszug.transaktionen().size() == 2;
        assert kontoauszug.endkontostand().doubleValue() == 9_655.0;

        kontoauszug.transaktionen().forEach(transaktion -> {
            assert transaktion.getZeitpunkt().toLocalDate().equals(erwartetesDatumDerTransaktionen);
            assert transaktion.getBetrag() != null;
        });
    }

    private static void bargeldMittelsGeldautomatAbheben(GeldautomatenService geldautomatenService, Geldautomat geldautomat, GeldautomatSession geldautomatSession) {
        BigDecimal bargeld = geldautomatenService.abheben(geldautomatSession, BigDecimal.valueOf(345));
        assert bargeld.doubleValue() == 345.0;
        assert geldautomatenService.ermittleKontostand(geldautomatSession).doubleValue() == 9_655.0;
        assert geldautomat.ermittleVerfuegbaresBargeld().doubleValue() == 39_655.0;
    }

    private static void bankkontoMittelsGeldautomatAufladen(GeldautomatenService geldautomatenService, Geldautomat geldautomat, GeldautomatSession geldautomatSession) {
        geldautomatenService.aufladen(geldautomatSession, BigDecimal.valueOf(10_000));
        assert geldautomatenService.ermittleKontostand(geldautomatSession).doubleValue() == 10_000.0;
        assert geldautomat.ermittleVerfuegbaresBargeld().doubleValue() == 40_000.0;
    }

    private static Geldautomat findeGeldautomat(GeldautomatenService geldautomatenService, Location location) {
        Geldautomat geldautomat = geldautomatenService.findByLocation(location).orElseThrow();
        assert geldautomat.ermittleVerfuegbaresBargeld().doubleValue() == 30_000.0;
        assert geldautomat.getLocation().latitude() == location.latitude();
        assert geldautomat.getLocation().longitude() == location.longitude();
        return geldautomat;
    }

    private static void erstelleGeldautomaten(GeldautomatenService geldautomatenService, Location location) {
        geldautomatenService.create(location, BigDecimal.valueOf(30_000));
    }

    private static Bankkonto eroeffneBankkonto(KontoService kontoService) {
        long kontonummer = kontoService.eroeffneKonto("Tony", "Stark", "0123");
        Bankkonto bankkonto = kontoService.findByKontonummer(kontonummer).orElseThrow();
        assert bankkonto.getKontonummer() > 0;
        Kunde kunde = bankkonto.getKunde();
        assert kunde != null;
        assert kunde.getKundennummer() > 0;
        assert kunde.getVorname().equals("Tony");
        assert kunde.getNachname().equals("Stark");
        Geldkarte geldkarte = kunde.getGeldkarte();
        assert geldkarte != null;
        assert geldkarte.getKartennummer() > 0;
        assert geldkarte.getKontonummer() > 0;
        return bankkonto;
    }


    public static void erstelleDatenbank(DataSource dataSource) {
        String createAllTables = """
                CREATE TABLE kunde (
                    kundennummer BIGINT GENERATED BY DEFAULT AS IDENTITY PRIMARY KEY,
                    vorname VARCHAR(50) NOT NULL,
                    nachname VARCHAR(50) NOT NULL
                );
                
                CREATE TABLE bankkonto (
                    kontonummer BIGINT GENERATED BY DEFAULT AS IDENTITY PRIMARY KEY,
                    kundennummer BIGINT NOT NULL,
                    konto_pin VARCHAR(50) NOT NULL,
                    kontostand DECIMAL(10,2) NOT NULL,
                
                    CONSTRAINT fk_bankkonto_kunde FOREIGN KEY (kundennummer) REFERENCES kunde(kundennummer)
                );
                
                CREATE TABLE geldkarte (
                    kartennummer BIGINT GENERATED BY DEFAULT AS IDENTITY PRIMARY KEY,
                    kontonummer BIGINT NOT NULL UNIQUE,
                    karte_pin VARCHAR(4) NOT NULL,
                
                    CONSTRAINT fk_geldkarte_konto FOREIGN KEY (kontonummer) REFERENCES bankkonto(kontonummer)
                );
                
                CREATE TABLE geldautomat (
                    automatennummer BIGINT GENERATED BY DEFAULT AS IDENTITY PRIMARY KEY,
                    latitude DOUBLE NOT NULL,
                    longitude DOUBLE NOT NULL,
                    bargeld DECIMAL(10,2) NOT NULL
                );
                
                CREATE TABLE transaktion (
                    id BIGINT GENERATED BY DEFAULT AS IDENTITY PRIMARY KEY,
                    sender_kontonummer BIGINT,
                    empfaenger_kontonummer BIGINT,
                    geldautomat_nummer BIGINT,
                    betrag DECIMAL(10, 2) NOT NULL,
                    typ VARCHAR(50) NOT NULL,
                    zeitpunkt TIMESTAMP WITH TIME ZONE DEFAULT CURRENT_TIMESTAMP NOT NULL,
                
                    CONSTRAINT fk_trans_sender FOREIGN KEY (sender_kontonummer) REFERENCES bankkonto(kontonummer),
                    CONSTRAINT fk_trans_empfaenger FOREIGN KEY (empfaenger_kontonummer) REFERENCES bankkonto(kontonummer),
                    CONSTRAINT fk_trans_geldautomat FOREIGN KEY (geldautomat_nummer) REFERENCES geldautomat(automatennummer)
                );
                """;
        try (Connection connection = dataSource.getConnection();
             Statement statement = connection.createStatement()) {
            statement.execute(createAllTables);
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

}
