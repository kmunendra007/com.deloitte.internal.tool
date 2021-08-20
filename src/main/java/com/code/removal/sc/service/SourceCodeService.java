package com.code.removal.sc.service;

import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.code.removal.model.SourceCode;
import com.code.removal.sc.util.SourceCodeUtil;
import com.google.googlejavaformat.java.Formatter;
import com.code.removal.sc.util.ISessionDataManager;
import com.code.removal.sc.util.SessionModel;

@Service
public class SourceCodeService extends SourceCodeUtil implements ISourceCodeService {
	
	@Autowired
	private ISessionDataManager sessionDataManager;

	@Override
	public boolean removeImports(Map<String, Set<String>> sourceMap, final String sessionId) {
		// find the imports for a specific file
		
		for (Entry<String, Set<String>> entry : sourceMap.entrySet()) {
			
			for (String className: entry.getValue()) {
				
				
				setFilePath(sessionDataManager.getSourcePath(sessionId)+ entry.getKey().replace(".", "\\")+ className);
				String[] imports = checkImports();
				
				// verify each import usage
				Map<String, Boolean> importsMap = searchStringFromFile(imports);
				
				// remove imports
				for (String importString : importsMap.keySet()) {
					removeLine(importString);
				}
			}
		}
		
		
		return false;
	}

	public List<SourceCode> checkNumberOfLines(Map<String, Set<String>> sourceMap, final String sessionId) {

		return checkNumberOfLinesOfCode(sourceMap);
	}
	
	@Override
	public boolean formatCode(Map<String, Set<String>> sourceMap, final String sessionId){
		
		try {
			Set<Entry<String, Set<String>>> entrySet = sourceMap.entrySet();

			for (Entry<String, Set<String>> entry : entrySet) {

				for (String className : entry.getValue()) {
					
					String filePath = sessionDataManager.getSourcePath(sessionId)+ entry.getKey().replace(".", "\\")+"\\"+className;
					// Read from File
					String content = Files.readString(Paths.get(filePath));

					Formatter formatter = new Formatter();
					String formatSource = formatter.formatSource(content);
					System.out.println(formatSource);

					// Write to File
					Files.write(Paths.get(filePath), formatSource.getBytes());
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
			return false;
		}
		
		return true;
	}
}
