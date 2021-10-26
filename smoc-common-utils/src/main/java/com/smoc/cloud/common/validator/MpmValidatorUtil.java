package com.smoc.cloud.common.validator;

import javax.validation.ConstraintViolation;
import javax.validation.Validation;
import javax.validation.Validator;
import javax.validation.groups.Default;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;

/**
 * validator 手动验证规则
 * <p>
 * Description:对象规则验证 在get 提交的时候使用，post提交 直接用 @Validated
 * </p>
 * 2019/3/29 14:29
 */
public class MpmValidatorUtil {

	private static Validator validator = Validation.buildDefaultValidatorFactory().getValidator();

	/**
	 * 验证validator 是否有错误 返回true 或 false
	 * 
	 * @param obj
	 * @return
	 */
	public static <T> boolean validate(T obj) {

		boolean errorMessage = true;

		Set<ConstraintViolation<T>> set = validator.validate(obj, Default.class);
		if (set != null && set.size() > 0) {
			return false;
		}

		return errorMessage;
	}

	/**
	 * 验证validator 返回错误信息 map
	 * 
	 * @param obj
	 * @return
	 */
	public static <T> String validateMessage(T obj) {

        String result = new String();
		Set<ConstraintViolation<T>> set = validator.validate(obj, Default.class);
		if (set != null && set.size() > 0) {
			for (ConstraintViolation<T> cv : set) {
				result = cv.getMessage();
				break;
			}
		}
		return result;
	}

}
