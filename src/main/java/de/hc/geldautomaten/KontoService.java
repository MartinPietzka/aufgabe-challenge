package de.hc.geldautomaten;

import java.time.LocalDate;
import java.util.Optional;

public interface KontoService {

    static KontoService create(Repository repository) {
        return null;
    }

    Optional<Bankkonto> findByKontonummer(long kontonummer);

    long eroeffneKonto(String vorname, String nachname, String pin);

    Kontoauszug erstelleKontoauszug(Session session, LocalDate start, LocalDate inklusivesEnde);

}
