/**
 * 
 */
package com.mulesoft.services.eu;

import java.util.Vector;
import java.io.*;

import org.mule.api.MuleMessage;
import org.mule.api.transformer.TransformerException;
import org.mule.transformer.AbstractMessageTransformer;

/**
 * @author andreasgrimm
 *
 */
public class FileReader extends AbstractMessageTransformer  {

	/**
	 * 
	 */
	public FileReader() {
	}

	private Vector<String> getListOfFiles(String fileName) {
		Vector<String> returnVector = new Vector<String>(0,1);
		
		String directory = fileName.substring(0, fileName.lastIndexOf('/'));
		String fileMask = fileName.substring(fileName.lastIndexOf('/') + 1, fileName.length());
		String id = fileMask.substring(0,fileMask.indexOf('.'));

		System.out.println(fileMask);
		System.out.println(id);
		
		File[] files = new File(directory).listFiles();

		if (files != null) {
			for (File file : files) {
			    if (file.isFile()) {
					if (file.getName().contains(id)) {
						returnVector.add(directory + "/" + file.getName());
					}
			    }
			}		
		}
		return(returnVector);
	}
	
	private String read (String fileName) {
		String csvContent = new String();
		String thisLine = null;
		
		try {
			BufferedReader bufferedReader = new BufferedReader(new java.io.FileReader(fileName));
	        while ((thisLine = bufferedReader.readLine()) != null) {
	        	if (csvContent.length() > 1)
	        		csvContent += "\n";
	        	csvContent += thisLine;
	        }
	        bufferedReader.close();

		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}

		return (csvContent);
	}
	
	@Override
	public Object transformMessage(MuleMessage message, String outputEncoding) throws TransformerException {
		String stringMessage = message.getPayload().toString();
		String stringReturnMessage = new String();

		Vector<String> listOfFiles = getListOfFiles(stringMessage);
		
		System.out.println("Found Files:");
		
		for (int x = 0; x < listOfFiles.size(); x++) {
			System.out.println(listOfFiles.get(x));
			if (stringReturnMessage.length() > 1)
				stringReturnMessage += "\n";
			stringReturnMessage += read(listOfFiles.get(x));
		}
		
		return stringReturnMessage;
	}

}
