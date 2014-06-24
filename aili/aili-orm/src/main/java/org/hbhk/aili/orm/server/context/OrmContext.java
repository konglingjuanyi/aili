package org.hbhk.aili.orm.server.context;

import java.io.IOException;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import org.apache.commons.collections.CollectionUtils;
import org.hbhk.aili.orm.server.convertor.OrmConvertor;
import org.hbhk.aili.orm.share.model.Delete;
import org.hbhk.aili.orm.share.model.Insert;
import org.hbhk.aili.orm.share.model.Orm;
import org.hbhk.aili.orm.share.model.Select;
import org.hbhk.aili.orm.share.model.Update;
import org.hbhk.aili.orm.share.util.FileScanUtil;

public class OrmContext {

	public static Map<String, Object> context = new ConcurrentHashMap<String, Object>();

	public void inti() throws IOException {
		FileScanUtil scanUtil = new FileScanUtil();
		OrmConvertor convertor = new OrmConvertor();
		List<String> orms = scanUtil.scanBeansXml("org/hbhk/aili", "orm.xml");
		for (String str : orms) {
			Orm orm = convertor.toMessage(str);
			List<Insert> inserts = orm.getInsert();
			List<Delete> deletes = orm.getDelete();
			List<Update> updates = orm.getUpdate();
			List<Select> selects = orm.getSelect();
			if (CollectionUtils.isNotEmpty(inserts)) {
				for (Insert insert : inserts) {
					keyExit(insert.getId());
					context.put(insert.getId(), insert.getSql());
				}
			}
			if (CollectionUtils.isNotEmpty(deletes)) {
				for (Delete delete : deletes) {
					keyExit(delete.getId());
					context.put(delete.getId(), delete.getSql());
				}
			}
			if (CollectionUtils.isNotEmpty(updates)) {
				for (Update update : updates) {
					keyExit(update.getId());
					context.put(update.getId(), update.getSql());
				}
			}
			if (CollectionUtils.isNotEmpty(selects)) {
				for (Select select : selects) {
					keyExit(select.getId());
					context.put(select.getId(), select.getSql());
				}
			}

		}
	}

	private void keyExit(String key) {
		if (context.containsKey(key)) {
			throw new RuntimeException("id is exits");
		}
	}

}
