public record Phone(String mobile, String landLine) implements XmlElement {
    @Override
    public String generateXml() {
        return String.format(
                """
                <phone>
                    <mobile>%s</mobile>
                    <landline>%s</landline>
                </phone>""",
                mobile,
                landLine
        );
    }
}
