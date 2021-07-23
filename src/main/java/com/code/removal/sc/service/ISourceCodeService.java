package com.code.removal.sc.service;

import java.util.Map;
import java.util.Set;

public interface ISourceCodeService {
	boolean removeImports(final Map<String, Set<String>> sourceMap);
}
