package com.code.removal.sc.service;

import java.util.List;
import java.util.Map;
import java.util.Set;

import org.springframework.stereotype.Service;

import com.code.removal.model.SourceCode;
import com.code.removal.sc.util.SourceCodeUtil;

@Service
public class SourceCodeService extends SourceCodeUtil implements ISourceCodeService {

	@Override
	public boolean removeImports(Map<String, Set<String>> sourceMap) {
		// find the imports for a specific file

		setFilePath(
				"C:\\Users\\munekumar\\Java\\workspace\\code-formatter\\src\\main\\java\\com\\code\\formatter\\demo\\ImportsOrganizer.java");
		String[] imports = checkImports();

		// verify each import usage
		Map<String, Boolean> importsMap = searchStringFromFile(imports);

		// remove imports
		// TODO: write a code to remove line from a file

		return false;
	}

	public List<SourceCode> checkNumberOfLines(Map<String, Set<String>> sourceMap) {

		return checkNumberOfLinesOfCode(sourceMap);
	}

}
