package com.bmt.HolaMundo.Controllers;

import org.springframework.web.bind.annotation.GetMapping;

public class HomePageController {

	@GetMapping({"", "/"})
	public String home () {
		return "index";
	}

	
}
