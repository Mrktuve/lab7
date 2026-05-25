package client.input;

import common.commands.*;
import common.model.*;


public class CommandBuilder {

    private final ConsoleReader reader;
    private String ownerLogin;

    public CommandBuilder(ConsoleReader reader) {
        this.reader = reader;
        this.ownerLogin = "";
    }
    public void setOwnerLogin(String ownerLogin) {
        this.ownerLogin = ownerLogin;
    }
    public Command build() {
        String input = reader.readLine();
        if (input == null) return null;
        input = input.trim().toLowerCase();
        String[] parts = input.split(" ");
        String command = parts[0];


        try {
            return switch (command) {
                case "add" -> new Add(readWorker());
                case "add_if_max" -> new AddIfMax(readWorker());
                case "clear" -> new Clear();
                case "show" -> new Show();
                case "info" -> new Info();
                case "help" -> new Help();
                case "history" -> new History();
                case "exit" -> new Exit();

                case "remove_by_id" -> {
                    long id = Validator.parseLong(parts[1]);
                    yield new RemoveById(id); // yield - возвращает результат switch
                }

                case "update" -> {
                    long id = Validator.parseLong(parts[1]);
                    yield new UpdateId(id, readWorker());
                }

                case "remove_lower" -> new RemoveLower(readWorker());

                case "remove_any_by_status" -> {
                    Status status = Status.valueOf(parts[1].toUpperCase());
                    yield new RemoveAnyByStatus(status);
                }

                case "filter_starts_with_name" -> {
                    yield new FilterStartsWithName(parts[1]);
                }

                case "print_descending" -> new PrintDescending();

                case "execute_script" -> {
                    yield new ExecuteScript(parts[1]);
                }
                case "login":
                    return new Login();

                case "register":
                    return new Register();

                default -> throw new IllegalArgumentException("Unknown command");
            };

        } catch (Exception e) {
            reader.println("Error: " + e.getMessage());
            return null;
        }
    }


    private Worker readWorker() {
        reader.println("Enter worker:");

        reader.print("Имя: ");
        String name = Validator.requireNonEmpty(reader.readLine(), "Name");

        reader.print("Координаты X: ");
        int x = Validator.parseInt(reader.readLine());

        reader.print("Координаты Y: ");
        long y = Validator.parseLong(reader.readLine());

        Coordinates coordinates = new Coordinates(x, y);

        reader.print("Зарплата: ");
        double salary = Validator.parseDouble(reader.readLine());

        reader.print("Введите дату начала (yyyy-MM-dd): ");
        LocalDateTime startDate = Validator.parseDate(reader.readLine());

        reader.print("Введите дату окончания (or empty): ");
        String endInput = reader.readLine();
        LocalDateTime endDate = endInput.isEmpty() ? null : Validator.parseDate(endInput);

        reader.print("Введите статус (FIRED, RECOMMENDED_FOR_PROMOTION, REGULAR, PROBATION): ");
        String statusInput = reader.readLine();
        Status status = Status.valueOf(statusInput.toUpperCase());

        Person person = readPerson();

        return new Worker(name, coordinates, salary, startDate, endDate, status, person, ownerLogin);
    }


    private Person readPerson() {
        reader.print("Введите passportID: ");
        String h = reader.readLine();
        Long height = Long.parseLong(h);

        reader.print("Цвет глаз (GREEN, BLUE, BROWN): ");
        EyeColor eye = EyeColor.valueOf(reader.readLine().toUpperCase());

        reader.print("Цвет волос (GREEN, RED, BLACK, BLUE, BROWN): ");
        HairColor hair = HairColor.valueOf(reader.readLine().toUpperCase());

        reader.print("Страна (USA, BELARUS, RUSSIAN, CHINA, JAPAN): ");
        Country country = Country.valueOf(reader.readLine().toUpperCase());

        return new Person(height, eye, hair, country);
    }
}