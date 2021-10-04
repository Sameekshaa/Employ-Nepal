package android.example.employnepal.models;

public class SaveUserProfile {

    //for editProfile
    public String firstName; String lastName; String contact; String location; String gender; String dob;

    public SaveUserProfile(String firstName, String lastName, String contact, String location, String gender, String dob) {
        this.firstName = firstName;
        this.lastName = lastName;
        this.contact = contact;
        this.location = location;
        this.gender = gender;
        this.dob = dob;
    }

    public SaveUserProfile() {

    }
}
