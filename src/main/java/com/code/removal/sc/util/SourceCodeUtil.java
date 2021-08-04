package com.code.removal.sc.util;

import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;
import java.util.function.Supplier;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Stream;

import org.springframework.beans.factory.annotation.Autowired;

import com.code.removal.demo.model.ClassLineCount;
import com.code.removal.demo.model.SourceCode;
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

		// create the supplier
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

	public List<SourceCode> checkNumberOfLinesOfCode(Map<String, Set<String>> sourceMap) {

		final String rootDirectory = "C:\\Users\\JKALYANI\\Desktop\\practice\\spring-boot-crud-operation\\";
		final String sourcePath = rootDirectory + "src\\main\\java\\";

		SourceCode sourcecode = null;

		ArrayList<SourceCode> sourceCodeList = new ArrayList<>();

		List<ClassLineCount> classLineCounts = new ArrayList<>();

		Set<Entry<String, Set<String>>> entrySet = sourceMap.entrySet();

		sourcecode = new SourceCode();
		sourcecode.setSourceMap(sourceMap);

		// iterate each package and class names
		for (Entry<String, Set<String>> entry : entrySet) {

			for (String classNames : entry.getValue()) {

				// get the class names and number of lines for each class
				getNumberOfLinesOfCode(sourcePath, classNames, classLineCounts);

				// get the total number of lines for specific package
				int sumOfPackage = classLineCounts.stream().mapToInt(i -> i.getLoc()).sum();

				sourcecode.setLoc(sumOfPackage);
				sourcecode.setClassLineCounts(classLineCounts);

			}

			sourceCodeList.add(sourcecode);
			System.out.println("sourceCodeList:" + sourceCodeList);

		}
		return sourceCodeList;

	}

	public static void getNumberOfLinesOfCode(String sourcePath, String className,
			List<ClassLineCount> classLineCounts) {

		try {
			// make a connection to the file
			String filePath = (sourcePath + className).replace('.', '\\') + ".java";

			Path files = Paths.get(filePath);

			int count = (int) Files.lines(files).count();

			String classNames = className.substring(className.lastIndexOf('.') + 1) + ".java";

			classLineCounts.add(new ClassLineCount(classNames, count));

		} catch (Exception e) {
			e.getStackTrace();
		} finally {

		}
	}
}
