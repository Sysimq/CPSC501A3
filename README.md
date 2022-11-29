# CPSC501A3

## Description
The goal of this assignment is to create a reflective object inspector that does a complete introspection of an object at runtime. The inspector will be implemented in a Java class called Inspector, and will be invoked using the method:
public void inspect(Object obj, boolean recursive)
This method will introspect on the object specified by the first parameter obj, printing what it
finds to standard output. 

## Direction
All source code can be found in `cpsc501a3/src/main/java/org/example` and all output script text file in `cpsc501a3`.

Bonus code can be found in `cpsc501a3/src/main/java/org/example/DriverBonus.java`

To run `DriverBonus.java`, you need 3 arguments: ` [Inspector Class] [Class to inspect] [Recursive(True/False)]`. For example, you can run code using following 3 command line arguments `org.example.Inspector org.example.ClassB True`

For VM flag, add following commands to vm configuration:
```--add-opens java.base/java.lang=ALL-UNNAMED```


# Refactoring

## Long Method
One of the bad code smell I had was `Long Method`. My `printFieldInfo` method was getting long and hard to read. As a result I decide to apply `Extract Method` technique in order to better organize the code. 
```
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
                    print("Value: ",t);
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
```

I first extracted the functionality which performs inspecting field object into a seperate method called `inspectFieldObj`.

```
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
```
See commit: b0753fbf7c67ca67c09c98be150c504b141d8c34


## Rename Variable
The next couple of refactorings I performed dealth with the fact that some of the variable names are not informative.
```
    Class cType = c.getComponentType();

```
```
  int t = depth +1;
```
I rename some of the variable to give more information about the varialbe.
```
        Class componentType = c.getComponentType();

```
```
        int tab = depth +1;
```
See commit: b0753fbf7c67ca67c09c98be150c504b141d8c34

## Javadoc
Then I added some javadoc so other can better understand the code.
See commit: 60c4ca07734ecc92ca0e439c84e669cfe6a3f38e









