package com.siebre.util;

import java.io.BufferedInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.TimeZone;

import org.apache.commons.lang.StringUtils;
import org.apache.commons.net.ftp.FTPClient;
import org.apache.commons.net.ftp.FTPClientConfig;
import org.apache.commons.net.ftp.FTPReply;
import org.apache.log4j.Logger;

public class FtpUtils {
	private String ip;
	private int port = -1;
	private String username;
	private String password;
	private static FTPClient ftpClient;
	private String encoding = "GBK";
	public static final Logger log = Logger.getLogger(FtpUtils.class);

	public FtpUtils(String ip, String username, String password) {
		this.ip = ip;
		this.username = username;
		this.password = password;
		this.ftpClient = new FTPClient();
	}

	public FtpUtils(String ip, int port, String username, String password ){
		this.ip = ip;
		this.username = username;
		this.password = password;
		this.port = port;
		this.ftpClient = new FTPClient();
	}

	/**
	 * 建立连接
	 * 
	 * @return
	 */
	public boolean connectServer() {
		FTPClientConfig config = new FTPClientConfig();
		config.setServerTimeZoneId(TimeZone.getDefault().getID());
		ftpClient.setControlEncoding(encoding);
		ftpClient.configure(config);
		boolean isLogin = false;
		try {
			if (port != -1) {
				ftpClient.connect(ip, port);
			} else {
				ftpClient.connect(ip);
			}
			int reply = ftpClient.getReplyCode();
			if (!FTPReply.isPositiveCompletion(reply)) {
				ftpClient.disconnect();
				log.error("FTP连接失败");
				return isLogin;
			}
			ftpClient.login(username, password);
			// 设置工作模式
			ftpClient.enterLocalPassiveMode();
			// 设置文件类型
			ftpClient.setFileType(FTPClient.BINARY_FILE_TYPE);
		} catch (Exception e) {
			log.error("连接FTP错误:" + e.getMessage());
			e.printStackTrace();
		}
		ftpClient.setBufferSize(1024 * 2);
		ftpClient.setDataTimeout(30 * 1000);
		return isLogin;
	}

	/**
	 * 用户退出，关闭连接
	 */
	public void close() {
		if (null != ftpClient && ftpClient.isConnected()) {
			try {
				ftpClient.logout();
			} catch (IOException e) {
				e.printStackTrace();
				log.error("FTP退出错误:" + e.getMessage());
			} finally {
				try {
					ftpClient.logout();
					ftpClient.disconnect();
				} catch (IOException e) {
					e.printStackTrace();
					log.error("FTP关闭错误:" + e.getMessage());
				}
			}
		}
	}

	/**
	 * 上传文件
	 * @param localFilePath --本地文件全路径
	 * @param newFileName   --服务器新文件名
	 * @param ftpDir        --服务器新文件路径
	 * @return
	 */
	public boolean uploadFile(String localFilePath, String newFileName, String ftpDir){
		BufferedInputStream buffIn = null;
		boolean flag = true;
		try {
			log.debug("uploadFile,ftpDir:" + ftpDir + ";localFilePath:" + localFilePath + ";newFileName:" + newFileName);
			if (ftpDir == null || "".equals(ftpDir)) {
				ftpDir = "/";
			}
			cd(ftpDir);
			buffIn = new BufferedInputStream(new FileInputStream(localFilePath));
			ftpClient.storeFile(newFileName, buffIn);
		} catch (Exception e) {
			flag = false;
			log.debug("上传文件失败:" + e.getMessage());
			e.printStackTrace();
		} finally {
			try {
				if (buffIn != null)
					buffIn.close();
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
		return flag;
	}
	
	/**
	 * 上传文件
	 * @param file         --本地文件
	 * @param newFileName  --服务器新文件名
	 * @param ftpDir       --服务器新文件路径
	 * @return
	 */
	public boolean uploadFile(File file, String newFileName, String ftpDir){
		BufferedInputStream buffIn = null;
		boolean flag = true;
		try {
			cd(ftpDir);
			buffIn = new BufferedInputStream(new FileInputStream(file));
			ftpClient.storeFile(newFileName, buffIn);
		} catch (Exception e) {
			flag = false;
			log.debug("上传文件失败:" + e.getMessage());
			e.printStackTrace();
		} finally {
			try {
				if (buffIn != null)
					buffIn.close();
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
		return flag;
	}

	/**
	 * 上传文件
	 * @param inputStream   --本地文件I/O流
	 * @param newFileName   --服务器新文件名
	 * @param ftpDir        --服务器新文件路径
	 * @return
	 */
	public boolean uploadFile(InputStream inputStream, String newFileName, String ftpDir){
		BufferedInputStream buffIn = null;
		boolean flag = true;
		try {
			cd(ftpDir);
			buffIn = new BufferedInputStream(inputStream);
			ftpClient.storeFile(newFileName, buffIn);
		} catch (Exception e) {
			flag = false;
			log.debug("上传文件失败:" + e.getMessage());
			e.printStackTrace();;
		} finally {
			try {
				if (buffIn != null)
					buffIn.close();
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
		return flag;
	}
	
	/**
	 * 下载文件
	 * @param ftpDir          --服务器路径名
	 * @param remoteFileName  --服务器文件名
	 * @param localFileName   --本地文件全路径名
	 * @return
	 */
	public File loadFile(String ftpDir,String remoteFileName,String localFileName){
		//下载文件(BufferedOutputStream / ByteArrayOutputStream)
		//BufferedOutputStream buffOut = null;
		ByteArrayOutputStream tempByteStream = null;
		boolean success = false;
		File localFile = null;
		try {
			cd(ftpDir);
			tempByteStream = new ByteArrayOutputStream();
			//buffOut = new BufferedOutputStream(new FileOutputStream(localFileName));
			//ftpClient.retrieveFile(new String(remoteFileName.getBytes("GBK"), "ISO-8859-1"), buffOut);
			success = ftpClient.retrieveFile(new String(remoteFileName.getBytes("GBK"), "ISO-8859-1"), tempByteStream);
			log.debug("下载文件结果：" + success);
			if (success) {
				localFile = new File(localFileName);
				if (localFile.exists()) {
					localFile.delete();
					localFile = new File(localFileName);
				}

				org.apache.commons.io.FileUtils.writeByteArrayToFile(localFile, tempByteStream.toByteArray());
			} else {
				log.debug("下载文件失败");
				File nullFile = new File(localFileName);
				if (nullFile != null && nullFile.exists()) {
					log.debug("删除存在的文件……");
					nullFile.deleteOnExit();
				}
				//throw new FtpException("下载文件失败");
			}
			//buffOut.flush();
		} catch (IOException e) {
			log.debug("下载文件失败:" + e.getMessage());
			e.printStackTrace();
		} finally {
			try {
				//if (buffOut != null)
				//	buffOut.close();
				if (tempByteStream != null)
					tempByteStream.close();

				if (!success) {
					if (localFile != null && localFile.exists()) {
						localFile.delete();
					}
				}
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
		return localFile;
	}
	
	/**
	  * 删除FTP服务器文件
	 */
	public void deleteFile(String filename){
		try {
			ftpClient.deleteFile(filename);
		} catch (IOException e) {
			log.debug("删除FTP服务器文件错误");
			e.printStackTrace();
		}
	}

	/**
	  * 重命名FTP服务器文件
	  * @param oldFileName --原文件名
	  * @param newFileName --新文件名
	 */
	public void renameFileString(String oldFileName, String newFileName){
		try {
			ftpClient.rename(oldFileName, newFileName);
		} catch (IOException e) {
			log.debug("重命名FTP服务器文件名错误");
			e.printStackTrace();
		}
	}
	
	/** 进入到服务器的某个目录下 */
	public void cd(String directory){
		try {
			boolean flag = ftpClient.changeWorkingDirectory(directory);
			log.debug("服务器的目录（" + directory + "）连接结果：" + flag);
		} catch (IOException e) {
			log.debug("切换ftp服务器目录失败");
			e.printStackTrace();
		}
	}
	
	/**
	 * 进入本机的某个目录
	*/
	public void intoLocalDirectory(String ftpDir){
		try {
			ftpClient.cwd(ftpDir);
			cd(ftpDir);
		} catch (IOException e) {
			log.debug("进入本机目录失败");
			e.printStackTrace();
		}
	}

	/**
	  * 返回到上一层目录
	 */
	public void changeToParentDirectory(){
		try {
			ftpClient.changeToParentDirectory();
		} catch (IOException e) {
			log.debug("返回上层目录失败");
			e.printStackTrace();
		}
	}
	
}
