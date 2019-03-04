package com.perago.techtest;

import com.google.gson.Gson;
import java.io.Serializable;
import java.lang.reflect.Method;
import java.util.HashSet;
import java.util.Objects;
import java.util.Set;
import org.apache.commons.beanutils.BeanMap;
import org.apache.commons.beanutils.PropertyUtilsBean;

/**
 * The object representing a diff. Implement this class as you see fit.
 *
 */
public class Diff<T extends Serializable> implements DiffEngine {

    private static final long serialVersionUID = 1L;

    private Object diffObject;
    private Gson gson = new Gson();

    public Diff(Object diffObjec) {
        this.diffObject = diffObject;
    }

    public Object getDiffObject() {
        return diffObject;
    }

    public final String toString() {
        return String.format("[%s: %s, %s]", new Object[]{diffObject});
    }

    public <T extends Serializable> T apply(T original, Diff<?> diff) throws DiffException {
        Object value = null;

        if (!original.getClass().isAssignableFrom(diff.getDiffObject().getClass())) {
            return null;
        }

        Method[] methods = original.getClass().getMethods();

        for (Method fromMethod : methods) {
            if (fromMethod.getDeclaringClass().equals(original.getClass())
                    && fromMethod.getName().startsWith("get")) {

                String fromName = fromMethod.getName();
                String toName = fromName.replace("get", "set");

                try {
                    Method toMetod = original.getClass().getMethod(toName, fromMethod.getReturnType());
                    value = fromMethod.invoke(diff, (Object[]) null);
                    if (value != null) {
                        toMetod.invoke(original, value);
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }

        return (T) value;
    }

    public <T extends Serializable> Diff<T> calculate(T original, T modified) throws DiffException {

        Set<String> propertyNamesToAvoid = new HashSet();
        Long deep = 10L;
        String parentPropertyPath = null;

        propertyNamesToAvoid = propertyNamesToAvoid != null ? propertyNamesToAvoid : new HashSet<>();
        parentPropertyPath = parentPropertyPath != null ? parentPropertyPath : "";

        Object diffObject = null;
        try {
            diffObject = original.getClass().newInstance();
        } catch (Exception e) {
            return (Diff<T>) diffObject;
        }

        BeanMap map = new BeanMap(original);

        PropertyUtilsBean propUtils = new PropertyUtilsBean();

        for (Object propNameObject : map.keySet()) {
            String propertyName = (String) propNameObject;
            String propertyPath = parentPropertyPath + propertyName;

            if (!propUtils.isWriteable(diffObject, propertyName) || !propUtils.isReadable(modified, propertyName)
                    || propertyNamesToAvoid.contains(propertyPath)) {
                continue;
            }

            Object property1 = null;
            try {
                property1 = propUtils.getProperty(original, propertyName);
            } catch (Exception e) {
            }
            Object property2 = null;
            try {
                property2 = propUtils.getProperty(modified, propertyName);
            } catch (Exception e) {
            }
            try {

                if (!Objects.deepEquals(property1, property2)) {
                    propUtils.setProperty(diffObject, propertyName, property2);
                    System.out.println("> " + propertyPath + " is different (oldValue=\"" + property1 + "\", newValue=\""
                            + property2 + "\")");
                } else {
                    System.out.println("  " + propertyPath + " is equal");
                }
            } catch (Exception e) {
            }
        }

        Diff<T> result = new Diff(diffObject) {
        };       

        result.diffObject = diffObject;      

        return result;
    }
}
