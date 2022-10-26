package kg.peaksoft.giftlistb6.db.repositories;

import kg.peaksoft.giftlistb6.db.models.Wish;
import kg.peaksoft.giftlistb6.dto.responses.WishResponse;
import kg.peaksoft.giftlistb6.dto.responses.WishResponse1;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

public interface WishRepository extends JpaRepository<Wish, Long> {
    @Query("select new kg.peaksoft.giftlistb6.dto.responses.WishResponse1(w) from Wish w where w.user.email = ?1")
    List<WishResponse1> getALlReservoirWishes(String email);
}