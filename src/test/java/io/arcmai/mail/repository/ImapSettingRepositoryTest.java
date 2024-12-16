package io.arcmai.mail.repository;

import io.arcmai.mail.model.ImapSetting;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

@DataJpaTest
public class ImapSettingRepositoryTest {

    @Autowired
    private ImapSettingRepository imapSettingRepository;

    @Test
    public void testSelectAndInset()
    {
        ImapSetting setting = new ImapSetting();
        setting.setUsername("username");
        setting.setPassword("password");
        setting.setHost("host");
        setting.setPort(12);

        imapSettingRepository.save(setting);

        var result = imapSettingRepository.findAll().getFirst();

        Assertions.assertThat(result)
                .usingRecursiveComparison()
                .isEqualTo(setting);
    }
}
