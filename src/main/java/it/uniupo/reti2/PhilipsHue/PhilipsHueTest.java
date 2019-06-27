package it.uniupo.reti2.PhilipsHue;

class PhilipsHueTest {

    private PhilipsHue luce;

    public static void main(String[] args) {
        PhilipsHueTest ph = new PhilipsHueTest();
        ph.setup();
        ph.turnOnLight();
    }

    void setup ()
    {
        luce= new PhilipsHue(2);
    }

    void turnOnLight() {
        try {
            luce.turnOnLight(10);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }
}