package com.wuerth.phoenix.cis.university.example2.util;

import java.io.File;
import java.io.FileOutputStream;
import java.io.PrintStream;

import com.wuerth.phoenix.cis.university.example2.adapters.IFRS16ImportAssignmentType;

public class FileGenerator {

	public static void generate(Scenario scenario) throws Exception {
		PrintStream printStream = new PrintStream(new FileOutputStream(new File(scenario.getSettings().getFilePath())));
		for(CombinationLine line : scenario.getCombination().getLineList()) {
			StringBuffer buffer = new StringBuffer();
			boolean first = true;
			for(IFRS16ImportAssignmentType type : scenario.getSettings().getTypeList()) {
				if(first) {
					first = false;
				}
				else {
					buffer.append(Constants.SEPARATOR);
				}
				String name = line.getDataMap().containsKey(type) ? line.getDataMap().get(type).getName() : "";
				if(name != null && name.contains(Character.toString(Constants.SEPARATOR))) {
					buffer.append("\"").append(name).append("\"");
				}
				else {
					buffer.append(name);
				}
			}
			printStream.println(buffer.toString());
		}
		printStream.flush();
		printStream.close();
	}
}
