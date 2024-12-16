package io.arcmai.mail.repository;

import io.arcmai.mail.model.Option;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

public interface OptionRepository extends JpaRepository<Option, String> {
    @Query("from Option where id = 'eml_filename'")
    Option findEMLFilename();
    @Query("from Option where id = 'sync_interval_in_minutes'")
    Option findSyncInterval();
}
