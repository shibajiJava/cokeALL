package com.ko.cds.dao.member;

import java.math.BigInteger;
import java.util.List;

import org.springframework.stereotype.Component;

import com.ko.cds.pojo.csr.CSRMemberInfo;
import com.ko.cds.pojo.janrainIntegration.CdsoProfileInterest;
import com.ko.cds.pojo.member.Address;
import com.ko.cds.pojo.member.Client;
import com.ko.cds.pojo.member.Email;
import com.ko.cds.pojo.member.LegalAcceptances;
import com.ko.cds.pojo.member.MemberDomainProfile;
import com.ko.cds.pojo.member.MemberIdentifier;
import com.ko.cds.pojo.member.MemberInfo;
import com.ko.cds.pojo.member.MemberSocialDomain;
import com.ko.cds.pojo.member.MergeMemberInfo;
import com.ko.cds.pojo.member.MergedMember;
import com.ko.cds.pojo.member.PhoneNumber;
import com.ko.cds.pojo.member.SMSNumber;
import com.ko.cds.request.csr.CSRAdvanceSearchRequest;
import com.ko.cds.request.csr.DeleteMemberRequest;
import com.ko.cds.request.member.MemberSearchRequest;
import com.ko.cds.request.member.MemberSearchRequestV2;
import com.ko.cds.request.member.MergeMemberRequest;
import com.ko.cds.request.member.UpdateMemberRequest;
import com.ko.cds.response.member.MergeMemberResponse;

@Component
public interface MemberDAO {
	public MemberInfo searchMemberInfo(MemberSearchRequest memberSearchRequest);
	public void insertMemberInfo(MemberInfo memberInfo);
	public void insertEmail(Email email);
	public void insertAddress(Address address);
	public void insertPhoneNumber(PhoneNumber phoneNumber );
	public void insertSMSNumber(SMSNumber smsNumber);
	public void insertSMSNumberForMerge(SMSNumber smsNumber);
	public void insertMemberIdentifier(MemberIdentifier memberIdentifier);
	public MergeMemberResponse mergeMembers(MergeMemberRequest mergeMemberRequest);
	public SMSNumber fetchSMSNumber(SMSNumber smsNumber);
	public List<MemberIdentifier> fetchIdentifier(MemberIdentifier memberIdentifier);
	public Email fetchEmail(Email email);
	public void updateStatusOfSMSNUmber(String statusCode, BigInteger fromMemberId);
	public int updateMemberStatus(String statusCode,BigInteger fromMemberId);
	public MemberInfo getMemberInfo(BigInteger memberId);
	public void updateStatusOfEmail(String statusCode, BigInteger fromMemberId);
	//public void updateStatusOfOpts(String statusCode, BigInteger fromMemberId);
	public void updateStatusOfIdentifier(String statusCode, BigInteger fromMemberId);
	public void updateSMSNumber(SMSNumber smsNumber);
	public void updateMemberAddress(Address address);
	public Email getEmailAddressByMemId(BigInteger memberID);
	public void updateEmailAddress(Email email);
	public void updateMemberInfo(MemberInfo memberInfo);
	public void updateMemberIdentifier(MemberIdentifier memberIdentifier);
	public MemberInfo getmemberInfoWithUUID(String janrainUUID);
	public void insertMergedMember(MergedMember mergedMember);
	public MemberInfo getMemberUUIDbyMemID(BigInteger memberID);
	public MergeMemberInfo getmemberInfoForMerge(BigInteger memberId);
	public void updateSMSNumberForMember(UpdateMemberRequest updateMemberRequest);
	public void updateExternalIDForMember(MemberIdentifier memberIdentifier);
	public SMSNumber fetchSMSNumberBySMSTypeAndMember(SMSNumber smsNumber);
	public MemberIdentifier fetchIdentifierByDomainAndMember(MemberIdentifier memberIdentifier);
	public SMSNumber findBySMSNumber(int smsNumber);
	public MemberIdentifier findByExternalId(String externalId);
	public void deleteFromSMSNumberForUpdateMember(SMSNumber smsNumber);
	public void deleteFromMemberIdentifierForUpdateMember(MemberIdentifier memberIdentifier);
	public MemberInfo getmemberInfoWithUUIDForJanrainSync(String UUID);
	public void insertClient(Client client);
	public void updateClient(Client client);
	public void insertProfile(MemberDomainProfile profile);
	public void insertProfileForJanrainBatch(MemberDomainProfile profile);
	
	
	public void updateProfile(MemberDomainProfile profile);
	public void insertLegalAcceptance(LegalAcceptances legalAcceptance);
	public void updateLegalAcceptance(LegalAcceptances legalAcceptance);

	public List<CSRMemberInfo> csrAdvanceSearch(CSRAdvanceSearchRequest csrAdvanceSearchRequest);
	public int insertMemberDeletionReason(DeleteMemberRequest request);
	public int getMemberStatusFromDeleteTable(BigInteger memberId);

	public void insertEmailAddress(Email email);
	public void updateMemberInfoForJanrain(MemberInfo memberInfo);
	public void inserUpdateProfileInterest(CdsoProfileInterest allCdsoProfileInterest);
	public BigInteger getProfileInterestId(CdsoProfileInterest allCdsoProfileInterest);
	public void insertProfileInterestDtl(CdsoProfileInterest allCdsoProfileInterest);

	public void deleteFromProfileInterestDTLForJanrain(CdsoProfileInterest allCdsoProfileInterest);
	
	
	public void deleteProfileInterestForJanrain(CdsoProfileInterest allCdsoProfileInterest);
	public void phoneNumberDelete(PhoneNumber phoneNumber);
	public void insertPhoneNumberForJanrain(List<PhoneNumber> phoneNumberList );
	public void insertAddressJanrain(Address address);
	public void insertMemberIdentifierJanrain(MemberIdentifier memberIdentifier);
	//public void inserUpdateProfileInterest(@Param("allCdsoProfileInterest")List<CdsoProfileInterest> allCdsoProfileInterest);
	
	public int getMemberCount(String uuid);
	public BigInteger getMemberIdbyJanrainUUID(String janrainUUID);
	
	
	public int updateMemberSMSStatus(String statusCode,BigInteger fromMemberId);
	public int updateMemberIdentifierStatus(String statusCode,BigInteger fromMemberId);
	
	public int deleteMemberFromSMSNUmber(BigInteger fromMemberId);
	public int deleteMemberFromEmail(BigInteger fromMemberId);
	public int deleteMemberFromIdentifier(BigInteger fromMemberId);
	public int deleteMemberFromMember(BigInteger fromMemberId);
	
	public void insertMemberSocialData(MemberSocialDomain socialDomain);
	public int updateMemberSocialDomain(MemberSocialDomain socialDomain);
	public int deleteMemberSocialDomain(MemberSocialDomain socialDomain);
	
	public int updateMemberSocialDomainForMember(MemberSocialDomain socialDomain);
	public List<MemberInfo> searchMemberInfoV2WithSocialSearch(MemberSearchRequestV2 memberSearchRequest);
	public List<MemberInfo> searchMemberInfoV2WithOutSocialSearch(MemberSearchRequestV2 memberSearchRequest);
}
