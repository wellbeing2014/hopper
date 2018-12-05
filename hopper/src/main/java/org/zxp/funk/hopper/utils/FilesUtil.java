package org.zxp.funk.hopper.utils;

import java.io.File;

public class FilesUtil {
	
	/**
	 *  检查文件是否存在如果存在则重命名
	 * @param file
	 * @param i 从1开始
	 * @return
	 */
	public static File fileAutoRename(File file,int i) {
		if(file.exists()) {
			String newfilepath = file.getParent()+getShortName(file.getName())+"_"+i+getExtention(file.getName());
			file = fileAutoRename(new File(newfilepath),i+1);
		}
		return file;
	}
	
	/** 
	 * * 获取短文件名,不带扩展名 
     * @param fileName 
     * @return 
     */  
    public static String getShortName(String fileName){  
        if(fileName != null && fileName.length()>0 && fileName.lastIndexOf(".")>-1){  
            return fileName.substring(0, fileName.lastIndexOf("."));  
        }   
        return fileName;  
    }  
      
    /** 
     * * 获取扩展名,带点 
     * @param fileName 
     * @return 
     */  
    public static String getExtention(String fileName){  
        if(fileName!=null && fileName.length()>0 && fileName.lastIndexOf(".")>-1){  
            return fileName.substring(fileName.lastIndexOf("."));  
        }  
        return "";  
    }  

}
