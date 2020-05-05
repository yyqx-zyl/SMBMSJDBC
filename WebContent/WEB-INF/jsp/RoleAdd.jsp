<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@include file="/WEB-INF/jsp/common/head.jsp"%>
<div class="right">
        <div class="location">
            <strong>你现在所在的位置是:</strong>
            <span>用户管理页面 >> 用户添加页面</span>
        </div>
        <div class="providerAdd">
            <form  method="post" action="${ctx }/user/rolesave.html">
                <div>
                    <label for="userCode">角色编码：</label>
                    <input type="text" name="roleCode" id="roleCode" value=""> 
					
                </div>
                <div>
                    <label for="userName">角色名称：</label>
                    <input type="text" name="roleName" id="roleName" value=""> 
					
                </div>
                <div class="providerAddBtn">
                    <input type="button"  value="保存" >
                </div>
            </form>
        </div>
</div>
</section>
<%@include file="/WEB-INF/jsp/common/foot.jsp" %>
