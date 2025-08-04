package de.hc.geldautomaten;

public record OnlineBankingSession(Bankkonto bankkonto) implements Session {
    @Override
    public Bankkonto getBankkonto() {
        return bankkonto();
    }
}
