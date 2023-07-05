package com.stubhub.test.platform;

import java.util.HashMap;
import java.util.Map;

@RunType(
        value = "sequenceCache",
        topLevelKeys = {"key","value"}
)
public class SequenceCache extends Run{
    private static final Map<String,Map<String,String>> INSTANCE = new HashMap<String,Map<String,String>>();

    private Object key;
    private Object value;

    public SequenceCache(Map<String, Object> map, SequenceCache sharedConfig){
        super(map, sharedConfig);
        this.key=map.get("key");
        this.value=map.get("value");
    }

    private  void set(String sequenceName,String key, String value) {
        if(!INSTANCE.containsKey(sequenceName)){
            INSTANCE.put(sequenceName,new HashMap<String, String>());
        }
        ((Map)INSTANCE.get(sequenceName)).put(key, value);
        System.out.println("sequenceName:"+sequenceName+",key:"+key+",value="+value);
    }

    private  String get(String sequenceName,String key) {
        String value=INSTANCE.get(sequenceName).get(key);
        System.out.println("sequenceName:"+sequenceName+",key:"+key+",value="+value);
        return value;
    }

    @Override
    public void test() {
        if(null==this.key){
            throw new IllegalArgumentException("key missing");
        }
        this.key=evaluateAsString(key.toString());
        String sequenceName=getSequenceName();
        if(null==value){
            this.value=get(sequenceName,key.toString());
        }else{
            this.value=evaluateAsString(this.value.toString());
            set(sequenceName,key.toString(),value.toString());
        }
    }

    private String getSequenceName(){
        Sequence sequence=getSequence();
        return (null==sequence?null:sequence.getName());
    }

    @Override
    public void assertResults() {

    }

    @Override
    public Object getValue(Reference reference) {
        return get(getSequenceName(),key.toString());
    }
}
