package com.ibm.app.services.netBase;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.net.SocketException;

import org.apache.commons.net.ftp.FTP;
import org.apache.commons.net.ftp.FTPClient;

import com.jcraft.jsch.Channel;
import com.jcraft.jsch.ChannelSftp;
import com.jcraft.jsch.JSch;
import com.jcraft.jsch.JSchException;
import com.jcraft.jsch.Session;
import com.jcraft.jsch.SftpException;

public class FtpSftp {

	public static void main(String aregs[]) throws SocketException, IOException
	{
		
		//testSFTP();
		testFTP();
		
	}

	private static void testFTP() throws SocketException, IOException
	{
		String server = "170.225.233.215";
        int port = 21;
        String user = "netbaseusr";
        String pass = "netbaSE123!@#";
        FTPClient ftpClient = new FTPClient();
        
        ftpClient.connect(server, port);
        ftpClient.login(user, pass);
        ftpClient.enterLocalPassiveMode();

        ftpClient.setFileType(FTP.BINARY_FILE_TYPE);
        
        File firstLocalFile = new File("C:/netBaseOutput/prodTest.txt");
        
        String firstRemoteFile = "prodTest.txt";
        InputStream inputStream = new FileInputStream(firstLocalFile);

        System.out.println("Start uploading first file");
        boolean done = ftpClient.storeFile(firstRemoteFile, inputStream);
        inputStream.close();
        if (done) {
            System.out.println("The first file is uploaded successfully.");
        }
        
	}
	
	
	private static void testSFTP() throws FileNotFoundException {
		JSch jsch = new JSch();
        Session session = null;
        try {
            session = jsch.getSession("netbaseusr", "170.225.233.215", 21);
            session.setConfig("StrictHostKeyChecking", "no");
            session.setPassword("netbaSE123!@#");
            session.connect();

            Channel channel = session.openChannel("ftp");
            channel.connect();
            ChannelSftp sftpChannel = (ChannelSftp) channel;
            File f1 = new File("C:\\netBaseOutput\\prodTest.txt");
            
            sftpChannel.put(new FileInputStream(f1), f1.getName());
            sftpChannel.exit();
            session.disconnect();
        } catch (JSchException e) {
            e.printStackTrace();  
        } catch (SftpException e) {
            e.printStackTrace();
        }
	}
}
