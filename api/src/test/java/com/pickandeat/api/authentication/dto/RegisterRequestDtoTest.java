import java.time.LocalDate;
import java.util.Set;

import static org.assertj.core.api.Assertions.assertThat;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import com.pickandeat.api.authentication.dto.RegisterRequestDto;

import jakarta.validation.ConstraintViolation;
import jakarta.validation.Validation;
import jakarta.validation.Validator;
import jakarta.validation.ValidatorFactory;

public class RegisterRequestDtoTest {

    private static Validator validator;

    @BeforeAll
    static void setupValidator() {
        ValidatorFactory factory = Validation.buildDefaultValidatorFactory();
        validator = factory.getValidator();
    }

    private RegisterRequestDto createValidDto() {
        return new RegisterRequestDto(
                "john.doe@example.com",
                "StrongPassword123",
                "John",
                "Doe",
                "0601020304",
                LocalDate.of(2000, 1, 1),
                "CONSUMER");
    }

    @Test
    void shouldPassValidation_whenAllFieldsAreValid() {
        RegisterRequestDto dto = createValidDto();

        Set<ConstraintViolation<RegisterRequestDto>> violations = validator.validate(dto);

        assertThat(violations).isEmpty();
    }

    @Test
    void shouldFailValidation_whenEmailIsBlank() {
        RegisterRequestDto dto = createValidDto();
        dto = new RegisterRequestDto(
                "", dto.getPassword(), dto.getFirstName(), dto.getLastName(),
                dto.getPhoneNumber(), dto.getBirthDate(), dto.getRole());

        Set<ConstraintViolation<RegisterRequestDto>> violations = validator.validate(dto);

        assertThat(violations)
                .anyMatch(v -> v.getPropertyPath().toString().equals("email"));
    }

    @Test
    void shouldFailValidation_whenPasswordTooShort() {
        RegisterRequestDto dto = createValidDto();
        dto = new RegisterRequestDto(
                dto.getEmail(), "short", dto.getFirstName(), dto.getLastName(),
                dto.getPhoneNumber(), dto.getBirthDate(), dto.getRole());

        Set<ConstraintViolation<RegisterRequestDto>> violations = validator.validate(dto);

        assertThat(violations)
                .anyMatch(v -> v.getPropertyPath().toString().equals("password"));
    }

    @Test
    void shouldFailValidation_whenFirstNameIsBlank() {
        RegisterRequestDto dto = createValidDto();
        dto = new RegisterRequestDto(
                dto.getEmail(), dto.getPassword(), "", dto.getLastName(),
                dto.getPhoneNumber(), dto.getBirthDate(), dto.getRole());

        Set<ConstraintViolation<RegisterRequestDto>> violations = validator.validate(dto);

        assertThat(violations)
                .anyMatch(v -> v.getPropertyPath().toString().equals("firstName"));
    }

    @Test
    void shouldFailValidation_whenLastNameIsBlank() {
        RegisterRequestDto dto = createValidDto();
        dto = new RegisterRequestDto(
                dto.getEmail(), dto.getPassword(), dto.getFirstName(), "",
                dto.getPhoneNumber(), dto.getBirthDate(), dto.getRole());

        Set<ConstraintViolation<RegisterRequestDto>> violations = validator.validate(dto);

        assertThat(violations)
                .anyMatch(v -> v.getPropertyPath().toString().equals("lastName"));
    }

    @Test
    void shouldFailValidation_whenPhoneNumberIsBlank() {
        RegisterRequestDto dto = createValidDto();
        dto = new RegisterRequestDto(
                dto.getEmail(), dto.getPassword(), dto.getFirstName(), dto.getLastName(),
                "", dto.getBirthDate(), dto.getRole());

        Set<ConstraintViolation<RegisterRequestDto>> violations = validator.validate(dto);

        assertThat(violations)
                .anyMatch(v -> v.getPropertyPath().toString().equals("phoneNumber"));
    }

    @Test
    void shouldFailValidation_whenBirthDateIsNull() {
        RegisterRequestDto dto = createValidDto();
        dto = new RegisterRequestDto(
                dto.getEmail(), dto.getPassword(), dto.getFirstName(), dto.getLastName(),
                dto.getPhoneNumber(), null, dto.getRole());

        Set<ConstraintViolation<RegisterRequestDto>> violations = validator.validate(dto);

        assertThat(violations)
                .anyMatch(v -> v.getPropertyPath().toString().equals("birthDate"));
    }

    @Test
    void shouldFailValidation_whenBirthDateIsInFuture() {
        RegisterRequestDto dto = createValidDto();
        dto = new RegisterRequestDto(
                dto.getEmail(), dto.getPassword(), dto.getFirstName(), dto.getLastName(),
                dto.getPhoneNumber(), LocalDate.now().plusDays(1), dto.getRole());

        Set<ConstraintViolation<RegisterRequestDto>> violations = validator.validate(dto);

        assertThat(violations)
                .anyMatch(v -> v.getPropertyPath().toString().equals("birthDate"));
    }

    @Test
    void shouldFailValidation_whenRoleIsInvalid() {
        RegisterRequestDto dto = createValidDto();
        dto = new RegisterRequestDto(
                dto.getEmail(), dto.getPassword(), dto.getFirstName(), dto.getLastName(),
                dto.getPhoneNumber(), dto.getBirthDate(), "FAKE_ROLE");

        Set<ConstraintViolation<RegisterRequestDto>> violations = validator.validate(dto);

        assertThat(violations)
                .anyMatch(v -> v.getPropertyPath().toString().equals("role") &&
                        v.getMessage().contains("Invalid role"));
    }
}
