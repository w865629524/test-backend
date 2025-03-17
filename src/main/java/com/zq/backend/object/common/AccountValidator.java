package com.zq.backend.object.common;

import org.passay.CharacterRule;
import org.passay.EnglishCharacterData;
import org.passay.LengthRule;
import org.passay.PasswordData;
import org.passay.PasswordValidator;
import org.passay.RuleResult;

import java.util.Arrays;

public class AccountValidator {

    private static final int MIN_USERNAME_LENGTH = 6;
    private static final int MAX_USERNAME_LENGTH = 20;
    private static final int MIN_PASSWORD_LENGTH = 6;
    private static final int MAX_PASSWORD_LENGTH = 20;

    private static final PasswordValidator USERNAME_VALIDATOR = new PasswordValidator(Arrays.asList(
            // 长度规则
            new LengthRule(MIN_USERNAME_LENGTH, MAX_USERNAME_LENGTH)
    ));

    private static final PasswordValidator PASSWORD_VALIDATOR = new PasswordValidator(Arrays.asList(
            // 长度规则
            new LengthRule(MIN_PASSWORD_LENGTH, MAX_PASSWORD_LENGTH),
            // 至少一个小写字母
            new CharacterRule(EnglishCharacterData.Alphabetical, 1),
            // 至少一个数字
            new CharacterRule(EnglishCharacterData.Digit, 1)
    ));

    public static void validateUsername(String username) {
        ParamChecker.checkNotBlank(username, "username");
        RuleResult ruleResult = USERNAME_VALIDATOR.validate(new PasswordData(username));
        if(!ruleResult.isValid()) {
            ExceptionUtil.throwException(ErrorEnum.ILLEGAL_USERNAME);
        }
    }

    public static void validatePassword(String password) {
        ParamChecker.checkNotBlank(password, "password");
        RuleResult ruleResult = PASSWORD_VALIDATOR.validate(new PasswordData(password));
        if(!ruleResult.isValid()) {
            ExceptionUtil.throwException(ErrorEnum.UNSAFE_PASSWORD);
        }
    }
}
