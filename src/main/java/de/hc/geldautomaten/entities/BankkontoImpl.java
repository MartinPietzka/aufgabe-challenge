package de.hc.geldautomaten.entities;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import static java.util.Objects.requireNonNull;

public class BankkontoImpl implements Bankkonto {
    private static final Logger logger = LoggerFactory.getLogger(BankkontoImpl.class);

    private final long kontonummer;
    private final Kunde kunde;
    private final String onlineBankingPin;
    private BigDecimal aktuellerKontostand;

    private List<Transaktion> transaktionen = new ArrayList<>();

    public BankkontoImpl(long kontonummer, Kunde kunde, String onlineBankingPin, BigDecimal aktuellerKontostand) {
        if (kontonummer <= 0) {
            throw new IllegalArgumentException("kontonummer muss positiv sein.");
        }
        this.kontonummer = kontonummer;
        this.kunde = requireNonNull(kunde, "kunde darf nicht null sein.");
        this.onlineBankingPin = requireNonNull(onlineBankingPin, "onlineBankingPin darf nicht null sein.");
        this.aktuellerKontostand = requireNonNull(aktuellerKontostand, "kontostand darf nicht null sein.");
    }

    @Override
    public long getKontonummer() {
        return kontonummer;
    }

    @Override
    public Kunde getKunde() {
        return kunde;
    }

    @Override
    public boolean checkOnlineBankingPin(String pin) {
        if (pin == null) return false;
        boolean isPinCorrect = pin.equals(onlineBankingPin);
        if (!isPinCorrect) {
            logger.warn("Falsche Pin eingegeben für Konto {}.", kontonummer);
        }
        return isPinCorrect;
    }

    @Override
    public BigDecimal ermittleKontostand() {
        return aktuellerKontostand;
    }

    @Override
    public BigDecimal ermittleKontostand(LocalDate localDate) {  // liefert den Kontostand kurz vor Mitternacht
        List<Transaktion> transaktionenBisDatum = ermitteleTransaktionen(localDate);
        return transaktionenBisDatum.stream()
                .map(t -> t.getBetrag())
                .reduce(BigDecimal.ZERO, BigDecimal::add);
    }

    @Override
    public List<Transaktion> ermitteleTransaktionen(LocalDate inklusivesEnde) {
        return transaktionen.stream()
                .filter(t -> !t.getZeitpunkt().toLocalDate().isAfter(inklusivesEnde))
                .toList();
    }

    @Override
    public void abheben(BigDecimal betrag) {
        requireNonNull(betrag, "Betrag darf nicht null sein.");
        if (betrag.signum() <= 0) {
            throw new IllegalArgumentException("Abzuhebender Betrag muss positiv sein.");
        }
        if (aktuellerKontostand.compareTo(betrag) < 0) {
            throw new IllegalStateException("Kontostand nicht ausreichend. Aktuell: " + aktuellerKontostand + ", benötigt: " + betrag);
        }
        aktuellerKontostand = aktuellerKontostand.subtract(betrag);
        transaktionen.add(new TransaktionImpl(LocalDateTime.now(), betrag.negate()));
        logger.info("ABHEBUNG: Betrag {} von Konto {} abgebucht. Neuer Kontostand: {}", betrag, kontonummer, aktuellerKontostand);
    }

    @Override
    public void aufladen(BigDecimal betrag) {
        requireNonNull(betrag, "Betrag darf nicht null sein.");
        if (betrag.signum() <= 0) {
            throw new IllegalArgumentException("Aufzuladender Betrag muss positiv sein.");
        }
        aktuellerKontostand = aktuellerKontostand.add(betrag);
        transaktionen.add(new TransaktionImpl(LocalDateTime.now(), betrag));
        logger.info("AUFLADUNG: Betrag {} auf Konto {} eingezahlt. Neuer Kontostand: {}", betrag, kontonummer, aktuellerKontostand);
    }

    @Override
    public boolean equals(Object o) {
        if (o == null || getClass() != o.getClass()) return false;
        BankkontoImpl bankkonto = (BankkontoImpl) o;
        return kontonummer == bankkonto.kontonummer;
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(kontonummer);
    }


    @Override
    public String toString() {
        return "BankkontoImpl{" +
                "kontonummer=" + kontonummer +
                ", kunde=" + kunde +
                ", kontostand=" + aktuellerKontostand +
                '}';
    }
}
