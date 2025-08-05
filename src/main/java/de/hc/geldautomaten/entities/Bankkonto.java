package de.hc.geldautomaten.entities;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;

public interface Bankkonto {

    long getKontonummer();

    Kunde getKunde();

    boolean checkOnlineBankingPin(String pin);

    BigDecimal ermittleKontostand();

    BigDecimal ermittleKontostand(LocalDate localDate);

    List<Transaktion> ermitteleTransaktionen(LocalDate inklusivesEnde);

    void abheben(BigDecimal betrag);

    void aufladen(BigDecimal betrag);


}
