package com.delivery.monitor.admins;

import org.apache.ibatis.annotations.Mapper;

import com.delivery.monitor.domain.Admins;

@Mapper
public interface AdminsMapper {
    void autoInsertAdmins(Admins admins);
}
