import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;


class XmlGeneratorTest {
    private XmlGenerator generator;

    @BeforeEach
    public void setUp() {
        generator = new XmlGenerator();
    }

    @Test
    public void noInput() {
        assertEquals(generator.generate(""), "<people>\n</people>\n");
    }

    @Test
    public void noPerson() {
        assertEquals(generator.generate("A|Gåsvägen 13|Ankeborg|11133"), "<people>\n</people>\n");
    }

    @Test
    public void junkInput() {
        assertEquals(generator.generate("42"), "<people>\n</people>\n");
    }

    @Test
    public void onePerson() {
        assertTrue(generator.generate("P|Pelle|Svanslös").contains("<person>"));
    }

    @Test
    public void twoPeople() {
        String xml = generator.generate("P|Pelle|Svanslös\nP|Kalle|Anka");
        assertTrue(xml.contains("<firstname>Pelle"));
        assertTrue(xml.contains("<lastname>Svanslös"));
        assertTrue(xml.contains("<firstname>Kalle"));
        assertTrue(xml.contains("<lastname>Anka"));
    }

    @Test
    public void multipleAddresses() {
        String xml = generator.generate("P|Kalle|Anka\nA|test1|test1|test1\nA|test2|test2|test2");
        assertTrue(xml.contains("<street>test2"));
        assertFalse(xml.contains("<street>test1"));
    }

    @Test
    public void oneFamilyMember() {
        String xml = generator.generate("P|A|B\nF|C|D\nA|E|F|G");
        assertTrue(xml.contains("<name>C"));
        assertTrue(xml.contains("<born>D"));
        assertTrue(xml.contains("<street>E"));
    }

    @Test
    public void twoFamilyMembers() {
        String xml = generator.generate("P|A|B\nF|C|D\nF|E|F");
        assertTrue(xml.contains("<name>C"));
        assertTrue(xml.contains("<born>D"));
        assertTrue(xml.contains("<name>E"));
        assertTrue(xml.contains("<born>F"));
    }

    @Test
    public void multiplePeopleAndFamilyMembers() {
        assertEquals(generator.generate("P|A|B\nF|C|D\nF|E|F\nT|T1|T2\nP|G|H\nP|I|J\nA|A1|A2|A3\nF|K|L\nA|A4|A5|A6\nP|M|N"),
                """
                <people>
                    <person>
                        <firstname>A</firstname>
                        <lastname>B</lastname>
                        <family>
                            <name>C</name>
                            <born>D</born>
                        </family>
                        <family>
                            <name>E</name>
                            <born>F</born>
                            <phone>
                                <mobile>T1</mobile>
                                <landline>T2</landline>
                            </phone>
                        </family>
                    </person>
                    <person>
                        <firstname>G</firstname>
                        <lastname>H</lastname>
                    </person>
                    <person>
                        <firstname>I</firstname>
                        <lastname>J</lastname>
                        <address>
                            <street>A1</street>
                            <city>A2</city>
                            <zip>A3</zip>
                        </address>
                        <family>
                            <name>K</name>
                            <born>L</born>
                            <address>
                                <street>A4</street>
                                <city>A5</city>
                                <zip>A6</zip>
                            </address>
                        </family>
                    </person>
                    <person>
                        <firstname>M</firstname>
                        <lastname>N</lastname>
                    </person>
                </people>
                """
        );
    }

    @Test
    public void missingPhoneDetails() {
        assertTrue(generator.generate("P|A|B\nT\n").contains("<mobile></mobile>"));
        assertTrue(generator.generate("P|A|B\nT\n").contains("<landline></landline>"));
        assertTrue(generator.generate("P|A|B\nT|0\n").contains("<mobile>0</mobile>"));
        assertTrue(generator.generate("P|A|B\nT|0\n").contains("<landline></landline>"));
    }

    @Test
    public void missingAddressDetails() {
        assertTrue(generator.generate("P|A|B\nA").contains("<street></street>"));
        assertTrue(generator.generate("P|A|B\nA|test").contains("<city></city>"));
        assertTrue(generator.generate("P|A|B\nA|test1|test2").contains("<zip></zip>"));
    }

    @Test
    public void missingPersonDetails() {
        assertTrue(generator.generate("P\n").contains("<firstname></firstname>"));
        assertTrue(generator.generate("P\n").contains("<lastname></lastname>"));
        assertTrue(generator.generate("P|A\n").contains("<firstname>A</firstname>"));
        assertTrue(generator.generate("P|A\n").contains("<lastname></lastname>"));
    }

    @Test
    public void missingFamilyMemberDetails() {
        assertTrue(generator.generate("P\nF").contains("<name></name>"));
        assertTrue(generator.generate("P\nF").contains("<born></born>"));
        assertTrue(generator.generate("P\nF|A").contains("<name>A</name>"));
        assertTrue(generator.generate("P\nF|A").contains("<born></born>"));
    }
}