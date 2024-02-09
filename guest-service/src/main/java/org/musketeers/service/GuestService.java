package org.musketeers.service;

import org.musketeers.entity.Guest;
import org.musketeers.entity.enums.ActivationStatus;
import org.musketeers.exception.ErrorType;
import org.musketeers.exception.GuestServiceException;
import org.musketeers.repository.GuestRepository;
import org.musketeers.utility.ServiceManager;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class GuestService extends ServiceManager<Guest, String> {

    private final GuestRepository guestRepository;

    public GuestService(GuestRepository guestRepository) {
        super(guestRepository);
        this.guestRepository = guestRepository;
    }

    public Guest getGuestById(String id) {
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
        save(guest);
    }

}
