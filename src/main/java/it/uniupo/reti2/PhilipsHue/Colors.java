package it.uniupo.reti2.PhilipsHue;

public enum Colors {
    red(65535),
    green(24845),
    yellow(10000),
    blue(34277);
    ;
    private final int value;

    Colors(int value) {
        this.value = value;
    }
    public int getValue() {
        return value;
    }
}
