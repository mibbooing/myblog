package com.example.myblog.repository;

import com.example.myblog.dto.MemberLogDto;
import com.example.myblog.entity.Member;
import com.example.myblog.entity.MemberLog;
import org.springframework.data.jpa.repository.JpaRepository;

public interface MemberLogRepository extends JpaRepository<MemberLog, Long> {
}
