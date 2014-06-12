package org.hbhk.theme.server.controller;

import org.hbhk.aili.core.server.annotation.SecurityFilter;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/theme")
public class DemoAction{
	
	@RequestMapping("btsp")
	@SecurityFilter(false)
	public  String  bootstap(){
		return  "bootstrap";
	}
	
	
	
}