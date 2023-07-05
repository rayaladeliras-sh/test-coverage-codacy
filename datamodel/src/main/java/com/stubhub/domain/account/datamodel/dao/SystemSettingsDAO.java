package com.stubhub.domain.account.datamodel.dao;

import com.stubhub.domain.account.datamodel.entity.SystemSettings;

/**
 * @author yuntzhao
 */
public interface SystemSettingsDAO {
    public SystemSettings findByName(String name);
}
