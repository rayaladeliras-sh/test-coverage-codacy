package com.stubhub.domain.account.datamodel.dao;

import com.stubhub.domain.account.datamodel.entity.UserContact;

/**
 * Created by jicui on 9/6/15.
 */
public interface UserContactDAO {
    UserContact getUserContactById(Long userContactId);

    UserContact getDefaultUserContactByOwnerId(Long ownerId);
}