package com.shopme.site;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class MainController {
	@GetMapping("/")
	private String viewHomePage() {
		// TODO Auto-generated method stub
		return "index";
	}
}
