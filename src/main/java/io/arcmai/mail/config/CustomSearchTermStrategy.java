package io.arcmai.mail.config;

import jakarta.mail.Flags;
import jakarta.mail.Folder;
import jakarta.mail.search.AndTerm;
import jakarta.mail.search.ComparisonTerm;
import jakarta.mail.search.ReceivedDateTerm;
import jakarta.mail.search.SearchTerm;
import org.springframework.integration.mail.SearchTermStrategy;

import java.util.Date;

public class CustomSearchTermStrategy implements SearchTermStrategy {

    public SearchTerm generateSearchTerm(Flags flags, Folder folder) {
        SearchTerm olderThan = new ReceivedDateTerm(ComparisonTerm.GT, new Date("01-Aug-1950"));

        SearchTerm[] termArray = new SearchTerm[]{olderThan};
        return new AndTerm(termArray);
    }
}
