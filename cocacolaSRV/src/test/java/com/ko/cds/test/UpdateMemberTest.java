package com.ko.cds.test;

import java.math.BigInteger;

import junit.framework.Assert;

import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import com.ko.cds.dao.member.MemberDAO;
import com.ko.cds.exceptions.BadRequestException;
import com.ko.cds.pojo.member.MemberSocialDomain;
import com.ko.cds.request.member.UpdateMemberRequest;
import com.ko.cds.service.helper.MemberServiceHelper;

@Component
@Transactional
public class UpdateMemberTest extends CDSTest{

	
	@Autowired
	private MemberServiceHelper serviceHelper;
	
	@Autowired
	private MemberDAO memberDAO;
	
	@Test
	public void deleteSocialDomainData() throws BadRequestException {
		MemberSocialDomain socialDomain1 = new MemberSocialDomain();
		socialDomain1.setDomain("Twitter");
		socialDomain1.setIdentifier("http://uiuijkm");
		socialDomain1.setUserName("");

		UpdateMemberRequest request = new UpdateMemberRequest();
		request.setMemberId(new BigInteger("34190"));
		request.setSocialDomain(socialDomain1);
		
			serviceHelper.updateMember(request);
			// TODO Auto-generated catch block
			Assert.assertEquals("1","1");
	}
	
	@Test
	public void socialDomainDataNULL() {
		MemberSocialDomain socialDomain1 = new MemberSocialDomain();
		socialDomain1.setDomain(null);
		socialDomain1.setIdentifier("http://uiuijkm");
		socialDomain1.setUserName("7899");

		UpdateMemberRequest request = new UpdateMemberRequest();
		request.setMemberId(new BigInteger("34190"));
		request.setSocialDomain(socialDomain1);
		
		try {
			serviceHelper.updateMember(request);
		} catch (BadRequestException e) {
			// TODO Auto-generated catch block
			Assert.assertEquals(e.getMessage(), "Domain in Social Domain is null");
		}
	}
	
	@Test
	public void createSocialDomainDataWithUUID() {
		

		UpdateMemberRequest request = new UpdateMemberRequest();
		request.setMemberId(new BigInteger("34190"));
		
		try {
			serviceHelper.updateMember(request);
		} catch (BadRequestException e) {
			// TODO Auto-generated catch block
			Assert.assertEquals(e.getMessage(), "SMSObject and MemberIdentifiers and Social Domain both are null");
		}
	}
	
	

}
