package com.demo.dao;

import com.demo.entity.Users;
import com.demo.utils.SqlSessionFactoryUtils;
import org.apache.ibatis.session.SqlSession;
import org.apache.ibatis.session.SqlSessionFactory;

import java.util.List;

public class UsersDao {
    private SqlSession sqlSession;
    private List<Users> list;

    private Users user;

    private SqlSession getSqlSession(){
        sqlSession = SqlSessionFactoryUtils.getSqlSessionFactory().openSession();
        return sqlSession;
    }

    public List<Users> findAll(){
        try {
            list = getSqlSession().selectList("findUsers");
        }catch (Exception e){
            e.printStackTrace();
        }finally {
            sqlSession.close();
        }
        return list;
    }

    // 根据id查询单个用户
    public Users findById(Integer id){
        try {
            user = getSqlSession().selectOne("findUsers",new Users(id));
        }catch (Exception e){
            e.printStackTrace();
        }finally {
            sqlSession.close();
        }
        return user;
    }

    // 增加一个用户数据到数据库
    public Users addUser(Users user){
        try {
            //返回值：是insert执行过程中影响的行数
            getSqlSession().insert("addUser",user);
            sqlSession.commit();
        }catch (Exception e){
            e.printStackTrace();
        }finally {
            sqlSession.close();
        }
        return user;
    }

    // 用于修改用户资料的方法
    public Users updateUser(Users user){
        try {
            //返回值：是insert执行过程中影响的行数
            getSqlSession().update("updateUser",user);
            sqlSession.commit();
        }catch (Exception e){
            e.printStackTrace();
        }finally {
            sqlSession.close();
        }
        return user;
    }

    // 根据id进行删除
    public void delUsers(Integer id){
        try {
            //返回值：是insert执行过程中影响的行数
            getSqlSession().delete("delUser",id);
            sqlSession.commit();
        }catch (Exception e){
            e.printStackTrace();
        }finally {
            sqlSession.close();
        }
    }


}
