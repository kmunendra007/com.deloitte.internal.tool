package com.code.removal.sc.util;

import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.HashMap;
import java.util.Map;
import java.util.function.Supplier;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Stream;

import org.springframework.beans.factory.annotation.Autowired;

import com.thoughtworks.qdox.JavaDocBuilder;
import com.thoughtworks.qdox.model.JavaSource;

public abstract class SourceCodeUtil {
	private String filePath;

	@Autowired
	JavaDocBuilder builder;

	public void setFilePath(String filePath) {
		this.filePath = filePath;
	}

	public String[] checkImports() {
		String[] imports = null;

		builder = new JavaDocBuilder();
		try {
			builder.addSource(new FileReader(filePath));

			JavaSource src = builder.getSources()[0];
			imports = src.getImports();

		} catch (FileNotFoundException e) {
			e.printStackTrace();
		}
		return imports;
	}

	public Map<String, Boolean> searchStringFromFile(String[] imports) {
		Map<String, Boolean> importsMap = new HashMap<>();

		Path path = Paths.get(filePath);
		
		//create the supplier
		Supplier<Stream<String>> fileSupplier = () -> {
			try {
				return Files.lines(path);
			} catch (IOException e) {
				e.printStackTrace();
			}
			return null;
		};

		try {
			for (String searchTerm : imports) {
				// split each import by dot and get the last index to determine occurrence of it
				String[] terms = searchTerm.split("\\.");

				long foundLine = fileSupplier.get().filter(line -> isExactMatch(line, terms[terms.length - 1])).count();

				if (foundLine == 1) {
					importsMap.put(searchTerm, false);
				}

			}
		} catch (Exception e) {
			e.printStackTrace();
		}

		return importsMap;
	}
	

	public boolean isExactMatch(String source, String subItem) {
		String pattern = "\\b" + subItem + "\\b";
		Pattern p = Pattern.compile(pattern);
		Matcher m = p.matcher(source);
		return m.find();
	}

}
