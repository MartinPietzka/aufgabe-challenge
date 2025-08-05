package de.hc.geldautomaten.services;

import de.hc.geldautomaten.Session;
import de.hc.geldautomaten.entities.Bankkonto;
import de.hc.geldautomaten.records.Kontoauszug;
import de.hc.geldautomaten.repositories.Repository;

import java.time.LocalDate;
import java.util.Optional;

public interface KontoService {

    static KontoService create(Repository repository) {
        return new KontoServiceImpl(repository);
    }

    Optional<Bankkonto> findByKontonummer(long kontonummer);

    long eroeffneKonto(String vorname, String nachname, String pin);

    Kontoauszug erstelleKontoauszug(Session session, LocalDate start, LocalDate inklusivesEnde);

}
