package kg.peaksoft.giftlistb6.dto.responses;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class MyProfileResponse {

    private Long id;
    private String photo;
    private String firstName;
    private String lastName;
    private String email;
    private ProfileResponse profileResponse;
}
