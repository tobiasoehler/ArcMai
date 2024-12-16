package io.arcmai.mail.repository;

import io.arcmai.mail.model.ImapSetting;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.UUID;

public interface ImapSettingRepository extends JpaRepository<ImapSetting, UUID> {

}
