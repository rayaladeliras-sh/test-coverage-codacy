package com.stubhub.domain.account.helper.dto;

import org.codehaus.jackson.map.annotate.JsonSerialize;
import org.springframework.util.CollectionUtils;

import javax.xml.bind.annotation.*;
import java.util.ArrayList;
import java.util.List;

@XmlAccessorType(XmlAccessType.FIELD)
@XmlRootElement(name = "response")
@XmlType(propOrder = { "seatQualityScore", "bestValueScore", "sectionCoefficient", "rowCoefficient",
		"priceCoefficient", "coefficientVersion", "deliverTypeCoefficient" })
@JsonSerialize(include = JsonSerialize.Inclusion.NON_DEFAULT)
public class TicketScoreRecResponse implements RecResponse {

	private String impressionToken;
	private List<ListingScore> scores;

	public String getImpressionToken() {
		return impressionToken;
	}

	public void setImpressionToken(String impressionToken) {
		this.impressionToken = impressionToken;
	}

	public static class ListingScore {
		@XmlElement(name = "seatQualityScore")
		private Double seatQualityScore;

		@XmlElement(name = "bestValueScore")
		private Double bestValueScore;

		private Double sectionCoefficient;

		private Double rowCoefficient;

		private Double priceCoefficient;

		private Integer coefficientVersion;

		private Double deliverTypeCoefficient;

		private Double price;

		private Long eventId;

		private Long listingId;

		private Long sectionId;
		
		private String row;
		
		private String seats;
		
		private List<Integer> deliveryTypeIds;

		public Long getSectionId() {
			return sectionId;
		}

		public void setSectionId(Long sectionId) {
			this.sectionId = sectionId;
		}

		public String getRow() {
			return row;
		}

		public void setRow(String row) {
			this.row = row;
		}

		public String getSeats() {
			return seats;
		}

		public void setSeats(String seats) {
			this.seats = seats;
		}

		public List<Integer> getDeliveryTypeIds() {
			if(CollectionUtils.isEmpty(deliveryTypeIds)) {
				return new ArrayList<Integer>();
			} else {
				return new ArrayList<Integer>(deliveryTypeIds);
			}
		}

		public void setDeliveryTypeIds(List<Integer> deliveryTypeIds) {
			if(CollectionUtils.isEmpty(deliveryTypeIds)) {
				this.deliveryTypeIds = new ArrayList<Integer>();				
			} else {
				this.deliveryTypeIds = new ArrayList<Integer>(deliveryTypeIds);
			}			
		}		

		public Long getListingId() {
			return listingId;
		}

		public void setListingId(Long listingId) {
			this.listingId = listingId;
		}

		public Long getEventId() {
			return eventId;
		}

		public void setEventId(Long eventId) {
			this.eventId = eventId;
		}

		public Double getSeatQualityScore() {
			return seatQualityScore;
		}

		public void setSeatQualityScore(Double seatQualityScore) {
			this.seatQualityScore = seatQualityScore;
		}

		public Double getBestValueScore() {
			return bestValueScore;
		}

		public void setBestValueScore(Double bestValueScore) {
			this.bestValueScore = bestValueScore;
		}

		public Double getSectionCoefficient() {
			return sectionCoefficient;
		}

		public void setSectionCoefficient(Double sectionCoefficient) {
			this.sectionCoefficient = sectionCoefficient;
		}

		public Double getRowCoefficient() {
			return rowCoefficient;
		}

		public void setRowCoefficient(Double rowCoefficient) {
			this.rowCoefficient = rowCoefficient;
		}

		public Double getPriceCoefficient() {
			return priceCoefficient;
		}

		public void setPriceCoefficient(Double priceCoefficient) {
			this.priceCoefficient = priceCoefficient;
		}

		public Integer getCoefficientVersion() {
			return coefficientVersion;
		}

		public void setCoefficientVersion(Integer coefficientVersion) {
			this.coefficientVersion = coefficientVersion;
		}

		public Double getDeliverTypeCoefficient() {
			return deliverTypeCoefficient;
		}

		public void setDeliverTypeCoefficient(Double deliverTypeCoefficient) {
			this.deliverTypeCoefficient = deliverTypeCoefficient;
		}

		public Double getPrice() {
			return price;
		}

		public void setPrice(Double price) {
			this.price = price;
		}

		@Override
		public String toString() {
			return "ListingScore{" +
					"seatQualityScore=" + seatQualityScore +
					", bestValueScore=" + bestValueScore +
					", sectionCoefficient=" + sectionCoefficient +
					", rowCoefficient=" + rowCoefficient +
					", priceCoefficient=" + priceCoefficient +
					", coefficientVersion=" + coefficientVersion +
					", deliverTypeCoefficient=" + deliverTypeCoefficient +
					", price=" + price +
					", eventId=" + eventId +
					", listingId=" + listingId +
					", sectionId=" + sectionId +
					", row='" + row + '\'' +
					", seats='" + seats + '\'' +
					", deliveryTypeIds=" + deliveryTypeIds +
					'}';
		}
	}

	public List<ListingScore> getScores() {
		if (CollectionUtils.isEmpty(scores)) {
			return new ArrayList<ListingScore>();
		} else {
			return new ArrayList<ListingScore>(scores);
		}
	}

	public void setScores(List<ListingScore> scores) {
		if (CollectionUtils.isEmpty(scores)) {
			this.scores = new ArrayList<ListingScore>();
		} else {
			this.scores = new ArrayList<ListingScore>(scores);
		}
	}

	@Override
	public String toString() {
		return "TicketScoreRecResponse{" +
				"impressionToken='" + impressionToken + '\'' +
				", scores=" + scores +
				'}';
	}
}
