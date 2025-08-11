package de.hc.geldautomaten.entities;

import de.hc.geldautomaten.records.Location;

import java.math.BigDecimal;
import java.util.Objects;

import static java.util.Objects.requireNonNull;

public class GeldautomatImpl implements Geldautomat {
    private final long nummer;
    private final Location location;

    private BigDecimal verfuegbaresBargeld;

    public GeldautomatImpl(long nummer, Location location, BigDecimal verfuegbaresBargeld) {
        if (nummer <= 0) {
            throw new IllegalArgumentException("nummer muss positiv sein.");
        }
        this.nummer = nummer;

        this.location = requireNonNull(location, "location darf nicht null sein.");

        requireNonNull(verfuegbaresBargeld, "verfügbares Bargeld darf nicht null sein.");
        if (verfuegbaresBargeld.signum() < 0) {
            throw new IllegalArgumentException("verfügbares Bargeld darf nicht negativ sein.");
        }
        this.verfuegbaresBargeld = verfuegbaresBargeld;
    }

    @Override
    public long getNummer() {
        return nummer;
    }

    @Override
    public Location getLocation() {
        return location;
    }

    @Override
    public BigDecimal ermittleVerfuegbaresBargeld() {
        return verfuegbaresBargeld;
    }

    @Override
    public void aufladen(BigDecimal betrag) {
        requireNonNull(betrag, "Betrag darf nicht null sein.");
        if (betrag.signum() <= 0) {
            throw new IllegalArgumentException("Aufzuladender Betrag muss positiv sein.");
        }
        verfuegbaresBargeld = verfuegbaresBargeld.add(betrag);
    }

    @Override
    public void abheben(BigDecimal betrag) {
        requireNonNull(betrag, "Betrag darf nicht null sein.");
        if (betrag.signum() <= 0) {
            throw new IllegalArgumentException("Abzuhebender Betrag muss positiv sein.");
        }
        if (verfuegbaresBargeld.compareTo(betrag) < 0) {
            throw new IllegalStateException("Nicht genügend Bargeld im Automaten verfügbar. Verfügbar: " + verfuegbaresBargeld + ", benötigt: " + betrag);
        }
        verfuegbaresBargeld = verfuegbaresBargeld.subtract(betrag);
    }

    @Override
    public boolean equals(Object o) {
        if (o == null || getClass() != o.getClass()) return false;
        GeldautomatImpl that = (GeldautomatImpl) o;
        return nummer == that.nummer;
    }


    @Override
    public int hashCode() {
        return Objects.hashCode(nummer);
    }


    @Override
    public String toString() {
        return "GeldautomatImpl{" +
                "nummer=" + nummer +
                ", location=" + location +
                ", verfuegbaresBargeld=" + verfuegbaresBargeld +
                '}';
    }
}
