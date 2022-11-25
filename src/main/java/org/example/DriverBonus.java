package org.example;

import java.lang.reflect.*;

public class DriverBonus {
    public static void main(String[] args){
        if (args.length !=3){
            System.err.println("Invalid number of arguments. 3 arguments needed!");
            System.err.println("ARGUMENTS: [Inspector Class] [Class to inspect] [Recursive(True/False)]");
            return;
        }
        try{
            Class inspectorClass = Class.forName(args[0]);
            Constructor inspectMethodCon = inspectorClass.getConstructor();
            Object inspectMethodObj =inspectMethodCon.newInstance();
            Method inspectMethod = inspectorClass.getDeclaredMethod("inspect", Object.class, boolean.class);

            Class inspectClass =Class.forName(args[1]);
            Constructor inspectCon = inspectClass.getConstructor();
            Object inspectObj = inspectCon.newInstance();

            boolean recursive = Boolean.parseBoolean(args[2]);

            inspectMethod.invoke(inspectMethodObj, inspectObj, recursive);

        } catch(Exception e){
            System.err.println("Error: "+e);
        }
    }
}
