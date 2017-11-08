package org.zxp.funk.hopper.utils;

import java.io.Closeable;
import java.io.File;
import java.io.IOException;
import java.net.Socket;
import java.net.UnknownHostException;
import java.nio.file.FileVisitResult;
import java.nio.file.Path;
import java.nio.file.SimpleFileVisitor;
import java.nio.file.attribute.BasicFileAttributes;


public class IOUtil {
    /**
     * 关闭一个或多个流对象
     * 
     * @param closeables
     *            可关闭的流对象列表
     * @throws IOException
     */
    public static void close(Closeable... closeables) throws IOException {
        if (closeables != null) {
            for (Closeable closeable : closeables) {
                if (closeable != null) {
                    closeable.close();
                }
            }
        }
    }
 
    /**
     * 关闭一个或多个流对象
     * 
     * @param closeables
     *            可关闭的流对象列表
     */
    public static void closeQuietly(Closeable... closeables) {
        try {
            close(closeables);
        } catch (IOException e) {
            // do nothing
        }
    }
    
    /**
     * 删除指定的一个文件夹
     * @param path
     * @变更记录 2017年9月8日 下午4:07:45 朱新培 zxp@wisoft.com.cn 创建
     */
    public static void deleteDirectory(File dirpath,boolean deleteSelf) throws IOException {
    	if (!dirpath.exists()) {
    		if(!deleteSelf)
    			dirpath.mkdir();
    		return;
    	}
    	
    	java.nio.file.Files.walkFileTree(dirpath.toPath(), new SimpleFileVisitor<Path>() {
			@Override
			public FileVisitResult visitFile(Path file,
					BasicFileAttributes attrs) throws IOException {
				java.nio.file.Files.delete(file);
				return FileVisitResult.CONTINUE;
			}
			@Override
			public FileVisitResult postVisitDirectory(Path dir, IOException exc)
					throws IOException {
				java.nio.file.Files.delete(dir);
				return FileVisitResult.CONTINUE;
			}
		});
    	
    	if(!deleteSelf)
			dirpath.mkdir();
		return;
    }
    
    /**
     * 检查端口是否联通
     * 
     * @param ip
     * @param port
     * @return
     *
     * @变更记录 2017年9月8日 下午4:07:45 朱新培 zxp@wisoft.com.cn 创建
     *
     */
    public static boolean portAlive(String ip, int port) {
    	Socket s = null;
    	boolean alive = false;
    	 try {
             s = new Socket(ip, port); //IP 要扫描的
             alive =  true;
             s.close();
         } catch (UnknownHostException ex) {
            // Logger.getLogger(NewClass.class.getName()).log(Level.SEVERE, null, ex);
         } catch (IOException ex) {
            // Logger.getLogger(NewClass.class.getName()).log(Level.SEVERE, null, ex);
         }
    	 finally {
    		 s = null;
		}
    	 return alive;
    }
    
    public static void main(String[] args) {
    	boolean alive = portAlive("192.10.110.68",9090);
    	System.out.println(alive);
	}
 
}