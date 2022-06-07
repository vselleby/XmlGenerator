import java.util.List;
import java.util.stream.Collectors;

public record Person(String firstName, String lastName, Address address, Phone phone, List<FamilyMember> familyMembers) implements XmlElement {
    @Override
    public String generateXml() {
        return String.format(
                """
                <person>
                    <firstname>%s</firstname>
                    <lastname>%s</lastname>
                %s%s%s</person>""",
                firstName,
                lastName,
                address == null ? "" : address.generateXml().indent(4),
                phone == null ? "" : phone.generateXml().indent(4),
                familyMembers.stream().map(FamilyMember::generateXml).collect(Collectors.joining("\n")).indent(4)
        );
    }
}
