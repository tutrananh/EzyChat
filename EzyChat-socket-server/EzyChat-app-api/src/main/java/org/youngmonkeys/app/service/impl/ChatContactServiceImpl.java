package org.youngmonkeys.app.service.impl;

import com.tvd12.ezyfox.bean.annotation.EzyAutoBind;
import com.tvd12.ezyfox.bean.annotation.EzySingleton;
import com.tvd12.ezyfox.io.EzySets;

import lombok.Setter;
import org.youngmonkeys.app.entity.ChatContact;
import org.youngmonkeys.app.entity.ChatContactId;
import org.youngmonkeys.app.repo.ChatContactRepo;
import org.youngmonkeys.app.service.ChatContactService;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Setter
@EzySingleton("contactService")
public class ChatContactServiceImpl implements ChatContactService {

    @EzyAutoBind
    private ChatContactRepo contactRepo;

    @Override
    public Set<String> addContacts(String actor, Set<String> target) {
        Set<ChatContact> contacts = new HashSet<>();
        for (String friend : target) {
            ChatContactId id = new ChatContactId(actor, friend);
            ChatContact contact = new ChatContact();
            contact.setId(id);
            contacts.add(contact);
        }
        contactRepo.save(contacts);
        return target;
    }


}
