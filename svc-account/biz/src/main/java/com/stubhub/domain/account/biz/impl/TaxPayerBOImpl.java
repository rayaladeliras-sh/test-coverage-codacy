package com.stubhub.domain.account.biz.impl;

import com.stubhub.domain.account.common.util.RSAEncryptionUtil;
import com.stubhub.domain.account.datamodel.dao.UserContactDAO;
import com.stubhub.domain.account.datamodel.entity.Tin;
import com.stubhub.domain.account.datamodel.entity.UserContact;
import com.stubhub.domain.account.intf.TaxIdRequest;
import com.stubhub.domain.account.intf.TaxIdResponse;
import com.stubhub.domain.account.intf.TaxIdShouldShowResponse;
import com.stubhub.domain.infrastructure.common.exception.base.SHSystemException;
import org.apache.commons.codec.binary.Base64;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;

import com.stubhub.domain.account.biz.intf.TaxPayerBO;
import com.stubhub.domain.account.datamodel.dao.TaxPayerDetailsDAO;
import com.stubhub.domain.account.datamodel.dao.TinDAO;
import com.stubhub.domain.account.datamodel.entity.TaxPayerDetails;
import com.stubhub.domain.account.datamodel.enums.TaxIdStatus;
import com.stubhub.domain.account.intf.TaxPayerAlertStatusResponse;

/**
 * Created at 11/4/15 6:06 PM
 *
 * @author : Caron Zhao
 * @version : 1.0.0
 */
@Service("taxPayerBO")
public class TaxPayerBOImpl implements TaxPayerBO {
    private final static Logger LOGGER = LoggerFactory.getLogger(TaxPayerBOImpl.class);
    private static final String LOG_ERROR_PREFIX = "api_domain=account, api_resource={}, api_method={}, error_message={}";

    @Autowired
    private TaxPayerDetailsDAO taxPayerDetailsDAO;

    @Autowired
    private UserContactDAO userContactDAO;

    @Autowired
    @Qualifier("tinDAO")
    private TinDAO tinDAO;

    @Autowired
    @Qualifier("rsaEncryptionUtil")
    private RSAEncryptionUtil rsaEncryptionUtil;

    @Override
    public TaxPayerAlertStatusResponse calculateShowTaxPayerAlert(String userId) {
        TaxPayerAlertStatusResponse response = new TaxPayerAlertStatusResponse();
        TaxPayerDetails taxPayerDetails = taxPayerDetailsDAO.getTaxPayerDetailsByUserId(Long.valueOf(userId));

        if (taxPayerDetails != null) {
            Integer taxIdStatusValue = taxPayerDetails.getTaxIdStatus();
            if (taxIdStatusValue != null) {
                if (taxIdStatusValue.intValue() == TaxIdStatus.TIN_NOT_NEEDED.getId().intValue() ||
                        taxIdStatusValue.intValue() == TaxIdStatus.TAX_YEAR_RESET.getId().intValue()) {
                    // Don't need show TIN Info
                    return response;
                }
                else if (taxIdStatusValue.intValue() == TaxIdStatus.TIN_NEEDED.getId().intValue()
                        || taxIdStatusValue.intValue() == TaxIdStatus.TIN_REQUIRED.getId().intValue()
                        || taxIdStatusValue.intValue() == TaxIdStatus.TIN_UNBLOCKED.getId().intValue()) {
                    return response.setAlert(Boolean.TRUE);
                }else{
                    return response.setAlert(Boolean.FALSE);
                }
            }
        }
        return response;
    }

    @Override
    public TaxIdShouldShowResponse isTaxIdNeeded(String userId, Boolean validateNotExist) {
        TaxIdShouldShowResponse taxIdShouldShowResponse = new TaxIdShouldShowResponse();

        UserContact defaultUserContact = userContactDAO.getDefaultUserContactByOwnerId(Long.valueOf(userId));
        if ("US".equals(defaultUserContact.getCountry())){
            if (validateNotExist) {
                TaxPayerDetails taxPayerDetails = taxPayerDetailsDAO.getTaxPayerDetailsByUserId(Long.valueOf(userId));
                if (taxPayerDetails == null) {
                    taxIdShouldShowResponse.setShouldShow(true);
                }
                else {
                    taxIdShouldShowResponse.setShouldShow(false);
                }
            } else {
                taxIdShouldShowResponse.setShouldShow(true);
            }
        } else {
            taxIdShouldShowResponse.setShouldShow(false);
        }
        return taxIdShouldShowResponse;
    }

    @Override
    public void createTaxId(TaxIdRequest taxIdRequest, String userId) {
        UserContact defaultUserContact = userContactDAO.getDefaultUserContactByOwnerId(Long.valueOf(userId));
        Tin tin = new Tin();
        byte[] taxIdEncrypted = encryptTin(taxIdRequest.getTaxId());
        byte[] taxIdEncrypted64 = Base64.encodeBase64(taxIdEncrypted);
        String taxIdEncoded = new String(taxIdEncrypted64);
        tin.setTin(taxIdEncoded);
        tin.setCreatedBy(userId);
        String tinGuid = tinDAO.addTin(tin);

        TaxPayerDetails taxPayerDetails = new TaxPayerDetails();
        taxPayerDetails.setUserId(Long.valueOf(userId));
        taxPayerDetails.setTaxIdStatus(TaxIdStatus.TIN_VALID.getId());
        taxPayerDetails.setTinGuid(tinGuid);
        taxPayerDetails.setTinType((taxIdRequest.getTaxId().length() == 10) ? "TIN" : "SSN");
        taxPayerDetails.setNameFirst(defaultUserContact.getFirstName());
        taxPayerDetails.setNameLast(defaultUserContact.getLastName());
        taxPayerDetails.setCompany("N/A");
        taxPayerDetails.setAddr1(defaultUserContact.getStreet());
        taxPayerDetails.setAddr2("N/A");
        taxPayerDetails.setAddrCity(defaultUserContact.getCity());
        taxPayerDetails.setAddrState(defaultUserContact.getState());
        taxPayerDetails.setAddrZip(defaultUserContact.getZip());
        taxPayerDetails.setPhone1(defaultUserContact.getPhoneNumber());
        taxPayerDetails.setAddrCountry(defaultUserContact.getCountry());
        taxPayerDetails.setActive(1);
        taxPayerDetails.setTaxCountry("N/A");
        taxPayerDetails.setCountryCallingCode(defaultUserContact.getPhoneCountryCd());
        taxPayerDetails.setCreatedBy("CreateTaxId");
        taxPayerDetails.setLastUpdatedBy("CreateTaxId");

        taxPayerDetailsDAO.addTaxPayerDetails(taxPayerDetails);
    }

    @Override
    public TaxIdResponse getTaxId(String userId) {
        TaxIdResponse taxIdResponse = new TaxIdResponse();
        TaxPayerDetails taxPayerDetails = taxPayerDetailsDAO.getTaxPayerDetailsByUserId(Long.valueOf(userId));
        Tin tin = tinDAO.getTinByGuid(taxPayerDetails.getTinGuid());

        String taxIdEncrypted64 = tin.getTin();
        byte[] taxIdEncrypted = Base64.decodeBase64(taxIdEncrypted64);
        String taxId = decryptTin(taxIdEncrypted);

        taxIdResponse.setTaxId(taxId);

        return taxIdResponse;
    }

    @Override
    public void updateTaxId(TaxIdRequest taxIdRequest, String userId) {
        TaxPayerDetails taxPayerDetails = taxPayerDetailsDAO.getTaxPayerDetailsByUserId(Long.valueOf(userId));

        Tin tin = tinDAO.getTinByGuid(taxPayerDetails.getTinGuid());
        byte[] taxIdEncrypted = encryptTin(taxIdRequest.getTaxId());
        byte[] taxIdEncrypted64 = Base64.encodeBase64(taxIdEncrypted);
        String taxIdEncoded = new String(taxIdEncrypted64);
        tin.setTin(taxIdEncoded);
        tin.setLastUpdatedBy(userId);
        tinDAO.updateTin(tin);

        taxPayerDetails.setTinType((taxIdRequest.getTaxId().length() == 10) ? "TIN" : "SSN");
        taxPayerDetails.setLastUpdatedBy("UpdateTaxId");

        taxPayerDetailsDAO.updateTaxPayerDetails(taxPayerDetails, false);
    }

    @Override
    public boolean userHasTaxId(String userId) {
        TaxPayerDetails taxPayerDetails = taxPayerDetailsDAO.getTaxPayerDetailsByUserId(Long.valueOf(userId));
        if (taxPayerDetails == null) {
            return false;
        } else {
            return true;
        }
    }

    protected byte[] encryptTin(String taxIdNumber) {
        try {
            return rsaEncryptionUtil.encrypt(taxIdNumber);
        } catch (Exception e) {
            LOGGER.error(LOG_ERROR_PREFIX, this.getClass().getName(), "encryptTin",
                    "Exception while encrypting TIN " + taxIdNumber);
            e.printStackTrace();
            throw new SHSystemException(e);
        }
    }

    protected String decryptTin(byte[] taxIdNumberEncrypted) {
        try {
            return rsaEncryptionUtil.decrypt(taxIdNumberEncrypted);
        } catch (Exception e) {
            LOGGER.error(LOG_ERROR_PREFIX, this.getClass().getName(), "decryptTin",
                    "Exception while decrypting TIN ");
            e.printStackTrace();
            throw new SHSystemException(e);
        }
    }

}