package de.hc.geldautomaten.entities;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.Objects;
import java.util.Optional;

import static java.util.Objects.requireNonNull;

public class TransaktionImpl implements Transaktion {
    private final long id;
    private final Long senderKontonummer;
    private final Long empfaengerKontonummer;
    private final Long geldautomatNummer;
    private final BigDecimal betrag;
    private final Typ typ;
    private final LocalDateTime zeitpunkt;

    public TransaktionImpl(long id, Long senderKontonummer, Long empfaengerKontonummer, Long geldautomatNummer, BigDecimal betrag, Typ typ, LocalDateTime zeitpunkt) {

        if (id <= 0) throw new IllegalArgumentException("id muss positiv sein.");
        this.id = id;

        if (senderKontonummer == null && empfaengerKontonummer == null) {
            throw new IllegalArgumentException("Eine Transaktion muss ein Sender- oder ein Empfängerkonto haben.");
        }
        this.senderKontonummer = senderKontonummer;
        this.empfaengerKontonummer = empfaengerKontonummer;
        this.geldautomatNummer = geldautomatNummer;

        this.betrag = requireNonNull(betrag, "betrag darf nicht null sein.");
        this.typ = requireNonNull(typ, "typ der Transaktion darf nicht null sein.");
        this.zeitpunkt = requireNonNull(zeitpunkt, "zeitpunkt darf nicht null sein.");
    }


    @Override
    public long getId() {
        return id;
    }

    @Override
    public Optional<Long> getSenderKontonummer() {
        return Optional.ofNullable(senderKontonummer);
    }

    @Override
    public Optional<Long> getEmpfaengerKontonummer() {
        return Optional.ofNullable(empfaengerKontonummer);
    }

    @Override
    public Optional<Long> getGeldautomatNummer() {
        return Optional.ofNullable(geldautomatNummer);
    }

    @Override
    public Typ getTyp() {
        return typ;
    }

    @Override
    public LocalDateTime getZeitpunkt() {
        return zeitpunkt;
    }

    @Override
    public BigDecimal getBetrag() {
        return betrag;
    }

    @Override
    public boolean equals(Object o) {
        if (o == null || getClass() != o.getClass()) return false;
        TransaktionImpl that = (TransaktionImpl) o;
        return id == that.id;
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(id);
    }

    @Override
    public String toString() {
        return "TransaktionImpl{" + "id=" + id + ", senderKontonummer=" + senderKontonummer + ", empfaengerKontonummer=" + empfaengerKontonummer + ", geldautomatNummer=" + geldautomatNummer + ", betrag=" + betrag + ", typ=" + typ + ", zeitpunkt=" + zeitpunkt + '}';
    }
}
