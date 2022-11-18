package kg.peaksoft.giftlistb6.dto.responses;

import lombok.*;

import java.time.LocalDate;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class SearchCharityResponse {

    private Long charityId;
    private SearchUserResponse saveUserResponse;
    private String charityImage;
    private String charityName;
    private String charityCondition;
    private LocalDate createdAt;
    private UserFeedResponse reservoirUser;
}