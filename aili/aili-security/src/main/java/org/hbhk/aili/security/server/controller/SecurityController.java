package org.hbhk.aili.security.server.controller;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletResponse;

import org.hbhk.aili.core.server.annotation.SecurityFilter;
import org.hbhk.aili.core.server.web.BaseController;
import org.hbhk.aili.core.share.ex.BusinessException;
import org.hbhk.aili.core.share.pojo.ResponseEntity;
import org.hbhk.aili.security.server.service.IUserService;
import org.hbhk.aili.security.share.define.SecurityConstant;
import org.hbhk.aili.security.share.pojo.UserInfo;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

@Controller
@RequestMapping(SecurityConstant.moduleName)
public class SecurityController extends BaseController {

	@Resource
	private IUserService userService;

	@RequestMapping("/login")
	@SecurityFilter(false)
	public String login(HttpServletResponse response, String username,
			String password) {
		// CookieUtil.setCookie("username", username, 10000, response);
		userService.login(username, password);
		return "redirect:/theme/index.htm";
	}

	@RequestMapping("/login")
	@SecurityFilter(false)
	@ResponseBody
	public ResponseEntity regist(UserInfo user) {
		try {
			userService.save(user);
			return returnSuccess();
		} catch (BusinessException e) {
			return returnException(e.getMessage());
		}
	}

	private List<UserInfo> getUserList() {
		List<UserInfo> userList = new ArrayList<UserInfo>();
		for (int i = 0; i < 5; i++) {
			int number = new Random().nextInt(1000);
			UserInfo u = new UserInfo();
			u.setGender("男" + number);
			u.setMail(number + "@hbhk.com");
			u.setName("何波" + number);
			userList.add(u);
		}
		return userList;

	}

	@RequestMapping("/main")
	@SecurityFilter(false)
	public String main(ModelMap model) {
		model.put("userlist", getUserList());
		return "main";
	}

}