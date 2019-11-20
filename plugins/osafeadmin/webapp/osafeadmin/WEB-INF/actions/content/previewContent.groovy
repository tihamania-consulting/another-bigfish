package content;

import java.io.IOException;
import java.io.StringWriter;
import java.io.Writer;

import org.apache.ofbiz.base.util.template.FreeMarkerWorker;
import org.apache.ofbiz.base.util.UtilDateTime;
import org.apache.ofbiz.base.util.UtilHttp;
import org.apache.ofbiz.base.util.UtilValidate;

if (UtilValidate.isNotEmpty(parameters.textData)) 
{
    Writer writer = new StringWriter();
    nowTimestampString = UtilHttp.encodeBlanks(UtilDateTime.nowTimestamp().toString());
    try 
    {
        FreeMarkerWorker.renderTemplateFromString(parameters.textData, nowTimestampString, context, writer);
    } 
    catch (IOException ioe) 
    {
        //Debug.logError(ioe, module);
    } catch (Exception exc) {
        //Debug.logError(exc, module);
    }
    writerContent = writer.toString();
    globalContext.writerContent = writerContent;
}
