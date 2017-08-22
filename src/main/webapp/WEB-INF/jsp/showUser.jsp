<%@ page language="java" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib uri="http://java.sun.com/jstl/fmt_rt" prefix="fmt"%>
<!DOCTYPE>
<html>
<head>
<meta charset="UTF-8" />
<title>用户管理</title>
</head>
<body>
	<h1>用户信息表</h1>
	<table border="1">
		<tr>
			<th>ID</th>
			<th>用户名</th>
			<th>注册时间</th>
			<th>确定时间</th>
			<th>相关操作</th>
		</tr>
		
		<tbody>
			<c:forEach items="${userList}" var="user">
				<tr align="center">
					<td>${user.id}</td>
					<td>${user.name}</td>
					<td>
						<fmt:formatDate value="${user.createTime}" type="date" pattern="yyyy-MM-dd"/>
					</td>
					<td>
						<fmt:formatDate value="${user.expireTime}" type="date" pattern="yyyy-MM-dd"/>
					</td>
					<td>
						<a href="/demo/user/updateUser.action">
							修改
						</a>
						&nbsp;
						<a href="/demo/user/delUser.action?id=${user.id}">
						            删除
						</a>
					</td>
				</tr>
	     	</c:forEach>
	     </tbody>
	</table>
</body>
</html>