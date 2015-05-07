package org.hbhk.aili.gen.server.foss;

import java.util.ArrayList;
import java.util.List;

import org.hbhk.aili.gen.server.foss.service.FossMakeCodeServiceImpl;
import org.hbhk.aili.gen.server.foss.service.FossMakeModelServiceImpl;
import org.hbhk.aili.gen.server.model.ColumnDesc;
import org.hbhk.aili.gen.server.model.MakeModel;
import org.hbhk.aili.gen.server.service.MakeCodeService;
import org.hbhk.aili.gen.server.service.MakeModelService;
import org.hbhk.aili.gen.server.test.CarEntity;

public class FossGenerateMain {



	private static String getAutoMakeCode() {

		return System.getProperty("user.dir") + "/target/template/";
	}

	
	public static void execute(String projectName ,String moduleName,String tabName,List<ColumnDesc> list, Class<?> clazz, String author) throws Exception {
		MakeModelService mmService = new FossMakeModelServiceImpl();
		MakeModel mm = mmService.queryByClass(clazz);
		mm.setAuthName(author);
		mm.setTabName(tabName);
		mm.setProjectName(projectName);
		mm.setModuleName(moduleName);
		MakeCodeService mcs = new  FossMakeCodeServiceImpl("foss-template");

		mcs.makeSqlXml(mm, getAutoMakeCode());
		mcs.makeDao(mm, getAutoMakeCode());
		mcs.makeManager(mm, getAutoMakeCode());
		mcs.makeController(mm, getAutoMakeCode());
		mcs.makeJs(mm,  getAutoMakeCode());
		mcs.makeJsp(mm,  getAutoMakeCode());

		System.out.println(cls.getName() + " "+author);
	}

	public static void main(String[] args) throws Exception {

		List<ColumnDesc> list = new ArrayList<ColumnDesc>();
		ColumnDesc c = new ColumnDesc("name","c_name");
		list.add(c);
		execute("hbhk","aili","t_test",list,CarEntity.class, "何波");

	}
}
