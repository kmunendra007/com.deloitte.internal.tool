package com.code.removal.sc.controller;

import java.util.List;
import java.util.Map;
import java.util.Set;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.code.removal.demo.model.SourceCode;
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

	@PostMapping(value = "/loc", produces = MediaType.APPLICATION_JSON_VALUE)
	public List<SourceCode> getNumberOfLines(@RequestBody Map<String, Set<String>> sourceMap) {

		return codeService.checkNumberOfLines(sourceMap);

	}
}
