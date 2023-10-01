package ru.netology;

import com.codeborne.selenide.selector.ByText;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.openqa.selenium.Keys;

import java.time.Duration;

import static com.codeborne.selenide.Condition.*;
import static com.codeborne.selenide.Selectors.byText;
import static com.codeborne.selenide.Selenide.$;
import static com.codeborne.selenide.Selenide.open;

public class DataGeneratorTest {
    @BeforeEach
    void setUp() {
        open("http://localhost:9999/");
    }

    @Test
    @DisplayName("Successful Order")
    void successfulOrderTest() {
        DataGenerator.UserInfo validUser = DataGenerator.Registration.generateUser("ru");
        int daysToAddForFirstMeeting = 4;
        String firstMeetingDate = DataGenerator.generateData(daysToAddForFirstMeeting);
        int daysToAddForSecondMeeting = 7;
        String secondMeetingDate = DataGenerator.generateData(daysToAddForSecondMeeting);

        $("[data-test-id=city] input").setValue(validUser.getCity());
        $("[data-test-id=date] input").sendKeys(Keys.chord(Keys.SHIFT, Keys.HOME), Keys.BACK_SPACE);
        $("[data-test-id=date] input").setValue(firstMeetingDate);
        $("[data-test-id=name] input").setValue(validUser.getName());
        $("[data-test-id=phone] input").setValue(validUser.getPhone());
        $("[data-test-id=agreement]").click();

        $(byText("Запланировать")).click();
        $(byText("Успешно")).shouldBe(visible, Duration.ofSeconds(15));
        $("[date-test-id='success-notification'] .notification__content")
                .shouldHave(exactText("Встреча успешно запланирована" + firstMeetingDate))
                .shouldBe(visible);
        $("[data-test-id=date] input").sendKeys(Keys.chord(Keys.SHIFT, Keys.HOME), Keys.BACK_SPACE);
        $("[data-test-id=date] input").setValue(secondMeetingDate);
        $(byText("Запланировать")).click();
        $("[date-test-id='replan-notification'] .notification__content")
                .shouldHave(text("У вас уже запланирована встреча на другую дату. Перепланировать?"))
                .shouldBe(visible);
        $("[date-test-id='replan-notification'] button").click();
        $("[date-test-id='success-notification'] .notification__content")
                .shouldHave(exactText("Встреча успешно запланирована" + secondMeetingDate))
                .shouldBe(visible);

    }
}
