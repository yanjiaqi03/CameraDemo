package ihealthlabs.camerademo.utils;

import android.content.Context;
import android.graphics.Typeface;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;

public class FileUtils {
	private static FileUtils mInstance = null;

	public static FileUtils getInstance() {
		if (mInstance == null) {
			mInstance = new FileUtils();
		}

		return mInstance;
	}

	/**
	 * 创建本地空文件
	 * 
	 * @author YanJiaqi
	 * @param path
	 */
	public void createFile(String path,String fileName) throws IOException {
		createDir(path);
		File file = null;
		if(path.endsWith("/")){
			file = new File(path + fileName);
		}else{
			file = new File(path + "/" + fileName);
		}

		if (!file.exists()) {
			file.createNewFile();
		}
	}

	/**
	 * 创建本地目录
	 * 
	 * @author YanJiaqi
	 * @param path
	 */
	public void createDir(String path) {
		File file = new File(path);
		if (!file.exists()) {
			file.mkdir();
		}
	}

	/**
	 * 判断本地文件是否存在
	 * 
	 * @author YanJiaqi
	 * @param path
	 * @return boolean
	 */
	public boolean isExist(String path) {
		File file = new File(path);
		return file.exists();

	}

	/**
	 * 删除本地文件
	 * 
	 * @author YanJiaqi
	 * @param path
	 * @return
	 */
	public boolean deleteFile(String path) {
		File file = new File(path);
		if (!file.exists() || !file.isFile()) {
			return false;
		}

		return file.delete();
	}

	/**
	 * 删除本地文件夹
	 * 
	 * @author YanJiaqi
	 * @param path
	 */
	public void deleteDir(String path) {
		if (path == null) {
			return;
		}
		File file = new File(path);
		if (file.exists() && file.isDirectory()) {
			File[] alls = file.listFiles();
			for (int i = 0; i < alls.length; i++) {
				if (alls[i].isDirectory()) {
					if (path.endsWith("/")) {
						deleteDir(path + alls[i].getName());
					} else {
						deleteDir(path + "/" + alls[i].getName());
					}
				} else {
					alls[i].delete();
				}
			}

			file.delete();
		}
	}

	/**
	 * 复制文件
	 * 
	 * @author YanJiaqi
	 * @param src
	 * @param dest
	 */
	public void copyFile(String src, String dest) throws IOException {
		FileInputStream in = new FileInputStream(src);
		File file = new File(dest);
		if (!file.exists())
			file.createNewFile();
		FileOutputStream out = new FileOutputStream(file);
		int c;
		byte buffer[] = new byte[1024];
		while ((c = in.read(buffer)) != -1) {
			for (int i = 0; i < c; i++)
				out.write(buffer[i]);
		}
		in.close();
		out.flush();
		out.close();
	}

	public void writeFile(String path, StringBuffer content) throws IOException {
		writeFile(path, content, true);
	}

	/**
	 * 用StringBuffer写入文件
	 * 
	 * @author YanJiaqi
	 * @param path
	 * @param content
	 * @param isAppend
	 *            是否为追加方式
	 */
	public void writeFile(String path, StringBuffer content, boolean isAppend) throws IOException {
		if (content == null) {
			return;
		}
		File file = new File(path);
		if (!file.exists())
			file.createNewFile();

		FileOutputStream out = new FileOutputStream(file, isAppend);
		out.write(content.toString().getBytes("utf-8"));

		out.flush();
		out.close();
	}

	/**
	 * 读取本地文件
	 * 
	 * @author YanJiaqi
	 * @param path
	 * @return
	 * @throws IOException
	 */
	public String readFile(String path) throws IOException {
		File file = new File(path);
		@SuppressWarnings("resource")
		FileInputStream fis = new FileInputStream(file);
		byte[] buf = new byte[1024];
		StringBuffer sb = new StringBuffer();
		while ((fis.read(buf)) != -1) {
			sb.append(new String(buf));
			buf = new byte[1024];
		}
		return sb.toString();
	}
	
	/**
	 * 获取文件大小
	 * 
	 * @author YanJiaqi
	 * @param path
	 * @return
	 */
	public long getFileSize(String path){
		long size = 0;
		File file = new File(path);
		if(file.exists() && file.isFile()){
			size = file.length();
		}
		
		return size;
	}

	/**
	 * 从Assets中获取typeface字体文件
	 *
	 * @author YanJiaqi
	 * @param context
	 * @param fontName
	 * @return
	 */
	public Typeface getFontFromAsset(Context context,String fontName){
		return Typeface.createFromAsset(context.getAssets(), fontName);
	}
}
