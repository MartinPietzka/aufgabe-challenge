package de.hc.geldautomaten.entities;

import java.util.Objects;

import static java.util.Objects.requireNonNull;

public class GeldkarteImpl implements Geldkarte {
    private final long kartennummer;
    private final long kontonummer;
    private final String kartenPin;

    public GeldkarteImpl(long kartennummer, long kontonummer, String kartenPin) {
        if (kartennummer <= 0) {
            throw new IllegalArgumentException("kartennummer muss positiv sein.");
        }
        this.kartennummer = kartennummer;

        if (kontonummer <= 0) {
            throw new IllegalArgumentException("kontonummer muss positiv sein.");
        }
        this.kontonummer = kontonummer;
        this.kartenPin = requireNonNull(kartenPin, "kartenPin darf nicht null sein.");
    }

    @Override
    public long getKartennummer() {
        return kartennummer;
    }

    @Override
    public long getKontonummer() {
        return kontonummer;
    }

    @Override
    public boolean checkPin(String pin) {
        if (pin == null) return false;
        return pin.equals(kartenPin);
    }

    @Override
    public boolean equals(Object o) {
        if (o == null || getClass() != o.getClass()) return false;
        GeldkarteImpl geldkarte = (GeldkarteImpl) o;
        return kartennummer == geldkarte.kartennummer;
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(kartennummer);
    }

    @Override
    public String toString() {
        return "GeldkarteImpl{" +
                "kontonummer=" + kontonummer +
                ", kartennummer=" + kartennummer +
                '}';
    }
}
