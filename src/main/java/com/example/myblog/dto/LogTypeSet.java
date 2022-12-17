package com.example.myblog.dto;

import com.example.myblog.constant.LogType;
import lombok.Getter;
import lombok.Setter;

import java.util.HashMap;
import java.util.Map;

@Getter
@Setter
public class LogTypeSet {
    public Map<String, LogType> createLogTypeSet(){
        Map<String, LogType> logTypeMap = new HashMap<>();
        logTypeMap.put("CREATE", LogType.CREATE);
        logTypeMap.put("UPDATE", LogType.UPDATE);
        logTypeMap.put("DELETE", LogType.DELETE);
        return logTypeMap;
    }
}
