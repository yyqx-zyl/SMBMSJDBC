<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
    <!-- 引入Spring的表单标签库 -->
    <%@ taglib uri="http://www.springframework.org/tags/form" prefix="fm" %>
<!DOCTYPE html>
<html>
<head>
<meta charset="UTF-8">
<title>使用Spring的表单标签实现增加功能</title>
<script type="text/javascript" src="${pageContext.request.contextPath }/static/calendar/WdatePicker.js"></script>
</head>
<body>
	<!-- 使用Spring的表单标签进行页面的编写 
		modelAttribute==控制页面那边的@modelAttribute注解
	-->
	<fm:form method="post" modelAttribute="user">
		<!-- 添加表单元素 -->
		<fm:errors path="userCode"/><br/>
		用户编码：<fm:input path="userCode"/><br/>
		<fm:errors path="userName"/><br/>
		用户名称：<fm:input path="userName"/><br/>
		<fm:errors path="userPassword"/><br/>
		用户密码：<fm:password path="userPassword"/><br/>
		用户性别：<fm:radiobutton path="gender" value="1"/>男
 				  <fm:radiobutton path="gender" value="2"/>女<br/>
 		<fm:errors path="birthday"/><br/>
		出生日期：<fm:input path="birthday" Class="Wdate" id="birthday"
					readonly="readonly" onclick="WdatePicker();"/><br/>
		用户电话：<fm:input path="phone" /><br/>
		用户地址：<fm:input path="address"/><br/>
		用户角色：<fm:select path="userRole">
					<fm:option value="1">系统管理员</fm:option>
					<fm:option value="2">经理</fm:option>
					<fm:option value="3" selected="selected">普通员工</fm:option>
				  </fm:select><br/>
		<input type="submit" value="增加"/>
	</fm:form>
</body>
</html>
