package product;

import org.apache.commons.lang.StringUtils;
import org.apache.ofbiz.base.util.Debug;
import org.apache.ofbiz.base.util.UtilProperties;
import org.apache.ofbiz.order.order.OrderReadHelper;
import org.apache.ofbiz.party.contact.ContactHelper;
import org.apache.ofbiz.base.util.UtilMisc;
import org.apache.ofbiz.entity.GenericValue;
import org.apache.ofbiz.entity.condition.EntityCondition;
import org.apache.ofbiz.entity.condition.EntityOperator;
import org.apache.ofbiz.entity.util.EntityUtil;
import org.apache.ofbiz.base.util.UtilDateTime;

userLogin = session.getAttribute("userLogin");
context.userLogin=userLogin;
productReviewId = StringUtils.trimToEmpty(parameters.productReviewId);
if(productReviewId){
    context.productReviewId=productReviewId;
    review = delegator.findOne("ProductReview", [productReviewId : productReviewId], false);
    context.review=review;
    postedInterval = UtilDateTime.getIntervalInDays(UtilDateTime.getDayStart(review.postedDateTime), UtilDateTime.getDayEnd(UtilDateTime.nowTimestamp()));
    context.postedInterval=postedInterval;
}