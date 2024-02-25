package org.musketeers.service;

import org.musketeers.entity.Guest;
import org.musketeers.entity.Phone;
import org.musketeers.entity.enums.ActivationStatus;
import org.musketeers.entity.enums.Gender;
import org.musketeers.entity.enums.PhoneType;
import org.musketeers.exception.ErrorType;
import org.musketeers.exception.GuestServiceException;
import org.musketeers.rabbitmq.model.RegisterGuestModel;
import org.musketeers.repository.GuestRepository;
import org.musketeers.utility.JwtTokenManager;
import org.musketeers.utility.ServiceManager;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class GuestService extends ServiceManager<Guest, String> {

    private final GuestRepository guestRepository;

    private final JwtTokenManager jwtTokenManager;

    public GuestService(GuestRepository guestRepository, JwtTokenManager jwtTokenManager) {
        super(guestRepository);
        this.guestRepository = guestRepository;
        this.jwtTokenManager = jwtTokenManager;
    }

    public Guest getGuestById(String id) {
//        jwtTokenManager.getClaimsFromToken(id).get(0)
        return findById(id);
    }

    public List<Guest> getAllGuests() {
        return findAll();
    }

    public Boolean softDeleteGuestById(String id) {
        return softDeleteById(id);
    }

    public void activateGuest(String authId) {
        Guest guest = guestRepository.findOptionalByAuthId(authId).orElseThrow(() -> new GuestServiceException(ErrorType.GUEST_NOT_FOUND));
        guest.setActivationStatus(ActivationStatus.ACTIVATED);
        update(guest);
    }

    public void createGuest(RegisterGuestModel model) {
        Guest guest = Guest.builder()
                .authId(model.getAuthid())
                .name(model.getName())
                .lastName(model.getSurName())
                .gender(model.getGender().equalsIgnoreCase("MALE") ? Gender.MALE : Gender.FEMALE)
                .identityNumber(model.getIdentityNumber())
                .email(model.getEmail())
                .image("asd")
                .phone(Phone.builder().phoneType(PhoneType.PERSONAL).phoneNumber(model.getPhone()).build())
                .dateOfBirth(model.getDateOfBirth())
                .build();
        save(guest);
    }
}
