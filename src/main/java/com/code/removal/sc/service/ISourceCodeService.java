package com.code.removal.sc.service;

import java.util.List;
import java.util.Map;
import java.util.Set;

import com.code.removal.model.SourceCode;

public interface ISourceCodeService {
	boolean removeImports(final Map<String, Set<String>> sourceMap);

	List<SourceCode> checkNumberOfLines(final Map<String, Set<String>> sourceMap);

}