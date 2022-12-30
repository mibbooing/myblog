package com.example.myblog.dto;

import com.example.myblog.constant.LogType;
import com.example.myblog.constant.PostStatus;
import lombok.Getter;
import lombok.Setter;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Getter
@Setter
public class TypeSet {
    public Map<String, LogType> createLogTypeSet(){
        Map<String, LogType> logTypeMap = new HashMap<>();
        logTypeMap.put("CREATE", LogType.CREATE);
        logTypeMap.put("UPDATE", LogType.UPDATE);
        logTypeMap.put("DELETE", LogType.DELETE);
        return logTypeMap;
    }

    public List<PostStatus> createPostStatusSet(){
        List<PostStatus> postStatusList = new ArrayList<>();
        postStatusList.add(PostStatus.PUBLIC);
        postStatusList.add(PostStatus.HIDE);
        postStatusList.add(PostStatus.PERMITTED);
        postStatusList.add(PostStatus.TEMP);
        postStatusList.add(PostStatus.DELETE);
        return postStatusList;
    }
}
