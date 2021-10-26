package com.smoc.cloud.common.validator;

import javax.validation.constraints.*;

/**
 * ID Validator字段规则验证
 * <p>
 * Description:验证平台ID 规则 对于要验证多字段的表单可以继承该Validator，用来验证get提交参数
 * </p>
 * 2019/3/29 14:29
 */
public class MpmIdValidator {

	@NotNull(message = "ID不能为空！")
	@Pattern(regexp = "^[0-9A-Za-z]{1,32}", message = "ID不符合规则！")
	@Size(min = 1, max = 32, message = "ID长度不符合规则！")
	public String id;

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

}
