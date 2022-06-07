import java.util.ArrayList;
import java.util.List;

public class PersonBuilder {
    private String firstName;
    private String lastName;
    private Address address;
    private Phone phone;
    private final List<FamilyMember> familyMembers = new ArrayList<>();
    private String familyMemberName;
    private String familyMemberBorn;
    private Address familyMemberAddress;
    private Phone familyMemberPhone;
    private boolean buildingFamilyMember;
    private boolean personCreated;

    public PersonBuilder newPerson() {
        buildingFamilyMember = false;
        personCreated = true;
        return this;
    }

    public PersonBuilder withFirstName(String firstName) {
        this.firstName = firstName;
        return this;
    }
    public PersonBuilder withLastName(String lastName) {
        this.lastName = lastName;
        return this;
    }

    public PersonBuilder withAddress(Address address) {
        if (buildingFamilyMember) {
            this.familyMemberAddress = address;
        }
        else {
            this.address = address;
        }
        return this;
    }

    public PersonBuilder withPhone(Phone phone) {
        if (buildingFamilyMember) {
            this.familyMemberPhone = phone;
        }
        else {
            this.phone = phone;
        }
        return this;
    }

    public PersonBuilder addFamilyMember() {
        if (buildingFamilyMember) {
            familyMembers.add(new FamilyMember(familyMemberName, familyMemberBorn, familyMemberAddress, familyMemberPhone));
        }
        familyMemberName = "";
        familyMemberBorn = "";
        familyMemberAddress = null;
        familyMemberPhone = null;
        buildingFamilyMember = true;
        return this;
    }

    public PersonBuilder withName(String name) {
        this.familyMemberName = name;
        return this;
    }

    public PersonBuilder withBirthYear(String birthYear) {
        this.familyMemberBorn = birthYear;
        return this;
    }

    public boolean isPersonCreated() {
        return personCreated;
    }

    public Person build() {
        if (buildingFamilyMember) {
            familyMembers.add(new FamilyMember(familyMemberName, familyMemberBorn, familyMemberAddress, familyMemberPhone));
        }
        return new Person(firstName, lastName, address, phone, familyMembers);
    }
}
