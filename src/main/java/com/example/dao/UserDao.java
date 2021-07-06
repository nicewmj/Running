package com.example.dao;

import com.example.entity.User;

public class UserDao extends BaseDao<User> {
    /**
     * 继承父类，可以重写父类方法，也可以不重写
     */
    @Override
    public void add(User bean) {
        super.add(bean);
    }


    /**
     * 可以有自己的方法
     */

    public void getName() {

    }
}