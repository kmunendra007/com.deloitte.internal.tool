package com.code.removal.sc.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.code.removal.sc.service.ISourceCodeService;

@RestController
@RequestMapping("/sc")
public class SourceCodeController {
	
	@Autowired
	private ISourceCodeService codeService;
	
	@GetMapping("/removeImports")
	public boolean removeImports() {
		codeService.removeImports(null);
		return false;
	}
}
