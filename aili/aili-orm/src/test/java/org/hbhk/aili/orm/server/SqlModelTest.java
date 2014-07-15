package org.hbhk.aili.orm.server;

import org.hbhk.aili.orm.server.annotation.Column;
import org.hbhk.aili.orm.server.annotation.PrimaryKey;
import org.hbhk.aili.orm.server.annotation.Tabel;

@Tabel("t_test_sql")
public class SqlModelTest {

	@Column("id")
	@PrimaryKey
	private String id;
	@Column("test_sss")
	private String sss;
	@Column("test_qqq")
	private String qqq;

	public String getSss() {
		return sss;
	}

	public void setSss(String sss) {
		this.sss = sss;
	}

	public String getQqq() {
		return qqq;
	}

	public void setQqq(String qqq) {
		this.qqq = qqq;
	}

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

}