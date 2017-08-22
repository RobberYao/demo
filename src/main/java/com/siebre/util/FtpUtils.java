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
	 * ��������
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
				log.error("FTP����ʧ��");
				return isLogin;
			}
			ftpClient.login(username, password);
			// ���ù���ģʽ
			ftpClient.enterLocalPassiveMode();
			// �����ļ�����
			ftpClient.setFileType(FTPClient.BINARY_FILE_TYPE);
		} catch (Exception e) {
			log.error("����FTP����:" + e.getMessage());
			e.printStackTrace();
		}
		ftpClient.setBufferSize(1024 * 2);
		ftpClient.setDataTimeout(30 * 1000);
		return isLogin;
	}

	/**
	 * �û��˳����ر�����
	 */
	public void close() {
		if (null != ftpClient && ftpClient.isConnected()) {
			try {
				ftpClient.logout();
			} catch (IOException e) {
				e.printStackTrace();
				log.error("FTP�˳�����:" + e.getMessage());
			} finally {
				try {
					ftpClient.logout();
					ftpClient.disconnect();
				} catch (IOException e) {
					e.printStackTrace();
					log.error("FTP�رմ���:" + e.getMessage());
				}
			}
		}
	}

	/**
	 * �ϴ��ļ�
	 * @param localFilePath --�����ļ�ȫ·��
	 * @param newFileName   --���������ļ���
	 * @param ftpDir        --���������ļ�·��
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
			log.debug("�ϴ��ļ�ʧ��:" + e.getMessage());
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
	 * �ϴ��ļ�
	 * @param file         --�����ļ�
	 * @param newFileName  --���������ļ���
	 * @param ftpDir       --���������ļ�·��
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
			log.debug("�ϴ��ļ�ʧ��:" + e.getMessage());
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
	 * �ϴ��ļ�
	 * @param inputStream   --�����ļ�I/O��
	 * @param newFileName   --���������ļ���
	 * @param ftpDir        --���������ļ�·��
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
			log.debug("�ϴ��ļ�ʧ��:" + e.getMessage());
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
	 * �����ļ�
	 * @param ftpDir          --������·����
	 * @param remoteFileName  --�������ļ���
	 * @param localFileName   --�����ļ�ȫ·����
	 * @return
	 */
	public File loadFile(String ftpDir,String remoteFileName,String localFileName){
		//�����ļ�(BufferedOutputStream / ByteArrayOutputStream)
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
			log.debug("�����ļ������" + success);
			if (success) {
				localFile = new File(localFileName);
				if (localFile.exists()) {
					localFile.delete();
					localFile = new File(localFileName);
				}

				org.apache.commons.io.FileUtils.writeByteArrayToFile(localFile, tempByteStream.toByteArray());
			} else {
				log.debug("�����ļ�ʧ��");
				File nullFile = new File(localFileName);
				if (nullFile != null && nullFile.exists()) {
					log.debug("ɾ�����ڵ��ļ�����");
					nullFile.deleteOnExit();
				}
				//throw new FtpException("�����ļ�ʧ��");
			}
			//buffOut.flush();
		} catch (IOException e) {
			log.debug("�����ļ�ʧ��:" + e.getMessage());
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
	  * ɾ��FTP�������ļ�
	 */
	public void deleteFile(String filename){
		try {
			ftpClient.deleteFile(filename);
		} catch (IOException e) {
			log.debug("ɾ��FTP�������ļ�����");
			e.printStackTrace();
		}
	}

	/**
	  * ������FTP�������ļ�
	  * @param oldFileName --ԭ�ļ���
	  * @param newFileName --���ļ���
	 */
	public void renameFileString(String oldFileName, String newFileName){
		try {
			ftpClient.rename(oldFileName, newFileName);
		} catch (IOException e) {
			log.debug("������FTP�������ļ�������");
			e.printStackTrace();
		}
	}
	
	/** ���뵽��������ĳ��Ŀ¼�� */
	public void cd(String directory){
		try {
			boolean flag = ftpClient.changeWorkingDirectory(directory);
			log.debug("��������Ŀ¼��" + directory + "�����ӽ����" + flag);
		} catch (IOException e) {
			log.debug("�л�ftp������Ŀ¼ʧ��");
			e.printStackTrace();
		}
	}
	
	/**
	 * ���뱾����ĳ��Ŀ¼
	*/
	public void intoLocalDirectory(String ftpDir){
		try {
			ftpClient.cwd(ftpDir);
			cd(ftpDir);
		} catch (IOException e) {
			log.debug("���뱾��Ŀ¼ʧ��");
			e.printStackTrace();
		}
	}

	/**
	  * ���ص���һ��Ŀ¼
	 */
	public void changeToParentDirectory(){
		try {
			ftpClient.changeToParentDirectory();
		} catch (IOException e) {
			log.debug("�����ϲ�Ŀ¼ʧ��");
			e.printStackTrace();
		}
	}
	
}
