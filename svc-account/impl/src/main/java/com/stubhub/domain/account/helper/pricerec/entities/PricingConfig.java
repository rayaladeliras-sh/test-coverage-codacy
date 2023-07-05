package com.stubhub.domain.account.helper.pricerec.entities;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;

import org.codehaus.jackson.annotate.JsonIgnoreProperties;
import org.codehaus.jackson.map.annotate.JsonSerialize;

@XmlAccessorType(XmlAccessType.FIELD)
@JsonSerialize(include = JsonSerialize.Inclusion.NON_NULL)
@JsonIgnoreProperties(ignoreUnknown = true)
public class PricingConfig {
	 private String algorithm;
	  private Integer listingKeepPercent;
	  private Integer transactionKeepPercent;
	  private Integer minTransaction;
	  private Double lowerPct;
	  private Double medianPct;
	  private Double upperPct;
	  private Integer minQualityScore;
	  
	  public String getAlgorithm() { return this.algorithm; }


	  
	  public void setAlgorithm(String algorithm) { this.algorithm = algorithm; }


	  
	  public Integer getListingKeepPercent() { return this.listingKeepPercent; }


	  
	  public void setListingKeepPercent(Integer listingKeepPercent) { this.listingKeepPercent = listingKeepPercent; }


	  
	  public Integer getTransactionKeepPercent() { return this.transactionKeepPercent; }


	  
	  public void setTransactionKeepPercent(Integer transactionKeepPercent) { this.transactionKeepPercent = transactionKeepPercent; }


	  
	  public Integer getMinTransaction() { return this.minTransaction; }


	  
	  public void setMinTransaction(Integer minTransaction) { this.minTransaction = minTransaction; }


	  
	  public Double getLowerPct() { return this.lowerPct; }


	  
	  public void setLowerPct(Double lowerPct) { this.lowerPct = lowerPct; }


	  
	  public Double getMedianPct() { return this.medianPct; }


	  
	  public void setMedianPct(Double medianPct) { this.medianPct = medianPct; }


	  
	  public Double getUpperPct() { return this.upperPct; }


	  
	  public void setUpperPct(Double upperPct) { this.upperPct = upperPct; }


	  
	  public Integer getMinQualityScore() { return this.minQualityScore; }


	  
	  public void setMinQualityScore(Integer minQualityScore) { this.minQualityScore = minQualityScore; }
	}