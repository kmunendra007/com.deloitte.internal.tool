package com.code.removal;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.net.URL;
import java.net.URLClassLoader;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;

import org.reflections.Reflections;
import org.reflections.scanners.ResourcesScanner;
import org.reflections.scanners.SubTypesScanner;
import org.reflections.util.ClasspathHelper;
import org.reflections.util.ConfigurationBuilder;
import org.reflections.util.FilterBuilder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import com.code.removal.model.ClassLineCount;
import com.code.removal.sc.service.ISourceCodeService;
import com.code.removal.sc.util.SourceCodeUtil;
import com.thoughtworks.qdox.JavaDocBuilder;
import com.thoughtworks.qdox.model.JavaSource;

@SpringBootApplication
public class CodeRemovalApplication {

	@Autowired
	ISourceCodeService codeService;

	public static void main(String[] args) {

		SpringApplication.run(CodeRemovalApplication.class, args);

//		final String rootDirectory = "C:\\Users\\munekumar\\Java\\workspace\\completable-future\\";
//		final String sourcePath = rootDirectory + "src\\main\\java";
//		final String classPath = rootDirectory + "target\\classes\\";

//		Set<String> packages = new HashSet<>();
//		Map<String, Set<String>> sourceMap = new HashMap<>();

		// get all the packages for the specified ldirectory
//		getPackages(sourcePath, packages);

		// iterate each package and get the all classes and interfaces for each
//		packages.stream().forEach(packageName-> getClasses(classPath, packageName, sourceMap));
//		
//		Set<Entry<String, Set<String>>> entrySet = sourceMap.entrySet();
//		
//		for (Entry<String, Set<String>> entry : entrySet) {
//			System.out.println("Package: "+entry.getKey()+" Classes: "+entry.getValue());
//		}

		final String rootDirectory = "C:\\Users\\JKALYANI\\Desktop\\practice\\spring-boot-crud-operation\\";
		final String sourcePath = rootDirectory + "src\\main\\java\\";
		final String classPath = rootDirectory + "target\\classes\\";

		Set<String> packages = new HashSet<>();

		Map<String, Set<String>> sourceMap = new HashMap<>();

		Map<String, List<ClassLineCount>> classCount = new HashMap<>();

		// get all the packages for the specified directory
		getPackages(sourcePath, packages);

		// iterate each package and get the all classes and interfaces for each
		getClasses(classPath, packages, sourceMap);

		Set<Entry<String, Set<String>>> entrySet = sourceMap.entrySet();

		String packageName = null;
		for (Entry<String, Set<String>> entry : entrySet) {

			packageName = entry.getKey();

			List<ClassLineCount> classLineCounts = new ArrayList<>();

			for (String className : entry.getValue()) {

				// getting the class names and number of lines for each class
				SourceCodeUtil.getNumberOfLinesOfCode(sourcePath, className, classLineCounts);

			}

			classCount.put(packageName, classLineCounts);

			// getting total number of lines for specific package
			int sumOfPackage = classLineCounts.stream().mapToInt(i -> i.getLoc()).sum();
			System.out.println("sumOfPackage:" + sumOfPackage);

		}

	}

	public static void getPackages(final String sourcePath, Set<String> packages) {

		File directory = new File(sourcePath);
		// get all the files from a directory
		File[] files = directory.listFiles();
		for (File file : files) {
			if (file.isFile()) {
				String filePath = file.getPath();
				String packageName = filePath.substring(filePath.indexOf("java") + 5, filePath.lastIndexOf('\\'));

				packages.add(packageName.replace('\\', '.'));
			} else if (file.isDirectory()) {
				getPackages(file.getAbsolutePath(), packages);
			}
		}
	}

	private static void getClasses(final String classPath, final Set<String> packages,
			Map<String, Set<String>> sourceMap) {
		try {

			File file = new File(classPath);

			// convert the file to URL format
			URL url = file.toURI().toURL();
			URL[] urls = new URL[] { url };

			// load this folder into Class loader
			ClassLoader cl = new URLClassLoader(urls);
			for (String packageName : packages) {
				Reflections reflections = new Reflections(
						new ConfigurationBuilder().setScanners(new SubTypesScanner(false), new ResourcesScanner())
								.setUrls(ClasspathHelper.forClassLoader(cl))
								.filterInputsBy(new FilterBuilder().includePackage(packageName)));

				// get all the classes and interfaces
				Set<String> allTypes = reflections.getAllTypes();

				sourceMap.put(packageName, allTypes);
			}
		} catch (Exception ex) {
			ex.printStackTrace();
		}
	}

	public static void checkImports() {
		String fileFullPath = "C:\\Users\\munekumar\\Java\\workspace\\code-formatter\\src\\main\\java\\com\\code\\formatter\\demo\\ImportsOrganizer.java";
		JavaDocBuilder builder = new JavaDocBuilder();
		try {
			builder.addSource(new FileReader(fileFullPath));

			JavaSource src = builder.getSources()[0];
			String[] imports = src.getImports();

			for (String imp : imports) {
				System.out.println(imp);
			}
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}

}
