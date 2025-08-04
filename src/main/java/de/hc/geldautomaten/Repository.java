package de.hc.geldautomaten;

import java.math.BigDecimal;
import java.util.Optional;

public interface Repository {

    Optional<Bankkonto> findBankkontoByKontonummer(long kontonummer);

    Optional<Geldautomat> findGeldautomatByNummer(long nummer);

    Optional<Geldautomat> findGeldautomatByLocation(Location location);

    <T> void save(T obj);

    void beginTransaction();

    void commitTransaction();

    void rollbackTransaction();

    Geldautomat createGeldautomat(Location location, BigDecimal verfuegbaresBargeld);

    Bankkonto createBankkonto(String vorname, String nachname, String pin);
}
