package com.cs35.enums;

public enum QuestionTypeEnum {
    SINGLE("radio", "Single Choice Options"),
    MULTIPLE("checkbox", "Multiple Choice Options"),
    CALCULATION("3", "Calculation Questions Options"),
    QUESTIONS("input", "Q&A Questions Options");


    private final String code;
    private final String value;

    QuestionTypeEnum(String code, String value) {
        this.code = code;
        this.value = value;
    }

    public String getCode() {
        return code;
    }

    public static String getValue(String codeIn) {
        QuestionTypeEnum[] values = values();
        for (QuestionTypeEnum questionTypeEnum : values) {
            String code = questionTypeEnum.getCode();
            if (codeIn.equals( code)) {
                return questionTypeEnum.value;
            }
        }
        return null;
    }

}
