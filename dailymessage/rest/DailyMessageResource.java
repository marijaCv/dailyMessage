package de.pickert.module.dailymessage.rest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import java.net.URISyntaxException;
import java.util.List;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;

import org.springframework.web.bind.annotation.RestController;

import de.pickert.module.dailymessage.domain.DailyMessage;
import de.pickert.module.dailymessage.service.DailyMessageService;
import de.pickert.web.rest.errors.BadRequestAlertException;

@RestController
@RequestMapping("/api/core")
public class DailyMessageResource {

    @Autowired
    private DailyMessageService dailyMessageService;


    @PostMapping("/daily-message")
    public ResponseEntity<DailyMessage> createDailyMessage(@RequestBody DailyMessage dailyMessage)
            throws URISyntaxException {
        if (dailyMessage.getId() != null) {
            throw new BadRequestAlertException("New daily message has a set id.", "Daily Message", "400");
        } 

        return ResponseEntity.ok(dailyMessageService.createMessage(dailyMessage));
    }

    
    @GetMapping("/daily-message/{id}")
    public ResponseEntity<DailyMessage> findDailyMessageById(@PathVariable(value = "id") String id)
            throws URISyntaxException {
        return ResponseEntity.ok(dailyMessageService.findMessageById(id));
    }

  
    @GetMapping("/daily-message")
    public ResponseEntity<List<DailyMessage>> findAllDailyMessages() throws URISyntaxException {
        return ResponseEntity.ok(dailyMessageService.findAllMessages());
    }

    
    @GetMapping("/daily-message/current")
    public ResponseEntity<DailyMessage> findCertainDailyMessage() throws URISyntaxException {
        return ResponseEntity.ok(dailyMessageService.findCurrentMessage());
    }

   
    @PutMapping("/daily-message/{id}")
    public ResponseEntity<DailyMessage> modifyDailyMessage(@RequestBody DailyMessage dailyMessage)
            throws URISyntaxException {
        if (dailyMessage.getId() == null) {
            throw new BadRequestAlertException("Daily message has no id.", "Daily Message", "400");
        } 
        return ResponseEntity.ok(dailyMessageService.updateMessage(dailyMessage));
    }


    @DeleteMapping("/daily-message/{id}")
    public ResponseEntity<?> deleteDailyMessage(@PathVariable(value = "id") String messageId)
            throws URISyntaxException {
        if (messageId == null) {
            throw new BadRequestAlertException("Daily message does already not exist.", "Daily Message", "400");
        }
        dailyMessageService.deleteMessage(messageId);
        return ResponseEntity.ok().build();
    }

    // delete all
    @DeleteMapping("/daily-message/all")
    public ResponseEntity<?> deleteAllMessages() {
        dailyMessageService.deleteAllMessages();
        return ResponseEntity.ok().body(null);

    }

    @GetMapping("/daily-message/sorted") 
    public ResponseEntity<?> sortDailyMessages() throws URISyntaxException {
        return ResponseEntity.ok(dailyMessageService.sortDailyMessageOrder());
    }

}

// GET MAPPING BEKOMME EIN msg ZURÜCK ÜBER ID
// POST MAPPING SPEICHERE EIN msg IN DER DB
