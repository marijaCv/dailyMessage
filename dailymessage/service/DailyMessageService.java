package de.pickert.module.dailymessage.service;

import java.time.DayOfWeek;
import java.time.Instant;
import java.time.ZoneId;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.query.Criteria;

import org.springframework.data.mongodb.core.query.Query;
import org.springframework.stereotype.Service;

import de.pickert.config.mongo.TenantMongoTemplate;
import de.pickert.module.core.domain.UUIDFactory;
import de.pickert.module.dailymessage.domain.DailyMessage;

@Service
public class DailyMessageService {

   @Autowired()
   private TenantMongoTemplate tenantMongoTemplate; // radi operacije nad bazom samo u okviru tenanta kao sto su save,
                                                    // find...

   public DailyMessage createMessage(DailyMessage msg) {
      List<DailyMessage> messageList = tenantMongoTemplate.findAll(DailyMessage.class);

      if (messageList.size() == 0) {
         msg.setIndex(1);
      } else {
         DailyMessage latestMessage = messageList.get(messageList.size() - 1);
         if (latestMessage.getIndex() >= 0) {
            msg.setIndex(latestMessage.getIndex() + 1);
         } else {
            msg.setIndex(1);
         }
      }

      return tenantMongoTemplate.save(msg);

   }

   public DailyMessage findMessageById(String id) {
      return tenantMongoTemplate.findById(id, DailyMessage.class);
   }

   public List<DailyMessage> findAllMessages() {
      return tenantMongoTemplate.findAll(DailyMessage.class);
   }

   // todays message
   public DailyMessage findCurrentMessage() {
      HashMap<String, ArrayList<DailyMessage>> dmMap = sortDailyMessageOrder();
      Instant now = Instant.now();
      int day = now.atZone(ZoneId.systemDefault()).getDayOfWeek().getValue();

      if (day > 5) {
         day = 5;
      }

      for (int i = day; i > 0; i -= 1) { 
         String nameOfTheDay = DayOfWeek.of(i).toString();

         if (dmMap.get(nameOfTheDay).size() > 0) {
            return dmMap.get(nameOfTheDay).get(dmMap.get(nameOfTheDay).size() - 1);
         }
      }

      for (int i = 5; i > day; i -= 1) {
         String nameOfTheDay = DayOfWeek.of(i).toString();

         if (dmMap.get(nameOfTheDay).size() > 0) {
            return dmMap.get(nameOfTheDay).get(dmMap.get(nameOfTheDay).size() - 1);
         }
      }
      return null;
   }

   // 1 - 5 days -> 0 - 4
   // 0 - 4 list

   // 3 messages , thrusday day 4 idx 3

   // 3 % 3 = 0

   // Vratis odgovarajuce poruke/ ili samo ids od poruka

   // update (modify) (by id)
   public DailyMessage updateMessage(DailyMessage udpdateMessage) { 
      return tenantMongoTemplate.save(udpdateMessage);
   }

   // delete (by id)
   public void deleteMessage(String messageId) { 
      Query q = new Query();
      q.addCriteria(Criteria.where("id").is(UUIDFactory.fromString(messageId))); 
      tenantMongoTemplate.remove(q, DailyMessage.class);
   }

   public void deleteAllMessages() {
      tenantMongoTemplate.dropCollection(DailyMessage.class);
   }

   public HashMap<String, ArrayList<DailyMessage>> sortDailyMessageOrder() { 
      List<DailyMessage> messageList = tenantMongoTemplate.findAll(DailyMessage.class);
      LinkedHashMap<String, ArrayList<DailyMessage>> sortedMessages = new LinkedHashMap<String, ArrayList<DailyMessage>>();

      for (int i = 1; i <= 5; i++) {
         sortedMessages.put(DayOfWeek.of(i).toString(), new ArrayList<>());
      }

      messageList.forEach(message -> {
         ArrayList<DailyMessage> messageArray = sortedMessages.get(DayOfWeek.of(message.getDayToShow()).toString());
         messageArray.add(message);
      });

      return sortedMessages;

   }

}
