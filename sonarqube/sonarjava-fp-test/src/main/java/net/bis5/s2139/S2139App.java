package net.bis5.s1948;

public class S2139App {

    public BigDecimal calculate(BigDecimal first, BigDecimal second) {
        try {
            return first.add(second);
        } catch (RuntimeException e) {
            return null;
        }
    }
}
