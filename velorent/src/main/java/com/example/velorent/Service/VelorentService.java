package com.example.velorent.Service;

import com.example.velorent.Entity.Velorent;
import com.example.velorent.Repository.VelorentRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;


import java.util.Date;
import java.util.List;
import java.util.Optional;

@Service
public class VelorentService {

    public VelorentRepository velorentRepository;

    @Autowired
    public VelorentService(VelorentRepository velorentRepository) {
        this.velorentRepository = velorentRepository;
    }




    public Velorent rentVelorent(Velorent velorent){
        return velorentRepository.save(velorent);
    }
    public Optional<Velorent> getVelorent(Long idVelorent){
        return velorentRepository.findById(idVelorent);
    }
    public List<Velorent> getAllVelorent(){
        return velorentRepository.findAll();
    }
    public List<Velorent> getVelorentsByDate(Date date){
    return velorentRepository.findByStartRentDate(date);
    }

    public Velorent updateVelorent (Velorent velorent){
        return velorentRepository.saveAndFlush(velorent);
    }
    public void cancelVelorent(Long idVelorent){
        velorentRepository.deleteById(idVelorent);
    }
    public List<Velorent> getVelorentsByuserId(Long userId){
        return velorentRepository.findByUserId(userId);
    }

}
