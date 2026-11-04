package demo.common_entity_demo.enumurations;

public enum AccountMapTypeToCode {
    SAVINGS("100"),
    CURRENT("200"),
    FIXED_DEPOSIT("300");

    private final String prefix;

    AccountMapTypeToCode(String prefix) {
        this.prefix = prefix;
    }

    public String getPrefix() {
        return prefix;
    }
    public static AccountMapTypeToCode fromCode(String code) {
        if (code == null) {
            throw new IllegalArgumentException("Account type is required");
        }
        for (AccountMapTypeToCode t : values()) {
            if (t.name().equalsIgnoreCase(code.trim())) {
                return t;
            }
        }
        throw new IllegalArgumentException("Invalid account type: " + code);
    }
}