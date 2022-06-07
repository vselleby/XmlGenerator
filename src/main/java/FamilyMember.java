public record FamilyMember(String name, String born, Address address, Phone phone) implements XmlElement {
    @Override
    public String generateXml() {
        return String.format(
                """
                <family>
                    <name>%s</name>
                    <born>%s</born>
                %s%s</family>""",
                name,
                born,
                address == null ? "" : address.generateXml().indent(4),
                phone == null ? "" : phone.generateXml().indent(4)
        );
    }
}
