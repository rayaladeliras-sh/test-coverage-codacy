package com.stubhub.domain.account.intf;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlType;

/**
 * Created by jicui on 8/31/15.
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "SeatDetail", propOrder = {
        "stubTransDtlId",
        "seatId",
        "section",
        "row",
        "seatNum",
        "tixListTypeId",
        "seatStatusId",
        "isGA"
})
public class SeatDetail {
    private Long stubTransDtlId;
    private Long seatId;
    private String section;
    private String row;
    private String seatNum;
    private Long tixListTypeId;
    private Long seatStatusId;
    private Long isGA;

    public Long getSeatId() {
        return seatId;
    }

    public void setSeatId(Long seatId) {
        this.seatId = seatId;
    }

    public String getSection() {
        return section;
    }

    public void setSection(String section) {
        this.section = section;
    }

    public String getRow() {
        return row;
    }

    public void setRow(String row) {
        this.row = row;
    }

    public String getSeatNum() {
        return seatNum;
    }

    public void setSeatNum(String seatNum) {
        this.seatNum = seatNum;
    }

    public Long getTixListTypeId() {
        return tixListTypeId;
    }

    public void setTixListTypeId(Long tixListTypeId) {
        this.tixListTypeId = tixListTypeId;
    }

    public Long getSeatStatusId() {
        return seatStatusId;
    }

    public void setSeatStatusId(Long seatStatusId) {
        this.seatStatusId = seatStatusId;
    }

    public Long getIsGA() {
        return isGA;
    }

    public void setIsGA(Long isGA) {
        this.isGA = isGA;
    }

    public Long getStubTransDtlId() {
        return stubTransDtlId;
    }

    public void setStubTransDtlId(Long stubTransDtlId) {
        this.stubTransDtlId = stubTransDtlId;
    }
}
