package com.stubhub.domain.account.helper.pricerec.entities;

import java.util.ArrayList;
import java.util.List;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlElementWrapper;
import javax.xml.bind.annotation.XmlRootElement;

import org.apache.commons.collections.CollectionUtils;
import org.codehaus.jackson.annotate.JsonIgnoreProperties;
import org.codehaus.jackson.map.annotate.JsonSerialize;

@XmlAccessorType(XmlAccessType.FIELD)
@XmlRootElement(name = "response")
@JsonSerialize(include = JsonSerialize.Inclusion.NON_DEFAULT)
@JsonIgnoreProperties(ignoreUnknown = true)
public class PricingRecResponse implements RecResponse {

	@XmlElementWrapper(name = "pricingRecommendations")
	@XmlElement(name = "pricingRecommendation")
	private List<PricingRec> pricingRecommendations = new ArrayList();


	private PricingConfig pricingConfig;


	private String impressionToken;


	public List<PricingRec> getPricingRecommendations() { return this.pricingRecommendations; }



	public void setPricingRecommendations(List<PricingRec> pricingRecommendations) { this.pricingRecommendations = pricingRecommendations; }



	public PricingConfig getPricingConfig() { return this.pricingConfig; }



	public void setPricingConfig(PricingConfig pricingConfig) { this.pricingConfig = pricingConfig; }



	public String getImpressionToken() { return this.impressionToken; }



	public void setImpressionToken(String impressionToken) { this.impressionToken = impressionToken; }
}
