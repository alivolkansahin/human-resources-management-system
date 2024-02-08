package org.musketeers.service;

import org.musketeers.entity.Guest;
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

    public Guest register(Guest guest) {
        return save(guest);
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
}
