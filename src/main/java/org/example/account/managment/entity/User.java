package org.example.account.managment.entity;

public class User {

    private Long id;

    private Long amount;

    private String name;

    private String PhoneNumber;

    public User() {
    }

    public User(Long id, Long amount, String name, String phoneNumber) {
        this.id = id;
        this.amount = amount;
        this.name = name;
        PhoneNumber = phoneNumber;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getAmount() {
        return amount;
    }

    public void setAmount(Long amount) {
        this.amount = amount;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getPhoneNumber() {
        return PhoneNumber;
    }

    public void setPhoneNumber(String phoneNumber) {
        PhoneNumber = phoneNumber;
    }

    @Override
    public String toString() {
        return "User{" +
                "id=" + id +
                ", amount=" + amount/100 + "." + amount%100 +
                ", name='" + name + '\'' +
                ", PhoneNumber='" + PhoneNumber + '\'' +
                '}';
    }
}
