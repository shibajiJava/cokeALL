package com.ko.cds.dao.codeRedemption;

import java.util.List;

import org.springframework.stereotype.Component;

import com.ko.cds.pojo.csr.CodeRedemptionHistory;
import com.ko.cds.request.csr.CodeRedemptionHistoryRequest;

/**
 * DAO Interface for the Code Redemption APIs
 * @author ibm
 *
 */
@Component
public interface CodeRedemptionDAO {
	
	public List<CodeRedemptionHistory> getCodeRedemptionInfo(CodeRedemptionHistoryRequest request);

}
