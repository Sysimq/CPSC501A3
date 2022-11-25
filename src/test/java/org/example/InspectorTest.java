package org.example;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.io.ByteArrayOutputStream;
import java.io.PrintStream;
import java.io.Serializable;

import org.example.Inspector;
import static org.junit.jupiter.api.Assertions.*;

class InspectorTest {

    private static final ByteArrayOutputStream output = new ByteArrayOutputStream();


    @BeforeEach
    public void setUp() {
        System.setOut(new PrintStream(output));
    }

    @AfterEach
    public void tearDown() {
        output.reset();
    }

    @Test
    public void testStringClassInfo(){
        new Inspector().getClassInfo(String.class, 0);
        String expected = "Class Name: java.lang.String";
        assert(output.toString().contains(expected));
    }

    @Test
    public void testStringSuperClassInfo(){
        new Inspector().getSuperClassInfo(String.class, "",false,0);
        String expected = "Super Class Name: java.lang.Object";
        assert(output.toString().contains(expected));

    }
    @Test
    public void testObjectSuperClassInfo(){
        new Inspector().getSuperClassInfo(Object.class, new Object(), false, 0);
        String expected ="";
        assert(output.toString().contains(expected));
    }
    @Test
    public void testSuperClassInfo_noSuperClass(){
        new Inspector().getSuperClassInfo(Serializable.class, null, false, 0);
        String expected = "No super class found";
        assert(output.toString().contains(expected));
    }
    @Test
    public void testObjectArray(){
        new Inspector().inspect(new ClassB[10], false);
        String expected = "Array Length: 10";
        assert(output.toString().contains(expected));
    }
    @Test
    public void testInterfaceInfo(){
        new Inspector().getInterfaceInfo(String.class, "",false,0);
        String expected = "Class Implements Interface: java.io.Serializable";
        assert(output.toString().contains(expected));
    }
    @Test
    public void testInterfaceInfo_noInterface(){
        new Inspector().getInterfaceInfo(Object.class, new Object(), false, 0);
        String expected ="";
        assert(output.toString().contains(expected));
    }

    @Test
    public void testConstructorInfo(){
        new Inspector().getConstructor(String.class, 0);
        String expected = "Constructor Name: java.lang.String";
        assert(output.toString().contains(expected));

        expected = "Constructor Parameter Types: int";
        assert(output.toString().contains(expected));

        expected = "Constructor Modifiers: public";
        assert(output.toString().contains(expected));
    }

    @Test
    public void testMethodInfo(){
        new Inspector().getMethod(String.class, 0);

        String expected ="Method Name: length";
        assert(output.toString().contains(expected));

        expected="Method Exceptions: java.io.UnsupportedEncodingException";
        assert(output.toString().contains(expected));

        expected = "Method Parameter TypesL int";
        assert(output.toString().contains(expected));

        expected = "Method Return Type: int";
        assert(output.toString().contains(expected));

        expected = "Method Modifiers: public";
        assert(output.toString().contains(expected));
    }

    @Test
    public void testFieldInfo(){
        new Inspector().getFieldInfo(String.class,"",false,0);

        String expected = "Field Name: hash";
        assert(output.toString().contains(expected));

        expected = "Field Type: int";
        assert(output.toString().contains(expected));

        expected = "Field Modifiers: private";
        assert(output.toString().contains(expected));

        expected = "Value: 0";
        assert(output.toString().contains(expected));
    }

    @Test
    public void testArrayInfo(){
        new Inspector().inspect("array",false);

        String expected = "Component Type: char";
        assert(output.toString().contains(expected));

        expected = "Array Length: 5";
        assert(output.toString().contains(expected));

        expected = "java.lang.Character";
        assert(output.toString().contains(expected));
    }
    @Test
    public void testPrint(){
        new Inspector().print("test one tab", 1);
        String expected = "  test one tab\n";
        assert(output.toString().equals(expected));
    }
}