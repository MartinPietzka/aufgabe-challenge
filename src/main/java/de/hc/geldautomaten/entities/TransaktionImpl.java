package de.hc.geldautomaten.entities;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.Objects;

import static java.util.Objects.requireNonNull;

public class TransaktionImpl implements Transaktion {
    private final LocalDateTime zeitpunkt;
    private final BigDecimal betrag;

    public TransaktionImpl(LocalDateTime zeitpunkt, BigDecimal betrag) {
        this.zeitpunkt = requireNonNull(zeitpunkt, "zeitpunkt darf nicht null sein.");
        this.betrag = requireNonNull(betrag, "betrag darf nicht null sein.");
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
        return Objects.equals(zeitpunkt, that.zeitpunkt) && Objects.equals(betrag, that.betrag);
    }

    @Override
    public int hashCode() {
        return Objects.hash(zeitpunkt, betrag);
    }

    @Override
    public String toString() {
        return "TransaktionImpl{" +
                "zeitpunkt=" + zeitpunkt +
                ", betrag=" + betrag +
                '}';
    }
}
