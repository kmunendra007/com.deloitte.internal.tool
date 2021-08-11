package com.code.removal.model;

import java.util.List;
import java.util.Map;
import java.util.Set;

import lombok.Data;

@Data
public class SourceCode {

	private Map<String, Set<String>> sourceMap;
	private Integer loc;
	private List<ClassLineCount> classLineCounts;

	
}