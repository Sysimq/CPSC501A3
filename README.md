# CPSC501A3

## Getting started



# Refactoring

## Long Method
One bad code smell I had was `Long Method`. My `printFieldInfo` method was getting long and hard to read. As a result I decide to apply `Extract Method` technique in order to better organize the code. 
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



## Rename Field

