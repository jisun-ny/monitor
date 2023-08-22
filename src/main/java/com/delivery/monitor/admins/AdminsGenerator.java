package com.delivery.monitor.admins;

import org.springframework.stereotype.Component;
import com.delivery.monitor.domain.Admins;
import org.springframework.dao.DataAccessException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Component
@RequiredArgsConstructor
public class AdminsGenerator {

    private final AdminsMapper adminsMapper;

    // 관리자 정보를 생성하고 DB에 삽입하는 메서드
    public void insertAdmins() {
        log.info("관리자 등록");
        try {
            Admins admins = Admins.builder()
                    .name("admin")
                    .email("admin@gmail.com")
                    .password("1234")
                    .build();
            adminsMapper.autoInsertAdmins(admins);
        } catch (DataAccessException e) {
            handleDataAccessException(e);
        } catch (Exception e) {
            handleGeneralException(e);
        }
    }

    // 데이터 액세스 예외 처리를 위한 메서드
    private void handleDataAccessException(DataAccessException e) {
        log.error("An error occurred during data access.", e);
        throw new RuntimeException("Error during data access", e);
    }

    // 일반 예외 처리를 위한 메서드
    private void handleGeneralException(Exception e) {
        log.error("An error occurred while inserting an admin.", e);
        throw new RuntimeException("Unexpected error during admin insertion", e);
    }
}
