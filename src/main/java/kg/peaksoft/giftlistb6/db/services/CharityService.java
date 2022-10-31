package kg.peaksoft.giftlistb6.db.services;

import kg.peaksoft.giftlistb6.db.models.Charity;
import kg.peaksoft.giftlistb6.db.models.User;
import kg.peaksoft.giftlistb6.db.repositories.CategoryRepository;
import kg.peaksoft.giftlistb6.db.repositories.CharityRepository;
import kg.peaksoft.giftlistb6.db.repositories.SubCategoryRepository;
import kg.peaksoft.giftlistb6.db.repositories.UserRepository;
import kg.peaksoft.giftlistb6.dto.requests.CharityRequest;
import kg.peaksoft.giftlistb6.dto.responses.*;
import kg.peaksoft.giftlistb6.exceptions.BadRequestException;
import kg.peaksoft.giftlistb6.exceptions.NotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.List;

@Service
@RequiredArgsConstructor
public class CharityService {

    private final CharityRepository charityRepository;
    private final UserRepository userRepository;
    private final CategoryRepository categoryRepository;
    private final SubCategoryRepository subCategoryRepository;


    public User getPrinciple() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String email = authentication.getName();
        return userRepository.findByEmail(email).orElseThrow(
                () -> new NotFoundException("не найден!")
        );

    }

    public YourCharityResponse saveCharity(CharityRequest charityRequest) {
        if (!categoryRepository.findByName(charityRequest.getCategory()).getSubCategory()
                .contains(subCategoryRepository.findByName(charityRequest.getSubCategory()))) {
            throw new BadRequestException();
        }
        User user = getPrinciple();
        Charity charity = new Charity(charityRequest);
        charity.setCategory(categoryRepository.findByName(charityRequest.getCategory()));
        charity.setSubCategory(subCategoryRepository.findByName(charityRequest.getSubCategory()));
        user.setCharities(List.of(charity));
        charity.setUser(user);
        charityRepository.save(charity);
        return new YourCharityResponse(charity.getId(), charity.getImage());
    }

    public InnerPageCharityResponse findById(Long id) {
        return charityRepository.getCharityById(id);
    }

    public CharityResponses getAllCharityResponse() {
        User user = getPrinciple();
        List<YourCharityResponse> yourCharityResponses = charityRepository.getAllMyCharity(user.getEmail());
        List<OtherCharityResponse> otherCharityResponses = charityRepository.getAllOther(user.getEmail());
        CharityResponses charityResponse = new CharityResponses();
        charityResponse.setYourCharityResponses(yourCharityResponses);
        charityResponse.setOtherCharityResponses(otherCharityResponses);
        return charityResponse;
    }

    @Transactional
    public InnerPageCharityResponse updateCharity(Long id, CharityRequest charityRequest) {
        User user = getPrinciple();
        if (charityRequest == null) {
            return null;
        }
        Charity charity1 = charityRepository.findById(id).orElseThrow(() -> new NotFoundException("not found"));
        if (charity1.getUser().equals(user)){
        charity1.setImage(charityRequest.getImage());
        charity1.setUser(user);
        charity1.setName(charityRequest.getName());
        charity1.setCondition(charityRequest.getCondition());
        charity1.setCategory(categoryRepository.findByName(charityRequest.getCategory()));
        charity1.setSubCategory(subCategoryRepository.findByName(charityRequest.getSubCategory()));
        charity1.setDescription(charityRequest.getDescription());
        charityRepository.save(charity1);
        }

        return new InnerPageCharityResponse(charity1.getId(), charity1.getImage(),
                charity1.getName(), charity1.getDescription(), categoryRepository.getByName(charity1.getCategory().getName()),
                 subCategoryRepository.getByName(charity1.getSubCategory().getName()),
                 charity1.getCondition(), charity1.getCreatedDate(),
                charity1.getCharityStatus());
    }

    public SimpleResponse deleteCharityById(Long id) {
        User user = getPrinciple();
        if (!user.getCharities().contains(charityRepository.findById(id).orElseThrow(()->new NotFoundException("not found")))) {
            throw new NotFoundException("You can't delete other charities");
        }
        charityRepository.deleteCharityById(id,user.getId());
        return new SimpleResponse("Благотворительность успешно удалено!", "");
    }
}