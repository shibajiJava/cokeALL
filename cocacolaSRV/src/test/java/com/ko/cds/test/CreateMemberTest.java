package com.ko.cds.test;

import java.math.BigInteger;
import java.util.ArrayList;
import java.util.List;

import junit.framework.Assert;

import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import com.ko.cds.dao.member.MemberDAO;
import com.ko.cds.exceptions.BadRequestException;
import com.ko.cds.pojo.member.MemberInfo;
import com.ko.cds.pojo.member.MemberSocialDomain;
import com.ko.cds.service.helper.MemberServiceHelper;


@Component
@Transactional
public class CreateMemberTest extends CDSTest{

	
	@Autowired
	private MemberServiceHelper serviceHelper;
	
	@Autowired
	private MemberDAO memberDAO;
	
	@Test
	public void createSocialDomainData() throws BadRequestException {
		MemberSocialDomain socialDomain1 = new MemberSocialDomain();
		socialDomain1.setDomain("Twitter");
		socialDomain1.setIdentifier("http://uiuijkm");
		socialDomain1.setUserName("aaaaa");
		MemberInfo req = new  MemberInfo();
		
		List<MemberSocialDomain> socialDomain = new ArrayList<MemberSocialDomain>();
		socialDomain.add(socialDomain1);
		req.setSocialDomain(socialDomain);
		BigInteger info = serviceHelper.createMemberInfo(req);
		System.out.println(info);
		Assert.assertNotNull(info);
	}
	
	@Test
	public void createSocialDomainDataWithUUID() {
		MemberSocialDomain socialDomain1 = new MemberSocialDomain();
		socialDomain1.setDomain("Twitter");
		socialDomain1.setIdentifier("http://uiuijkm");
		socialDomain1.setUserName("aaaaa");
		MemberInfo req = new  MemberInfo();
		
		List<MemberSocialDomain> socialDomain = new ArrayList<MemberSocialDomain>();
		socialDomain.add(socialDomain1);
		req.setSocialDomain(socialDomain);
		req.setUuid("89787jnkjk989");
		BigInteger info;
		try {
			info = serviceHelper.createMemberInfo(req);
		} catch (BadRequestException e) {
			// TODO Auto-generated catch block
			Assert.assertEquals(e.getMessage(), "Social Domain Member Creation is allowed for Lite Account only.");
		}
	}
	
	

}
