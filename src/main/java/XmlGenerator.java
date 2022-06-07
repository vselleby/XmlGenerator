import java.util.ArrayList;
import java.util.List;
import java.util.logging.Logger;
import java.util.stream.Collectors;

public class XmlGenerator {
    private static final Logger logger = Logger.getLogger(XmlGenerator.class.getName());

    public String generate(String input) {
        List<Person> people = new ArrayList<>();
        PersonBuilder builder = new PersonBuilder();
        for (String line : input.split("\n")) {
            String[] data = line.split("\\|");
            switch (data[0]) {
                case "P" -> {
                    if (builder.isPersonCreated()) {
                        people.add(builder.build());
                    }
                    builder = new PersonBuilder();
                    builder.newPerson().withFirstName(getValue(data, 1)).withLastName(getValue(data, 2));
                }
                case "F" -> builder.addFamilyMember().withName(getValue(data, 1)).withBirthYear(getValue(data, 2));
                case "A" -> builder.withAddress(new Address(getValue(data, 1), getValue(data, 2), getValue(data, 3)));
                case "T" -> builder.withPhone(new Phone(getValue(data, 1), getValue(data, 2)));
                default -> logger.warning("Invalid command received: " + data[0]);
            }
        }
        if (builder.isPersonCreated()) {
            people.add(builder.build());
        }
        return String.format(
                """
                <people>
                %s</people>
                """,
                people.stream().map(Person::generateXml).collect(Collectors.joining("\n")).indent(4)
        );
    }

    private String getValue(String[] data, int index) {
        if (data.length > index) {
            return data[index];
        }
        logger.warning("Missing data for: " + String.join("|", data) + " at index: " + index);
        return "";
    }

    public static void main(String[] args) {
        XmlGeneratorView view = new XmlGeneratorView(new XmlGenerator());
        view.pack();
        view.setVisible(true);
    }
}
