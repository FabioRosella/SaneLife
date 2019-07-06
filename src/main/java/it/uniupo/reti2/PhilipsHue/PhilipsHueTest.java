package it.uniupo.reti2.PhilipsHue;

class PhilipsHueTest {

    private PhilipsHue luce;

    public static void main(String[] args) {
        PhilipsHueTest ph = new PhilipsHueTest();
        ph.setup();
        ph.turnColorloopOn();
        ph.turnOnLight();

    }

    void setup ()
    {
        luce= new PhilipsHue(1);
    }

    void turnColorloopOn ()
    {
        luce.turnColorloopOn();
    }
    void turnOnLight() {
        try {
            luce.turnOnLight(10000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }
}