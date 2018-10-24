package com.ko.cds.dao.janrain;

import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.List;

import javax.sql.DataSource;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.jdbc.core.BatchPreparedStatementSetter;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Component;

import com.ko.cds.pojo.janrainIntegration.CdsoProfileInterest;
import com.ko.cds.pojo.janrainIntegration.CdsoProfileInterestDtl;
import com.ko.cds.pojo.member.Address;
import com.ko.cds.pojo.member.Client;
import com.ko.cds.pojo.member.CommunicationOptIn;
import com.ko.cds.pojo.member.MemberDomainProfile;
import com.ko.cds.pojo.member.MemberIdentifier;
import com.ko.cds.pojo.member.PhoneNumber;
import com.ko.cds.utils.CDSOUtils;
@Component
public class JanrainJDBCTemplate implements IJanrainJDBCTemplate {
	private static final Logger logger = LoggerFactory.getLogger(JanrainJDBCTemplate.class);
	private DataSource dataSource;
	private JdbcTemplate jdbcTemplate;
	private static final String  updateCommunicationOptsJanrainSQL="UPDATE MEMBER_COMMUNICATION_OPT SET OPTED_IN_IND=?, ACCEPTED_DTM=?, TYPE=?, FORMAT=?, SCHEDULE_PREFERENCE=?, CLIENT_ID=?  WHERE MEMBER_ID=? AND COMMUNICATION_TYPE_NAME=?";
	private static final String updateMemberAddressJanrainSQL="UPDATE MEMBER_ADDRESS SET STREET_ADDRESS_1=?, STREET_ADDRESS_2=?, POSTAL_CD=?, COUNTRY_CD=?,	CITY=?, STATE_CD=?,	UPDATE_DTM=CURRENT_TIMESTAMP	WHERE ADDRESS_Type=? and MEMBER_ID=?";
	private static final String updateMemberDomainProfileJanrainSQL="UPDATE MEMBER_DOMAIN_PROFILE SET DOMAIN_NAME = ?, DISPLAY_NAME = ?,	UPDATE_DTM = current_timestamp, IDENTIFIER = ?	WHERE MEMBER_ID = ? and PROFILE_ID = ?";
	private static final String updateExternalIdsJanrainSQL="UPDATE MEMBER_IDENTIFIER SET USER_ID = ?, STATUS_CD= ?, UPDATE_DTM = current_timestamp where MEMBER_ID = ? and DOMAIN_NAME = ?";
	private static final String insertClientForJanrainSQL="insert into CLIENT(MEMBER_ID, CLIENT_ID, IDENT, CHANNEL, FIRST_LOGIN_DTM, LAST_LOGIN_DTM, INSERT_DTM,UPDATE_DTM) values (?, ?, ?, ?, ?, ?, current_timestamp,current_timestamp)";
	private static final String updateClientForJanrainSQL="UPDATE CLIENT SET IDENT = ?,CHANNEL =?,FIRST_LOGIN_DTM = ?,LAST_LOGIN_DTM =  ?,UPDATE_DTM = current_timestamp WHERE MEMBER_ID = ? and CLIENT_ID = ?";
	private static final String deletePhoneNumberForJanrainSQL="DELETE FROM  MEMBER_PHONE_NUMBERS WHERE MEMBER_ID=?";
	private static final String profileInterestInsertForJanrainSQL="INSERT INTO PROFILE_INTEREST (PROFILE_INTEREST_ID,PROFILE_ID, INTEREST_NAME, MEMBER_ID, STATUS,JANRAIN_INTEREST_ID, INSERT_DTM, UPDATE_DTM) VALUES(?,?, ?, ?, ?,?,current_timestamp, current_timestamp)";
	private static final String profileInterestDtlInsertForJanrainSQL="INSERT INTO PROFILE_INTEREST_DTL (PROFILE_INTEREST_ID, INTEREST_VALUE) VALUES(?,?)";
	
	 public void setDataSource(DataSource dataSource) {
	      this.dataSource = dataSource;
	      this.jdbcTemplate = new JdbcTemplate(dataSource);
	   }
	 
	@Override
	public void updateCommunicationOptsJanrain(final List<CommunicationOptIn> janrainCommOptList) {
		logger.debug("updateCommunicationOptsJanrain");
		if(janrainCommOptList == null || janrainCommOptList.size()==0)
			return;
		getJdbcTemplate().batchUpdate(updateCommunicationOptsJanrainSQL, new BatchPreparedStatementSetter() {

		    @Override
		    public void setValues(PreparedStatement ps, int i) throws SQLException {
		    	CommunicationOptIn CommunicationOptIn = janrainCommOptList.get(i);
		        ps.setString(1, CommunicationOptIn.getOptedInIndicator());
		        ps.setDate(2, CDSOUtils.convertToSQLDate(CommunicationOptIn.getAcceptedDate()));
		        ps.setString(3, CommunicationOptIn.getType() );
		        ps.setString(4, CommunicationOptIn.getFormat());
		        ps.setString(5, CommunicationOptIn.getSchedulePreference());
		        ps.setString(6, CommunicationOptIn.getClientId());
		        ps.setString(7, CommunicationOptIn.getMemberId().toString());
		        ps.setString(8, CommunicationOptIn.getCommunicationTypeName());
		    }

		    @Override
		    public int getBatchSize() {
		        return janrainCommOptList.size();
		    }
		  });

		
	}
	
	public JdbcTemplate getJdbcTemplate() {
		return jdbcTemplate;
	}
	public DataSource getDataSource() {
		return dataSource;
	}
	@Override
	public void updateMemberAddressJanrain(final List<Address> addressUpdateList) {
		logger.debug("updateMemberAddressJanrain");
		if(addressUpdateList == null || addressUpdateList.size() ==0)
			return;
		getJdbcTemplate().batchUpdate(updateMemberAddressJanrainSQL, new BatchPreparedStatementSetter() {

		    @Override
		    public void setValues(PreparedStatement ps, int i) throws SQLException {
		    	Address address = addressUpdateList.get(i);
		        ps.setString(1, address.getStreetAddress1());
		        ps.setString(2, address.getStreetAddress2());
		        ps.setString(3, address.getPostalCode() );
		        ps.setString(4, address.getCountry());
		        ps.setString(5, address.getCity());
		        ps.setString(6, address.getState());
		        ps.setString(7, address.getAddressType());
		        ps.setString(8, address.getMemberId().toString());
		    }

		    @Override
		    public int getBatchSize() {
		        return addressUpdateList.size();
		    }
		  });
		
	}
	@Override
	public void updateMemberDomainProfileJanrain(
			final List<MemberDomainProfile> memberProfileUpdateList) {
		logger.debug("updateMemberDomainProfileJanrain");
		if(memberProfileUpdateList == null|| memberProfileUpdateList.size()==0)
			return;
		getJdbcTemplate().batchUpdate(updateMemberDomainProfileJanrainSQL, new BatchPreparedStatementSetter() {

		    @Override
		    public void setValues(PreparedStatement ps, int i) throws SQLException {
		    	MemberDomainProfile memberDomainProfile = memberProfileUpdateList.get(i);
		    	//System.out.println(" DomainNam :"+memberDomainProfile.getDomainName()+ " Display Name :"+memberDomainProfile.getDisplayName()+" memberId :"+memberDomainProfile.getMemberId().toString()+" profileId :"+memberDomainProfile.getProfileId().toString()+ " identifier :"+ memberDomainProfile.getIdentifier());
		        ps.setString(1, memberDomainProfile.getDomainName());
		        ps.setString(2, memberDomainProfile.getDisplayName());
		        ps.setString(3, memberDomainProfile.getIdentifier());
		        ps.setString(4, memberDomainProfile.getMemberId().toString());
		        ps.setString(5, memberDomainProfile.getProfileId().toString());
		       
		    }

		    @Override
		    public int getBatchSize() {
		        return memberProfileUpdateList.size();
		    }
		  });
		
	}
	@Override
	public void updateExternalIdsJanrain(
			final List<MemberIdentifier> memberIdentifierUpdateList) {
		logger.debug("updateExternalIdsJanrain");
		if(memberIdentifierUpdateList == null || memberIdentifierUpdateList.size()==0)
			return;
		getJdbcTemplate().batchUpdate(updateExternalIdsJanrainSQL, new BatchPreparedStatementSetter() {

		    @Override
		    public void setValues(PreparedStatement ps, int i) throws SQLException {
		    	MemberIdentifier memberIdentifier = memberIdentifierUpdateList.get(i);
		        ps.setString(1, memberIdentifier.getUserId());
		        ps.setString(2, memberIdentifier.getStatusCode());
		        ps.setString(3, memberIdentifier.getMemberId().toString());
		        ps.setString(4, memberIdentifier.getDomainName());
		      
		    }

		    @Override
		    public int getBatchSize() {
		        return memberIdentifierUpdateList.size();
		    }
		  });
	}
	@Override
	public void insertClientForJanrain(final List<Client> janrainInsertClientList) {
		logger.debug("insertClientForJanrain");
		if(janrainInsertClientList == null || janrainInsertClientList.size() == 0)
			return;
		getJdbcTemplate().batchUpdate(insertClientForJanrainSQL, new BatchPreparedStatementSetter() {

		    @Override
		    public void setValues(PreparedStatement ps, int i) throws SQLException {
		    	Client client = janrainInsertClientList.get(i);
		        ps.setString(1, client.getMemberId().toString());
		        ps.setString(2, client.getClientId());
		        if( client.getIdent() != null){
		        	ps.setString(3, client.getIdent().toString() );
		        }else{
		        	ps.setString(3, null );
		        }
		        ps.setString(4, client.getChannel());
		        ps.setDate(5, CDSOUtils.convertToSQLDate(client.getFirstLoginDtm()));
		        ps.setDate(6, CDSOUtils.convertToSQLDate(client.getLastLoginDtm()));
		       
		    }

		    @Override
		    public int getBatchSize() {
		        return janrainInsertClientList.size();
		    }
		  });

		
	}
	@Override
	public void updateClientForJanrain(final List<Client> janrainUpdateClientList) {
		logger.debug("updateClientForJanrainSQL");
		if(janrainUpdateClientList == null || janrainUpdateClientList.size() == 0)
			return;
		getJdbcTemplate().batchUpdate(updateClientForJanrainSQL, new BatchPreparedStatementSetter() {

		    @Override
		    public void setValues(PreparedStatement ps, int i) throws SQLException {
		    	Client client = janrainUpdateClientList.get(i);
		        
		        if( client.getIdent() != null){
		        	ps.setString(1, client.getIdent().toString() );
		        }else{
		        	ps.setString(1, null );
		        }
		        ps.setString(2, client.getChannel());
		        ps.setDate(3, CDSOUtils.convertToSQLDate(client.getFirstLoginDtm()));
		        ps.setDate(4, CDSOUtils.convertToSQLDate(client.getLastLoginDtm()));
		        ps.setString(5, client.getMemberId().toString());
		        ps.setString(6, client.getClientId());
		        
		       
		    }

		    @Override
		    public int getBatchSize() {
		        return janrainUpdateClientList.size();
		    }
		  });
		
	}

	@Override
	public void deletePhoneNumberForJanrain(final List<PhoneNumber> phoneNumberList) {
		logger.debug("deletePhoneNumberForJanrain");
		if(phoneNumberList == null || phoneNumberList.size() ==0)
			return;
		getJdbcTemplate().batchUpdate(deletePhoneNumberForJanrainSQL, new BatchPreparedStatementSetter() {

		    @Override
		    public void setValues(PreparedStatement ps, int i) throws SQLException {
		    	PhoneNumber phoneNumber = phoneNumberList.get(i);
		       
		        ps.setString(1, phoneNumber.getMemberId().toString());
		    }

		    @Override
		    public int getBatchSize() {
		        return phoneNumberList.size();
		    }
		  });
		
	}

	@Override
	public void insertProfileInterestForJanrain(
			final List<CdsoProfileInterest> profileInterestList) {
		logger.debug("insertProfileInterestForJanrain");
		if(profileInterestList == null || profileInterestList.size() ==0)
			return;
		//System.out.println("profileInterestList.size() :"+profileInterestList.size());
		getJdbcTemplate().batchUpdate(profileInterestInsertForJanrainSQL, new BatchPreparedStatementSetter() {

		    @Override
		    public void setValues(PreparedStatement ps, int i) throws SQLException {
		    	CdsoProfileInterest cdsoProfileInterest = profileInterestList.get(i);
		    	//logger.debug("insertProfileInterestForJanrain batch element #"+i+"profileInterestId :"+cdsoProfileInterest.getPrifileInterestId()+" profileId: "+cdsoProfileInterest.getProfileId()+" InterestName :"+cdsoProfileInterest.getInterestName()+" memberId: "+cdsoProfileInterest.getMemberId());
		    	//System.out.println("insertProfileInterestForJanrain batch element #"+i+"profileInterestId :"+cdsoProfileInterest.getPrifileInterestId()+" profileId: "+cdsoProfileInterest.getProfileId()+" InterestName :"+cdsoProfileInterest.getInterestName()+" memberId: "+cdsoProfileInterest.getMemberId());
		    	ps.setString(1, cdsoProfileInterest.getPrifileInterestId().toString());
		        ps.setString(2, cdsoProfileInterest.getProfileId().toString());
		        ps.setString(3, cdsoProfileInterest.getInterestName());
		        ps.setString(4, cdsoProfileInterest.getMemberId().toString());
		        ps.setString(5, cdsoProfileInterest.getStatus());
		        if(cdsoProfileInterest.getJanrainInterestId() != null){
		        	ps.setString(6, cdsoProfileInterest.getJanrainInterestId().toString());
		        }else{
		        	ps.setString(6, null);
		        }
		        
		        
		    }

		    @Override
		    public int getBatchSize() {
		        return profileInterestList.size();
		    }
		  });
		
	}

	@Override
	public void insertProfileInterestDtlForJanrain(
			final List<CdsoProfileInterestDtl> profileInterestDtls) {
		logger.debug("insertProfileInterestDtlForJanrain");
		if(profileInterestDtls == null || profileInterestDtls.size() ==0)
			return;
		//System.out.println("profileInterestDtls.size() :"+profileInterestDtls.size());
		getJdbcTemplate().batchUpdate(profileInterestDtlInsertForJanrainSQL, new BatchPreparedStatementSetter() {

		    @Override
		    public void setValues(PreparedStatement ps, int i) throws SQLException {
		    	CdsoProfileInterestDtl cdsoProfileInterestDtl = profileInterestDtls.get(i);
		    	//logger.debug("insertProfileInterestDtlForJanrain batch# "+i+" profileInterestId :"+cdsoProfileInterestDtl.getProfileInteretId() + " interest Value :"+ cdsoProfileInterestDtl.getInterestValue());
		    	//System.out.println("insertProfileInterestDtlForJanrain batch# "+i+" profileInterestId :"+cdsoProfileInterestDtl.getProfileInteretId() + " interest Value :"+ cdsoProfileInterestDtl.getInterestValue());
		        ps.setString(1, cdsoProfileInterestDtl.getProfileInteretId().toString());
		        ps.setString(2, cdsoProfileInterestDtl.getInterestValue());
		        		        
		    }

		    @Override
		    public int getBatchSize() {
		    	
		        return profileInterestDtls.size();
		    }
		  });
		
	}
	

}
