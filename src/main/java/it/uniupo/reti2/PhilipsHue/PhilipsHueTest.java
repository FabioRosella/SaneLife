package it.uniupo.reti2.PhilipsHue;



import org.junit.Before;
import org.junit.Test;


class PhilipsHueTest {

    private PhilipsHue luce;
    public static void main(String[] args) {
        PhilipsHueTest ph = new PhilipsHueTest();
        ph.setup();
        ph.turnOnLight();
    }
    @Before
    void setup ()
    {
        luce= new PhilipsHue(2);
    }
    @Test
    void turnOnLight() {
        try {
            luce.turnOnLight(10);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    @Test
    void turnOffLight() {
    }
}