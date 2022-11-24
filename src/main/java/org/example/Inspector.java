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
        print("CLASS INSEPCTION", depth);
        print("Class: "+c.getName(), depth);
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

    }
    public void getFieldInfo(Class c, Object obj, boolean recursive, int depth){

    }
    public void getArrayInfo(Class c, Object obj, boolean recursive, int depth){

    }


    private void print(String output, int depth){
        for (int i = 0; i < depth; i++)
            System.out.print("  ");
        System.out.println(output);
    }

}
