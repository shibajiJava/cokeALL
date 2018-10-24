
package com.ko.cds.pojo.member;

import java.io.Serializable;
import java.math.BigInteger;
import java.sql.Timestamp;

import net.sf.oval.constraint.MaxLength;


public class LegalAcceptances implements Serializable{
	
   	/**
	 * 
	 */
	private static final long serialVersionUID = 108L;
	@MaxLength(value = 100, message = "member.create.violation")
	private String clientId;
   	private Timestamp dateAccepted;
   	private BigInteger ident;
   	private String legalAcceptanceId;
   	@MaxLength(value = 100, message = "member.create.violation")
   	private String type;
   	
   	private BigInteger memberId;
   	private Timestamp insertDtm;
   	private Timestamp updateDtm;
   	private String accepted;
   	
   	public String getAccepted() {
		return accepted;
	}
	public void setAccepted(String accepted) {
		this.accepted = accepted;
	}
	public Timestamp getInsertDtm() {
		return insertDtm;
	}
	public void setInsertDtm(Timestamp insertDtm) {
		this.insertDtm = insertDtm;
	}
	public Timestamp getUpdateDtm() {
		return updateDtm;
	}
	public void setUpdateDtm(Timestamp updateDtm) {
		this.updateDtm = updateDtm;
	}
	BigInteger getIdent() {
		return ident;
	}
	public void setIdent(BigInteger ident) {
		this.ident = ident;
	}
	public BigInteger getMemberId() {
		return memberId;
	}
	public void setMemberId(BigInteger memberId) {
		this.memberId = memberId;
	}
	public String getClientId(){
		return this.clientId;
	}
	public void setClientId(String clientId){
		this.clientId = clientId;
	}
 	public Timestamp getDateAccepted(){
		return this.dateAccepted;
	}
	public void setDateAccepted(Timestamp dateAccepted){
		this.dateAccepted = dateAccepted;
	}
 	public BigInteger getId(){
		return this.ident;
	}
	public void setId(BigInteger ident){
		this.ident = ident;
	}
 	public String getLegalAcceptanceId(){
		return this.legalAcceptanceId;
	}
	public void setLegalAcceptanceId(String legalAcceptanceId){
		this.legalAcceptanceId = legalAcceptanceId;
	}
 	public String getType(){
		return this.type;
	}
	public void setType(String type){
		this.type = type;
	}
}
