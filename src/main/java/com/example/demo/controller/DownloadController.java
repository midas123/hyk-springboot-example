package com.example.demo.controller;

import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.URLConnection;
import java.util.Base64;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.http.HttpHeaders;
import org.springframework.stereotype.Controller;
import org.springframework.util.FileCopyUtils;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class DownloadController {
	@GetMapping("/downloadfile")
	public void downloadFile(HttpServletRequest request, HttpServletResponse response) {
		
		String dir = System.getProperty("user.dir");
		String folder = "\\file\\";
		String fileName = "파일명.exe";
		File file = new File(dir+folder+fileName);
		byte[] encodeFileName = Base64.getEncoder().encode(fileName.getBytes());
		
		String mimeType = URLConnection.guessContentTypeFromName(file.getName());
		if (mimeType == null || mimeType.length() == 0) {
			mimeType = "application/octet-stream";
		}
		
		response.setContentType(mimeType);
		response.setContentLength((int) file.length());
		response.setHeader(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=\""+ new String(encodeFileName) +"\"");
		
		InputStream is = null;
		try {
			is = new FileInputStream(file);
			BufferedInputStream buf = new BufferedInputStream(is);
	        FileCopyUtils.copy(buf, response.getOutputStream());
	    } catch (IOException ioe) {
	        //throw new ServletException(ioe.getMessage());
	    } catch (Exception ex) {
	    }
	}
}
