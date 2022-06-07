public record Address(String street, String city, String zip) implements XmlElement {
    @Override
    public String generateXml() {
        return String.format(
                """
                <address>
                    <street>%s</street>
                    <city>%s</city>
                    <zip>%s</zip>
                </address>""",
                street,
                city,
                zip
        );
    }
}
