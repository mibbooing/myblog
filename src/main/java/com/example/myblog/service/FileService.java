package com.example.myblog.service;

import lombok.extern.java.Log;
import org.springframework.stereotype.Service;

import java.io.File;
import java.io.FileOutputStream;
import java.util.List;
import java.util.Map;
import java.util.UUID;

@Service
@Log
public class FileService {
    public String uploadFile(String uploadPath, String originalFileName, byte[] fileData) throws Exception{
        UUID uuid = UUID.randomUUID();
        String extension = originalFileName.substring(originalFileName.lastIndexOf("."));
        String savedFileName = uuid.toString() + extension;
        String fileUploadFullUrl = uploadPath + "/" + savedFileName;
        FileOutputStream fos = new FileOutputStream(fileUploadFullUrl);
        fos.write(fileData);
        fos.close();
        return savedFileName;
    }

    public void deleteFile(String filePath) throws Exception{
        File deleteFile = new File(filePath);

        if(deleteFile.exists()) {
            deleteFile.delete();
            log.info("파일을 삭제하였습니다.");
        } else{
            log.info("파일이 존재하지 않습니다.");
        }
    }

    public void replaceImgPath(String reqTargetPath, String reqDestPath){
        System.out.println("targetPath: "+reqTargetPath + "  destPath: "+reqDestPath);
        File targetPath = new File(reqTargetPath);
        File destPath = new File(reqDestPath);
        if(!(destPath.exists())){
            destPath = makePath(reqDestPath);
            System.out.println(destPath.getPath());
        }
        try {
            if (targetPath.exists()) {
                File[] fileList = targetPath.listFiles();
                for (int i = 0; i < fileList.length; i++) {
                    if (fileList[i].exists()) {
                        File moveFile = new File(destPath, fileList[i].getName());
                        fileList[i].renameTo(moveFile);
                        System.out.println("new path : "+ fileList[i].getPath() + "  ,  " + fileList[i].getName());
                    }
                }
            }
        }catch (Exception e){

        }
    }

    public File makePath(String path){
        File folder = new File(path);
        if (!folder.exists()) {
            try {
                folder.mkdirs();
            } catch (Exception e) {
                e.getStackTrace();
            }
        }
        return folder;
    }
}
