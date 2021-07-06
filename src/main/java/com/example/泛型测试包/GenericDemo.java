package com.example.泛型测试包;

import java.util.ArrayList;
import java.util.List;

public class GenericDemo {

    public static void main(String[] args) {
        /**
         * 两点变化：
         * 1.原先代码是 User user = userDao.get(new User());
         *   编译器根据泛型类型User，帮我们强转了
         * 2.List<User>的泛型被擦除了，只剩下List
         */
        UserDao userDao = new UserDao();
        User user = userDao.get(new User());
        List<User> list = userDao.getList(new User());
    }

}

class BaseDao<T> {

    public T get(T t) {
        return t;
    }

    public List<T> getList(T t) {
        return new ArrayList<>();
    }
}


class UserDao extends BaseDao<User> {

}

class User {

}