package de.hc.geldautomaten.entities;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;
import java.util.Objects;

import static java.util.Objects.requireNonNull;

public class BankkontoImpl implements Bankkonto {
        private final long kontonummer;
        private final Kunde kunde;
        private final String onlineBankingPin;
        private BigDecimal kontostand;

    public BankkontoImpl(long kontonummer, Kunde kunde, String onlineBankingPin, BigDecimal kontostand) {
        if (kontonummer <= 0) {
            throw new IllegalArgumentException("kontonummer muss positiv sein.");
        }
        this.kontonummer = kontonummer;
        this.kunde = requireNonNull(kunde, "kunde darf nicht null sein.");
        this.onlineBankingPin = requireNonNull(onlineBankingPin, "onlineBankingPin darf nicht null sein.");
        this.kontostand = requireNonNull(kontostand, "kontostand darf nicht null sein.");
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
        return pin.equals(onlineBankingPin);
    }

    @Override
    public BigDecimal ermittleKontostand() {
        return kontostand;
    }

    @Override
    public BigDecimal ermittleKontostand(LocalDate localDate) {
        return null;
    }  // gehört eher in den Service?

    @Override
    public List<Transaktion> ermitteleTransaktionen(LocalDate inklusivesEnde) {  // gehört eher in den Service?
        return List.of();
    }

    @Override
    public void abheben(BigDecimal betrag) {
        requireNonNull(betrag, "Betrag darf nicht null sein.");
        if (betrag.signum() <= 0) {
            throw new IllegalArgumentException("Abzuhebender Betrag muss positiv sein.");
        }
        if (kontostand.compareTo(betrag) < 0) {
            throw new IllegalStateException("Kontostand nicht ausreichend. Aktuell: " + kontostand + ", benötigt: " + betrag);
        }
        kontostand = kontostand.subtract(betrag);
    }

    @Override
    public void aufladen(BigDecimal betrag) {
        requireNonNull(betrag, "Betrag darf nicht null sein.");
        if (betrag.signum() <= 0) {
            throw new IllegalArgumentException("Aufzuladender Betrag muss positiv sein.");
        }
        kontostand = kontostand.add(betrag);
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
                ", kontostand=" + kontostand +
                '}';
    }
}
