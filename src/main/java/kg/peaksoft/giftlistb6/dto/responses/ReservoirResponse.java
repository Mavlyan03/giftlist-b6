package kg.peaksoft.giftlistb6.dto.responses;

import kg.peaksoft.giftlistb6.db.models.Charity;
import kg.peaksoft.giftlistb6.db.models.User;
import kg.peaksoft.giftlistb6.exceptions.BadCredentialsException;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class ReservoirResponse {

    private Long id;
    private String photo;

    public ReservoirResponse(Charity charity) {
        if (charity.getReservoir() != null) {
            this.id = charity.getReservoir().getId();
            this.photo = charity.getReservoir().getPhoto();
        }
        charity.setReservoir(new User("name"));
    }

}
