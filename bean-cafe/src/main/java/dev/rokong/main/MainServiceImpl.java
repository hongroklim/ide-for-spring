package dev.rokong.main;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class MainServiceImpl implements MainService {

    @Autowired MainDAO mainDAO;

    public String currentDate() {
        return mainDAO.selectCurrentDate();
    }
    
}