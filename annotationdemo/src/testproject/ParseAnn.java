package testproject;

import java.lang.annotation.Annotation;
import java.lang.reflect.Method;

public class ParseAnn {
	public static void main(String[] args) {
		try {
			Class c = Class.forName("testproject.Student");
			boolean isExist = c.isAnnotationPresent(Description.class);
			if (isExist) {
				Description d = (Description) c.getAnnotation(Description.class);
				System.out.println(d.value());
			}
			
			Method[] ms = c.getMethods();
			for(Method m:ms) {
				boolean isMExist = m.isAnnotationPresent(Description.class);
				if (isMExist) {
					Description d = m.getAnnotation(Description.class);
					System.out.println(d.value());
				}
			}
			
			for(Method m:ms) {
				Annotation[] as = m.getAnnotations();
				for(Annotation a:as) {
					if (a instanceof Description) {
						Description d = (Description)a;
						System.out.println(d.value());
					}
				}
			}
			
		} catch (ClassNotFoundException e) {
			e.printStackTrace();
		}
	}
}
