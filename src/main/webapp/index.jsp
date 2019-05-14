<%--
  Created by IntelliJ IDEA.
  User: JluTiger
  Date: 2019/5/13
  Time: 16:19
  To change this template use File | Settings | File Templates.
--%>
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
                <p><a class="btn btn-primary btn-lg" href="https://www.imooc.com" role="button">查看更多，请上慕课网</a></p>
                <p><a class="btn btn-primary btn-lg" href="${pageContext.request.contextPath}/addusers.jsp" role="button">新增用户</a></p>
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
                <td>${user.name}</td>
                <td>${user.nickname}</td>
                <td>${user.email}</td>
                <td>${user.phone}</td>
                <td>
                    <p class="form-control-static">
                        <fmt:formatDate value="${user.createTime}" pattern="yyyy-MM-dd"></fmt:formatDate>
                    </p>
                </td>
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
                <a href="${pageContext.request.contextPath}/detail?id=${user.id}">查看</a>
                <c:if test="${user.userStatus == 0}">
                    <a href="${pageContext.request.contextPath}/deluser?id=${user.id}&type=lock">锁定</a>
                </c:if>
                <c:if test="${user.userStatus == 1}">
                    <a href="${pageContext.request.contextPath}/deluser?id=${user.id}&type=unlock">解锁</a>
                </c:if>
                <a href="${pageContext.request.contextPath}/deluser?id=${user.id}&type=del">删除</a>
                </td>

            </tr>
            </c:forEach>
        </table>
    </div>
</div>

</body>
</html>
