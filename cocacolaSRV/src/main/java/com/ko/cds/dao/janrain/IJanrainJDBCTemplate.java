package com.ko.cds.dao.janrain;

import java.util.List;

import org.springframework.stereotype.Component;

import com.ko.cds.pojo.janrainIntegration.CdsoProfileInterest;
import com.ko.cds.pojo.janrainIntegration.CdsoProfileInterestDtl;
import com.ko.cds.pojo.member.Address;
import com.ko.cds.pojo.member.Client;
import com.ko.cds.pojo.member.CommunicationOptIn;
import com.ko.cds.pojo.member.MemberDomainProfile;
import com.ko.cds.pojo.member.MemberIdentifier;
import com.ko.cds.pojo.member.PhoneNumber;

@Component
public interface IJanrainJDBCTemplate {
	public void updateCommunicationOptsJanrain(final List<CommunicationOptIn> janrainCommOptList);
	public void updateMemberAddressJanrain(final List<Address> addressUpdateList);
	public void updateMemberDomainProfileJanrain(final List<MemberDomainProfile> memberProfileUpdateList);
	public void updateExternalIdsJanrain(final List<MemberIdentifier> memberIdentifierUpdateList);
	public void insertClientForJanrain(final List<Client> janrainInsertClientList);
	public void updateClientForJanrain(final List<Client> janrainUpdateClientList);
	public void deletePhoneNumberForJanrain(final List<PhoneNumber> phoneNumberList);
	public void insertProfileInterestForJanrain(List<CdsoProfileInterest> profileInterestList);
	public void insertProfileInterestDtlForJanrain(List<CdsoProfileInterestDtl> profileInterestDtls);
}
