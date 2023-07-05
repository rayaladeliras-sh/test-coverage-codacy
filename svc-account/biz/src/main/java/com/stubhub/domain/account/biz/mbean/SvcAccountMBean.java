package com.stubhub.domain.account.biz.mbean;

import com.stubhub.domain.infrastructure.caching.client.core.impl.SHCache;
import com.stubhub.domain.infrastructure.caching.client.core.management.Statistics;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.Cache;
import org.springframework.cache.CacheManager;
import org.springframework.jmx.export.annotation.ManagedOperation;
import org.springframework.jmx.export.annotation.ManagedResource;
import org.springframework.stereotype.Component;

@Component("svcAccountMBean")
@ManagedResource(objectName = "AccountDomain-service:name=SvcAccountMBean", description = "Domain Account MBean" )
public class SvcAccountMBean {

    private static final Logger log = LoggerFactory.getLogger(SvcAccountMBean.class);

    @Autowired
    private CacheManager cacheManager;

    @ManagedOperation(description = "getCacheStatistics")
    public String getCacheStatistics(String cache) {
        StringBuffer res = new StringBuffer();

        try{
            if(cache==null || cache.length()==0){
                cache = "getEventV3";
            }
            Cache target = cacheManager.getCache(cache);
            if(target!=null){
                Statistics statistics = ((SHCache)target.getNativeCache()).getStatisticsData();

                res.append("getCount=" + statistics.getCount()).append("\n");
                res.append("getHits=" + statistics.getHits()).append("\n");
                res.append("getHitPercentage=" + statistics.getHitPercentage()).append("\n");
                res.append("getExpirationRate=" + statistics.getExpirationRate()).append("\n");
                res.append("getEvictionRate=" + statistics.getEvictionRate()).append("\n");
                res.append("getGets=" + statistics.getGets()).append("\n");
                res.append("getGetRate=" + statistics.getGetRate()).append("\n");
                res.append("getMisses=" + statistics.getMisses()).append("\n");
                res.append("getMissPercentage=" + statistics.getMissPercentage()).append("\n");
                res.append("getUpdateRate=" + statistics.getUpdateRate()).append("\n");
                res.append("getStatsAge=" + statistics.getStatsAge()).append("\n");
                log.info("api_domain=account api_method=statistics cache={} getCount={}", cache, statistics.getCount());
                log.info("api_domain=account api_method=statistics cache={} getHits={}" , cache,  statistics.getHits());
                log.info("api_domain=account api_method=statistics cache={} getHitPercentage={}" , cache,  statistics.getHitPercentage());
                log.info("api_domain=account api_method=statistics cache={} getExpirationRate={}" , cache,  statistics.getExpirationRate());
                log.info("api_domain=account api_method=statistics cache={} getEvictionRate={}" , cache,  statistics.getEvictionRate());
                log.info("api_domain=account api_method=statistics cache={} getGets={}" , cache,  statistics.getGets());
                log.info("api_domain=account api_method=statistics cache={} getGetRate={}" , cache,  statistics.getGetRate());
                log.info("api_domain=account api_method=statistics cache={} getMisses={}" , cache,  statistics.getMisses());
                log.info("api_domain=account api_method=statistics cache={} getMissPercentage={}" , cache,  statistics.getMissPercentage());
                log.info("api_domain=account api_method=statistics cache={} getUpdateRate={}" , cache,  statistics.getUpdateRate());
                log.info("api_domain=account api_method=statistics cache={} getStatsAge={}" , cache,  statistics.getStatsAge());
            }else{
                res.append("api_method=statistics message=\"no cache found\" cacheName=" + cache);
            }

        }catch (Exception e){
            log.error("api_domain=account api_method=statistics error={}", e.getMessage() );
            res.append("api_method=statistics error=" + e.getMessage());
        }

        return res.toString();
    }


    @ManagedOperation(description = "turnOnCacheStatistics")
    public String turnOnCacheStatistics(String cache, String flag){
        StringBuffer res = new StringBuffer();
        try{
            if(cache==null || cache.length()==0){
                cache = "getEventV3";
            }
            Cache target = cacheManager.getCache(cache);
            if(target!=null && "True".equalsIgnoreCase(flag)){
                ((SHCache)target.getNativeCache()).setStatisticsSetting(true);
                res.append("api_method=turnOnCacheStatistics turnOnStatistics");
            } else if(target!=null && "False".equalsIgnoreCase(flag)){
                ((SHCache)target.getNativeCache()).setStatisticsSetting(false);
                res.append("api_method=turnOnCacheStatistics turnOffStatistics");
            } else{
                res.append("api_method=turnOnCacheStatistics message=\"no cache found\" cacheName=" + cache);
            }

        }catch (Exception e){
            log.error("api_domain=account api_method=turnOnCacheStatistics error={}", e.getMessage() );
            res.append("api_method=turnOnCacheStatistics error=" + e.getMessage());
        }

        return res.toString();
    }
}