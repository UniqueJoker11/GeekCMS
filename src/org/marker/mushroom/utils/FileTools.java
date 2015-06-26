package org.marker.mushroom.utils;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.io.UnsupportedEncodingException;

/**
 * 文件工具类
 * @author marker
 * @date 2012-12-03
 * */
public class FileTools {
	
	/** 文本文件UTF-8编码 */
	public static final String FILE_CHARACTER_UTF8 = "UTF-8";
	/** 文本文件GBK编码 */
	public static final String FILE_CHARACTER_GBK  = "GBK";
 

	
	/**
	 * 获取文本文件内容
	 * @param filePath 文件路径
	 * @param character 字符编码
	 * @return String 文件文本内容
	 * @throws IOException 
	 * */
	public static final String getFileContet(File filePath,String character) throws IOException{
		return FileTools.getContent(filePath, character);
	}
	
	
	/**
	 * 写入文本文件内容
	 * @param filePath 文件路径
	 * @param character 字符编码 
	 * @throws IOException 
	 * */
	public static final void setFileContet(File filePath, String content, String character) throws IOException{
		FileTools.setContent(filePath, content, character);
	}	
	
	
	//内部处理文件方法
	private static String getContent(File filePath, String character) throws IOException{
		StringBuffer sb = new StringBuffer();
	 
		FileInputStream   __fis = new FileInputStream(filePath);//文件字节流
		InputStreamReader __isr = new InputStreamReader(__fis, character);//字节流和字符流的桥梁，可以指定指定字符格式
		BufferedReader    __br  = new BufferedReader(__isr);
		
		String temp = null;
		while ((temp = __br.readLine()) != null) { sb.append(temp+"\n"); }
		__br.close();__isr.close(); __fis.close();
	  
		return sb.toString();//返回文件内容
	}
	
	//内部处理文件保存
	private static void setContent(File filePath, String content, String character) throws FileNotFoundException, UnsupportedEncodingException{
		PrintWriter __pw = new PrintWriter(filePath, character);
		__pw.write(content); __pw.close();
	}
	
	/**
	 * 根据路径删除指定的目录或文件，无论存在与否
	 * 
	 * @param sPath
	 *            要删除的目录或文件
	 * @return 删除成功返回 true，否则返回 false。
	 */
	public boolean deleteFolder(File delFolder) { 
		// 判断目录或文件是否存在
		if (!delFolder.exists()) { // 不存在返回 false
			return false;
		} else {
			// 判断是否为文件
			if (delFolder.isFile()) { // 为文件时调用删除文件方法
				return deleteFile(delFolder);
			} else { // 为目录时调用删除目录方法
				return deleteDirectory(delFolder);
			}
		}
	}

	/**
	 * 删除单个文件
	 * 
	 * @param sPath
	 *            被删除文件的文件名
	 * @return 单个文件删除成功返回true，否则返回false
	 */
	public static boolean deleteFile(File delFile) {
		// 路径为文件且不为空则进行删除
		if (delFile.isFile() && delFile.exists()) {
			return delFile.delete();
		}
		return false;
	}

	/**
	 * 删除目录（文件夹）以及目录下的文件
	 * 
	 * @param sPath
	 *            被删除目录的文件路径
	 * @return 目录删除成功返回true，否则返回false
	 */
	public static boolean deleteDirectory(File dirFile) {
		// 如果dir对应的文件不存在，或者不是一个目录，则退出
		if (!dirFile.exists() || !dirFile.isDirectory()) {
			return false;
		}
		boolean flag = true;
		// 删除文件夹下的所有文件(包括子目录)
		File[] files = dirFile.listFiles();
		for (int i = 0; i < files.length; i++) {
			// 删除子文件
			if (files[i].isFile()) {
				flag = deleteFile(files[i]);
				if (!flag)
					break;
			} // 删除子目录
			else {
				flag = deleteDirectory(files[i]);
				if (!flag)
					break;
			}
		}
		if (!flag)
			return false;
		// 删除当前目录
		if (dirFile.delete()) {
			return true;
		} else {
			return false;
		}
	}

}
