package common;

import java.util.HashMap;
import org.apache.ofbiz.party.contact.ContactMechWorker;
import org.apache.ofbiz.base.util.UtilHttp;
import org.apache.ofbiz.base.util.UtilMisc;
import org.apache.ofbiz.entity.util.EntityUtil;
import org.apache.ofbiz.base.util.UtilValidate;

contactMechPurposeTypeId =  parameters.contactMechPurposeTypeId;
partyContactMechPurposes = context.partyContactMechPurposes;
if (UtilValidate.isNotEmpty(partyContactMechPurposes))
{
	partyContactMechPurpose = EntityUtil.getFirst(partyContactMechPurposes);
	contactMechPurposeTypeId = partyContactMechPurpose.contactMechPurposeTypeId;
}
context.purposeType = contactMechPurposeTypeId;
