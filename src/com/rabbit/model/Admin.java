package com.rabbit.model;

import com.jfinal.plugin.activerecord.Model;
import com.jfinal.plugin.activerecord.Page;
/**
 * Admin model.
mysql> desc bg_admin;
+----------+---------------+------+-----+---------+----------------+
| Field    | Type          | Null | Key | Default | Extra          |
+----------+---------------+------+-----+---------+----------------+
| id       | int(11)       | NO   | PRI | NULL    | auto_increment |
| rid      | int(11)       | YES  |     | NULL    |                |
| username | varchar(256)  | YES  |     | NULL    |                |
| password | varchar(128)  | YES  |     | NULL    |                |
| mobile   | varchar(20)   | YES  |     |         |                |
| remark   | varchar(1024) | YES  |     |         |                |
| stat     | int(2)        | YES  |     | 1       |                |
+----------+---------------+------+-----+---------+----------------+

 */
public class Admin extends Model<Admin> {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1939726258619543345L;
	public static final Admin dao = new Admin();
	
	public Admin findByUserPass(String username,String password){
		return findFirst("select t.*,r.rname from bg_admin t left join bg_role r on r.id = t.rid where (t.username = ? or t.email = ?) and t.password = ?", username, username,password);
	}
	
	public Page<Admin> paginate(int pageNumber, int pageSize, String filter) {
		return paginate(pageNumber, pageSize, "SELECT t.*,r.rname","from bg_admin t left join bg_role r on r.id = t.rid where t.stat=1 "+filter);
	}
}
