package io.arcmai.mail.repository;

import io.arcmai.mail.model.Option;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

@DataJpaTest
public class OptionRepositoryTest {
    @Autowired
    OptionRepository optionRepository;

    @Test
    public void getFileNameTest() {
        var result = optionRepository.findEMLFilename();

        var option = new Option();
        option.setId("eml_filename");
        option.setValue("{date}-{subject}-{messageId}");

        Assertions.assertThat(result)
                .usingRecursiveComparison()
                .isEqualTo(option);
    }

    @Test
    public void getSyncIntervalInMinutesTest() {
        var result = optionRepository.findSyncInterval();

        var option = new Option();
        option.setId("sync_interval_in_minutes");
        option.setValue("60");

        Assertions.assertThat(result)
                .usingRecursiveComparison()
                .isEqualTo(option);
    }
}
