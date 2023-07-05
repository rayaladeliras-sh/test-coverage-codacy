package com.stubhub.domain.account.biz.intf;

import com.stubhub.domain.account.datamodel.entity.UserContact;

/**
 * Created by jicui on 9/6/15.
 */
public interface UserContactBiz {
    UserContact getUserContactById(Long contactId);

    UserContact getDefaultUserContactByOwernId(Long ownerId);
}