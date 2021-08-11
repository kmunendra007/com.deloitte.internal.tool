package com.code.removal.model;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class ClassLineCount {
	private String className;
	private Integer loc;

}
