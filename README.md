# MyBatis项目案例

##  1、项目功能
项目案例：后台管理系统用户数据维护平台

- 所有用户数据查询

- 单个用户数据查询

- 用户数据修改(完善资料)

- 锁定用户帐号

- 删除用户帐号(可撤回)

- 彻底删除用户账号


## 2、数据表创建

- 数据库：MySQL 8.0

- 数据库名称：mydb

- 数据表：用户表（users）

```sql
# 创建数据库
CREATE DATABASE mydb;
USE mydb;

CREATE TABLE users(
	id INT AUTO_INCREMENT PRIMARY KEY COMMENT '用户编号',
	username VARCHAR(50) NOT NULL COMMENT '登录账号',
	userpass VARCHAR(50) NOT NULL COMMENT '登录密码',
	nickname VARCHAR(50) NOT NULL COMMENT '昵称',
	age INT COMMENT '用户年龄',
	gender VARCHAR(5) COMMENT '用户性别',
	phone VARCHAR(13) COMMENT '联系方式',
	email VARCHAR(20) COMMENT '用户邮箱',
	createTime DATETIME COMMENT '账号创建时间',
	updateTime DATETIME COMMENT '账号最后修改时间',
	lastLogin DATETIME COMMENT '账号最后一次登录时间',
	userStatus INT COMMENT '用户帐号状态 0 正常 1 锁定 2 删除',
	remark TEXT COMMENT '备注'
);

SELECT * FROM users;
```
## 3、界面准备工作

- 开发工具：Intellij

- 使用技术：

	- HTML + CSS + Bootstrap

### 第一步--->>>>简单的web项目配置与发布：
- pom.xml配置

```HTML
<?xml version="1.0" encoding="UTF-8"?>
<project xmlns="http://maven.apache.org/POM/4.0.0"
         xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance"
         xsi:schemaLocation="http://maven.apache.org/POM/4.0.0 http://maven.apache.org/xsd/maven-4.0.0.xsd">
    <modelVersion>4.0.0</modelVersion>

    <groupId>com.demo.mybatis</groupId>
    <artifactId>mybatis-pro</artifactId>
    <version>1.0-SNAPSHOT</version>
    <packaging>war</packaging>

</project>
```

- web.xml

```HTML
<?xml version="1.0" encoding="utf-8" ?>
<web-app xmlns="http://xmlns.jcp.org/xml/ns/javaee"
         xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance"
         xsi:schemaLocation="http://xmlns.jcp.org/xml/ns/javaee
                      http://xmlns.jcp.org/xml/ns/javaee/web-app_3_1.xsd"
         id="WebApp_ID" version="3.1">
    <display-name>mybatispro</display-name>

    <welcome-file-list>
        <welcome-file>index.html</welcome-file>
        <welcome-file>index.htm</welcome-file>
        <welcome-file>index.jsp</welcome-file>
        <welcome-file>default.html</welcome-file>
        <welcome-file>default.htm</welcome-file>
        <welcome-file>default.jsp</welcome-file>
    </welcome-file-list>

</web-app>
```

- index.jsp

```HTML
<%--
  Created by IntelliJ IDEA.
  User: JluTiger
  Date: 2019/5/13
  Time: 16:19
  To change this template use File | Settings | File Templates.
--%>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<html>
<head>
    <title>慕课网用户管理中心</title>
</head>
<body>
    <h1>用户管理中心</h1>
</body>
</html>
```

之后配置Tomcat本地服务器发布即可。

### 第二步--->>>>界面优化
对index.jsp进行修改，使用Bootstrap和jquery进行美化

```HTML
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<html>
<head>
    <title>慕课网用户管理中心</title>
    <link rel="stylesheet" href="lib/bootstrap-3.3.7-dist/css/bootstrap.min.css">
    <script src="lib/jquery-3.3.1/jquery-3.3.1.min.js"></script>
    <script src="lib/bootstrap-3.3.7-dist/js/bootstrap.min.js"></script>

</head>
<body>
<div class="container">
    <div class="row">
        <div class="page-header">
            <h1>慕课网后台管理系统 <small>用户数据管理中心</small></h1>
        </div>
    </div>

    <div class="row">
        <div class="jumbotron">
            <div class="container">
                <h1>MyBatis基础入门课程！</h1>
                <p>通过一个项目来完成基础部分的学习</p>
                <p><a class="btn btn-primary btn-lg" href="#" role="button">查看更多，请上慕课网</a></p>
            </div>
        </div>
    </div>
    <div class="row">
        <table class="table table-hover table-striped">
            <tr>
                <th>用户编号</th>
                <th>登录帐号</th>
                <th>用户昵称</th>
                <th>邮箱</th>
                <th>联系方式</th>
                <th>账号创建时间</th>
                <th>用户状态</th>
                <th>操作</th>
            </tr>

            <tr>
                <th>1</th>
                <th>admin</th>
                <th>小木</th>
                <th>10086@email.com</th>
                <th>12465456</th>
                <th>2017-12-4</th>
                <th>正常</th>
                <th>
                    <a href="">查看</a>
                    <a href="">修改</a>
                    <a href="">删除</a>
                </th>
            </tr>
        </table>

    </div>
</div>
</body>
</html>
```
## 4、基础操作——MyBatis主配置解析

- properties配置加载

- environments环境加载

- settings环境配置

- typeAliases别名设置

- mapper映射加载

首先添加MyBatis依赖：
- pom.xml

```HTML
<?xml version="1.0" encoding="UTF-8"?>
<project xmlns="http://maven.apache.org/POM/4.0.0"
         xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance"
         xsi:schemaLocation="http://maven.apache.org/POM/4.0.0 http://maven.apache.org/xsd/maven-4.0.0.xsd">
    <modelVersion>4.0.0</modelVersion>

    <groupId>com.demo.mybatis</groupId>
    <artifactId>mybatis-pro</artifactId>
    <version>1.0-SNAPSHOT</version>
    <packaging>war</packaging>

    <dependencies>
        <dependency>
            <groupId>org.mybatis</groupId>
            <artifactId>mybatis</artifactId>
            <version>3.4.3</version>
        </dependency>
        <dependency>
            <groupId>mysql</groupId>
            <artifactId>mysql-connector-java</artifactId>
            <version>8.0.12</version>
        </dependency>
    </dependencies>
</project>
```
之后进行MyBatis的相关配置:
- mybatis-config.xml

```HTML
<?xml version="1.0" encoding="UTF-8" ?>
<!DOCTYPE configuration
        PUBLIC "-//mybatis.org//DTD Config 3.0//EN"
        "http://mybatis.org/dtd/mybatis-3-config.dtd">
<configuration>
    <!--
    properties配置，用于加载外部的properties配置文件
    -->
    <properties resource="db.properties"></properties>

    <!--
    environments 主要用户数据源的配置
    可以配置多个数据源，通过default属性来指定当前项目运行过程中使用的是哪个数据源
    -->
    <environments default="development">
        <!--
        environment 用于配置一个具体的独立的数据源
        id属性用于给当前数据源定义一个名称，方便我们的项目指定
        -->
        <environment id="development">
            <!--
            transactionManager用于配置事务管理，默认情况下使用的是JDBC事务管理
            -->
            <transactionManager type="JDBC"/>
            <!-- 使用数据库连接池 -->
            <!--
            dataSource具体数据源的连接信息：type属性用于指定是否使用数据库连接池
            -->
            <dataSource type="POOLED">
                <property name="driver" value="${driver}"/>
                <property name="url" value="${url}"/>
                <property name="username" value="${username}"/>
                <property name="password" value="${password}"/>
            </dataSource>
        </environment>

        <environment id="product">
            <transactionManager type="JDBC"/>
            <!-- 使用数据库连接池 -->
            <dataSource type="POOLED">
                <property name="driver" value="${driver}"/>
                <property name="url" value="${url}"/>
                <property name="username" value="${username}"/>
                <property name="password" value="${password}"/>
            </dataSource>
        </environment>

        <environment id="test">
            <transactionManager type="JDBC"/>
            <!-- 使用数据库连接池 -->
            <dataSource type="POOLED">
                <property name="driver" value="${driver}"/>
                <property name="url" value="${url}"/>
                <property name="username" value="${username}"/>
                <property name="password" value="${password}"/>
            </dataSource>
        </environment>
    </environments>
    <!--mappers主要用于配置我们外部的映射配置文件，在主配置中需要引入加载映射配置文件
        映射配置文件的路径
    -->
    <mappers>
        <!-- mapper主要配置引入某一个具体的映射文件，resource进行路径方式的引入-->
        <mapper resource="mapper/usersMapper.xml"></mapper>
    </mappers>
</configuration>
```

- db.properties

```HTML
driver=com.mysql.cj.jdbc.Driver
url=jdbc:mysql://localhost:3306/mydb?useUnicode=true&characterEncoding=UTF8&serverTimezone=PRC&useSSL=false
username=root
password=******
```

之后创建实体类对象：

- Users.java

```java
import java.util.Date;

public class Users {
    private Integer id;         //用户编号
    private String username;    //登录帐号
    private String userpass;    //登录密码
    private String nickname;    //用户昵称
    private Integer age;        //用户年龄
    private String gender;      //用户性别
    private String phone;       //联系方式
    private String email;       //用户邮箱
    private Date createTime;    //创建时间
    private Date updateTime;    //账号最后修改时间
    private Date lastLogin;     //账号最后登录时间
    private Integer userStatus; //用户状态 0 正常 1 锁定 2 删除
    private String remark;      //用户备注信息

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getUserpass() {
        return userpass;
    }

    public void setUserpass(String userpass) {
        this.userpass = userpass;
    }

    public String getNickname() {
        return nickname;
    }

    public void setNickname(String nickname) {
        this.nickname = nickname;
    }

    public Integer getAge() {
        return age;
    }

    public void setAge(Integer age) {
        this.age = age;
    }

    public String getGender() {
        return gender;
    }

    public void setGender(String gender) {
        this.gender = gender;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public Date getCreateTime() {
        return createTime;
    }

    public void setCreateTime(Date createTime) {
        this.createTime = createTime;
    }

    public Date getUpdateTime() {
        return updateTime;
    }

    public void setUpdateTime(Date updateTime) {
        this.updateTime = updateTime;
    }

    public Date getLastLogin() {
        return lastLogin;
    }

    public void setLastLogin(Date lastLogin) {
        this.lastLogin = lastLogin;
    }

    public Integer getUserStatus() {
        return userStatus;
    }

    public void setUserStatus(Integer userStatus) {
        this.userStatus = userStatus;
    }

    public String getRemark() {
        return remark;
    }

    public void setRemark(String remark) {
        this.remark = remark;
    }
}
```

进行SqlSessionFactory的连接：

- SqlSessionFactoryUtils.java

```java
package com.demo.utils;

import org.apache.ibatis.io.Resources;
import org.apache.ibatis.session.SqlSession;
import org.apache.ibatis.session.SqlSessionFactory;
import org.apache.ibatis.session.SqlSessionFactoryBuilder;

import java.io.IOException;
import java.io.InputStream;

public class SqlSessionFactoryUtils {
    private static String RESOURCE = "mybatis-config.xml";
    private static SqlSessionFactory sqlSessionFactory;
    private static ThreadLocal<SqlSession> threadLocal = new ThreadLocal<SqlSession>();

    /**
     * 创建一个初始化SqlSessionFactory的方法
     * */
    public static void initSqlSessionFactory(){
        try {
            InputStream is = Resources.getResourceAsStream(RESOURCE);
            sqlSessionFactory = new SqlSessionFactoryBuilder().build(is);

        }catch (IOException e){
            e.printStackTrace();
        }
    }

    /**
     * 获取工厂对象的方法
     * */
    public SqlSessionFactory getSqlSessionFactory(){
        return sqlSessionFactory;
    }

    /**
     * 关闭SqlSession的方法
     * */
    public static void close(){
        SqlSession session = threadLocal.get();
        if (session != null){
            session.close();
            threadLocal.set(null);
        }
    }
}
```

 初始化SqlSessionFactory工厂对象：

- 添加依赖

```HTML
<dependency>
            <groupId>javax.servlet</groupId>
            <artifactId>javax.servlet-api</artifactId>
            <version>3.1.0</version>
</dependency>
```

- SqlSessionFactoryUtils.java

```java
package com.demo.utils;

import org.apache.ibatis.io.Resources;
import org.apache.ibatis.session.SqlSession;
import org.apache.ibatis.session.SqlSessionFactory;
import org.apache.ibatis.session.SqlSessionFactoryBuilder;

import java.io.IOException;
import java.io.InputStream;

public class SqlSessionFactoryUtils {
    private static String RESOURCE = "mybatis-config.xml";
    private static SqlSessionFactory sqlSessionFactory;
    private static ThreadLocal<SqlSession> threadLocal = new ThreadLocal<SqlSession>();

    /**
     * 创建一个初始化SqlSessionFactory的方法
     * */
    public static void initSqlSessionFactory(){
        try {
            InputStream is = Resources.getResourceAsStream(RESOURCE);
            sqlSessionFactory = new SqlSessionFactoryBuilder().build(is);

        }catch (IOException e){
            e.printStackTrace();
        }
    }

    /**
     * 获取工厂对象的方法
     * */
    public SqlSessionFactory getSqlSessionFactory(){
        return sqlSessionFactory;
    }

    /**
     * 关闭SqlSession的方法
     * */
    public static void close(){
        SqlSession session = threadLocal.get();
        if (session != null){
            session.close();
            threadLocal.set(null);
        }
    }
}

```

- InitSqlSessionListener.java

```java
package com.demo.listener;

import com.demo.utils.SqlSessionFactoryUtils;

import javax.servlet.ServletContextListener;
import javax.servlet.ServletContextEvent;
import javax.servlet.annotation.WebListener;

@WebListener
public class InitSqlSessionListener implements ServletContextListener {
    public void contextInitialized(ServletContextEvent servletContextEvent){
        System.out.println("容器加载中...");
        // 初始化我们的SqlSessionFactory对象
        SqlSessionFactoryUtils.initSqlSessionFactory();
    }

    public void contextDestroyed(ServletContextEvent servletContextEvent){
        System.out.println("容器销毁中...");
        // 关闭Sqlsession对象
        SqlSessionFactoryUtils.close();
    }

}
```

## 5、基础操作——查询数据

- 映射配置：sql片段

- 映射配置：select配置

	- 特殊配置：字段和属性不一致时resultMap配置

配置映射：

- mybatis-config.xml

```HTML
<mappers>
			 <!-- mapper主要配置引入某一个具体的映射文件，resource进行路径方式的引入-->
			 <mapper resource="mapper/usersMapper.xml"></mapper>
	 </mappers>
```

- usersMapper.xml

```HTML
<mapper namespace="com.demo.entity.Users">
    <select id="findAll" resultType="com.demo.entity.Users">
        select * from mydb.users
    </select>
</mapper>
```

用户查询DAO编写：

- UsersDao.java

```java
package com.demo.dao;
import com.demo.entity.Users;
import com.demo.utils.SqlSessionFactoryUtils;
import org.apache.ibatis.session.SqlSession;
import org.apache.ibatis.session.SqlSessionFactory;
import java.util.List;

public class UsersDao {
    private SqlSession sqlSession = SqlSessionFactoryUtils.getSqlSessionFactory().openSession();
    private List<Users> list;

    public List<Users> findAll(){
        try {
            list = sqlSession.selectList("findAll");
        }catch (Exception e){
            e.printStackTrace();
        }finally {
            sqlSession.close();
        }
        return list;
    }
}
```

servlet层，用于调用DAO的数据：

- UsersFindServlet.java

```java
package com.demo.servlet;

import com.demo.dao.UsersDao;
import com.demo.entity.Users;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.List;
@WebServlet("/index")
public class UsersFindServlet extends HttpServlet {

    private UsersDao usersDAO = new UsersDao();

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        this.doPost(req, resp);
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        List<Users> list = usersDAO.findAll();
        req.setAttribute("usersList",list);
        req.getRequestDispatcher("index.jsp").forward(req,resp);
    }
}
```

新建一个JSP页面,将请求转发到下一个页面：

- home.jsp

```HTML
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<html>
<head>
    <title>Title</title>
</head>
<body>
<%
    response.sendRedirect("/index");
%>
</body>
</html>
```

修改web.xml文件，令项目首先访问home.jsp页面：

- web.xml

```html
<?xml version="1.0" encoding="utf-8" ?>
<web-app xmlns="http://xmlns.jcp.org/xml/ns/javaee"
         xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance"
         xsi:schemaLocation="http://xmlns.jcp.org/xml/ns/javaee
                      http://xmlns.jcp.org/xml/ns/javaee/web-app_3_1.xsd"
         id="WebApp_ID" version="3.1">
    <display-name>mybatispro</display-name>
    <welcome-file-list>
        <welcome-file>index.html</welcome-file>
        <welcome-file>index.htm</welcome-file>
        <welcome-file>home.jsp</welcome-file>
        <welcome-file>default.html</welcome-file>
        <welcome-file>default.htm</welcome-file>
        <welcome-file>default.jsp</welcome-file>
    </welcome-file-list>
</web-app>
```

引入JSTL依赖：

```HTML
<dependency>
            <groupId>javax.servlet</groupId>
            <artifactId>jstl</artifactId>
            <version>1.2</version>
</dependency>
```

在index.jap页面遍历输出数据：

- index.jsp

```html
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<html>
<head>
    <title>慕课网用户管理中心</title>
    <link rel="stylesheet" href="lib/bootstrap-3.3.7-dist/css/bootstrap.min.css">
    <script src="lib/jquery-3.3.1/jquery-3.3.1.min.js"></script>
    <script src="lib/bootstrap-3.3.7-dist/js/bootstrap.min.js"></script>

</head>
<body>
<div class="container">
    <div class="row">
        <div class="page-header">
            <h1>慕课网后台管理系统 <small>用户数据管理中心</small></h1>
        </div>
    </div>

    <div class="row">
        <div class="jumbotron">
            <div class="container">
                <h1>MyBatis基础入门课程！</h1>
                <p>通过一个项目来完成基础部分的学习</p>
                <p><a class="btn btn-primary btn-lg" href="#" role="button">查看更多，请上慕课网</a></p>
            </div>
        </div>
    </div>

    <div class="row">
        <table class="table table-hover table-striped">
            <tr>
                <th>用户编号</th>
                <th>登录帐号</th>
                <th>用户昵称</th>
                <th>邮箱</th>
                <th>联系方式</th>
                <th>账号创建时间</th>
                <th>用户状态</th>
                <th>操作</th>
            </tr>

            <c:forEach var="user" items="${usersList}">
            <tr>
                <td>${user.id}</td>
                <td>${user.username}</td>
                <td>${user.nickname}</td>
                <td>${user.email}</td>
                <td>${user.phone}</td>
                <td>${user.createTime}</td>
                <c:if test="${user.userStatus == 0}">
                    <td>正常</td>
                </c:if>
                <c:if test="${user.userStatus == 1}">
                    <td>锁定</td>
                </c:if>
                <c:if test="${user.userStatus == 2}">
                    <td>删除</td>
                </c:if>

                <td>
                    <a href="">查看</a>
                    <a href="">修改</a>
                    <a href="">删除</a>
                </td>
            </tr>
            </c:forEach>
        </table>
    </div>
</div>
</body>
</html>
```

启动TomCat之后，页面成功显示数据库数据，但是在TomCat端可以看到如下错误：

> org.apache.ibatis.exceptions.PersistenceException:
> ###Error querying database.  Cause: org.apache.ibatis.executor.ExecutorException: Executor was closed.
> ###Cause: org.apache.ibatis.executor.ExecutorException: Executor was closed.

原因可能是：

> Executor was closed.则很有可能是ibatis的session被关闭了，你这边如果使用全局变量的service进行操作，由于session每次得到dao接口都必须先提交然后关闭，共享service对象导致下一个不可用.

通过一个方法将sqlSession封装起来：

>将UsersDao.java更改如下:

```java
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
            list = getSqlSession().selectList("findAll");
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
            user = getSqlSession().selectOne("findById",id);
        }catch (Exception e){
            e.printStackTrace();
        }finally {
            sqlSession.close();
        }
        return user;
    }
}

```



之后，如果要查看用户详细信息，应该怎么处理呢？

- UserMapper.xml中添加：

```HTML
<select id="findById" resultType="com.demo.entity.Users">
        SELECT  * from mydb.users where id = #{id}
</select>
```

- 完善UsersDao.java的内容：

```java
private Users user;
// 根据id查询单个用户
public Users findById(Integer id){
        try {
            user = sqlSession.selectOne("findById",id);
        }catch (Exception e){
            e.printStackTrace();
        }finally {
            sqlSession.close();
        }
        return user;
}
```

- 增加一个查询用户的Servlet，UsersFindByIdServlet.java：

```java
package com.demo.servlet;

import com.demo.dao.UsersDao;
import com.demo.entity.Users;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
@WebServlet("/detail")
public class UsersFindByIdServlet extends HttpServlet {
    private UsersDao usersDao = new UsersDao();

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        this.doPost(req, resp);
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {

        String id = req.getParameter("id");

        Users user = usersDao.findById(Integer.parseInt(id));

        req.setAttribute("user",user);
        req.getRequestDispatcher("detail.jsp").forward(req,resp);
    }
}

```

- 在index.jsp页面中进行详情信息的跳转配置,访问某个用户的来源：

```HTML
<td>
    <a href="${pageContext.request.contextPath}/detail?id=${user.id}">查看</a>
    <a href="">修改</a>
    <a href="">删除</a>
</td>
```

- 新创建一个目标页面，detail.jsp：

```HTML
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<html>
<head>
    <title>慕课网用户管理中心</title>
    <link rel="stylesheet" href="lib/bootstrap-3.3.7-dist/css/bootstrap.min.css">
    <script src="lib/jquery-3.3.1/jquery-3.3.1.min.js"></script>
    <script src="lib/bootstrap-3.3.7-dist/js/bootstrap.min.js"></script>

</head>
<body>
<div class="container">
    <div class="row">
        <div class="page-header">
            <h1>慕课网后台管理系统 <small>用户数据管理中心</small></h1>
        </div>
    </div>

    <c:set var="user" value="${user}"></c:set>


    <div class="row">
        <div class="jumbotron">
            <div class="container">
                <h1>MyBatis基础入门课程！</h1>
                <p>通过一个项目来完成基础部分的学习</p>
                <p><a class="btn btn-primary btn-lg" href="#" role="button">查看更多，请上慕课网</a></p>
            </div>
        </div>
    </div>

    <div class="row">
        <div class="col-md-8 col-md-offset-2">
            <form class="form-horizontal">
                <div class="form-group">
                    <label class="col-sm-2 control-label">用户帐号</label>
                    <div class="col-sm-10">
                        <p class="form-control-static">${user.username}</p>
                    </div>
                </div>

                <div class="form-group">
                    <label class="col-sm-2 control-label">登录密码</label>
                    <div class="col-sm-10">
                        <p class="form-control-static">*******</p>
                    </div>
                </div>

                <div class="form-group">
                    <label for="nickname" class="col-sm-2 control-label">昵称</label>
                    <div class="col-sm-10">
                        <input type="text" class="form-control" id="nickname" value="${user.nickname}" name="nickname" placeholder="请输入昵称">
                    </div>
                </div>

                <div class="form-group">
                    <label for="age" class="col-sm-2 control-label">年龄</label>
                    <div class="col-sm-10">
                        <input type="text" class="form-control" id="age" value="${user.age}" name="age" placeholder="请输入年龄">
                    </div>
                </div>

                <div class="form-group">
                    <label for="gender" class="col-sm-2 control-label">性别</label>
                    <div class="col-sm-10">
                        <input type="text" class="form-control" id="gender" value="${user.gender}" name="gender" placeholder="请输入性别">
                    </div>
                </div>

                <div class="form-group">
                    <label for="phone" class="col-sm-2 control-label">联系方式</label>
                    <div class="col-sm-10">
                        <input type="text" class="form-control" id="phone" name="phone" value="${user.phone}" placeholder="请输入联系方式">
                    </div>
                </div>

                <div class="form-group">
                    <label for="email" class="col-sm-2 control-label">邮箱</label>
                    <div class="col-sm-10">
                        <input type="text" class="form-control" id="email" name="email"  value="${user.email}" placeholder="请输入邮箱">
                    </div>
                </div>

                <div class="form-group">
                    <label class="col-sm-2 control-label">账号创建时间</label>
                    <div class="col-sm-10">
                        <p class="form-control-static">${user.createTime}</p>
                    </div>
                </div>

                <div class="form-group">
                    <label class="col-sm-2 control-label">最后修改时间</label>
                    <div class="col-sm-10">
                        <p class="form-control-static">${user.updateTime}</p>
                    </div>
                </div>

                <div class="form-group">
                    <label class="col-sm-2 control-label">最后登录时间</label>
                    <div class="col-sm-10">
                        <p class="form-control-static">${user.lastLogin}</p>
                    </div>
                </div>

                <div class="form-group">
                    <label class="col-sm-2 control-label">用户状态</label>
                    <div class="col-sm-10">
                        <c:if test="${user.userStatus == 0}">
                            <p class="form-control-static">正常</p>
                        </c:if>
                        <c:if test="${user.userStatus == 1}">
                            <p class="form-control-static">锁定</p>
                        </c:if>
                        <c:if test="${user.userStatus == 2}">
                            <p class="form-control-static">删除</p>
                        </c:if>
                    </div>
                </div>

                <div class="form-group">
                    <label for="remark" class="col-sm-2 control-label">备注</label>
                    <div class="col-sm-10">
                        <input type="text" class="form-control" id="remark" name="remark"  value="${user.remark}" placeholder="请输入备注">
                    </div>
                </div>

            </form>
        </div>
    </div>
</div>

</body>
</html>

```

- 可以对时间显示格式进行修改

> 引入:

> <%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>

>对显示时间的地方使用:

> <fmt:formatDate value="${user.createTime}" pattern="yyyy-MM-dd"></fmt:formatDate>即可

## 6、动态SQL语句的配置和使用

> 如果在查询条件多的时候，我们就需要配置多个查询语句，这显然不是我们使用MyBatis想要见到的，MyBatis支持动态SQL语句的使用，因而，我们可以对usersMapper.xml进行优化，较少代码量。

- usersMapper.xml修改为：

```HTML
<?xml version="1.0" encoding="UTF-8" ?>
<!DOCTYPE mapper
        PUBLIC "-//mybatis.org//DTD Mapper 3.0//EN"
        "http://mybatis.org/dtd/mybatis-3-mapper.dtd">
<mapper namespace="com.demo.entity.Users">
    <select id="findAll" resultType="com.demo.entity.Users">
        select * from mydb.users
        <!-- 增加的动态SQL子句 -->
        <if test="id != null">
            where id = #{id}
        </if>
    </select>
</mapper>
```

- 同样的，对UsersDao.java进行改造：

```java
// 根据id查询单个用户
    public Users findById(Integer id){
        try {
            user = getSqlSession().selectOne("findAll",id); //这里传入了带有变量的数据
        }catch (Exception e){
            e.printStackTrace();
        }finally {
            sqlSession.close();
        }
        return user;
    }
```

- 这个时候如果启动项目，查看详细信息的时候，会出现如下错误：

> ###Error querying database.  Cause: org.apache.ibatis.reflection.ReflectionException: There is no getter for property named 'id' in 'class java.lang.Integer'
Cause: org.apache.ibatis.reflection.ReflectionException: There is no getter for property named 'id' in 'class java.lang.Integer'

这是因为在我们的映射关系（usersMapper.xml）中，需要一个id，但是这个id是我们当前命名空间Users给它的提供的一个id，也就是说，在usersMapper.xml修改里一旦写了id，是从我们传递进去的对象里面去提取id这样一个属性的，我们需要改造Users.java，给他增加构造方法。

```java
public Users() {
	 }

public Users(Integer id) {
	this.id = id;
}
```

之后修改UsersDao.java，将id包装到Users对象里交给MyBatis进行处理，MyBatis在usersMapper.xml里获取id数据的时候就从users对象里调用getid()来进行获取。

## 7、查询操作之resultMap配置

如果实体类的属性和表中的字段属性不一致的情况下，应该怎么去操作？？

我们在前面的基础上，在Users.java中将登录帐号从username改为name，并提供name的set和get方法，并将页面上用于展示的username全部修改为name。

启动项目后，会发现我们的其他信息是正常的，只有用户帐号（登录帐号）name是不显示的（属性和字段信息不一致）。常规下，我们的usersMapper.xml中的resultType、Users和我们的数据库中的表是按照字段名称一一对应的。如果出现不一致的情况，MyBatis就不会对其进行对应关系的配置。就需要我们手工进行配置。

- usersMapper.xml

```HTML
<?xml version="1.0" encoding="UTF-8" ?>
<!DOCTYPE mapper
        PUBLIC "-//mybatis.org//DTD Mapper 3.0//EN"
        "http://mybatis.org/dtd/mybatis-3-mapper.dtd">
<!--
mapper 用于定义一个映射配置文件的根节点
namespace 命令空间，主要进行session级别的缓存管理
 通常情况，命名空间的值就是当前操作实体类的全名称（全路径）-->
<mapper namespace="com.demo.entity.Users">
    <!-- <select id="findUsers" resultType="com.demo.entity.Users"> -->
    <select id="findUsers" resultMap="forUsers">
        select * from mydb.users

        <!-- 增加的动态SQL子句 -->
        <if test="id != null">
            where id = #{id}
        </if>
    </select>
    <!-- 自定义映射关系集合：主要包含一些自定义操作的配置，如不一致的属性和字段名称 -->
    <resultMap id="forUsers" type="com.demo.entity.Users">
        <!-- result配置，主要配置普通属性，column表示配置的是数据库字段的名称，property配置的是实体类的属性名称 -->
        <result column="username" property="name"></result>
    </resultMap>
</mapper>
```

## 8、log4j在MyBatis中的使用

- 在pom文件中增加依赖

```HTML
<dependency>
			<groupId>log4j</groupId>
			<artifactId>log4j</artifactId>
			<version>1.2.17</version>
</dependency>
```

- 在resource目录下增加log4j.properties文件

```HTML
log4j.rootLogger=DEBUG, A1
log4j.appender.A1=org.apache.log4j.ConsoleAppender
log4j.appender.A1.layout=org.apache.log4j.PatternLayout
log4j.appender.A1.layout.ConversionPattern=%-4r %-5p [%t] %37c %3x - %m%n
```

- 配置好之后，就能够看到输出信息(如下所示)，便于调试

```
7935 DEBUG [http-nio-8080-exec-6] org.apache.ibatis.transaction.jdbc.JdbcTransaction     - Opening JDBC Connection
7935 DEBUG [http-nio-8080-exec-6] org.apache.ibatis.datasource.pooled.PooledDataSource     - Checked out connection 1083949341 from pool.
7936 DEBUG [http-nio-8080-exec-6] org.apache.ibatis.transaction.jdbc.JdbcTransaction     - Setting autocommit to false on JDBC Connection [com.mysql.cj.jdbc.ConnectionImpl@409bc11d]
7937 DEBUG [http-nio-8080-exec-6]       com.demo.entity.Users.findUsers     - ==>  Preparing: select * from mydb.users
7937 DEBUG [http-nio-8080-exec-6]       com.demo.entity.Users.findUsers     - ==> Parameters:
7941 DEBUG [http-nio-8080-exec-6]       com.demo.entity.Users.findUsers     - <==      Total: 3
7942 DEBUG [http-nio-8080-exec-6] org.apache.ibatis.transaction.jdbc.JdbcTransaction     - Resetting autocommit to true on JDBC Connection [com.mysql.cj.jdbc.ConnectionImpl@409bc11d]
7942 DEBUG [http-nio-8080-exec-6] org.apache.ibatis.transaction.jdbc.JdbcTransaction     - Closing JDBC Connection [com.mysql.cj.jdbc.ConnectionImpl@409bc11d]
7942 DEBUG [http-nio-8080-exec-6] org.apache.ibatis.datasource.pooled.PooledDataSource     - Returned connection 1083949341 to pool.
```

- 要在servlet中使用应该怎么使用呢？以对UsersFindByIdServlet.java为例：

```java
package com.demo.servlet;

import com.demo.dao.UsersDao;
import com.demo.entity.Users;
import org.apache.log4j.Logger;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@WebServlet("/detail")
public class UsersFindByIdServlet extends HttpServlet {

    /**
     * 创建对应的日志记录对象
     * 通过不同的级别进行日志的记录【DEBUG/WARN/INFO/LOG】
     * */
    private Logger log = Logger.getLogger(UsersFindByIdServlet.class);
    private UsersDao usersDao = new UsersDao();

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        this.doPost(req, resp);
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {

        String id = req.getParameter("id");

        log.info("获取到查询参数id-->>"+id);

        Users user = usersDao.findById(Integer.parseInt(id));

        log.info("查询完成，查询得到的数据："+user);

        req.setAttribute("user",user);
        req.getRequestDispatcher("detail.jsp").forward(req,resp);
    }
}
```
- 运行项目，进行查询，打印输出信息为：

```
20430 INFO  [http-nio-8080-exec-10] com.demo.servlet.UsersFindByIdServlet     - 获取到查询参数id-->>1
20441 DEBUG [http-nio-8080-exec-10] org.apache.ibatis.transaction.jdbc.JdbcTransaction     - Opening JDBC Connection
20441 DEBUG [http-nio-8080-exec-10] org.apache.ibatis.datasource.pooled.PooledDataSource     - Checked out connection 1608755042 from pool.
20441 DEBUG [http-nio-8080-exec-10] org.apache.ibatis.transaction.jdbc.JdbcTransaction     - Setting autocommit to false on JDBC Connection [com.mysql.cj.jdbc.ConnectionImpl@5fe3a762]
20442 DEBUG [http-nio-8080-exec-10]       com.demo.entity.Users.findUsers     - ==>  Preparing: select * from mydb.users where id = ?
20443 DEBUG [http-nio-8080-exec-10]       com.demo.entity.Users.findUsers     - ==> Parameters: 1(Integer)
20450 DEBUG [http-nio-8080-exec-10]       com.demo.entity.Users.findUsers     - <==      Total: 1
20450 DEBUG [http-nio-8080-exec-10] org.apache.ibatis.transaction.jdbc.JdbcTransaction     - Resetting autocommit to true on JDBC Connection [com.mysql.cj.jdbc.ConnectionImpl@5fe3a762]
20451 DEBUG [http-nio-8080-exec-10] org.apache.ibatis.transaction.jdbc.JdbcTransaction     - Closing JDBC Connection [com.mysql.cj.jdbc.ConnectionImpl@5fe3a762]
20451 DEBUG [http-nio-8080-exec-10] org.apache.ibatis.datasource.pooled.PooledDataSource     - Returned connection 1608755042 to pool.
20452 INFO  [http-nio-8080-exec-10] com.demo.servlet.UsersFindByIdServlet     - 查询完成，查询得到的数据：Users{id=1, name='xiaoming', userpass='jlujiang', nickname='小明', age=18, gender='男', phone='13565984875', email='4836165@qq.com', createTime=Thu Apr 11 00:00:00 CST 2019, updateTime=null, lastLogin=null, userStatus=0, remark='null'}
```

## 9、基础操作——增加数据（insert增加数据操作及sql片段配置）

- 映射配置：sql片段

- 映射配置：insert配置

- usersMapper.xml进行insert配置：

```HTML
<insert id="addUser" useGeneratedKeys="true" keyProperty="id">
        insert into mydb.users(username, userpass, nickname, age, gender, phone, email, createTime, updateTime, lastLogin, userStatus, remark)
        values (#{name},#{userpass},#{nickname},#{age},#{gender},#{email},#{phone},#{createTime},#{updateTime},#{lastLogin},#{userStatus},#{remark})
</insert>
```

- 在Users实体类中先创建构造方法：

```java
public Users(String name, String userpass, String nickname, Integer age, String gender, String phone, String email, Date createTime, Date updateTime, Date lastLogin, Integer userStatus) {
        this.name = name;
        this.userpass = userpass;
        this.nickname = nickname;
        this.age = age;
        this.gender = gender;
        this.phone = phone;
        this.email = email;
        this.createTime = createTime;
        this.updateTime = updateTime;
        this.lastLogin = lastLogin;
        this.userStatus = userStatus;
    }
```

- servlet中调用dao进行数据库的操作，新建UsersAddServlet.java

```java
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
```

- 新建添加用户的页面addusers.jsp

```HTML
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<html>
<head>
    <title>慕课网用户管理中心</title>
    <link rel="stylesheet" href="lib/bootstrap-3.3.7-dist/css/bootstrap.min.css">
    <script src="lib/jquery-3.3.1/jquery-3.3.1.min.js"></script>
    <script src="lib/bootstrap-3.3.7-dist/js/bootstrap.min.js"></script>

</head>
<body>
<div class="container">
    <div class="row">
        <div class="page-header">
            <h1>慕课网后台管理系统 <small>用户数据管理中心</small></h1>
        </div>
    </div>

    <div class="row">
        <div class="jumbotron">
            <div class="container">
                <h1>MyBatis基础入门课程！</h1>
                <p>通过一个项目来完成基础部分的学习</p>
                <p><a class="btn btn-primary btn-lg" href="#" role="button">查看更多，请上慕课网</a></p>
            </div>
        </div>
    </div>

    <div class="row">
        <div class="col-md-8 col-md-offset-2">
            <form class="form-horizontal" action="${pageContext.request.contextPath}/addusers">
                <div class="form-group">
                    <label for="username" class="col-sm-2 control-label">用户帐号</label>
                    <div class="col-sm-10">
                        <input type="text" class="form-control" id="username" name="username" placeholder="请输入用户帐号">
                    </div>
                </div>

                <div class="form-group">
                    <label for="userpass" class="col-sm-2 control-label">登录密码</label>
                    <div class="col-sm-10">
                        <input type="text" class="form-control" id="userpass" name="userpass" placeholder="请输入登录密码">
                    </div>
                </div>

                <div class="form-group">
                    <label for="nickname" class="col-sm-2 control-label">昵称</label>
                    <div class="col-sm-10">
                        <input type="text" class="form-control" id="nickname" name="nickname" placeholder="请输入昵称">
                    </div>
                </div>

                <div class="form-group">
                    <label for="age" class="col-sm-2 control-label">年龄</label>
                    <div class="col-sm-10">
                        <input type="text" class="form-control" id="age" name="age" placeholder="请输入年龄">
                    </div>
                </div>

                <div class="form-group">
                    <label for="gender" class="col-sm-2 control-label">性别</label>
                    <div class="col-sm-10">
                        <input type="text" class="form-control" id="gender" name="gender" placeholder="请输入性别">
                    </div>
                </div>

                <div class="form-group">
                    <label for="phone" class="col-sm-2 control-label">联系方式</label>
                    <div class="col-sm-10">
                        <input type="text" class="form-control" id="phone" name="phone" placeholder="请输入联系方式">
                    </div>
                </div>

                <div class="form-group">
                    <label for="email" class="col-sm-2 control-label">邮箱</label>
                    <div class="col-sm-10">
                        <input type="text" class="form-control" id="email" name="email" placeholder="请输入邮箱">
                    </div>
                </div>

								<div class="form-group">
                    <input type="submit" value="点击新增用户" class="btn btn-primary">
                </div>

            </form>
        </div>
    </div>
</div>
</body>
</html>
```

- 接下来，我们的增加用户的页面需要在首页增加按钮，跳转到这个页面上去，在index.jsp中

```HTML
<p><a class="btn btn-primary btn-lg" href="${pageContext.request.contextPath}/addusers.jsp" role="button">新增用户</a></p>
```

## 10、基础操作——更新操作(update配置及set动态语句操作)

- 映射配置：update配置

- 动态SQL配置：set配置

- 在用户详情页进行信息的修改，detail.jsp中添加：

```HTML
对from表单添加：
	action="${pageContext.request.contextPath}/updateusers"
添加按钮：
	<div class="form-group">
    	<input type="submit" value="提交数据更新" class="btn btn-primary">
	</div>
```

- 映射文件usersMapper.xml中添加映射

```HTML
    <update id="updateUser">
        update mydb.users set username = #{name},
                              userpass = #{userpass},
                              nickname = #{nickname},
                              age = #{age},
                              gender = #{gender},
                              email = #{email},
                              phone = #{phone},
                              createTime = #{createTime},
                              updateTime = #{updateTime},
                              lastLogin = #{lastLogin},
                              userStatus = #{userStatus},
                              remark = #{remark}
        where id = #{id}
    </update>
```

- 在DAO中增加调用配置，UsersDao.java：

```java
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
```

- 增加一个Servlet专门用于用户修改，UsersUpdateServlet.java

> 事先需要获取更新的用户的id用来进行更改，就需要在修改页面增加一个隐藏域，用来获取用户id。

> input type="hidden" name="id" value="${user.id}"

```java
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

@WebServlet("/updateusers")
public class UsersUpdateServlet extends HttpServlet {

    private UsersDao usersDao = new UsersDao();

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        this.doPost(req, resp);
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        // 获取用户要更新的数据
        String id = req.getParameter("id");
        String nickname = req.getParameter("nickname");
        String age = req.getParameter("age");
        String gender = req.getParameter("gender");
        String email = req.getParameter("email");
        String phone = req.getParameter("phone");
        String remark = req.getParameter("remark");
        // 创建用户对象
        Users user = new Users(Integer.parseInt(id),nickname,Integer.parseInt(age),gender,phone,email,new Date(),remark);
        // 提交更新
        usersDao.updateUser(user);
        //查看更新后的数据
        resp.sendRedirect("/detail?id="+user.getId());
    }
}
```

> 需要在Users.java中添加更新操作是对应的构造函数：

```java
public Users(Integer id, String nickname, Integer age, String gender, String phone, String email, Date updateTime, String remark) {
        this.id = id;
        this.nickname = nickname;
        this.age = age;
        this.gender = gender;
        this.phone = phone;
        this.email = email;
        this.updateTime = updateTime;
        this.remark = remark;
    }
```

> 这时候启动项目，进行数据修改，但是修改失败，后台提示：

> Error updating database.  Cause: java.sql.SQLIntegrityConstraintViolationException: Column 'username' cannot be null

> 这里提示username不能为null，但是我们并没有更新username，为什么会提示这里出错呢？其实原因是在usersMapper.xml的update操作的时候，设置了username = #{name}，但是我们创建的用户对象又没有包含这个属性，就会设置为null了，所以在update操作的不能使用这种语句，应该使用动态SQL语句。

> 将usersMapper.xml中的update语句修改为：

```HTML
<update id="updateUser">
        update mydb.users
        <set>
            <if test="name != null">username = #{name},</if>
            <if test="userpass != null">userpass = #{userpass},</if>
            <if test="nickname != null">nickname = #{nickname},</if>
            <if test="age != null">age = #{age},</if>
            <if test="gender != null">gender = #{gender},</if>
            <if test="email != null">email = #{email},</if>
            <if test="phone != null">phone = #{phone},</if>
            <if test="createTime != null">createTime = #{createTime},</if>
            <if test="updateTime != null">updateTime = #{updateTime},</if>
            <if test="lastLogin != null">lastLogin = #{lastLogin},</if>
            <if test="userStatus != null">userStatus = #{userStatus},</if>
            <if test="remark != null">remark = #{remark},</if>
        </set>
        where id = #{id}
</update>
```

## 11、基础操作——删除数据(delete删除数据及项目中的账号锁定操作)

- 映射配置：delete配置

- 补充：业务功能中的删除帐号、锁定帐号和彻底删除账号

- 首先修改index.jsp页面中的删除anniu

```HTML
<td>
    <a href="${pageContext.request.contextPath}/detail?id=${user.id}">查看</a>
    <a href="${pageContext.request.contextPath}/deluser?id=${user.id}&type=lock">锁定</a>
    <a href="${pageContext.request.contextPath}/deluser?id=${user.id}&type=del">删除</a>
</td>
```

- 在usersMapper.xml中增加delete配置

```HTML
<delete id="delUser">
		delete from mydb.users where id = #{id}
</delete>
```

- 在Dao中（UsersDao.java）增加配置

```java
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
```

- 补充：解锁和锁定功能，修改index.jsp页面

> 执行删除/锁定操作：update操作
	锁定：
	锁定的用户进行标注解锁
  没锁定的用户进行标注锁定

```HTML
        <td>
        <a href="${pageContext.request.contextPath}/detail?id=${user.id}">查看</a>
        <c:if test="${user.userStatus == 0}">
            <a href="${pageContext.request.contextPath}/deluser?id=${user.id}&type=lock">锁定</a>
        </c:if>
        <c:if test="${user.userStatus == 1}">
            <a href="${pageContext.request.contextPath}/deluser?id=${user.id}&type=uplock">解锁</a>
        </c:if>
            <a href="${pageContext.request.contextPath}/deluser?id=${user.id}&type=del">删除</a>
        </td>
```

- 接下来开发servlet，命名为UsersDelServlet.java：

```java
package com.demo.servlet;

import com.demo.dao.UsersDao;
import com.demo.entity.Users;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
@WebServlet("/deluser")
public class UsersDelServlet extends HttpServlet {

    private UsersDao usersDao = new UsersDao();

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        this.doPost(req, resp);
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        //获取参数
        String id = req.getParameter("id");
        String type = req.getParameter("type");
        //执行删除或锁定
        if ("lock".equals(type)){
            // 执行锁定操作：update操作
            // 锁定的用户进行标注解锁
            // 没锁定的用户进行标注锁定
            Users user = new Users();
            user.setId(Integer.parseInt(id));
            user.setUserStatus(1);

            usersDao.updateUser(user);

        }else if ("del".equals(type)){
            // 执行删除操作：delete操作
            usersDao.delUsers(Integer.parseInt(id));
        }else if ("unlock".equals(type)){
            // 解锁
            Users user = new Users();
            user.setId(Integer.parseInt(id));
            user.setUserStatus(0);

            usersDao.updateUser(user);
        }
        //跳转到首页
        resp.sendRedirect("/index");
    }
}
```
