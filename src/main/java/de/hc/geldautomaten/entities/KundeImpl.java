package de.hc.geldautomaten.entities;

import java.util.Objects;

import static java.util.Objects.requireNonNull;

public class KundeImpl implements Kunde {
    private final String vorname;
    private final String nachname;
    private final long kundennummer;
    private final Geldkarte geldkarte;

    public KundeImpl(long kundennummer, String vorname, String nachname, Geldkarte geldkarte) {
        if (kundennummer <= 0) {
            throw new IllegalArgumentException("kundennummer muss positiv sein.");
        }
        this.kundennummer = kundennummer;
        this.vorname = requireNonNull(vorname, "vorname darf nicht null sein.");
        this.nachname = requireNonNull(nachname, "nachname darf nicht null sein.");
        this.geldkarte = requireNonNull(geldkarte, "geldkarte darf nicht null sein.");
    }

    @Override
    public String getVorname() {
        return vorname;
    }

    @Override
    public String getNachname() {
        return nachname;
    }

    @Override
    public long getKundennummer() {
        return kundennummer;
    }

    @Override
    public Geldkarte getGeldkarte() {
        return geldkarte;
    }

    @Override
    public boolean equals(Object o) {
        if (o == null || getClass() != o.getClass()) return false;
        KundeImpl kunde = (KundeImpl) o;
        return kundennummer == kunde.kundennummer;
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(kundennummer);
    }

    @Override
    public String toString() {
        return "KundeImpl{" +
                "vorname='" + vorname + '\'' +
                ", nachname='" + nachname + '\'' +
                ", kundennummer=" + kundennummer +
                ", geldkarte=" + geldkarte +
                '}';
    }
}
