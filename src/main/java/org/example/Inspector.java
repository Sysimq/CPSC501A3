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

    public void getInterfaceInfo(Class c, Object obj, boolean recursive, int depth){
        Class[] classInterfaces = c.getInterfaces();
        if (classInterfaces.length > 0){
            for(Class i : classInterfaces){
                print("Class Implements Interface: "+ i.getName(), depth);
                inspectClass(i, obj, recursive, depth + 1);
            }
        }
    }

    public void getConstructor(Class c, int depth){
        Constructor[] classConstructors = c.getDeclaredConstructors();
        if (classConstructors.length > 0){
            for (Constructor con: classConstructors){
                printConstructorInfo(depth, depth + 1, con);
            }
        }
    }
    private void printConstructorInfo(int depth, int t, Constructor con){
        print("Constructor Name: " +con.getName(), depth);
        Class[] exceptions = con.getExceptionTypes();
        if(exceptions.length >0){
            for (Class e: exceptions){
                print("Constructor Exceptions: "+ e.getName(), t);
            }
        }
        Class[] paramTypes = con.getParameterTypes();
        for(Class paramType: paramTypes){
            print("Constructor Parameter Types: " + paramType.getName(),t);
        }
        print("Constructor Modifiers: "+Modifier.toString(con.getModifiers()),t);
    }

    public void getMethod(Class c, int depth){
        Method[] methods = c.getDeclaredMethods();

        if(methods.length > 0){
            for (Method m : methods){
                printMethodInfo(depth, depth + 1, m);
            }
        }
    }
    private void printMethodInfo(int depth, int t, Method m){
        print("Method: "+m.getName(), depth);
        Class[] exceptions = m.getExceptionTypes();
        if (exceptions.length > 0){
            for (Class e: exceptions){
                print("Method Exceptions: "+e.getName(), t);
            }
        }
        Class[] parameters = m.getParameterTypes();
        if(parameters.length > 0){
            for(Class p: parameters){
                print("Method Parameter Types: "+p.getName(), t);
            }
        }
        print("Method Return Type: " + m.getReturnType(), t);
        String modifier = Modifier.toString(m.getModifiers());
        print("Method Modifiers: " + modifier, t);

    }
    public void getFieldInfo(Class c, Object obj, boolean recursive, int depth){
        Field[] fields = c.getDeclaredFields();
        if(fields.length > 0){
            for(Field f: fields){
                printFieldInfo(obj, recursive, depth, depth + 1, f);
            }
        }
    }
    private void printFieldInfo(Object obj, boolean recursive, int depth, int t, Field f){
        f.setAccessible(true);
        print("Field Name: "+ f.getName(), depth);
        Class type = f.getType();
        print("Field Type: "+type.getSimpleName(),t);
        String modifiers = Modifier.toString(f.getModifiers());
        print("Field Modifiers: " + modifiers, t);

        try{
            Object fieldObj = f.get(obj);

            if(fieldObj == null){
                print("Value: null", t);
            }
            else if (type.isPrimitive()) {
                print("Value: "+fieldObj.toString(), t);
            }
            else if (type.isArray()) {
                getArrayInfo(f.getType(),fieldObj,recursive,t);
            }
            else{
                if(recursive){
                    inspectClass(type, fieldObj, true, t);
                }
                else{
                    print("Reference Value: "+ fieldObj.getClass().getName()
                            + "@" + Integer.toHexString(System.identityHashCode(obj)), t);
                }
            }
        }catch(IllegalAccessException e){
            e.printStackTrace();
        }
    }
    public void getArrayInfo(Class c, Object obj, boolean recursive, int depth){
        Class cType = c.getComponentType();
        int t = depth +1;
        int aLength = Array.getLength(obj);

        print("Component Type: " + cType.getSimpleName(), depth + 1);
        print("Array Length: " + aLength, depth + 1);

        for (int i = 0; i < aLength; i++) {
            Object arrayObj = Array.get(obj, i);

            if (arrayObj == null) {
                print(i +": null",t+1);
            }
            else if (cType.isPrimitive()) {
                print(i +": "+ arrayObj.getClass().getName(), t+1);
            }
            else if (cType.isArray()) {
                getArrayInfo(arrayObj.getClass(), arrayObj, recursive, t+1);
            }
            else {
                if (recursive) {
                    print(i + ": Array", t+1);
                    inspectClass(arrayObj.getClass(), arrayObj, true, t+1);
                }
                else {
                    print("Value: " + arrayObj.getClass().getName() + "@" + Integer.toHexString(System.identityHashCode(arrayObj)), t+1);
                }
            }

        }
    }

    private void print(String output, int depth){
        for (int i = 0; i < depth; i++)
            System.out.print("  ");
        System.out.println(output);
    }

}
