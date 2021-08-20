package com.code.removal.sc.controller;

import java.util.List;
import java.util.Map;
import java.util.Set;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.code.removal.model.SourceCode;
import com.code.removal.sc.service.ISourceCodeService;
import com.code.removal.sc.util.SessionDataManager;

@RestController
@RequestMapping("/sc")
public class SourceCodeController {

	@Autowired
	private ISourceCodeService codeService;

	@Autowired
	private SessionDataManager sessionDataManager;

	@GetMapping("/removeImports")
	public boolean removeImports(@RequestBody Map<String, Set<String>> sourceMap, @RequestHeader String sessionId) {
		codeService.removeImports(sourceMap, sessionId);
		return false;
	}

	@PostMapping(value = "/loc", produces = MediaType.APPLICATION_JSON_VALUE)
	public List<SourceCode> getNumberOfLines(@RequestBody Map<String, Set<String>> sourceMap,
			@RequestHeader String sessionId) {

		return codeService.checkNumberOfLines(sourceMap, sessionId);

	}

	@PostMapping("/format")
	public boolean formatSourceCode(@RequestBody Map<String, Set<String>> sourceMap, @RequestHeader String sessionId) {

		codeService.formatCode(sourceMap, sessionId);

		return false;
	}

	@PostMapping("/init")
	public String init(@RequestBody String rootDirectory) {

		return sessionDataManager.init(rootDirectory);
	}

	@GetMapping("/getPackages")
	public Map<String, Set<String>> retriveSourceMap() {
		return null;
	}
}
