package com.stubhub.domain.account.biz.intf;

import java.util.List;
import java.util.Map;

public interface CSStubTransFlagBO {

	public Map<String, Boolean> getCSStubTransFlag(List<String> orderIds);

}
