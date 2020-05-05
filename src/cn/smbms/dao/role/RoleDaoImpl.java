package cn.smbms.dao.role;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.List;

import org.springframework.stereotype.Repository;

import cn.smbms.dao.BaseDao;
import cn.smbms.pojo.Role;

@Repository
public class RoleDaoImpl implements RoleDao{

	@Override
	public List<Role> getRoleList(Connection connection) throws Exception {
		// TODO Auto-generated method stub
		PreparedStatement pstm = null;
		ResultSet rs = null;
		List<Role> roleList = new ArrayList<Role>();
		if(connection != null){
			String sql = "select * from smbms_role";
			Object[] params = {};
			rs = BaseDao.execute(connection, pstm, rs, sql, params);
			while(rs.next()){
				Role _role = new Role();
				_role.setId(rs.getInt("id"));
				_role.setRoleCode(rs.getString("roleCode"));
				_role.setRoleName(rs.getString("roleName"));
				roleList.add(_role);
			}
			BaseDao.closeResource(null, pstm, rs);
		}
		
		return roleList;
	}

	@Override
	public int add(Connection connection, Role role) throws Exception {
		// TODO Auto-generated method stub
				PreparedStatement pstm = null;
				int updateRows = 0;
				if(null != connection){
					String sql = "insert into smbms_role (roleCode,roleName,createdBy,creationDate" +
							"values(?,?,?,?)";
					Object[] params = {role.getRoleCode(),role.getRoleName(),role.getCreatedBy(),role.getCreationDate()};
					updateRows = BaseDao.execute(connection, pstm, sql, params);
					BaseDao.closeResource(null, pstm, null);
				}
				return updateRows;
	}

}
