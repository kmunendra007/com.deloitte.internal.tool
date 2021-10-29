package com.code.removal.sc.util;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.lang.reflect.Method;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLClassLoader;
import java.nio.charset.StandardCharsets;
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
import java.util.stream.Collectors;
import java.util.stream.Stream;

import org.apache.commons.io.FileUtils;
import org.springframework.beans.factory.annotation.Autowired;

import com.code.removal.model.ClassLineCount;
import com.code.removal.model.SourceCode;
import com.thoughtworks.qdox.JavaDocBuilder;
import com.thoughtworks.qdox.model.JavaSource;

import javassist.ClassClassPath;
import javassist.ClassPool;
import javassist.CtClass;
import javassist.CtMethod;
import javassist.NotFoundException;

public abstract class SourceCodeUtil {
	private String filePath;

	@Autowired
	JavaDocBuilder builder;
	
	@Autowired
	private ISessionDataManager sessionDataManager;

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
			String filePath = (sourcePath + className).replace('.', '\\')+ ".java";

			Path files = Paths.get(filePath);
			int count = (int) Files.lines(files).map(s->s.trim()).filter(s -> !s.isEmpty()&&!s.startsWith("//")&&!s.endsWith("*/")&&!s.startsWith("//")).count();

			String classNames = className.substring(className.lastIndexOf('.') + 1) + ".java";

			classLineCounts.add(new ClassLineCount(classNames, count));

		} catch (Exception e) {
			e.getStackTrace();
		} finally {

		}
	}
	
	public boolean removeLine(String importString) {
        File file = new File(filePath);
        List<String> lines;
        try {
            lines = FileUtils.readLines(file, StandardCharsets.UTF_8);
            List<String> updatedLines = lines.stream().filter(s -> !s.contains(importString)).collect(Collectors.toList());
            FileUtils.writeLines(file, updatedLines, false);
        } catch (IOException e) {
            e.printStackTrace();
        }
        return true;
    }
	
public List<String> checkUndocumentedMethods(Method[] methods,List<String> readAllLines,Class classObj) throws NotFoundException {
		
		List<String> missingDocMethods = new ArrayList<String>();
		
		for (Method method : methods) {
			System.out.println("Method Name: "+method.getName());
			
			//Using javassist to get method line number
			
			ClassPool pool = ClassPool.getDefault();
			ClassClassPath ccpath = new ClassClassPath(classObj);
			pool.insertClassPath(ccpath);
			CtClass ctClass = pool.get(method.getDeclaringClass().getCanonicalName());
			CtMethod declaredMethod = ctClass.getDeclaredMethod(method.getName());
			
			int lineNumber = declaredMethod.getMethodInfo().getLineNumber(0);
			int methodLineNumber = lineNumber-1; //InOrder to check the line before method name
			System.out.println("Line Number of Method "+methodLineNumber);
			
			while (true) {
				String line = readAllLines.get(methodLineNumber - 1); //Inorder to fetch from list 
				if (line.equals("\t */")) { //check the last line of documentation before method
					
					break;
				} else if (line.equals("\t}")) { // checking }
					
					missingDocMethods.add(method.getName());
					break;
				} else if(line.contains(";")){
					
					//Check for documentation on first method
					missingDocMethods.add(method.getName());
					break;
				} else {
					// decrement the lineNumber to check content
					methodLineNumber--;
				}
			}
		}
		System.out.println("Method without documentation "+missingDocMethods.toString());
		return missingDocMethods;
	}
	
	
	public Class getClassObj(String sessionId,String className,String packageName) throws ClassNotFoundException, MalformedURLException {
		
		File file = new File(sessionDataManager.getClassPath(sessionId));

		// convert the file to URL format
		URL url = file.toURI().toURL();
		URL[] urls = new URL[] { url };

		// load this folder into Class loader
		ClassLoader cl = new URLClassLoader(urls);
		
		Class<?> classObj = cl.loadClass(packageName+"."+className.substring(0, className.indexOf(".java")));
		return classObj;
	}
}
