package demo;

import java.lang.reflect.Field;
import java.lang.reflect.Method;

public class Test {
	public static void main(String[] args) {
		Filter f1 = new Filter();
		f1.setId(10);
		
		Filter f2 = new Filter();
		f2.setUserName("dolores");
		
		Filter f3 = new Filter();
		f3.setEmail("dolores@gmail.com,123@qq.com,tedy@163.com");
	
		String sql1 = query(f1);
		String sql2 = query(f2);
		String sql3 = query(f3);
		
		System.out.println(sql1);
		System.out.println(sql2);
		System.out.println(sql3);
	}
	
	private static String query(Filter f) {
		StringBuffer sb = new StringBuffer();
		Class c = f.getClass();
		
		boolean exists = c.isAnnotationPresent(Table.class);
		if(!exists) {
			return null;
		}
		
		//获取表名
		Table t = (Table) c.getAnnotation(Table.class);
		String tableName = t.value();
		sb.append("select * from ").append(tableName).append(" where 1=1");		
		
		//获取字段名
		Field[] fArray = c.getDeclaredFields();
		for(Field field:fArray) {
			boolean fExists = field.isAnnotationPresent(Column.class);
			if (!fExists) {
				continue;
			}
			
			Column column = field.getAnnotation(Column.class);
			String columnName = column.value();
			
			//获取字段值
			String fieldName = field.getName();
			String getMethodName = "get"+fieldName.substring(0,1).toUpperCase()+
					fieldName.substring(1);
			Object fieldValue = null;
			try {
				Method getMethod = c.getMethod(getMethodName);
				fieldValue = getMethod.invoke(f);
			} catch (Exception e) {
				e.printStackTrace();
			} 
			
			if (fieldValue == null || 
					(fieldValue instanceof Integer && (Integer)fieldValue == 0)) {
				continue;
			}
			
			sb.append(" and ").append(columnName);
			if (fieldValue instanceof String) {
				if (((String)fieldValue).contains(",")) {
					String[] values = ((String)fieldValue).split(",");
					sb.append(" in(");
					for(String v:values) {
						sb.append("'").append(v).append("'").append(",");
					}
					sb.deleteCharAt(sb.length()-1);
					sb.append(")");
				}else {
					sb.append("=").append("'").append(fieldValue).append("'");
				}
			}else if(fieldValue instanceof Integer) {
				sb.append("=").append(fieldValue);
			}
		}
		
		return sb.toString();
	}
}
