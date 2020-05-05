<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@include file="/WEB-INF/jsp/common/head.jsp"%>
	<div class="right">
	            <div class="location">
	                <strong>你现在所在的位置是:</strong>
	                <span>用户角色管理页面</span>
	            </div>
	            <div class="search">
	           		<a href="${ctx}/user/userNadd.html" >添加用户角色</a>
	            </div>
	</div>
	<table class="providerTable" cellpadding="0" cellspacing="0">
                <tr class="firstTr">
                    <th width="10%">用户角色编码</th>
                    <th width="20%">用户角色名称</th>
                    <th width="30%">操作</th>
                </tr>
                   <c:forEach var="role" items="${roleList }" varStatus="status">
					<tr>
						<td>
						<span>${role.roleCode }</span>
						</td>
						<td>
						<span>${role.roleName }</span>
						</td>
						<td>
						<span><a class="viewUser" href="javascript:;" userid=${role.id } username=${role.roleName }><img src="${ctx }/static/images/read.png" alt="查看" title="查看"/></a></span>
						<span><a class="modifyUser" href="javascript:;" userid=${role.id } username=${role.roleName }><img src="${ctx }/static/images/xiugai.png" alt="修改" title="修改"/></a></span>
						<span><a class="deleteUser" href="javascript:;" userid=${role.id } username=${role.roleName }><img src="${ctx}/static/images/schu.png" alt="删除" title="删除"/></a></span>
						</td>
					</tr>
				</c:forEach>
			</table>
</body>
</html>
<%@include file="/WEB-INF/jsp/common/foot.jsp" %>
<script type="text/javascript" src="${ctx }/static/js/userlist.js"></script>