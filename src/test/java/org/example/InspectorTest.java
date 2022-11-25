package org.example;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.io.ByteArrayOutputStream;
import java.io.PrintStream;

import static org.junit.jupiter.api.Assertions.*;

class InspectorTest {

    private static final ByteArrayOutputStream outStream = new ByteArrayOutputStream();

    @BeforeEach
    public void setUp() {
        System.setOut(new PrintStream(outStream));
    }

    @AfterEach
    public void tearDown() {
        outStream.reset();
    }

    @Test
    public void testClassName() {
        new Inspector().inspect(new String(), false);
        String expected = "\nClass Name: java.lang.String";
        assert(outStream.toString().contains(expected));
    }

    @Test
    public void testWrongClassName(){
        new Inspector().inspect(new String(), false);
        String notExpected = "\nClass Name: String";
        assertFalse(outStream.toString().contains(notExpected));
    }
    @Test
    public void testSuperClassInfo(){
        new Inspector().inspect(new String(), false);
        String expected = "\nSuper Class Name: java.lang.Object";
        assert(outStream.toString().contains(expected));

    }

}