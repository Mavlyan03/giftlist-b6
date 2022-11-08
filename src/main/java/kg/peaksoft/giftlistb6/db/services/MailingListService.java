package kg.peaksoft.giftlistb6.db.services;

import kg.peaksoft.giftlistb6.db.models.MailingList;
import kg.peaksoft.giftlistb6.db.repositories.MailingListRepository;
import kg.peaksoft.giftlistb6.dto.requests.MailingListRequest;
import kg.peaksoft.giftlistb6.dto.responses.AllMailingListResponse;
import kg.peaksoft.giftlistb6.dto.responses.MailingListResponse;
import kg.peaksoft.giftlistb6.exceptions.NotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
public class MailingListService {

    private final MailingListRepository mailingListRepository;

    public AllMailingListResponse saveMailingList(MailingListRequest request) {
        MailingList mailingList = convertToEntity(request);
        mailingListRepository.save(mailingList);
        return convertToResponse(mailingList);
    }

    @Transactional
    public MailingList convertToEntity(MailingListRequest request) {
        MailingList mailingList = new MailingList();
        mailingList.setName(request.getName());
        mailingList.setPhoto(request.getPhoto());
        mailingList.setText(request.getText());
        mailingList.setCreateDate(LocalDateTime.now());
        return mailingList;
    }

    @Transactional
    public AllMailingListResponse convertToResponse(MailingList mailingList) {
        AllMailingListResponse response = new AllMailingListResponse();
        response.setId(mailingList.getId());
        response.setName(mailingList.getName());
        response.setPhoto(mailingList.getPhoto());
        response.setLocalDateTime(LocalDateTime.now());
        return response;
    }

    public List<AllMailingListResponse> convertToAllView(List<MailingList> mailingLists) {
       List<AllMailingListResponse> list = new ArrayList<>();
       for (MailingList mailingList : mailingLists) {
           list.add(convertToResponse(mailingList));
       }
       return list;
    }

    public MailingListResponse getId(Long id) {
         return mailingListRepository.findMailingById(id).orElseThrow(
                ()-> new NotFoundException(String.format("Рассылка с таким id = %s не найдена!", id))
        );
    }

    public List<AllMailingListResponse> getAllMailingLists() {
        return convertToAllView(mailingListRepository.findAll());
    }
}