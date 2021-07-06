package com.example.dao;

import com.example.annotation.Table;
import com.example.entity.User;
import org.apache.commons.dbcp.BasicDataSource;
import org.springframework.jdbc.core.JdbcTemplate;

import java.lang.reflect.Field;
import java.lang.reflect.ParameterizedType;
import java.util.ArrayList;
import java.util.List;

public class BaseDao<T> {
    private static BasicDataSource datasource;

    //静态代码块链接数据库的参数
    static {
        datasource = new BasicDataSource();
        datasource.setDriverClassName("com.mysql.cj.jdbc.Driver");
        datasource.setUrl("jdbc:mysql://localhost:3306/test");
        datasource.setUsername("root");
        datasource.setPassword("root");
    }

    //得到一个jdbctemplate的对象
    private JdbcTemplate jdbcTemplate = new JdbcTemplate(datasource);

    //dao的操作
    private Class<T> beanClass;

    /**
     * 构造器
     * 初始化的时候完成实际类型参数的获取，比如beanClass<User> 插入User 那么beanClass就是User.class
     */
    public BaseDao() {
        beanClass = (Class<T>) ((ParameterizedType) this.getClass().getGenericSuperclass()).getActualTypeArguments()[0];
    }

    public void add(T bean) {
        //得道User对象的所有字段
        Field[] declaredFields = beanClass.getDeclaredFields();

        //拼接sql语句，表名的直接使用pojo的类名，所有创建类名的时候注意写成User 而不是t_user
        StringBuilder sql = new StringBuilder()
                .append("insert into ")
                //没有使用table注解的时候 直接获取pojo的名字 作为表名
//                .append(beanClass.getSimpleName())
                .append(beanClass.getAnnotation(Table.class).value())
                .append(" values (");
        for (int i = 0; i < declaredFields.length; i++) {
            sql.append("?");
            if (i < declaredFields.length - 1) {

                sql.append(",");
            }
        }
        sql.append(")");
//获取bean字段的值 需要插入的数据值
        List<Object> paramList = new ArrayList<>();
        try {
            for (Field declaredField : declaredFields) {
                declaredField.setAccessible(true);
                Object o = declaredField.get(bean);
                paramList.add(o);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        int size = paramList.size();
        Object[] params = paramList.toArray(new Object[size]);
//传入sql需要的语句模板喝模板需要的参数
        int num = jdbcTemplate.update(sql.toString(), params);
        System.out.println(num);

    }

}






















