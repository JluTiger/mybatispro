package com.demo.servlet;

import com.demo.dao.UsersDao;
import com.demo.entity.Users;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Date;

@WebServlet("/addusers")
public class UsersAddServlet extends HttpServlet {

    private UsersDao usersDao = new UsersDao();
    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        this.doPost(req, resp);
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        //获取要添加的数据
        String username = req.getParameter("username");
        String userpass = req.getParameter("userpass");
        String nickname = req.getParameter("nickname");
        String age = req.getParameter("age");
        String gender = req.getParameter("gender");
        String email = req.getParameter("email");
        String phone = req.getParameter("phone");

        //根据用户数据创建一个用户对象

        Users user = new Users(username,userpass,nickname,Integer.parseInt(age),gender,email,phone,new Date(),new Date(),new Date(),0);
        //将用户对象添加到数据库中
        usersDao.addUser(user);

        //查看刚新增的用户数据
        resp.sendRedirect("/detail?id="+user.getId());

    }
}
