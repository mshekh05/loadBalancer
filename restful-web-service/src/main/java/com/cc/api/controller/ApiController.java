package com.cc.api.controller;


import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;
@RestController
public class ApiController {
//	@Autowired
//	private StudentService studentService;

	@GetMapping("/{url}")
	public String retrieveCoursesForStudent(@PathVariable String url) {
		return url;
	}
	
	


}
