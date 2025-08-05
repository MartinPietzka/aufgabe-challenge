package de.hc.geldautomaten.entities;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.Optional;

public interface Transaktion {

    long getId();

    Optional<Long> getSenderKontonummer();

    Optional<Long> getEmpfaengerKontonummer();

    Optional<Long> getGeldautomatNummer();

    Typ getTyp();

    LocalDateTime getZeitpunkt();

    BigDecimal getBetrag();

}
