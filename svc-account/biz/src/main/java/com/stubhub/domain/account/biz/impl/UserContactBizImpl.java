package com.stubhub.domain.account.biz.impl;

import com.stubhub.domain.account.biz.intf.UserContactBiz;
import com.stubhub.domain.account.datamodel.dao.UserContactDAO;
import com.stubhub.domain.account.datamodel.entity.UserContact;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Component;

/**
 * Created by jicui on 9/6/15.
 */
@Component("userContactBiz")
public class UserContactBizImpl implements UserContactBiz {

    @Autowired
    @Qualifier("userContactDAO")
    private UserContactDAO userContactDAO;

    @Override
    public UserContact getUserContactById(Long contactId) {
        return userContactDAO.getUserContactById(contactId);
    }

    @Override
    public UserContact getDefaultUserContactByOwernId(Long ownerId) {
        return userContactDAO.getDefaultUserContactByOwnerId(ownerId);
    }
}