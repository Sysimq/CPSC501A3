package org.example;

import java.lang.reflect.*;

/**
 * CPSC 501
 * Inspector starter class
 *
 * @author Jonathan Hudson
 */
public class Inspector {

    public void inspect(Object obj, boolean recursive) {
        Class c = obj.getClass();
        inspectClass(c, obj, recursive, 0);
    }

    private void inspectClass(Class c, Object obj, boolean recursive, int depth) {
        if(!c.isArray()){
            getClassInfo(c,depth);
            getSuperClassInfo(c, obj, recursive, depth + 1);
            getInterfaceInfo(c, obj, recursive, depth + 1);
            getConstructor(c, depth + 1);
            getMethod(c, depth + 1);
            getFieldInfo(c, obj, recursive, depth + 1);
        }else
            getArrayInfo(c, obj, recursive, depth);
    }

    //Get object class information
    public void getClassInfo(Class c, int depth){
        print("Class Name: "+c.getName(), depth);
    }
    //Get the object's super class information
    public void getSuperClassInfo(Class c, Object obj, boolean recursive, int depth){
        Class superClass = c.getSuperclass();

        if(c.equals(Object.class))
            return;

        if(superClass != null){
            print("Super Class Name: "+ superClass.getName(), depth);
            inspectClass(superClass, obj, recursive, depth + 1);
        }
        else{
            print("No super class found", depth + 1);
        }
    }

    //Get name of each interface the class implements
    public void getInterfaceInfo(Class c, Object obj, boolean recursive, int depth){
        Class[] classInterfaces = c.getInterfaces();
        if (classInterfaces.length > 0){
            for(Class i : classInterfaces){
                print("Class Implements Interface: "+ i.getName(), depth);
                inspectClass(i, obj, recursive, depth + 1);
            }
        }
    }

    //Get the constructors that the class declares
    public void getConstructor(Class c, int depth){
        Constructor[] classConstructors = c.getDeclaredConstructors();
        if (classConstructors.length > 0){
            for (Constructor con: classConstructors){
                printConstructorInfo(depth, depth + 1, con);
            }
        }
    }
    //print constructor info
    private void printConstructorInfo(int depth, int tab, Constructor con){
        //print constructor name
        print("Constructor Name: " +con.getName(), depth);
        //print constructor exception
        Class[] exceptions = con.getExceptionTypes();
        if(exceptions.length >0){
            for (Class e: exceptions){
                print("Constructor Exceptions: "+ e.getName(), tab);
            }
        }
        //print constructor parameters
        Class[] paramTypes = con.getParameterTypes();
        for(Class paramType: paramTypes){
            print("Constructor Parameter Types: " + paramType.getName(),tab);
        }
        //print constructor modifiers
        print("Constructor Modifiers: "+Modifier.toString(con.getModifiers()),tab);
    }
    //Get the methods info that the class declares
    public void getMethod(Class c, int depth){
        Method[] methods = c.getDeclaredMethods();

        if(methods.length > 0){
            for (Method m : methods){
                printMethodInfo(depth, depth + 1, m);
            }
        }
    }
    //print methods
    private void printMethodInfo(int depth, int tab, Method m){
        //print methods name
        print("Method Name: "+m.getName(), depth);
        //print exception the method throws
        Class[] exceptions = m.getExceptionTypes();
        if (exceptions.length > 0){
            for (Class e: exceptions){
                print("Method Exceptions: "+e.getName(), tab);
            }
        }
        //print method's parameters
        Class[] parameters = m.getParameterTypes();
        if(parameters.length > 0){
            for(Class p: parameters){
                print("Method Parameter Types: "+p.getName(), tab);
            }
        }
        //print method's return type
        print("Method Return Type: " + m.getReturnType(), tab);
        //print method modifiers
        String modifier = Modifier.toString(m.getModifiers());
        print("Method Modifiers: " + modifier, tab);

    }
    //Get the field information
    public void getFieldInfo(Class c, Object obj, boolean recursive, int depth){
        Field[] fields = c.getDeclaredFields();
        if(fields.length > 0){
            for(Field f: fields){
                printFieldInfo(obj, recursive, depth, depth + 1, f);
            }
        }
    }
    //print field information
    private void printFieldInfo(Object obj, boolean recursive, int depth, int tab, Field f){
        //set field accessible
        f.setAccessible(true);
        //print field name
        print("Field Name: "+ f.getName(), depth);
        //print field type
        Class type = f.getType();
        print("Field Type: "+type.getSimpleName(),tab);
        //print field modifier
        String modifiers = Modifier.toString(f.getModifiers());
        print("Field Modifiers: " + modifiers, tab);

        try{
            Object fieldObj = f.get(obj);
            inspectFieldObj(obj, recursive, tab, f, type, fieldObj);
        }catch(IllegalAccessException e){
            e.printStackTrace();
        }
    }
    private void inspectFieldObj(Object obj, boolean recursive, int tab, Field f, Class type, Object fieldObj){
        if(fieldObj == null){
            print("Value: null", tab);
        }
        else if (type.isPrimitive()) {
            print("Value: "+fieldObj.toString(), tab);
        }
        else if (type.isArray()) {
            getArrayInfo(f.getType(),fieldObj,recursive,tab);
        }
        else{
            if(recursive){
                print("Value: ",tab);
                inspectClass(type, fieldObj, true, tab);
            }
            else{
                print("Reference Value: "+ fieldObj.getClass().getName()
                        + "@" + Integer.toHexString(System.identityHashCode(obj)), tab);
            }
        }
    }

    //Inspect array information
    public void getArrayInfo(Class c, Object obj, boolean recursive, int depth){
        Class componentType = c.getComponentType();
        int tab = depth +1;
        int aLen = Array.getLength(obj);

        print("Component Type: " + componentType.getSimpleName(), depth + 1);
        print("Array Length: " + aLen, depth + 1);

        for (int i = 0; i < aLen; i++) {
            Object arrayObj = Array.get(obj, i);

            if (arrayObj == null) {
                print(i +": null",tab+1);
            }
            else if (componentType.isPrimitive()) {
                print(i +": "+ arrayObj.getClass().getName(), tab+1);
            }
            else if (componentType.isArray()) {
                print(i + ": Array", tab+1);
                getArrayInfo(arrayObj.getClass(), arrayObj, recursive, tab+1);
            }
            else {
                if (recursive) {
                    print(i + ": ", tab+1);
                    inspectClass(arrayObj.getClass(), arrayObj, true, tab+1);
                }
                else {
                    print("Value: " + arrayObj.getClass().getName() + "@" + Integer.toHexString(System.identityHashCode(arrayObj)), tab+1);
                }
            }

        }
    }

    public void print(String output, int depth){
        for (int i = 0; i < depth; i++)
            System.out.print("  ");
        System.out.println(output);
    }

}
