package com.example.contacts.Models;

public class ContactModel {
    String contactName, phoneNumber;
    int contactId;

//    public ContactModel(int phoneNumber, String contactName){
//        this.phoneNumber = phoneNumber;
//        this.contactName = contactName;
//    }

    public int getContactId(){
        return this.contactId;
    }

    public void setContactId(int contactId){
        this.contactId = contactId;
    }

    public String getPhoneNumber(){
        return  this.phoneNumber;
    }

    public void setPhoneNumber(String phoneNumber){
        this.phoneNumber = phoneNumber;
    }

    public String getContactName(){
        return this.contactName;
    }

    public void setContactName(String contactName){
        this.contactName = contactName;
    }




}
