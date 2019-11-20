package content;

import javolution.util.FastList;
import javolution.util.FastMap;
import org.apache.ofbiz.base.util.*;
import org.apache.commons.lang.StringUtils;
import org.apache.ofbiz.base.util.Debug;
import org.apache.ofbiz.base.util.UtilGenerics;
import org.apache.ofbiz.base.util.UtilProperties;
import org.apache.ofbiz.base.util.UtilValidate;
import org.apache.ofbiz.entity.*
import org.apache.ofbiz.entity.util.*
import org.apache.ofbiz.entity.GenericValue;
import org.apache.ofbiz.service.LocalDispatcher;
import org.apache.ofbiz.service.ServiceUtil;
import java.io.File;
import com.osafe.util.OsafeAdminUtil;
import java.awt.*;
import java.awt.image.PixelGrabber;
import org.apache.ofbiz.base.util.string.FlexibleStringExpander;
import java.io.FileInputStream;


import java.util.List;


//get List of user_content directories
List<File> fileList = OsafeAdminUtil.getUserContentDirectories();
List<String> directoryNameList = new ArrayList();
osafeProperties = UtilProperties.getResourceBundleMap("OsafeProperties.xml", locale);
if(UtilValidate.isNotEmpty(fileList))
{
	for(File file : fileList)
	{
		directoryNameList.add(file.getName());
	}
}
context.directoryNameList = directoryNameList;

//retrieve media content by search
mediaTypes = new ArrayList();
for(directoryName in directoryNameList)
{
	if(UtilValidate.isNotEmpty(parameters.get(StringUtil.removeSpaces(directoryName))))
	{
		mediaTypes.add(parameters.get(StringUtil.removeSpaces(directoryName)));
	}
}
if (UtilValidate.isEmpty(mediaTypes))
{
    mediaTypes = directoryNameList;
}

mediaName = StringUtils.trimToEmpty(parameters.mediaName);
initializedCB = StringUtils.trimToEmpty(parameters.initializedCB);
preRetrieved = StringUtils.trimToEmpty(parameters.preRetrieved);
if (UtilValidate.isNotEmpty(preRetrieved))
{
   context.preRetrieved=preRetrieved;
}
else
{
  preRetrieved = context.preRetrieved;
}

if (UtilValidate.isNotEmpty(initializedCB))
{
   context.initializedCB=initializedCB;
}
fileListMap = new HashMap();
fileList = new ArrayList();
fileArray = new ArrayList();

//get List of user_content files as map
for (String mediaType: mediaTypes)
{
	fileArrayMediaType = OsafeAdminUtil.getUserContent(mediaType);
	fileArray = fileArray+fileArrayMediaType;
	context.put('view'+mediaType.toLowerCase(), mediaType);
}

//get images size
osafeThemeServerPath = FlexibleStringExpander.expandString(osafeProperties.osafeThemeServer, context);
userContentImagePath = FlexibleStringExpander.expandString(osafeProperties.userContentImagePath, context);
Toolkit tool = Toolkit.getDefaultToolkit();
if(UtilValidate.isNotEmpty(preRetrieved) && preRetrieved != "N") 
{
    for (File file: fileArray)
	{
		Image image = null;
		FileInputStream fis = null;
        try
		{
            parentPath = file.getParent();
            File parentDirPath = new File(parentPath);
            parentDirName = parentDirPath.getName()
            attrMap = new HashMap();
            String fileName = file.getName();
            double filesize = file.length();
            double filesizeInKB = filesize / 1024;
            filesizeInKB = Math.round(filesizeInKB*100)/100.0d;
            userContentPath = osafeThemeServerPath + userContentImagePath + parentDirName+"/";
			fis = new FileInputStream(userContentPath + fileName);
            image = tool.createImage(UtilObject.getBytes(fis));
            PixelGrabber grabber = new PixelGrabber(image,0, 0, -1, -1, false);
            grabber.grabPixels();
            int height = grabber.getHeight();
            int width = grabber.getWidth();
            
            attrMap.put("fileSize",String.valueOf(filesizeInKB));
            attrMap.put("height",height);
            attrMap.put("width",width);
            imagePath = userContentImagePath + parentDirName + "/" + fileName;
            attrMap.put("imagePath",imagePath);
            attrMap.put("parentDirName",parentDirName);
            fileListMap.put(fileName, attrMap);
            if(UtilValidate.isNotEmpty(mediaName))
            {
                //Search By Media Name
                filNameUpper = fileName.toUpperCase();
                mediaNameUpper = mediaName.toUpperCase();
                if(filNameUpper.contains(mediaNameUpper)) 
                {
                    fileList.add(fileName);
                }
            }
            else
            {
                fileList.add(fileName);
            }
        } 
		catch (Exception exc)
		{
            Debug.logError(exc, "MediaConetnt");
        }
		finally
		{
			if(UtilValidate.isNotEmpty(image))
			{
			    image.flush();
			}
			if(UtilValidate.isNotEmpty(fis))
			{
			    fis.close();
			}
		}
    }
}
if(UtilValidate.isNotEmpty(fileListMap)) {
    context.fileListMap = fileListMap;
}
Collections.sort(fileList);
pagingListSize = fileList.size();
context.pagingListSize = pagingListSize;
context.pagingList = fileList;
	
	
	