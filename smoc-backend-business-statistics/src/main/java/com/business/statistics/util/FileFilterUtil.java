package com.business.statistics.util;

import java.io.File;
import java.io.FileFilter;

public class FileFilterUtil {
	/**
	 * 	加载符合条件的文件
	 * @param filePath
	 * @param filename
	 * @return
	 */
	public static File[] findFile(String filePath,final String filename) {
		FileFilter ff = new FileFilter() {

			@Override
			public boolean accept(File pathname) {
				String s = pathname.getName();
				if (s.startsWith(filename)) {
					return true;
				}
				return false;
			}
		};
		File file = new File(filePath);
		File[]  files = file.listFiles(ff);
		if(files == null){
			return new File[0];
		}
		return files;
	}
}