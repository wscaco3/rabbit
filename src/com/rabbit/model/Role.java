package com.rabbit.model;

import java.util.List;

import com.jfinal.plugin.activerecord.Model;
import com.jfinal.plugin.activerecord.Page;
/**
 * Role model.
mysql> desc bg_role;
+--------+---------------+------+-----+---------+----------------+
| Field  | Type          | Null | Key | Default | Extra          |
+--------+---------------+------+-----+---------+----------------+
| id     | int(11)       | NO   | PRI | NULL    | auto_increment |
| rname  | varchar(64)   | YES  |     | NULL    |                |
| models | varchar(2048) | YES  |     | NULL    |                |
| remark | varchar(1024) | YES  |     | NULL    |                |
+--------+---------------+------+-----+---------+----------------+

 */
public class Role extends Model<Role> {
	public static final Role dao = new Role();
	

	public Page<Role> paginate(int pageNumber, int pageSize, String filter) {
		return paginate(pageNumber, pageSize, "select *", "from bg_role where 1=1 "+filter);
	}
	
	public List<Role> findAll(){
		return find("select id,rname from bg_role");
	}
}
