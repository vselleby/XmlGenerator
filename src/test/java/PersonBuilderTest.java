import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

class PersonBuilderTest {
    private PersonBuilder builder;

    @BeforeEach
    public void setUp() {
        builder = new PersonBuilder();
    }

    @Test
    public void emptyPerson() {
        Person person = builder.newPerson().build();
        assertNull(person.address());
        assertNull(person.phone());
        assertNull(person.firstName());
        assertNull(person.lastName());
        assertTrue(person.familyMembers().isEmpty());
    }

    @Test
    public void completePerson() {
        Person person = builder.newPerson().
                withFirstName("Test").
                withLastName("Testsson").
                withAddress(new Address("Test street 1", "Test City", "42")).
                withPhone(new Phone("001", "002")).
                addFamilyMember().
                withName("Lisa").
                withBirthYear("1888").
                withAddress(new Address("A1", "A2", "A3")).
                withPhone(new Phone("P1", "P2")).
                addFamilyMember().
                withName("Tester2").
                withBirthYear("1999").
                withAddress(new Address("B1", "B2", "B3")).
                withPhone(new Phone("Q1", "Q2")).
                build();


        assertEquals(person.firstName(), "Test");
        assertEquals(person.lastName(), "Testsson");
        assertEquals(person.address().street(), "Test street 1");
        assertEquals(person.address().city(), "Test City");
        assertEquals(person.address().zip(), "42");
        assertEquals(person.phone().mobile(), "001");
        assertEquals(person.phone().landLine(), "002");
        FamilyMember familyMember = person.familyMembers().get(0);
        assertEquals(familyMember.name(), "Lisa");
        assertEquals(familyMember.born(), "1888");
        assertEquals(familyMember.address().street(), "A1");
        assertEquals(familyMember.address().city(), "A2");
        assertEquals(familyMember.address().zip(), "A3");
        assertEquals(familyMember.phone().mobile(), "P1");
        assertEquals(familyMember.phone().landLine(), "P2");
        familyMember = person.familyMembers().get(1);
        assertEquals(familyMember.name(), "Tester2");
        assertEquals(familyMember.born(), "1999");
        assertEquals(familyMember.address().street(), "B1");
        assertEquals(familyMember.address().city(), "B2");
        assertEquals(familyMember.address().zip(), "B3");
        assertEquals(familyMember.phone().mobile(), "Q1");
        assertEquals(familyMember.phone().landLine(), "Q2");
    }
}